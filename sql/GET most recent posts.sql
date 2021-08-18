--- same q & n from GET business list

SELECT * FROM FORUM_POST_T fp
JOIN USER_T u 
ON fp.user_id = u.user_id
ORDER BY forum_post_id 
LIMIT n OFFSET q

--- from the list of forum_post_id from above this,

SELECT * FROM FORUM_TAG_T WHERE forum_post_id IN (<you can join ths list with comma>)
--- put these into a dictionary first, something like
--- tags_dict = {001: ['tag1', 'tag2', 'tag3']}
--- from the dictionary, you can get the tags and join them with |

--- you should return the id, title, content, tags, totalpoint, minus point, user_id, 
--- username, profile picture
--- date & time