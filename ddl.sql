SET TIME ZONE 'UTC';

CREATE TABLE books
(
    id       SERIAL PRIMARY KEY NOT NULL,
    isbn     VARCHAR(13) UNIQUE NOT NULL,
    title    TEXT               NOT NULL,
    author   TEXT,
    quantity INT                NOT NULL DEFAULT 1
);

CREATE TYPE ROLE AS ENUM ('LIBRARIAN', 'READER', 'ADMIN');

CREATE TABLE people
(
    id            SERIAL PRIMARY KEY NOT NULL,
    name          TEXT               NOT NULL,
    role          ROLE               NOT NULL DEFAULT 'READER',
    email         TEXT UNIQUE        NOT NULL,
    salt          TEXT               NOT NULL,
    hash_password TEXT               NOT NULL
);

CREATE TABLE loan
(
    id          SERIAL PRIMARY KEY NOT NULL,
    people_id   INT                NOT NULL REFERENCES people (id),
    book_id     INT                NOT NULL REFERENCES books (id),
--     start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    duration    INT                NOT NULL DEFAULT 15,
    return_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL
);

INSERT INTO people (name, role, email, salt, hash_password)
VALUES ('Artur Bruno', 'ADMIN', 'arturjuliao20@gmail.com',
        '1aed60d25ff24788f5d05d5428b7bdb1aaf5a8570aa82126d05c9dd71b313b8e',
        '82739b76cb4b898faa695a9511f5688aa2ade4b434ef03a8701befb8b3589b67');

-- Fake ISBNs
INSERT INTO books (isbn, title, author)
VALUES ('9788535902774', 'Dom Casmurro', 'Machado de Assis'),
       ('9788520933103', 'Memórias Póstumas de Brás Cubas', 'Machado de Assis'),
       ('9788503010103', 'O Cortiço', 'Aluísio Azevedo'),
       ('9788520932120', 'Iracema', 'José de Alencar'),
       ('9788503012503', 'Capitães da Areia', 'Jorge Amado'),
       ('9788535914845', 'Grande Sertão: Veredas', 'João Guimarães Rosa'),
       ('9788520932557', 'O Guarani', 'José de Alencar'),
       ('9788503013104', 'Vidas Secas', 'Graciliano Ramos'),
       ('9788503014408', 'Macunaíma', 'Mário de Andrade'),
       ('9788520932564', 'Senhora', 'José de Alencar'),
       ('9788503014606', 'A Moreninha', 'Joaquim Manuel de Macedo'),
       ('9788520932571', 'Lucíola', 'José de Alencar'),
       ('9788503014705', 'O Primo Basílio', 'Eça de Queirós'),
       ('9788503014804', 'A Escrava Isaura', 'Bernardo Guimarães'),
       ('9788503014903', 'O Ateneu', 'Raul Pompeia'),
       ('9788503015009', 'Triste Fim de Policarpo Quaresma', 'Lima Barreto'),
       ('9788503015108', 'O Alienista', 'Machado de Assis'),
       ('9788503015207', 'Quincas Borba', 'Machado de Assis'),
       ('9788503015306', 'A Luneta Mágica', 'Joaquim Manuel de Macedo'),
       ('9788503015405', 'Noite na Taverna', 'Álvares de Azevedo'),
       ('9788503015504', 'O Seminarista', 'Rubem Fonseca'),
       ('9788503015603', 'O Mulato', 'Aluísio Azevedo'),
       ('9788503015702', 'A Carne', 'Júlio Ribeiro'),
       ('9788503015801', 'Clara dos Anjos', 'Lima Barreto'),
       ('9788503015900', 'O Crime do Padre Amaro', 'Eça de Queirós'),
       ('9788503016006', 'A Relíquia', 'Eça de Queirós'),
       ('9788503016105', 'Esaú e Jacó', 'Machado de Assis'),
       ('9788503016202', 'Memorial de Aires', 'Machado de Assis'),
       ('9788503016309', 'Os Maias', 'Eça de Queirós'),
       ('9788503016408', 'A Cidade e as Serras', 'Eça de Queirós'),
       ('9788503016507', 'O Triste Fim de Policarpo Quaresma', 'Lima Barreto'),
       ('9788503016606', 'Recordações do Escrivão Isaías Caminha', 'Lima Barreto'),
       ('9788503016705', 'O Cabeleira', 'Franklin Távora'),
       ('9788503016804', 'Inocência', 'Visconde de Taunay'),
       ('9788503016903', 'A Normalista', 'Adolfo Caminha'),
       ('9788503017009', 'Bom-Crioulo', 'Adolfo Caminha'),
       ('9788503017108', 'O Missionário', 'Inglês de Sousa'),
       ('9788503017207', 'O Barão de Lavos', 'Abel Botelho'),
       ('9788503017306', 'O Conde de Abranhos', 'Eça de Queirós'),
       ('9788503017405', 'O Mandarim', 'Eça de Queirós'),
       ('9788503017504', 'A Ilustre Casa de Ramires', 'Eça de Queirós'),
       ('9788503017603', 'O Mistério da Estrada de Sintra', 'Eça de Queirós'),
       ('9788503017702', 'O Homem', 'Aluísio Azevedo'),
       ('9788503017801', 'O Esqueleto', 'Aluísio Azevedo'),
       ('9788503017900', 'O Livro de Uma Sogra', 'Aluísio Azevedo'),
       ('9788503018004', 'O Coruja', 'Aluísio Azevedo'),
       ('9788503018103', 'O Mulato', 'Aluísio Azevedo'),
       ('9788503018202', 'Casa de Pensão', 'Aluísio Azevedo'),
       ('9788503018301', 'O Cortiço', 'Aluísio Azevedo');