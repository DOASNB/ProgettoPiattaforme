



CREATE SEQUENCE user_seq;

CREATE TABLE "user" (
	id INTEGER DEFAULT NEXTVAL ('user_seq') PRIMARY KEY,
	code VARCHAR(70),
	firstname VARCHAR(50),
	lastname VARCHAR(50),
	phone_number VARCHAR(20),
	email VARCHAR(90),
	address VARCHAR(150)


);

CREATE TYPE category as ENUM('DECORAZIONE','STRUMENTO','GIOCATTOLO');

CREATE SEQUENCE product_seq;

CREATE TABLE product (
	id INTEGER DEFAULT NEXTVAL ('product_seq') PRIMARY KEY,
	name VARCHAR(50),
	code VARCHAR(70),
	description VARCHAR(500),
	price FLOAT,
	quantity INTEGER,
    category category

);

CREATE SEQUENCE order_seq;

CREATE TABLE "order" (
	id INTEGER DEFAULT NEXTVAL ('order_seq') PRIMARY KEY,
	buyer INTEGER,
	purchase_time TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    purchase_date DATE DEFAULT CURRENT_DATE,
    FOREIGN KEY (buyer) REFERENCES "user" (id)
);
	
CREATE SEQUENCE product_in_purchase_seq;

CREATE TABLE product_in_purchase (
	id INTEGER DEFAULT NEXTVAL ('product_in_purchase_seq') PRIMARY KEY,
	related_purchase INTEGER,
	product INTEGER,
	quantity INTEGER,
    FOREIGN KEY (related_purchase) REFERENCES "order" (id),
    FOREIGN KEY (product) REFERENCES product (id)
);





