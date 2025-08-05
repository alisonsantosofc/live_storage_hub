CREATE TABLE verification_codes (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(6) NOT NULL,
  app_id BIGINT NOT NULL REFERENCES apps(id),
  user_id BIGINT NOT NULL REFERENCES users(id),
  expires_at TIMESTAMP NOT NULL DEFAULT now(),
  created_at TIMESTAMP NOT NULL DEFAULT now()
);

