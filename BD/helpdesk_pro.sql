DROP DATABASE IF EXISTS helpdesk_pro;
CREATE DATABASE helpdesk_pro;
USE helpdesk_pro;

CREATE TABLE rol(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE departamento(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE usuario(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rol_id BIGINT NOT NULL,
    departamento_id BIGINT,

    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (rol_id)
        REFERENCES rol(id),

    CONSTRAINT fk_usuario_departamento
        FOREIGN KEY (departamento_id)
        REFERENCES departamento(id)
);

CREATE TABLE categoria(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE prioridad(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE estado(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE incidencia(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NULL,
    usuario_id BIGINT NOT NULL,
    tecnico_id BIGINT NULL,
    categoria_id BIGINT NOT NULL,
    prioridad_id BIGINT NOT NULL,
    estado_id BIGINT NOT NULL,

    CONSTRAINT fk_incidencia_usuario
        FOREIGN KEY(usuario_id)
        REFERENCES usuario(id),

    CONSTRAINT fk_incidencia_tecnico
        FOREIGN KEY(tecnico_id)
        REFERENCES usuario(id),

    CONSTRAINT fk_incidencia_categoria
        FOREIGN KEY(categoria_id)
        REFERENCES categoria(id),

    CONSTRAINT fk_incidencia_prioridad
        FOREIGN KEY(prioridad_id)
        REFERENCES prioridad(id),

    CONSTRAINT fk_incidencia_estado
        FOREIGN KEY(estado_id)
        REFERENCES estado(id)
);


CREATE TABLE comentario(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensaje TEXT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    incidencia_id BIGINT NOT NULL,

    CONSTRAINT fk_comentario_usuario
        FOREIGN KEY(usuario_id)
        REFERENCES usuario(id),

    CONSTRAINT fk_comentario_incidencia
        FOREIGN KEY(incidencia_id)
        REFERENCES incidencia(id)
);


CREATE TABLE adjunto(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_archivo VARCHAR(200) NOT NULL,
    url_archivo VARCHAR(500) NOT NULL,
    tipo_archivo VARCHAR(100),
    tamano BIGINT,
    incidencia_id BIGINT NOT NULL,

    CONSTRAINT fk_adjunto_incidencia
        FOREIGN KEY(incidencia_id)
        REFERENCES incidencia(id)
);

CREATE TABLE historial(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    accion VARCHAR(255) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    incidencia_id BIGINT NOT NULL,

    CONSTRAINT fk_historial_usuario
        FOREIGN KEY(usuario_id)
        REFERENCES usuario(id),

    CONSTRAINT fk_historial_incidencia
        FOREIGN KEY(incidencia_id)
        REFERENCES incidencia(id)
);


CREATE TABLE etiqueta(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);


CREATE TABLE incidencia_etiqueta(
    incidencia_id BIGINT NOT NULL,
    etiqueta_id BIGINT NOT NULL,
    PRIMARY KEY(incidencia_id, etiqueta_id),

    CONSTRAINT fk_ie_incidencia
        FOREIGN KEY(incidencia_id)
        REFERENCES incidencia(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ie_etiqueta
        FOREIGN KEY(etiqueta_id)
        REFERENCES etiqueta(id)
        ON DELETE CASCADE
);

INSERT INTO rol (nombre) VALUES
('Administrador'),
('Técnico'),
('Usuario'),
('Supervisor');

INSERT INTO departamento (nombre, descripcion) VALUES
('Informática', 'Gestión de equipos y soporte técnico'),
('Recursos Humanos', 'Gestión del personal'),
('Administración', 'Gestión administrativa y financiera'),
('Producción', 'Departamento de fabricación y producción');

INSERT INTO categoria (nombre, descripcion) VALUES
('Hardware', 'Problemas relacionados con el hardware'),
('Software', 'Errores en aplicaciones o programas'),
('Red', 'Incidencias de red y conectividad'),
('Correo', 'Problemas con el correo electrónico');

INSERT INTO prioridad (nombre) VALUES
('Baja'),
('Media'),
('Alta'),
('Urgente');

INSERT INTO estado (nombre) VALUES
('Abierta'),
('En proceso'),
('Resuelta'),
('Cerrada');

INSERT INTO usuario
(nombre, apellidos, email, password_hash, telefono, activo, rol_id, departamento_id)
VALUES
('Andrés', 'Fernández Pereira', 'andres@helpdesk.com', '$2a$10$123456', '600111111', TRUE, 1, 1),
('Laura', 'García López', 'laura@helpdesk.com', '$2a$10$123456', '600222222', TRUE, 2, 1),
('Carlos', 'Martín Pérez', 'carlos@helpdesk.com', '$2a$10$123456', '600333333', TRUE, 3, 2),
('Marta', 'Ruiz Sánchez', 'marta@helpdesk.com', '$2a$10$123456', '600444444', TRUE, 4, 3);


INSERT INTO incidencia
(titulo, descripcion, usuario_id, tecnico_id, categoria_id, prioridad_id, estado_id)
VALUES
('Ordenador no enciende',
'El ordenador no responde al pulsar el botón de encendido.',
3,2,1,3,1),

('Error al iniciar sesión',
'No es posible acceder a la aplicación.',
4,2,2,2,2),

('Sin conexión a Internet',
'No hay acceso a la red de la empresa.',
3,2,3,4,2),

('Correo bloqueado',
'Outlook muestra error al enviar mensajes.',
4,2,4,2,3);


INSERT INTO comentario
(mensaje, usuario_id, incidencia_id)
VALUES
('Se está revisando el equipo.',2,1),
('Se ha solicitado más información.',2,2),
('El problema parece ser del router.',2,3),
('Incidencia solucionada correctamente.',2,4);


INSERT INTO adjunto
(nombre_archivo,url_archivo,tipo_archivo,tamano,incidencia_id)
VALUES
('foto_pc.jpg','/adjuntos/foto_pc.jpg','image/jpeg',256000,1),
('error_login.png','/adjuntos/error_login.png','image/png',180000,2),
('router.jpg','/adjuntos/router.jpg','image/jpeg',310000,3),
('correo.pdf','/adjuntos/correo.pdf','application/pdf',540000,4);

INSERT INTO historial
(accion,usuario_id,incidencia_id)
VALUES
('Incidencia creada.',3,1),
('Incidencia asignada al técnico.',2,2),
('Prioridad cambiada a Urgente.',2,3),
('Incidencia cerrada.',2,4);

INSERT INTO etiqueta
(nombre,descripcion)
VALUES
('Urgente','Requiere atención inmediata'),
('Hardware','Problema físico del equipo'),
('Software','Problema de aplicaciones'),
('Red','Problema de conectividad');


INSERT INTO incidencia_etiqueta
(incidencia_id,etiqueta_id)
VALUES
(1,2),
(2,3),
(3,1),
(3,4),
(4,3);