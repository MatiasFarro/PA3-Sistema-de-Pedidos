# PA3 – Sistema de Pedidos

## Contenido

- `sistema-pedidos-springboot/`: aplicación principal Spring Boot, Spring MVC/JSP, Spring Security, JPA y las dos consultas PA3 con Spring JDBC.
- `inventario-service/`: servicio externo Jakarta EE entregado por el docente; se incluye sin modificar para conservar la integración del caso.
- `documentacion/Informe_PA3.pdf`: informe base.

## Orden de ejecución

1. Configurar MySQL con `sistema-pedidos-springboot/database/Script_BD_usuario_MySql.sql`.
2. Definir `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `PEDIDOS_OPERADOR_PASSWORD` y `PEDIDOS_CONSULTA_PASSWORD`.
3. Arrancar `inventario-service` con su README si se desea registrar pedidos y probar indisponibilidad.
4. En `sistema-pedidos-springboot`, ejecutar `mvnw.cmd clean test` y luego `mvnw.cmd spring-boot:run`.
5. Abrir `http://localhost:8081/sistema-pedidos/reportes/pedidos`.

Las contraseñas de laboratorio deben configurarse localmente; no se debe subir una contraseña personal al repositorio. Las pruebas y evidencias deben generarse en el equipo del estudiante. El PDF contiene espacios para NRC y docente y debe completarse antes de entregar.
