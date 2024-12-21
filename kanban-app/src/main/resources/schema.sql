
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE TABLE IF NOT EXISTS kanban  (
    id SERIAL PRIMARY KEY,
    title TEXT
) ;

CREATE TABLE IF NOT EXISTS image (
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      image BYTEA
    );