from flask import Blueprint, request, jsonify
import sqlite3
import datetime
from collections import defaultdict

posts_bp = Blueprint('posts', __name__)

# CONNECTING TO DATABASE FOR STORING AND READING PURPOSES
con = sqlite3.connect('bizBangkit.db', check_same_thread=False)
cur = con.cursor()
con.commit()

currentDateTime = datetime.datetime.now()


# To create new post in the forum
@posts_bp.route('/create/post/<user_authkey>', methods=['POST'])
def create_post(user_authkey):
    post = request.json  # Have to use .json to be able to verify user
    forum_post_id = ''

    # To check if user_authkey matches with the inputted user_id
    cur.execute("SELECT user_id FROM USER_T WHERE user_authkey = ?", (user_authkey,))
    check = cur.fetchone()
    check1 = check[0]

    data = post['user_id']

    if data == check1:
        cur.execute("""INSERT INTO FORUM_POST_T (forum_post_id, forum_post_title, forum_post_content, 
        forum_post_datetime, forum_post_status, forum_post_tos_flag, forum_post_totalpoint, forum_post_minuspoint, 
        forum_post_viewcount, user_id)
        VALUES ( (SELECT max(forum_post_id) FROM FORUM_POST_T)+1, ?, ?, ?, 'A', 'A', 0, 0, 0, ?)""",
                    (post['title'], post['content'], currentDateTime, data, ))
        con.commit()

        cur.execute("SELECT MAX(forum_post_id) AS max_post_id FROM FORUM_POST_T")
        find = cur.fetchall()
        for i in find:
            forum_post_id = i[0]

        items = len(post['tags'])

        for i in range(0, items):
            cur.execute("""INSERT INTO FORUM_TAG_T (forum_post_id, forum_tag_name) VALUES (?, ?) """,
                        (forum_post_id, post['tags'][i]))
            con.commit()

        return jsonify({'Status': 'New forum post created!', 'Verification': 'stored user_id matches with input data',
                        'Forum Title': post['title'], 'Forum Content': post['content']})
    else:
        return jsonify({'Stored user_id': check1, 'Input': data, 'message': 'Invalid user_id with the database!!'})


# To display a post based on the post_id
@posts_bp.route('/post/<post_id>', methods=['GET'])
def get_post(post_id):

    cur.execute("SELECT u.user_id, u.user_username, fp.forum_post_id, fp.forum_post_title, fp.forum_post_content, "
                "fp.forum_post_totalpoint, fp.forum_post_minuspoint, fp.forum_post_datetime, u.user_fpath_profilepic "
                "FROM FORUM_POST_T fp JOIN USER_T u ON fp.user_id = u.user_id WHERE forum_post_id = ?",
                (post_id,))
    forum = cur.fetchall()
    forum_dict = {}
    for i in forum:
        forum_dict = {'user_id': i[0], 'username': i[1], 'forum_post_id': i[2], 'forum_post_title': i[3],
                      'forum_post_content': i[4], 'forum_post_totalpoint': i[5], 'forum_post_minuspoint': i[6],
                      'forum_post_datetime': i[7], 'user_fpath_profilepic': i[8]}

    cur.execute("SELECT forum_reply_content, forum_reply_datetime, forum_reply_totalpoint, forum_reply_minuspoint "
                "FROM FORUM_REPLY_T fr JOIN USER_T u ON fr.user_id = u.user_id WHERE forum_post_id = ?",
                (post_id,))
    reply = cur.fetchall()
    reply_dict = {}
    for i in reply:
        reply_dict = {'forum_reply_content': i[0], 'forum_reply_datetime': i[1], 'forum_reply_totalpoint': i[2],
                      'forum_reply_minuspoint': i[3]}

    cur.execute("SELECT forum_tag_name FROM FORUM_TAG_T WHERE forum_post_id = ?", (post_id,))
    tags = cur.fetchall()
    tags_list = []
    for i in tags:
        tags_dict = {'forum_tag_name': i[0]}
        tags_list.append(tags_dict)

    cur.execute("UPDATE FORUM_POST_T SET forum_post_viewcount = forum_post_viewcount + 1 "
                "WHERE forum_post_id = ?",
                (post_id,))
    con.commit()

    return jsonify({'Forum Content': forum_dict, 'Forum Replies': reply_dict, 'Forum Tags': tags_list})


# To reply / comment on a post based on the post_id
@posts_bp.route('/post/comment/<post_id>', methods=['POST'])
def post_reply(post_id):
    reply = request.json

    # To check whether the authkey belongs to the inputted user_id
    cur.execute("SELECT user_id FROM USER_T WHERE user_authkey = ?", (reply['authkey'],))
    check = cur.fetchone()
    check1 = check[0]

    data = reply['user_id']

    if data == check1:
        cur.execute("""INSERT INTO FORUM_REPLY_T (reply_id, forum_reply_content, forum_reply_datetime, 
        forum_reply_status, forum_reply_tos_flag, forum_reply_totalpoint, forum_reply_minuspoint, forum_post_id, user_id)
        VALUES ((SELECT max(reply_id) FROM FORUM_REPLY_T)+1, ?, ?, 'A', 'A', 0, 0, ?, ?)""",
                    (reply['forum_reply_content'], currentDateTime, post_id, reply['user_id'],))
        con.commit()
        return jsonify({'Success': 'New reply created!', 'forum_reply_content': reply['forum_reply_content']})
    else:
        return jsonify({'Error': 'user_id and authkey does not match!'})


# To get the most recent post in descending order
@posts_bp.route('/post/recent', methods=['GET'])
def recent_post():
    n = request.args.get('n', type=int)  # LIMIT n (number of records shown)
    q = request.args.get('q', default=0, type=int)  # OFFSET q (start from where)

    cur.execute("SELECT u.user_id, u.user_username, fp.forum_post_id, fp.forum_post_title, fp.forum_post_content, "
                "fp.forum_post_totalpoint, fp.forum_post_minuspoint, fp.forum_post_datetime, u.user_fpath_profilepic "
                "FROM FORUM_POST_T fp JOIN USER_T u ON fp.user_id = u.user_id "
                "ORDER BY forum_post_id DESC LIMIT ? OFFSET ?", (n, q, ))  # max forum_post_id to low forum_post_id
    results = cur.fetchall()  # return a list by grouped by respective post_id

    cur.execute("SELECT * FROM FORUM_TAG_T WHERE forum_post_id IN "
                "(SELECT forum_post_id FROM FORUM_POST_T)")
    dict_all = cur.fetchall()
    d = defaultdict(list)  # tag_list
    for post_id, tag_name in dict_all:
        d[post_id].append(tag_name)

    res = []
    for result in results:
        tmp = {}
        tmp['user_id'] = result[0],
        tmp['username'] = result[1],
        tmp['forum_post_id'] = result[2],
        tmp['forum_post_title'] = result[3],
        tmp['forum_post_content'] = result[4],
        tmp['forum_post_totalpoint'] = result[5],
        tmp['forum_post_minuspoint'] = result[6],
        tmp['forum_post_datetime'] = result[7],
        tmp['user_fpath_profilepic'] = result[8],
        tmp['tags'] = d[result[2]]
        res.append(tmp)

    return jsonify({'res': res})
