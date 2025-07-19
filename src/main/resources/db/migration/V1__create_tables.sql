CREATE TABLE apps (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  api_key VARCHAR(255) NOT NULL UNIQUE,
  created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  app_id BIGINT NOT NULL REFERENCES apps(id),
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  CONSTRAINT users_app_email_unique UNIQUE (app_id, email)
);

CREATE TABLE users_data (
  id BIGSERIAL PRIMARY KEY,
  app_id BIGINT NOT NULL REFERENCES apps(id),
  user_id BIGINT NOT NULL REFERENCES users(id),
  json_data JSONB NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE users_file (
  id BIGSERIAL PRIMARY KEY,
  app_id BIGINT NOT NULL REFERENCES apps(id),
  user_id BIGINT NOT NULL REFERENCES users(id),
  file_url TEXT NOT NULL,
  file_type VARCHAR(50),
  metadata JSONB,
  created_at TIMESTAMP NOT NULL DEFAULT now()
);
