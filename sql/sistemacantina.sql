CREATE DATABASE IF NOT EXISTS sistemacantina CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS 'aluno_cd'@'localhost' IDENTIFIED BY 'aluno_pw';
GRANT ALL PRIVILEGES ON sistemacantina.* TO 'aluno_cd'@'localhost';
FLUSH PRIVILEGES;
USE sistemacantina;


CREATE TABLE IF NOT EXISTS Produto (
 id INT PRIMARY KEY AUTO_INCREMENT,
 nome VARCHAR(20) NOT NULL,
 categoria VARCHAR(40) NOT NULL,
 preco DECIMAL(6,2),
 
);
