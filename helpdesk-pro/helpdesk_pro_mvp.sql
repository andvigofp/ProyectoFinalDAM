-- ==========================================================
-- HELPDESK PRO - BASE DE DATOS DEL MVP
-- ==========================================================
-- Usuarios de prueba:
--   Todos utilizan la contraseña: Helpdesk123!
--   Los valores de password_hash son hashes BCrypt de esa clave.
--   En Spring Security, validar con BCryptPasswordEncoder.
--
-- IMPORTANTE: este script elimina la base de datos helpdesk_pro
-- si ya existe y la vuelve a crear desde cero.

DROP DATABASE IF EXISTS helpdesk_pro;
CREATE DATABASE helpdesk_pro
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE helpdesk_pro;

-- ROLES: solo los tres definidos para el MVP.
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE departamentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE usuarios (
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
    CONSTRAINT fk_usuarios_roles
        FOREIGN KEY (rol_id) REFERENCES roles(id),
    CONSTRAINT fk_usuarios_departamentos
        FOREIGN KEY (departamento_id) REFERENCES departamentos(id)
);

CREATE TABLE categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE prioridades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE estados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE incidencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    solucion TEXT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    tecnico_id BIGINT NULL,
    categoria_id BIGINT NOT NULL,
    prioridad_id BIGINT NOT NULL,
    estado_id BIGINT NOT NULL,
    CONSTRAINT fk_incidencias_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_incidencias_tecnico
        FOREIGN KEY (tecnico_id) REFERENCES usuarios(id),
    CONSTRAINT fk_incidencias_categoria
        FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    CONSTRAINT fk_incidencias_prioridad
        FOREIGN KEY (prioridad_id) REFERENCES prioridades(id),
    CONSTRAINT fk_incidencias_estado
        FOREIGN KEY (estado_id) REFERENCES estados(id)
);

CREATE TABLE comentarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensaje TEXT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    incidencia_id BIGINT NOT NULL,
    CONSTRAINT fk_comentarios_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_comentarios_incidencia
        FOREIGN KEY (incidencia_id) REFERENCES incidencias(id)
);

CREATE TABLE historiales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    accion VARCHAR(255) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    incidencia_id BIGINT NOT NULL,
    CONSTRAINT fk_historiales_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_historiales_incidencia
        FOREIGN KEY (incidencia_id) REFERENCES incidencias(id)
);

-- Datos iniciales
INSERT INTO roles (nombre) VALUES
('Administrador'),
('Técnico'),
('Empleado');

INSERT INTO departamentos (nombre, descripcion) VALUES
('Informática', 'Soporte informático interno'),
('Recursos Humanos', 'Gestión del personal'),
('Administración', 'Gestión administrativa'),
('Producción', 'Área de producción');

INSERT INTO categorias (nombre, descripcion) VALUES
('Hardware', 'Ordenadores y otros equipos'),
('Software', 'Programas y aplicaciones'),
('Red', 'Conectividad y red interna'),
('Correo', 'Correo electrónico');

INSERT INTO prioridades (nombre) VALUES
('Baja'),
('Media'),
('Alta'),
('Urgente');

INSERT INTO estados (nombre) VALUES
('Abierta'),
('En proceso'),
('Resuelta'),
('Cerrada');

-- Cuentas de prueba.
-- Contraseña para cada cuenta: Helpdesk123!
-- El hash BCrypt puede ser diferente entre usuarios porque incluye una sal.
INSERT INTO usuarios
(nombre, apellidos, email, password_hash, telefono, rol_id, departamento_id)
VALUES
('Andrés', 'Fernández', 'andres@helpdesk.com',
 '$2b$10$8ns.e75KDYZGts8WHNRoc.lWCJhcn47CGGwIRJLMxs3RMOE9Pq/8e',
 '600111111', 1, 1), -- Administrador | contraseña: Helpdesk123!
('Laura', 'García', 'laura@helpdesk.com',
 '$2b$10$GiBw3/8Kja5UA0UBekbjuu4A8oLFmIdd7Z/t0aIBLQTDOmB/2UhPm',
 '600222222', 2, 1), -- Técnico | contraseña: Helpdesk123!
('Carlos', 'Martín', 'carlos@helpdesk.com',
 '$2b$10$HBO94uxyF0Sx4TdsGtQCoew5PxCBjzbpuhSGJawCs/XjsnnO5X56a',
 '600333333', 3, 2), -- Empleado | contraseña: Helpdesk123!
('Marta', 'Ruiz', 'marta@helpdesk.com',
 '$2b$10$ARy5HRWRJ9yURPC8b.vdre.7.i7ziZCTk.0h37EgdtvKuBNwG.Y2G',
 '600444444', 3, 3); -- Empleado | contraseña: Helpdesk123!

INSERT INTO incidencias
(titulo, descripcion, solucion, usuario_id, tecnico_id, categoria_id, prioridad_id, estado_id)
VALUES
('PC no enciende', 'No responde al pulsar el botón', NULL, 3, 2, 1, 3, 1),
('Error de inicio de sesión', 'No permite iniciar sesión', NULL, 4, 2, 2, 2, 2),
('Sin Internet', 'No hay conexión a la red interna', NULL, 3, 2, 3, 4, 2),
('Correo bloqueado', 'No permite enviar correos electrónicos',
 'Se ha desbloqueado la cuenta y restablecido el acceso.', 4, 2, 4, 2, 3);

INSERT INTO comentarios (mensaje, usuario_id, incidencia_id) VALUES
('Estoy revisando la incidencia.', 2, 1),
('Comprobando los datos de acceso.', 2, 2),
('Se ha reiniciado el router y se sigue comprobando la conexión.', 2, 3),
('Incidencia solucionada.', 2, 4);

INSERT INTO historiales (accion, usuario_id, incidencia_id) VALUES
('Incidencia creada', 3, 1),
('Incidencia asignada al técnico', 1, 2),
('Prioridad actualizada', 2, 3),
('Solución registrada', 2, 4);

-- NOTAS:
-- 1. Adjuntos y etiquetas no se crean porque están fuera del MVP inicial.
-- 2. La aplicación Spring Boot debe validar los permisos por rol y por creador
--    de la incidencia; las claves foráneas por sí solas no aplican esos permisos.
-- 3. Si el inicio de sesión no acepta una cuenta de prueba, verifica que el hash
--    BCrypt corresponda realmente a Helpdesk123! y que se use BCryptPasswordEncoder.
