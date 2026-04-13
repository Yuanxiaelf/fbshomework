CREATE TABLE IF NOT EXISTS seckill_product (
    id          BIGINT PRIMARY KEY,
    product_id  BIGINT NOT NULL,
    stock       INT    NOT NULL,
    start_time  DATETIME NOT NULL,
    end_time    DATETIME NOT NULL,
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS seckill_order (
    id             BIGINT PRIMARY KEY,
    user_id        BIGINT NOT NULL,
    seckill_id     BIGINT NOT NULL,
    product_id     BIGINT NOT NULL,
    status         TINYINT DEFAULT 0,
    create_time    DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_seckill (user_id, seckill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO seckill_product (id, product_id, stock, start_time, end_time)
VALUES (1, 1001, 100, '2025-01-01 00:00:00', '2099-12-31 23:59:59');