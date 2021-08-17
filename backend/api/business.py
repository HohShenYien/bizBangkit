from flask import Blueprint, request, jsonify, make_response
import json
import sqlite3
import datetime

business_bp = Blueprint('business', __name__)

# CONNECTING TO DATABASE FOR STORING AND READING PURPOSES
con = sqlite3.connect('bizBangkit.db', check_same_thread=False)
cur = con.cursor()
con.commit()

JSON_MIME_TYPE = 'application/json; charset=utf-8'
currentDateTime = datetime.datetime.now()


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
        biz = request.json
        biz_id = ''

        if biz['bus_fpath_logo']:  # If business logo is uploaded
            l1 = r'C:\Users\rosss\OneDrive\Documents\GitHub\bizBangkit\backend\pictures'
            l2 = biz['bus_fpath_logo']
            logo = l1 + '\\' + l2
            cur.execute("""INSERT INTO BUSINESS_T (bus_id, bus_name, bus_type, bus_valuation, bus_total_emp, 
            bus_current_emp, bus_phone, bus_email, bus_ssm_id, bus_lic_no, bus_bank_acc, bus_tos_status, 
            bus_ops_start_time, bus_ops_end_time, bus_day, bus_reg_address, bus_loc_address_city, bus_fpath_logo) 
            VALUES ((SELECT max(bus_id) FROM BUSINESS_T)+1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'A', ?, ?, ?, ?, ?, ?)""",
                        (biz['bus_name'], biz['bus_type'], biz['bus_valuation'], biz['bus_total_emp'],
                         biz['bus_current_emp'], biz['bus_phone'], biz['bus_email'], biz['bus_ssm_id'],
                         biz['bus_lic_no'], biz['bus_bank_acc'], biz['bus_ops_start_time'], biz['bus_ops_end_time'],
                         biz['bus_day'], biz['bus_reg_address'], biz['bus_loc_address_city'], logo,))
            con.commit()

            find = cur.execute("SELECT bus_id FROM BUSINESS_T WHERE bus_name = ? AND bus_email = ?",
                               (biz['bus_name'], biz['bus_email']))
            for i in find:
                biz_id = i[0]
            new_biz = get_business(biz_id)
        else:  # If business logo is NOT uploaded, default pic will be used
            logo = r'C:\Users\rosss\OneDrive\Documents\GitHub\bizBangkit\backend\pictures\defaultpic.png'
            cur.execute("""INSERT INTO BUSINESS_T (bus_id, bus_name, bus_type, bus_valuation, bus_total_emp, 
            bus_current_emp, bus_phone, bus_email, bus_ssm_id, bus_lic_no, bus_bank_acc, bus_tos_status, 
            bus_ops_start_time, bus_ops_end_time, bus_day, bus_reg_address, bus_loc_address_city, bus_fpath_logo) 
            VALUES ((SELECT max(bus_id) FROM BUSINESS_T)+1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'A', ?, ?, ?, ?, ?, ?)""",
                        (biz['bus_name'], biz['bus_type'], biz['bus_valuation'], biz['bus_total_emp'],
                         biz['bus_current_emp'], biz['bus_phone'], biz['bus_email'], biz['bus_ssm_id'],
                         biz['bus_lic_no'], biz['bus_bank_acc'], biz['bus_ops_start_time'], biz['bus_ops_end_time'],
                         biz['bus_day'], biz['bus_reg_address'], biz['bus_loc_address_city'], logo,))
            con.commit()

            find = cur.execute("SELECT bus_id FROM BUSINESS_T WHERE bus_name = ? AND bus_email = ?",
                               (biz['bus_name'], biz['bus_email']))
            for i in find:
                biz_id = i[0]
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
