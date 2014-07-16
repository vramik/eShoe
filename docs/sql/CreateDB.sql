CREATE DATABASE eshoe;
GRANT ALL ON eshoe.* TO eshoe@'%' IDENTIFIED BY 'eshoe';
GRANT ALL ON eshoe.* TO eshoe@localhost IDENTIFIED BY 'eshoe';
FLUSH PRIVILEGES;
