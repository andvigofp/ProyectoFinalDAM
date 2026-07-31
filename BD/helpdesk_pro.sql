-- ===========================================
-- HELPDESK PRO - SCRIPT COMPLETO
-- ===========================================

DROP DATABASE IF EXISTS helpdesk_pro;
CREATE DATABASE helpdesk_pro;
USE helpdesk_pro;

CREATE TABLE roles(
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE departamentos(
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL UNIQUE,
 descripcion VARCHAR(255)
);

CREATE TABLE usuarios(
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL,
 apellidos VARCHAR(150) NOT NULL,
 email VARCHAR(150) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL,
 telefono VARCHAR(20),
 activo BOOLEAN DEFAULT TRUE,
 fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
 rol_id BIGINT NOT NULL,
 departamento_id BIGINT,
 FOREIGN KEY (rol_id) REFERENCES roles(id),
 FOREIGN KEY (departamento_id) REFERENCES departamentos(id)
);

CREATE TABLE categorias(
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL UNIQUE,
 descripcion VARCHAR(255)
);

CREATE TABLE prioridades(
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE estados(
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE incidencias(
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 titulo VARCHAR(150) NOT NULL,
 descripcion TEXT NOT NULL,
 fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
 fecha_actualizacion DATETIME,
 usuario_id BIGINT NOT NULL,
 tecnico_id BIGINT,
 categoria_id BIGINT NOT NULL,
 prioridad_id BIGINT NOT NULL,
 estado_id BIGINT NOT NULL,
 FOREIGN KEY(usuario_id) REFERENCES usuarios(id),
 FOREIGN KEY(tecnico_id) REFERENCES usuarios(id),
 FOREIGN KEY(categoria_id) REFERENCES categorias(id),
 FOREIGN KEY(prioridad_id) REFERENCES prioridades(id),
 FOREIGN KEY(estado_id) REFERENCES estados(id)
);

CREATE TABLE comentarios(
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 mensaje TEXT NOT NULL,
 fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
 usuario_id BIGINT NOT NULL,
 incidencia_id BIGINT NOT NULL,
 FOREIGN KEY(usuario_id) REFERENCES usuarios(id),
 FOREIGN KEY(incidencia_id) REFERENCES incidencias(id)
);

CREATE TABLE adjuntos(
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre_archivo VARCHAR(200) NOT NULL,
 url_archivo VARCHAR(500) NOT NULL,
 tipo_archivo VARCHAR(100),
 tamano BIGINT,
 incidencia_id BIGINT NOT NULL,
 FOREIGN KEY(incidencia_id) REFERENCES incidencias(id)
);

CREATE TABLE historiales(
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 accion VARCHAR(255) NOT NULL,
 fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
 usuario_id BIGINT NOT NULL,
 incidencia_id BIGINT NOT NULL,
 FOREIGN KEY(usuario_id) REFERENCES usuarios(id),
 FOREIGN KEY(incidencia_id) REFERENCES incidencias(id)
);

CREATE TABLE etiquetas(
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL UNIQUE,
 descripcion VARCHAR(255)
);

CREATE TABLE incidencias_etiquetas(
 incidencia_id BIGINT,
 etiqueta_id BIGINT,
 PRIMARY KEY(incidencia_id,etiqueta_id),
 FOREIGN KEY(incidencia_id) REFERENCES incidencias(id) ON DELETE CASCADE,
 FOREIGN KEY(etiqueta_id) REFERENCES etiquetas(id) ON DELETE CASCADE
);

INSERT INTO roles(nombre) VALUES
('Administrador'),('Técnico'),('Usuario'),('Supervisor');

INSERT INTO departamentos(nombre,descripcion) VALUES
('Informática','Soporte TI'),
('Recursos Humanos','Gestión del personal'),
('Administración','Gestión administrativa'),
('Producción','Área de producción');

INSERT INTO categorias(nombre,descripcion) VALUES
('Hardware','Equipos'),
('Software','Aplicaciones'),
('Red','Conectividad'),
('Correo','Correo electrónico');

INSERT INTO prioridades(nombre) VALUES
('Baja'),('Media'),('Alta'),('Urgente');

INSERT INTO estados(nombre) VALUES
('Abierta'),('En proceso'),('Resuelta'),('Cerrada');

INSERT INTO usuarios(nombre,apellidos,email,password_hash,telefono,rol_id,departamento_id) VALUES
('Andrés','Fernández','andres@helpdesk.com','$2a$10$demo','600111111',1,1),
('Laura','García','laura@helpdesk.com','$2a$10$demo','600222222',2,1),
('Carlos','Martín','carlos@helpdesk.com','$2a$10$demo','600333333',3,2),
('Marta','Ruiz','marta@helpdesk.com','$2a$10$demo','600444444',4,3);

INSERT INTO incidencias(titulo,descripcion,usuario_id,tecnico_id,categoria_id,prioridad_id,estado_id) VALUES
('PC no enciende','No responde al pulsar el botón',3,2,1,3,1),
('Error de login','No permite iniciar sesión',4,2,2,2,2),
('Sin Internet','No hay conexión',3,2,3,4,2),
('Correo bloqueado','No envía emails',4,2,4,2,3);

INSERT INTO comentarios(mensaje,usuario_id,incidencia_id) VALUES
('Revisando incidencia',2,1),
('Pendiente de respuesta',2,2),
('Router reiniciado',2,3),
('Incidencia solucionada',2,4);

INSERT INTO adjuntos(nombre_archivo,url_archivo,tipo_archivo,tamano,incidencia_id) VALUES
('pc.jpg','/adjuntos/pc.jpg','image/jpeg',200000,1),
('login.png','/adjuntos/login.png','image/png',150000,2),
('router.jpg','/adjuntos/router.jpg','image/jpeg',220000,3),
('correo.pdf','/adjuntos/correo.pdf','application/pdf',350000,4);

INSERT INTO historiales(accion,usuario_id,incidencia_id) VALUES
('Incidencia creada',3,1),
('Asignada al técnico',2,2),
('Prioridad actualizada',2,3),
('Incidencia cerrada',2,4);

INSERT INTO etiquetas(nombre,descripcion) VALUES
('Hardware','Equipo físico'),
('Software','Aplicación'),
('Urgente','Atención inmediata'),
('Red','Conectividad');

INSERT INTO incidencias_etiquetas VALUES
(1,1),
(2,2),
(3,3),
(3,4),
(4,2);
