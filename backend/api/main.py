from flask import Flask, jsonify, request, make_response
import jwt
import datetime
from functools import wraps
import sqlite3
from flask_cors import CORS
from user import user_bp as user_blueprint
from business import business_bp as business_blueprint

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})

app.register_blueprint(user_blueprint)
app.register_blueprint(business_blueprint)

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


def token_required(f):
    # to decorate the inner function by configuring the to-be-decorated function.
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.args.get('token')  # http://127.0.0.1:5000/route?token=alshfjfjdklsfj89549834ur

        if not token:
            return jsonify({'message': 'Token is missing!'}), 401

        try:
            data = jwt.decode(token, app.config['SECRET_KEY'], algorithms=["HS256"])
        except ValueError:
            return jsonify({'message': 'Token is invalid!'}), 401

        return f(*args, **kwargs)

    return decorated


# To login user credentials using their respective email and password
@app.route('/login', methods=['GET', 'POST'])
def login():
    auth = request.authorization

    if auth:
        cur.execute("SELECT * FROM USER_T WHERE user_email = ? AND user_password = ?", (auth.username, auth.password, ))
        find = cur.fetchall()

        if find:
            token = jwt.encode({'user': auth.username,
                                'exp': datetime.datetime.utcnow() + datetime.timedelta(minutes=60)},
                               app.config['SECRET_KEY'])
            return jsonify({'token': token, 'email': auth.username, 'password': auth.password, })

    return make_response('Invalid credentials!', 401,
                         {'WWW-Authenticate': 'Basic realm="Kindly login to access this page"'})


@app.route('/')
def unprotected():
    return jsonify({'Message': 'Welcome to bizBangkit!'})


@app.route('/check')
@token_required
def protected():
    return jsonify({'message': 'This is only available for people with valid tokens.'})


# Might delete if unnecessary / irrelevant
@app.route('/logout')
def logout():
    return jsonify({'message': 'You have successfully logged out from bizBangkit'})


if __name__ == '__main__':
    app.run(debug=True)
