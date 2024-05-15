
CREATE SCHEMA store;


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

CREATE SEQUENCE product_seq;

CREATE TABLE product (
	id INTEGER DEFAULT NEXTVAL ('product_seq') PRIMARY KEY,
	name VARCHAR(50),
	code VARCHAR(70),
	description VARCHAR(500),
	price FLOAT,
	quantity INTEGER

);

CREATE SEQUENCE purchase_seq;

CREATE TABLE purchase (
	id INTEGER DEFAULT NEXTVAL ('purchase_seq') PRIMARY KEY,
	buyer INTEGER,
	purchase_time TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (buyer) REFERENCES "user" (id)
);
	
CREATE SEQUENCE product_in_purchase_seq;

CREATE TABLE product_in_purchase (
	id INTEGER DEFAULT NEXTVAL ('product_in_purchase_seq') PRIMARY KEY,
	related_purchase INTEGER,
	product INTEGER,
	quantity INTEGER,
    FOREIGN KEY (related_purchase) REFERENCES purchase (id),
    FOREIGN KEY (product) REFERENCES product (id)
);

CREATE SEQUENCE delivery_seq;

DROP TABLE delivery;
CREATE TABLE delivery (
    id INTEGER DEFAULT NEXTVAL('delivery_seq') PRIMARY KEY ,
    starting_date DATE DEFAULT CURRENT_DATE,
    delivery_date DATE,
    related_purchase INTEGER,
    delivered BOOLEAN DEFAULT false,
    address VARCHAR(80),
    shipped BOOLEAN DEFAULT false,

    FOREIGN KEY (related_purchase) REFERENCES purchase (id)

);


