select count(*) from sqlite_master where type = 'table' and (
 name = 'Conversation' or
 name = 'Bridge' or
 name = 'Connection' or
 name = 'Profile' or
 name = 'Icon' or
 name = 'History' or
 name = 'Colour'
);