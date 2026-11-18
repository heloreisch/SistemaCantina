CREATE DATABASE IF NOT EXISTS sistemacantina CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS 'aluno_cd'@'localhost' IDENTIFIED BY 'aluno_pw';
GRANT ALL PRIVILEGES ON sistemacantina.* TO 'aluno_cd'@'localhost';
FLUSH PRIVILEGES;
USE sistemacantina;






DROP TABLE IF EXISTS item_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS produto;

CREATE TABLE IF NOT EXISTS Produto (
 id INT PRIMARY KEY AUTO_INCREMENT,
 nome VARCHAR(20) NOT NULL,
 categoria ENUM ('BEBIDA','SALGADO','MARMITA','DOCE','OUTROS'),
 preco DECIMAL(6,2) not null
 );
 
 create table  Pedido (
 id_pedido int primary key auto_increment,
status_pedido ENUM('RECEBIDO','PREPARANDO','FINALIZADO','PAGO', 'ENTREGUE','CANCELADO'),
 preco_total DECIMAL(6,2) not null,
 tipo_pagamento ENUM('NA_ENTREGA','PIX','CARTAO')

 );
 
 CREATE TABLE item_pedido (
    id_pedido  INT NOT NULL,
    id_produto INT NOT NULL,
    PRIMARY KEY (id_pedido, id_produto),
    FOREIGN KEY (id_pedido)  REFERENCES pedido(id_pedido),
    FOREIGN KEY (id_produto) REFERENCES produto(id)
);

INSERT INTO produto (nome, categoria, preco) VALUES
('Coxinha', 'SALGADO', 6.50),
('Suco', 'BEBIDA', 5.00);

INSERT INTO pedido (id_pedido, status_pedido, preco_total, tipo_pagamento) VALUES
(1, 'PREPARANDO',   11.50, 'PIX');

INSERT INTO item_pedido (id_pedido, id_produto) VALUES
(1, 1), (1, 2);


