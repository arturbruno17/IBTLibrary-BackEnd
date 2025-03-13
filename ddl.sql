CREATE TABLE books(
    id SERIAL PRIMARY KEY NOT NULL,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    title TEXT NOT NULL,
    author TEXT,
    quantity INT NOT NULL DEFAULT 1
);

CREATE TYPE ROLE AS ENUM ('LIBRARIAN', 'READER', 'ADMIN');

CREATE TABLE people(
    id SERIAL PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    role ROLE NOT NULL DEFAULT 'READER',
    email TEXT UNIQUE NOT NULL,
    salt TEXT NOT NULL,
    hash_password TEXT NOT NULL
);

CREATE TABLE loan(
    id SERIAL PRIMARY KEY NOT NULL,
    people_id INT NOT NULL REFERENCES people(id),
    book_id INT NOT NULL REFERENCES books(id),
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    duration INT NOT NULL DEFAULT 15,
    return_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL
);

INSERT INTO people (name, role, email, salt, hash_password) VALUES ('Artur Bruno', 'ADMIN', 'arturjuliao20@gmail.com', '1aed60d25ff24788f5d05d5428b7bdb1aaf5a8570aa82126d05c9dd71b313b8e', '82739b76cb4b898faa695a9511f5688aa2ade4b434ef03a8701befb8b3589b67');
