CREATE DATABASE IF NOT EXISTS sistemacantina CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS 'aluno_cd'@'localhost' IDENTIFIED BY 'aluno_pw';
GRANT ALL PRIVILEGES ON sistemacantina.* TO 'aluno_cd'@'localhost';
FLUSH PRIVILEGES;
USE sistemacantina;


CREATE TABLE IF NOT EXISTS Produto (
 id INT PRIMARY KEY AUTO_INCREMENT,
 nome VARCHAR(20) NOT NULL,
 categoria VARCHAR(40) NOT NULL,
 preco DECIMAL(6,2) not null
 );
 
 create table if not exists Pedido (
 id int primary key auto_increment,
 id_produto int not null,
status_pedido varchar(40) not null,
 precoTotal DECIMAL(6,2) not null,
 tipo_pagamento varchar(40) not null,
 horario datetime (10) not null,
 FOREIGN KEY(id_produto) references Produto (id)

 );
