-- Bases de datos de cada ambiente
CREATE DATABASE IF NOT EXISTS productos_dev;
CREATE DATABASE IF NOT EXISTS productos_qa;
CREATE DATABASE IF NOT EXISTS productos_prod;

-- En PROD se usa ddl-auto=validate: Hibernate NO crea tablas, solo verifica
-- que existan. Por eso las tablas de producción se crean a mano.
USE productos_prod;

CREATE TABLE IF NOT EXISTS producto (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(255),
    categoria VARCHAR(255),
    stock     INT,
    precio    DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS auditoria_log (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    accion  VARCHAR(255),
    metodo  VARCHAR(255),
    fecha   DATETIME(6),
    detalle VARCHAR(255),
    usuario VARCHAR(255)
);
