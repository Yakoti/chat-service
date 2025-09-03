-- ChatRooms
INSERT INTO chat_rooms (joined_users, pending, route_link) VALUES ('1,2', '3', '/chat/general');
INSERT INTO chat_rooms (joined_users, pending, route_link) VALUES ('2,3', '4', '/chat/bike');
INSERT INTO chat_rooms (joined_users, pending, route_link) VALUES ('1,3', '5', '/chat/adventure');

-- ChatRoomsUsers (include joined status)
INSERT INTO chat_rooms_users (chat_room_id, user_id, status) VALUES (1, 1, true);
INSERT INTO chat_rooms_users (chat_room_id, user_id, status) VALUES (1, 2, true);
INSERT INTO chat_rooms_users (chat_room_id, user_id, status) VALUES (1, 3, false);

INSERT INTO chat_rooms_users (chat_room_id, user_id, status) VALUES (2, 2, true);
INSERT INTO chat_rooms_users (chat_room_id, user_id, status) VALUES (2, 3, true);
INSERT INTO chat_rooms_users (chat_room_id, user_id, status) VALUES (2, 4, false);

INSERT INTO chat_rooms_users (chat_room_id, user_id, status) VALUES (3, 1, true);
INSERT INTO chat_rooms_users (chat_room_id, user_id, status) VALUES (3, 3, true);
INSERT INTO chat_rooms_users (chat_room_id, user_id, status) VALUES (3, 5, false);


