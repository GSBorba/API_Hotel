-- tables
-- Table: avaliacao
CREATE TABLE avaliacao (
    id serial  NOT NULL,
    nota int  NOT NULL,
    descricao varchar(4000)  NULL,
    id_reserva int  NOT NULL,
    CONSTRAINT avaliacao_pk PRIMARY KEY (id)
);

-- Table: cama
CREATE TABLE cama (
    id serial  NOT NULL,
    tipo_cama varchar(50)  NOT NULL,
    CONSTRAINT cama_pk PRIMARY KEY (id)
);

-- Table: cama_quarto
CREATE TABLE cama_quarto (
    id serial  NOT NULL,
    qt_cama int  NOT NULL,
    id_quarto int  NOT NULL,
    id_cama int  NOT NULL,
    CONSTRAINT cama_quarto_pk PRIMARY KEY (id)
);

-- Table: cliente
CREATE TABLE cliente (
    id serial  NOT NULL,
    nome varchar(255)  NOT NULL,
    cpf varchar(14)  NOT NULL,
    telefone varchar(15)  NOT NULL,
    email varchar(255)  NULL,
    ativo boolean  NOT NULL DEFAULT true,
    id_endereco int  NOT NULL,
    CONSTRAINT cliente_pk PRIMARY KEY (id)
);

-- Table: endereco
CREATE TABLE endereco (
    id serial  NOT NULL,
    cep varchar(9)  NOT NULL,
    uf varchar(255)  NOT NULL,
    cidade varchar(255)  NOT NULL,
    logradouro varchar(255)  NOT NULL,
    complemento varchar(255)  NULL,
    numero int  NULL,
    CONSTRAINT endereco_pk PRIMARY KEY (id)
);

-- Table: hotel
CREATE TABLE hotel (
    id serial  NOT NULL,
    nome varchar(255)  NOT NULL,
    telefone varchar(15)  NOT NULL,
    email varchar(255)  NOT NULL,
    descricao varchar(4000)  NOT NULL,
    categoria varchar(15)  NOT NULL,
    id_endereco int  NOT NULL,
    CONSTRAINT hotel_pk PRIMARY KEY (id)
);

-- Table: quarto
CREATE TABLE quarto (
    id serial  NOT NULL,
    numero int  NOT NULL,
    tipo_quarto varchar(50)  NOT NULL,
    descricao varchar(4000)  NOT NULL,
    preco_diaria decimal(10,2)  NOT NULL,
    id_hotel int  NOT NULL,
    CONSTRAINT quarto_pk PRIMARY KEY (id)
);

-- Table: quarto_reserva
CREATE TABLE quarto_reserva (
    id serial  NOT NULL,
    preco_diaria decimal(10,2)  NOT NULL,
    id_reserva int  NOT NULL,
    id_quarto int  NOT NULL,
    CONSTRAINT quarto_reserva_pk PRIMARY KEY (id)
);

-- Table: reserva
CREATE TABLE reserva (
    id serial  NOT NULL,
    checkin timestamp  NOT NULL,
    checkout timestamp  NOT NULL,
    valor decimal(10,2)  NOT NULL,
    id_cliente int  NOT NULL,
    CONSTRAINT reserva_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: Copy_of_avaliacao_reserva (table: avaliacao)
ALTER TABLE avaliacao ADD CONSTRAINT Copy_of_avaliacao_reserva
    FOREIGN KEY (id_reserva)
    REFERENCES reserva (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: Copy_of_reserva_cliente (table: reserva)
ALTER TABLE reserva ADD CONSTRAINT Copy_of_reserva_cliente
    FOREIGN KEY (id_cliente)
    REFERENCES cliente (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: cama_quarto_cama (table: cama_quarto)
ALTER TABLE cama_quarto ADD CONSTRAINT cama_quarto_cama
    FOREIGN KEY (id_cama)
    REFERENCES cama (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: cama_quarto_quarto (table: cama_quarto)
ALTER TABLE cama_quarto ADD CONSTRAINT cama_quarto_quarto
    FOREIGN KEY (id_quarto)
    REFERENCES quarto (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: cliente_endereco (table: cliente)
ALTER TABLE cliente ADD CONSTRAINT cliente_endereco
    FOREIGN KEY (id_endereco)
    REFERENCES endereco (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: hotel_endereco (table: hotel)
ALTER TABLE hotel ADD CONSTRAINT hotel_endereco
    FOREIGN KEY (id_endereco)
    REFERENCES endereco (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: quarto_hotel (table: quarto)
ALTER TABLE quarto ADD CONSTRAINT quarto_hotel
    FOREIGN KEY (id_hotel)
    REFERENCES hotel (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: quarto_reserva_quarto (table: quarto_reserva)
ALTER TABLE quarto_reserva ADD CONSTRAINT quarto_reserva_quarto
    FOREIGN KEY (id_quarto)
    REFERENCES quarto (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: quarto_reserva_reserva (table: quarto_reserva)
ALTER TABLE quarto_reserva ADD CONSTRAINT quarto_reserva_reserva
    FOREIGN KEY (id_reserva)
    REFERENCES reserva (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;
