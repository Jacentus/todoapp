DROP table if exists tasks;
CREATE table tasks(
id int primary key auto_increment,
description varchar(100) not null,
done bit
)