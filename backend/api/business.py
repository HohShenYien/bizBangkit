from flask import Flask, Blueprint, request, jsonify, make_response
from werkzeug.utils import secure_filename
import json
import sqlite3
import datetime
import os
import random

business_bp = Blueprint('business', __name__)

# CONNECTING TO DATABASE FOR STORING AND READING PURPOSES
con = sqlite3.connect('bizBangkit.db', check_same_thread=False)
cur = con.cursor()
con.commit()

JSON_MIME_TYPE = 'application/json; charset=utf-8'
currentDateTime = datetime.datetime.now()

# FOR UPLOADING FILE (IMAGE) INTO THE SYSTEM
app = Flask(__name__)
UPLOAD_FOLDER = './pictures/'
ALLOWED_EXTENSIONS = {'txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'}
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


def json_response(data='', status=200, headers=None):
    headers = headers or {}
    if 'Content-Type' not in headers:
        headers['Content-Type'] = JSON_MIME_TYPE

    return make_response(data, status, headers)


# To register new business proposal in bizBangkit
@business_bp.route('/register/business', methods=['GET', 'POST'])
def reg_business():
    if request.method == 'GET':
        return jsonify({'Error message': 'Wrong method! Send the parameters using POST method instead.'})
    elif request.method == 'POST':
        biz = request.form
        biz_id = ''

        cur.execute("SELECT * FROM BUSINESS_T WHERE bus_name = ? AND bus_email = ? AND bus_bank_acc = ?",
                    (biz['bus_name'], biz['bus_email'], biz['bus_bank_acc']))
        account = cur.fetchone()
        if account:
            return jsonify({'message': 'Account already exists!'})
        else:
            logo = upload_image()
            cur.execute("""INSERT INTO BUSINESS_T (bus_id, bus_name, bus_type, bus_valuation, bus_total_emp, 
                        bus_current_emp, bus_phone, bus_email, bus_ssm_id, bus_lic_no, bus_bank_acc, bus_tos_status, 
                        bus_ops_start_time, bus_ops_end_time, bus_day, bus_reg_address, bus_loc_address_city, 
                        bus_fpath_logo) VALUES ((SELECT max(bus_id) FROM BUSINESS_T)+1, ?, ?, ?, ?, ?, ?, ?, ?, ?, 
                        ?, 'A', ?, ?, ?, ?, ?, ?)""",
                        (biz['bus_name'], biz['bus_type'], biz['bus_valuation'], biz['bus_total_emp'],
                         biz['bus_current_emp'], biz['bus_phone'], biz['bus_email'], biz['bus_ssm_id'],
                         biz['bus_lic_no'], biz['bus_bank_acc'], biz['bus_ops_start_time'], biz['bus_ops_end_time'],
                         biz['bus_day'], biz['bus_reg_address'], biz['bus_loc_address_city'], logo,))
            con.commit()

            find = cur.execute("SELECT bus_id FROM BUSINESS_T WHERE bus_name = ? AND bus_email = ?",
                               (biz['bus_name'], biz['bus_email']))
            for i in find:
                biz_id = i[0]

            cur.execute("""INSERT INTO BUSINESS_OWNER_T (bus_id, user_id, share_alloc_qty) VALUES 
                        (?, ?, 60)""", (biz_id, biz['user_id'],))
            con.commit()

            cur.execute("""INSERT INTO BUS_SHARE_T (bus_share_total_investor, bus_share_approve_limit,
            bus_share_current_investor, bus_share_phase, bus_share_total_divestor, bus_id) VALUES (7, 1, 0, 1, 0, ?)""",
                        (biz_id,))
            con.commit()

            cur.execute("""INSERT INTO BUS_REPORT_T (rep_total_fund, rep_current_fund, rep_total_spent, 
            rep_total_income, bus_id) VALUES (0, 0, 0, 0, ?)""", (biz_id,))
            con.commit()

            new_biz = get_business(biz_id)

    return new_biz


# To view business details based on bus_id
@business_bp.route('/business/info/<bus_id>', methods=['GET'])
def get_business(bus_id):
    biz_info = {}
    cur.execute("SELECT * FROM BUSINESS_T WHERE bus_id = ?", (bus_id,))
    info = cur.fetchone()

    if info:
        biz_info["bus_id"] = info[0]
        biz_info["bus_name"] = info[1]
        biz_info["bus_type"] = info[2]
        biz_info["bus_valuation"] = info[3]
        biz_info["bus_total_emp"] = info[4]
        biz_info["bus_current_emp"] = info[5]
        biz_info["bus_phone"] = info[6]
        biz_info["bus_email"] = info[7]
        biz_info["bus_ssm_id"] = info[8]
        biz_info["bus_lic_no"] = info[9]
        biz_info["bus_bank_acc"] = info[10]
        biz_info["bus_ops_start_time"] = info[14]
        biz_info["bus_ops_end_time"] = info[15]
        biz_info["bus_day"] = info[16]
        biz_info["bus_reg_address"] = info[17]
        biz_info["bus_loc_address_city"] = info[18]
        biz_info["bus_fpath_logo"] = info[19]

        return json_response(json.dumps(biz_info))
    else:
        return jsonify({"Error": "Business ID cannot be found!"})


# To view business details based on user_id
@business_bp.route('/business/info/user/<user_id>', methods=['GET'])
def get_user_business(user_id):
    cur.execute("SELECT bus_id FROM BUSINESS_OWNER_T WHERE user_id = ?", (user_id,))
    the_user = cur.fetchone()
    if not the_user:
        return jsonify({"Error": "This user does not have business"})
    biz_info = {}
    cur.execute("SELECT * FROM BUSINESS_T WHERE bus_id = ?", (the_user[0],))
    info = cur.fetchone()

    if info:
        biz_info["bus_id"] = info[0]
        biz_info["bus_name"] = info[1]
        biz_info["bus_type"] = info[2]
        biz_info["bus_valuation"] = info[3]
        biz_info["bus_total_emp"] = info[4]
        biz_info["bus_current_emp"] = info[5]
        biz_info["bus_phone"] = info[6]
        biz_info["bus_email"] = info[7]
        biz_info["bus_ssm_id"] = info[8]
        biz_info["bus_lic_no"] = info[9]
        biz_info["bus_bank_acc"] = info[10]
        biz_info["bus_ops_start_time"] = info[14]
        biz_info["bus_ops_end_time"] = info[15]
        biz_info["bus_day"] = info[16]
        biz_info["bus_reg_address"] = info[17]
        biz_info["bus_loc_address_city"] = info[18]
        biz_info["bus_fpath_logo"] = info[19]

        return json_response(json.dumps(biz_info))
    else:
        return jsonify({"Error": "Business ID cannot be found!"})


# To view the business list
@business_bp.route('/business/list', methods=['GET'])
def biz_list():
    n = request.args.get('n', type=int)  # LIMIT n (number of records shown)
    q = request.args.get('q', default=0, type=int)  # OFFSET q (start from where)

    cur.execute("SELECT b.bus_id, b.bus_name, b.bus_type, b.bus_fpath_logo, b.bus_valuation, bs.bus_share_phase, "
                "br.rep_current_fund FROM BUSINESS_T b JOIN BUS_REPORT_T br ON b.bus_id = br.bus_id "
                "JOIN BUS_SHARE_T bs ON b.bus_id = bs.bus_id ORDER BY b.bus_id DESC LIMIT ? OFFSET ?", (n, q, ))
    bus_list = cur.fetchall()

    bus_list1 = []
    for b in bus_list:
        bus_dict = {'bus_id': b[0], 'bus_name': b[1], 'bus_type': b[2], 'bus_fpath_logo': b[3], 'bus_valuation': b[4],
                    'bus_share_phase': b[5], 'rep_current_fund': b[6]}
        bus_list1.append(bus_dict)

    return jsonify(bus_list1)


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


def upload_image():
    file = request.files['file']
    path = ''

    if file.filename == '':  # If no file is selected, default picture will be used
        path = './pictures/default.png'

    if file and allowed_file(file.filename):
        # stores the name of the file uploaded
        filename = str(random.randint(0, 99999)) + secure_filename(file.filename)
        path = (os.path.join(app.config['UPLOAD_FOLDER'], filename))  # save uploaded image under the filename
        file.save(path)

    return path
