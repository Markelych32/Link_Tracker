CREATE TABLE chat
(
    id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    tg_chat_id BIGINT NOT NULL UNIQUE
);

CREATE TABLE link
(
    id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    url         VARCHAR   NOT NULL UNIQUE,
    last_update TIMESTAMP NOT NULL
);

CREATE TABLE chat_link
(
    chat_id BIGINT REFERENCES chat (id),
    link_id BIGINT REFERENCES link (id),
    PRIMARY KEY (chat_id, link_id)
);
