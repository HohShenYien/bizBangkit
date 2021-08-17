from flask import Blueprint, request, jsonify, make_response
import json
import sqlite3
import uuid
import datetime

user_bp = Blueprint('user', __name__)

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


# To register new user account in bizBangkit
@user_bp.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'GET':
        return jsonify({'Error message': 'Wrong method! Send the parameters using POST method instead.'})
    elif request.method == 'POST':
        user = request.json

        cur.execute("SELECT * FROM USER_T WHERE user_fullname = ? AND user_ic_no = ? AND user_username = ?",
                    (user['fullname'], user['ic_number'], user['username'], ))
        account = cur.fetchone()
        if account:
            return jsonify({'message': 'Account already exists!'})
        elif user['profile_pic']:  # If profile picture is uploaded
            p = user['profile_pic']
            path = r'C:\Users\rosss\OneDrive\Documents\GitHub\bizBangkit\backend\pictures'
            path1 = path + '\\' + p
            authkey = str(uuid.uuid4())
            cur.execute("""INSERT INTO USER_T (user_id, user_fullname, user_ic_no, user_username, user_password, 
            user_authkey, user_dob, user_phone, user_email, user_gender, user_created_at, user_tos_status, 
            user_fpath_profilepic) 
            VALUES ( (SELECT max(user_id) FROM USER_T)+1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'A', ?)""",
                        (user['fullname'], user['ic_number'], user['username'], user['password'], authkey, user['dob'],
                         user['phone_num'], user['email'], user['gender'], currentDateTime, path1, ))
            con.commit()
            new_acc = get_user(user['username'])
        else:  # If profile picture is NOT uploaded
            path1 = r'C:\Users\rosss\OneDrive\Documents\GitHub\bizBangkit\backend\pictures\defaultpic.png'
            authkey = str(uuid.uuid4())
            cur.execute("""INSERT INTO USER_T (user_id, user_fullname, user_ic_no, user_username, user_password, 
            user_authkey, user_dob, user_phone, user_email, user_gender, user_created_at, user_tos_status, 
            user_fpath_profilepic) 
            VALUES ( (SELECT max(user_id) FROM USER_T)+1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'A', ?)""",
                        (user['fullname'], user['ic_number'], user['username'], user['password'], authkey, user['dob'],
                         user['phone_num'], user['email'], user['gender'], currentDateTime, path1,))
            con.commit()
            new_acc = get_user(user['username'])

    return new_acc


# To view user credentials based on their username
@user_bp.route('/profile/<username>', methods=['GET'])
def get_user(username):
    user_profile = {}
    cur.execute("SELECT * FROM USER_T WHERE user_username = ?", (username, ))
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
    update = request.json
    user_name = ''

    if update['fullname'] or update['ic_number'] or update['username'] or update['password'] or update['dob'] \
            or update['phone'] or update['email'] or update['gender'] or update['fulladdress'] or update['aboutme'] \
            or update['profile_pic']:

        p = update['profile_pic']
        path = r'C:\Users\rosss\OneDrive\Documents\GitHub\bizBangkit\backend\pictures'
        path1 = path + '\\' + p

        cur.execute("UPDATE USER_T SET user_fullname = ?, user_ic_no = ?, user_username = ?, user_password = ?, "
                    "user_dob = ?, user_phone = ?, user_email = ?, user_gender = ?, user_fulladdress = ?, "
                    "user_aboutme = ?, user_fpath_profilepic = ? WHERE user_id =?",
                    (update['fullname'], update['ic_number'], update['username'], update['password'], update['dob'],
                     update['phone'], update['email'], update['gender'], update['fulladdress'],
                     update['aboutme'], path1, user_id,))
        con.commit()

    find = cur.execute("SELECT user_username FROM USER_T WHERE user_id = ?", (user_id,))
    for i in find:
        user_name = i[0]
    updated = get_user(user_name)

    return updated
