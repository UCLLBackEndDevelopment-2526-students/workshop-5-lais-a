DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS profiles;

CREATE TABLE profiles(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bio VARCHAR(255),
    location VARCHAR(255),
    interests VARCHAR(255)
);

CREATE TABLE users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE, 
    name VARCHAR(255), 
    age INT, 
    password VARCHAR(255),
    profile_id BIGINT UNIQUE,
    FOREIGN KEY (profile_id) REFERENCES profiles(id)
);
