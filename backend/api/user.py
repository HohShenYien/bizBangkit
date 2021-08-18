from flask import Flask, Blueprint, request, jsonify, make_response
import json
import sqlite3
import datetime
import string
import secrets
import os
from werkzeug.utils import secure_filename

user_bp = Blueprint('user', __name__)

# CONNECTING TO DATABASE FOR STORING AND READING PURPOSES
con = sqlite3.connect('bizBangkit.db', check_same_thread=False)
cur = con.cursor()
con.commit()

JSON_MIME_TYPE = 'application/json; charset=utf-8'
currentDateTime = datetime.datetime.now()

# FOR UPLOADING FILE (IMAGE) INTO THE SYSTEM
app = Flask(__name__)
UPLOAD_FOLDER = r'C:\Users\rosss\OneDrive\Documents\GitHub\bizBangkit\backend\api\pictures'
ALLOWED_EXTENSIONS = {'txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'}
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


def json_response(data='', status=200, headers=None):
    headers = headers or {}
    if 'Content-Type' not in headers:
        headers['Content-Type'] = JSON_MIME_TYPE

    return make_response(data, status, headers)


# To register new user account in bizBangkit
@user_bp.route('/register', methods=['GET', 'POST'])
def register():
    letter = string.ascii_letters
    digit = string.digits
    length = 32

    if request.method == 'GET':
        return jsonify({'Error message': 'Wrong method! Send the parameters using POST method instead.'})
    elif request.method == 'POST':
        user = request.form
        user_id = ''

        cur.execute("SELECT * FROM USER_T WHERE user_fullname = ? AND user_ic_no = ? AND user_username = ?",
                    (user['fullname'], user['ic_number'], user['username'], ))
        account = cur.fetchone()
        if account:
            return jsonify({'message': 'Account already exists!'})
        else:
            path1 = upload_image()
            authkey = ''.join(secrets.choice(letter + digit) for x in range(length))
            cur.execute("""INSERT INTO USER_T (user_id, user_fullname, user_ic_no, user_username, user_password, 
            user_authkey, user_dob, user_phone, user_email, user_gender, user_created_at, user_tos_status, 
            user_fpath_profilepic) 
            VALUES ( (SELECT max(user_id) FROM USER_T)+1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'A', ?)""",
                        (user['fullname'], user['ic_number'], user['username'], user['password'], authkey, user['dob'],
                         user['phone_num'], user['email'], user['gender'], currentDateTime, path1, ))
            con.commit()

            find = cur.execute("SELECT user_id FROM USER_T WHERE user_authkey = ?", (authkey,))
            for i in find:
                user_id = i[0]

            new_acc = get_user(user_id)

    return new_acc


# To view user credentials based on their username
@user_bp.route('/profile/<user_id>', methods=['GET'])
def get_user(user_id):
    user_profile = {}
    cur.execute("SELECT * FROM USER_T WHERE user_id = ?", (user_id, ))
    display = cur.fetchone()

    if display:
        user_profile["user_id"] = display[0]
        user_profile["user_fullname"] = display[1]
        user_profile["user_ic_no"] = display[2]
        user_profile["user_username"] = display[3]
        user_profile["user_password"] = display[4]
        user_profile["user_authkey"] = display[5]
        user_profile["user_dob"] = display[6]
        user_profile["user_phone"] = display[7]
        user_profile["user_email"] = display[8]
        user_profile["user_gender"] = display[9]
        user_profile["user_fpath_profilepic"] = display[14]

        return json_response(json.dumps(user_profile))
    else:
        return jsonify({"Error": "Username is not found!"})


# To update user credentials
@user_bp.route('/update/profile/<user_id>', methods=['PUT'])
def update_profile(user_id):
    update = request.form

    if update['fullname'] or update['ic_number'] or update['username'] or update['password'] or update['dob'] \
            or update['phone_num'] or update['email'] or update['gender'] or update['fulladdress'] or update['aboutme']:

        cur.execute("UPDATE USER_T SET user_fullname = ?, user_ic_no = ?, user_username = ?, user_password = ?, "
                    "user_dob = ?, user_phone = ?, user_email = ?, user_gender = ?, user_fulladdress = ?, "
                    "user_aboutme = ? WHERE user_id = ?",
                    (update['fullname'], update['ic_number'], update['username'], update['password'], update['dob'],
                     update['phone_num'], update['email'], update['gender'], update['fulladdress'],
                     update['aboutme'], user_id,))
        con.commit()

    updated = get_user(user_id)

    return updated


# To update user's profile picture only
@user_bp.route('/update/profile/image/<user_authkey>', methods=['PUT'])
def update_image(user_authkey):
    new_image = upload_image()

    cur.execute("UPDATE USER_T SET user_fpath_profilepic= ? WHERE user_authkey = ?", (new_image, user_authkey,))
    con.commit()

    return jsonify({"Updated profile picture directory": new_image})


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


def upload_image():
    file = request.files['file']

    if file.filename == '':  # If no file is selected, default picture will be used
        path = r'C:\Users\rosss\OneDrive\Documents\GitHub\bizBangkit\backend\api\pictures'

    if file and allowed_file(file.filename):
        filename = secure_filename(file.filename)  # stores the name of the file uploaded
        path = (os.path.join(app.config['UPLOAD_FOLDER'], filename))  # save uploaded image under the filename
        file.save(path)

    return path
