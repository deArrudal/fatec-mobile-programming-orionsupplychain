CREATE DATABASE orionsc;
USE DATABASE orionsc;

CREATE TABLE customer (
	customerId INTEGER NOT NULL,
	customerCNPJ CHAR(14) NOT NULL,
	customerName VARCHAR(100) NOT NULL,
	customerAddress VARCHAR(100) NOT NULL,
	customerPhone VARCHAR(13) NOT NULL,
	PRIMARY KEY (customerId)
);

CREATE TABLE product (
	productId INTEGER NOT NULL,
	productName VARCHAR(100) NOT NULL,
	productPrice DECIMAL(10,2) NOT NULL,
	PRIMARY KEY (productId)
);

CREATE TABLE food (
	foodProductId INTEGER,
	foodProducer VARCHAR(100) NOT NULL,
	FOREIGN KEY (foodProductId) REFERENCES product(productId)
		ON DELETE CASCADE
);

CREATE TABLE goods (
	goodsProductId INTEGER NOT NULL,
	goodsCategory VARCHAR(100) NOT NULL,
	goodsIsBuild BOOLEAN NOT NULL DEFAULT 'FALSE',
	FOREIGN KEY (goodsProductId) REFERENCES product(productId)
		ON DELETE CASCADE
);

CREATE TABLE supplyOrder (
	supplyOrderId INTEGER NOT NULL,
	supplyOrderCustomerId INTEGER,
	supplyOrderDate CHAR(8) NOT NULL,
	supplyOrderDeliveryDate CHAR(8) NOT NULL,
	PRIMARY KEY (supplyOrderId),
	FOREIGN KEY (supplyOrderCustomerId) REFERENCES customer(customerId)
		ON DELETE CASCADE
);

CREATE TABLE item (
	itemSupplyOrderId INTEGER,
	itemProductId INTEGER,
	itemQuantity INTEGER NOT NULL,
	PRIMARY KEY (itemSupplyOrderId, itemProductId),
	FOREIGN KEY (itemSupplyOrderId) REFERENCES supplyOrder(supplyOrderId)
		ON DELETE CASCADE,
	FOREIGN KEY (itemProductId) REFERENCES product(productId)
		ON DELETE CASCADE
);


