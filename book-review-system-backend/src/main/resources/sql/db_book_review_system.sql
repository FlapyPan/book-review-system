CREATE TABLE t_user
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    avatar_id  INT                NOT NULL,
    username   VARCHAR(50) UNIQUE NOT NULL,
    password   VARCHAR(50)        NOT NULL,
    email      VARCHAR(50)        NOT NULL,
    phone      VARCHAR(20)        NOT NULL,
    roles      VARCHAR(20)        NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE t_book
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    cover_id     INT           NOT NULL,
    book_name    VARCHAR(100)  NOT NULL,
    author       VARCHAR(50)   NOT NULL,
    publisher    VARCHAR(50)   NOT NULL,
    translator   VARCHAR(50),
    publish_date DATE          NOT NULL,
    pages        INT           NOT NULL,
    price        DECIMAL(8, 2) NOT NULL,
    binding      VARCHAR(20),
    isbn         VARCHAR(20),
    status       INT      DEFAULT 0,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE t_image
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    filepath   VARCHAR(200) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE t_comment
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    content    TEXT NOT NULL,
    user_id    INT  NOT NULL,
    book_id    INT  NOT NULL,
    status     INT      DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE t_ebook
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    filepath   VARCHAR(200) NOT NULL,
    book_id    INT          NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE t_member
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT NOT NULL,
    level      INT      DEFAULT 0,
    status     INT      DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
