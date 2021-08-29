from flask import Flask, jsonify, request, make_response
import datetime
import sqlite3
from flask_cors import CORS
from user import user_bp as user_blueprint
from business import business_bp as business_blueprint
from posts import posts_bp as posts_blueprint
from phases import phases_bp as phases_blueprint

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})

app.register_blueprint(user_blueprint)
app.register_blueprint(business_blueprint)
app.register_blueprint(posts_blueprint)
app.register_blueprint(phases_blueprint)

app.config['SECRET_KEY'] = 'secretkey'
JSON_MIME_TYPE = 'application/json; charset=utf-8'
currentDateTime = datetime.datetime.now()

# CONNECTING TO DATABASE FOR STORING AND READING PURPOSES
con = sqlite3.connect('bizBangkit.db', check_same_thread=False)
cur = con.cursor()
con.commit()


def json_response(data='', status=200, headers=None):
    headers = headers or {}
    if 'Content-Type' not in headers:
        headers['Content-Type'] = JSON_MIME_TYPE

    return make_response(data, status, headers)


# To login user credentials using their respective email and password
@app.route('/login', methods=['GET', 'POST'])
def login():
    auth = request.authorization
    user_id = ''
    authkey = ''

    if auth:
        # auth.username = email AND auth.password = password
        cur.execute("SELECT * FROM USER_T WHERE user_email = ? AND user_password = ?", (auth.username, auth.password, ))
        find = cur.fetchall()

        if find:
            show = cur.execute("SELECT user_id, user_authkey FROM USER_T WHERE user_email = ? AND user_password = ?",
                               (auth.username, auth.password,))
            for i in show:
                user_id = i[0]
                authkey = i[1]

            return jsonify({'authkey': authkey, 'user_id': user_id})

    return make_response('Invalid credentials!', 401,
                         {'WWW-Authenticate': 'Basic realm="Kindly login to access this page"'})


# Might delete if unnecessary / irrelevant
@app.route('/')
def unprotected():
    return jsonify({'Message': 'Welcome to bizBangkit!'})


if __name__ == '__main__':
    app.run(debug=True)
