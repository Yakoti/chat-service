
INSERT INTO chat_rooms (joined_users, pending_users, route_link) VALUES ('Alice', 'Bob,Charlie', '/chat/general');
INSERT INTO chat_rooms (joined_users, pending_users, route_link) VALUES ('Bob', 'Charlie,Dave', '/chat/bike');
INSERT INTO chat_rooms (joined_users, pending_users, route_link) VALUES ('Alice', 'Charlie,Eve', '/chat/adventure');


-- Chat Room 1
INSERT INTO chat_rooms_users (chat_room_id, user_id, name, status) VALUES (1, 1, 'Alice', true);
INSERT INTO chat_rooms_users (chat_room_id, user_id, name, status) VALUES (1, 2, 'Bob', false);
INSERT INTO chat_rooms_users (chat_room_id, user_id, name, status) VALUES (1, 3, 'Charlie', false);

-- Chat Room 2
INSERT INTO chat_rooms_users (chat_room_id, user_id, name, status) VALUES (2, 2, 'Bob', true);
INSERT INTO chat_rooms_users (chat_room_id, user_id, name, status) VALUES (2, 3, 'Charlie', false);
INSERT INTO chat_rooms_users (chat_room_id, user_id, name, status) VALUES (2, 4, 'Dave', false);

-- Chat Room 3
INSERT INTO chat_rooms_users (chat_room_id, user_id, name, status) VALUES (3, 1, 'Alice', true);
INSERT INTO chat_rooms_users (chat_room_id, user_id, name, status) VALUES (3, 3, 'Charlie', false);
INSERT INTO chat_rooms_users (chat_room_id, user_id, name, status) VALUES (3, 5, 'Eve', false);
