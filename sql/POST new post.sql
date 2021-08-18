--- POSTING, you will check the user id and authkey if they match,
--- if matches then this user posts a new post as below

INSERT INTO FORUM_POST_T (forum_post_id, forum_post_title, forum_post_content, forum_post_datetime, forum_post_status,
forum_post_tos_flag, forum_post_total_point, forum_post_minuspoint, user_id) 
VALUES (<generated>, <from_request>, <from_request>, <use time/datetime>, <not sure>, 0, 0,
<user_id>)

--- setting the tags for posts
--- the tags in the request will be like 'news|kuala lumpur|another tag'
--- so you need to split it first, and then execute the following query a few times

INSERT INTO FORUM_TAG_T (forum_post_id, forum_tag_name) VALUES (<the_post_id>, <the_tag_name>)