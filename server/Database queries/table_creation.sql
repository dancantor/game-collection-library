CREATE TABLE GameLibrary (
	id INT PRIMARY KEY,
	name VARCHAR(63),
	producer VARCHAR(31),
	purchaseDate VARCHAR(11),
	lastTimePlayed VARCHAR(18),
	price FLOAT,
	currency VARCHAR(15),
	platform VARCHAR(7),
	formatOfGame VARCHAR(15),
	picturePath VARCHAR(255)
);