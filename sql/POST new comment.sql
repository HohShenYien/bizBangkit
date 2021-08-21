--- POST /post/comment/{post_id}
-- {
--     user_id: 
--     authkey:
--     forum_reply_content:
-- }
INSERT INTO FORUM_REPLY_T (reply_id, forum_reply_content, forum_reply_datetime, forum_reply_status,
forum_reply_tos_flag, forum_reply_totalpoint, forum_reply_minuspoint, forum_post_id, user_id) 
VALUES (<generated>, <the_content>, <datetime/time generate>, A, A, 0, 0, <post_id>, <user_id>)