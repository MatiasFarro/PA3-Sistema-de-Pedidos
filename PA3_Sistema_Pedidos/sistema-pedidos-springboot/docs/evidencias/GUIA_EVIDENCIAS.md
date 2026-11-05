# Guía para evidencias reales PA3

No se incluyen capturas simuladas. Guarda aquí las capturas obtenidas al ejecutar tu entorno.

| Evidencia | Qué debe verse | Nombre sugerido |
|---|---|---|
| Caso 1 JSP | Filtro cliente + total mínimo y filas esperadas | `01-busqueda-jsp.png` |
| Caso 1 API | URL, autenticación, HTTP 200 y JSON | `02-busqueda-api.png` |
| Caso 1 MySQL | SELECT con los mismos filtros y filas | `03-busqueda-mysql.png` |
| Caso 2 API/JSP | `minPedidos=2` y cuatro indicadores correctos | `04-clientes-frecuentes.png` |
| Caso 3 inválidos | 400 para cliente vacío, total negativo y minPedidos 0 | `05-parametros-invalidos.png` |
| Caso 4 inventario | POST pedido con inventario detenido (503) y reporte local exitoso | `06-inventario-caido.png` |
| Caso 5 seguridad | anónimo, CONSULTA, CONSULTA POST+CSRF, OPERADOR POST sin CSRF | `07-seguridad-csrf.png` |
| Pruebas | Consola con `mvnw.cmd clean test` y BUILD SUCCESS | `08-mvn-test.png` |

## Datos que deben aparecer en el informe

- Repositorio Git personal y rama evaluada.
- Hash del commit final.
- URL, parámetros y estado HTTP de cada escenario.
- Captura de MySQL cuando se solicita comparar resultados.
- Salida completa de `mvnw.cmd clean test`.
