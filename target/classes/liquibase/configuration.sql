-- liquibase formatted sql

-- changeset disaev:1
CREATE TABLE notification_tasks (
                                    id BIGSERIAL PRIMARY KEY,
                                    chat_id BIGINT NOT NULL,
                                    message TEXT NOT NULL,
                                    notification_time TIMESTAMP NOT NULL
);