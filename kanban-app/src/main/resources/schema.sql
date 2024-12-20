CREATE TABLE kanban (
    id SERIAL PRIMARY KEY,
    title TEXT
);

CREATE TABLE task (
                      id SERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description TEXT,
                      color VARCHAR(50),
                      kanban_id INTEGER,
                      status VARCHAR(20)
);