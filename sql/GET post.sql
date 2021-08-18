--- GET /post/{post_id}
SELECT * FROM FORUM_POST_T fp
JOIN USER_T u 
ON fp.user_id = u.user_id
WHERE forum_post_id = post_id

--- then get the comments
SELECT * FROM FORUM_REPLY_T fr
JOIN USER_T u
ON fr.user_id = u.user_id
WHERE forum_post_id = post_id

--- and the tags
SELECT * FROM FORUM_TAG_T WHERE forum_post_id

--- update views 
UPDATE FORUM_POST_T
SET forum_post_viewcount = forum_post_viewcount + 1
WHERE forum_post_id = post_id

--- then response should be like 
--- id, title, content, tags, totalpoint, minus point, user_id, username, profile picture
--- date & time, 

--- reply content, reply datetime, reply_totalpoint, reply_minuespoint, user_id
--- username, profile picture, date & time