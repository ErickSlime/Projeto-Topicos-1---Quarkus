-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-1');
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-2');
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-3');

-- insert into pessoa (nome) values('Marco');
-- insert into pessoa (nome) values('Fredson');

-- insert into pessoafisica(id, cpf, sexo) values (1, '111.111.111-11', 1);
-- insert into pessoafisica(id, cpf) values (2, '222.222.222-22');

--  insert into estado (nome, sigla) values( 'Tocantins', 'TO');
--  insert into estado (nome, sigla) values( 'Goiás', 'GO');
--  insert into estado (nome, sigla) values( 'São Paulo', 'SP');
--  insert into estado (nome, sigla) values( 'Rio de Janeiro', 'RJ');
--  insert into estado (nome, sigla) values( 'Pará', 'PA');

INSERT INTO estado (nome, sigla)
VALUES 
('Acre', 'AC'),
('Alagoas', 'AL'),
('Amapá', 'AP'),
('Amazonas', 'AM'),
('Bahia', 'BA'),
('Ceará', 'CE'),
('Distrito Federal', 'DF'),
('Espírito Santo', 'ES'),
('Goiás', 'GO'),
('Maranhão', 'MA'),
('Mato Grosso', 'MT'),
('Mato Grosso do Sul', 'MS'),
('Minas Gerais', 'MG'),
('Pará', 'PA'),
('Paraíba', 'PB'),
('Paraná', 'PR'),
('Pernambuco', 'PE'),
('Piauí', 'PI'),
('Rio de Janeiro', 'RJ'),
('Rio Grande do Norte', 'RN'),
('Rio Grande do Sul', 'RS'),
('Rondônia', 'RO'),
('Roraima', 'RR'),
('Santa Catarina', 'SC'),
('São Paulo', 'SP'),
('Sergipe', 'SE'),
('Tocantins', 'TO');

insert into municipio (nome, id_estado) values( 'Palmas', 27);
insert into municipio (nome, id_estado) values( 'Paraiso do Tocantins', 27);

insert into marca (nome, cnpj) values( 'Ambev', '1236547891234');
insert into marca (nome, cnpj) values( 'Coca-Cola', '5954987634521');
insert into marca (nome, cnpj) values( 'Schin', '65384921687534');
insert into marca (nome, cnpj) values( 'Hideal', '30455198916563');

insert into produto(nome, valor, estoque) values ('Antartica' , 2.50, 50);
insert into produto(nome, valor, estoque) values ('Skol', 3.50, 40);
insert into produto(nome, valor, estoque) values ('Coca' , 4.00, 20);
insert into produto(nome, valor, estoque) values ('Guarana', 3.50, 30);

insert into bebida (id, id_marca, descricao, tipoBebida) values(1, 1, 'Muito Boa', 1);
insert into bebida (id, id_marca, descricao, tipoBebida) values(2, 1, 'Cerveja Top', 1);
insert into bebida (id, id_marca, descricao, tipoBebida) values(3, 2, 'Coca Faz Mal', 2);
insert into bebida (id, id_marca, descricao, tipoBebida) values(4, 3, 'Guarana faz Bem', 2);

insert into telefone(codigoArea, numero) values ('63', '981795463');
insert into telefone(codigoArea, numero) values ('63', '986532145');

insert into endereco(logradouro,bairro,cep,numero,complemento,id_municipio) values ('rua1', 'Sonho meu', '9849894', '1', 'Mato' , 1);
insert into endereco(logradouro,bairro,cep,numero,complemento,id_municipio) values ('rua2', 'Sonho Nosso', '7894651', '2', 'Sorveteria' , 2);

insert into pessoa(nome) values ('Erick Santos');
insert into pessoa(nome) values ('Joaoasdas');

insert into pessoafisica (id,email, cpf, sexo) values( 1,'ericksantos@unitins.br', '70385420113', 1);
insert into pessoafisica (id,email, cpf, sexo) values( 2,'Joao@unitins.br', '5165561651', 2);

insert into usuario (loginUsu, senha, id_telefone, id_endereco, id_pessoafisica) values( 'Erick', '3F9r0FaVCmV1fvmPOf4odavPHE2EGqbmP3WHVtTyKqyoJSqQYu86OtyV+0BdPeKUUGwrdAbZAjueiNZAHW0E3w==', 1, 1, 1);
insert into usuario (loginUsu, senha, id_telefone, id_endereco, id_pessoafisica) values( 'Joao', '3F9r0FaVCmV1fvmPOf4odavPHE2EGqbmP3WHVtTyKqyoJSqQYu86OtyV+0BdPeKUUGwrdAbZAjueiNZAHW0E3w==', 2, 2, 2);

insert into lista_produtos (id_usuario, id_produto) values (1,1);

insert into perfis (id_usuario, perfil) values (1, 'Admin');
insert into perfis (id_usuario, perfil) values (1, 'User');
insert into perfis (id_usuario, perfil) values (2, 'User');
insert into perfis (id_usuario, perfil) values (2, 'Admin');

insert into itemcompra(quant, totalItem, idComprado, id_usuario, id_produto) values (5, 12.50, true , 2, 1);
insert into itemcompra(quant, totalItem, idComprado, id_usuario, id_produto) values (5, 12.50, false , 2, 2);

insert into formapagamento(valor, confirmacaoPagamento, dataConfirmacaoPagamento) values( 0, true, '2023-05-22');
insert into pix(id, nome, cpf, dataExpiracaoTokenPix) values (1,'Joao', '5165561651', '2023-05-22');

insert into compra(totalCompra, id_endereco, id_usuario, id_pagamento) values(0, 1, 2, 1);