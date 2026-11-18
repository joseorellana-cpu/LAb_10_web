-- ===== TechSolutions SAC: una base de datos por ambiente =====
CREATE DATABASE IF NOT EXISTS empleados_dev;
CREATE DATABASE IF NOT EXISTS empleados_qa;
CREATE DATABASE IF NOT EXISTS empleados_test;
CREATE DATABASE IF NOT EXISTS empleados_prod;

-- La tabla se crea en las 4 bases (en PROD es obligatorio porque ddl-auto=validate)
USE empleados_dev;
CREATE TABLE IF NOT EXISTS empleado(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    email VARCHAR(100),
    sueldo DECIMAL(10,2)
);

USE empleados_qa;
CREATE TABLE IF NOT EXISTS empleado(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    email VARCHAR(100),
    sueldo DECIMAL(10,2)
);

USE empleados_test;
CREATE TABLE IF NOT EXISTS empleado(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    email VARCHAR(100),
    sueldo DECIMAL(10,2)
);

USE empleados_prod;
CREATE TABLE IF NOT EXISTS empleado(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    email VARCHAR(100),
    sueldo DECIMAL(10,2)
);
