CREATE TABLE IF NOT EXISTS chat_link
(
    chat_id BIGINT REFERENCES chat (id) ON DELETE CASCADE,
    link_id BIGINT REFERENCES link (id),
    PRIMARY KEY (chat_id, link_id)
);
