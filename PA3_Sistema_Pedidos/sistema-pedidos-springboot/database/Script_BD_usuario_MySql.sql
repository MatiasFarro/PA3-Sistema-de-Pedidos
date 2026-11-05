-- Ejecutar con una cuenta administradora de MySQL.
-- Cambia la contraseña de laboratorio y utiliza el mismo valor en DB_PASSWORD.
CREATE DATABASE IF NOT EXISTS pedidosdb
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'spedidos_app'@'localhost'
  IDENTIFIED BY 'CAMBIAR_PASSWORD_LOCAL';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX
  ON pedidosdb.* TO 'spedidos_app'@'localhost';
FLUSH PRIVILEGES;

-- La tabla pedido se crea/actualiza mediante Hibernate (ddl-auto=update).
-- Para verificar después de iniciar la aplicación:
-- USE pedidosdb;
-- SELECT id, cliente, producto_codigo, cantidad, total, fecha
-- FROM pedido ORDER BY id DESC;
