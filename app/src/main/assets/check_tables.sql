select count(*) from sqlite_master where type = 'table' and (
 name = 'Conversation' and
 name = 'Bridge' and
 name = 'Connection' and
 name = 'Profile' and
 name = 'Icon' and
 name = 'History' and
 name = 'Colour'
);