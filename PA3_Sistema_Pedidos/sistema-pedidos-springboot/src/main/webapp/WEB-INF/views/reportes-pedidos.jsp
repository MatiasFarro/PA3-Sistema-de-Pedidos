<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Reportes de pedidos - ISIL</title>
  <c:url var="cssUrl" value="/assets/css/app.css"/><link rel="stylesheet" href="${cssUrl}">
  <style>
    .acciones{display:flex;gap:12px;flex-wrap:wrap;margin:16px 0}.acciones a{color:#174ea6}
    .metricas{display:grid;grid-template-columns:repeat(auto-fit,minmax(170px,1fr));gap:12px}
    .metrica{background:#f3f6fa;border-radius:10px;padding:16px}.metrica strong{display:block;font-size:1.35rem;margin-top:8px}
    .form-reportes{display:flex;gap:12px;align-items:end;flex-wrap:wrap}.form-reportes label{display:flex;flex-direction:column;gap:6px}
    .form-reportes input{padding:10px;border:1px solid #bbb;border-radius:6px}.tabla-scroll{overflow:auto}
    .nota{color:#555;font-size:.92rem}.alerta.error{margin:12px 0}
  </style>
</head>
<body><div class="contenedor">
  <header class="cabecera">
    <div><p class="eyebrow">Spring MVC · Spring JDBC · Spring Security</p><h1>Reportes de pedidos</h1>
      <p class="subtitulo">Consultas locales sobre los pedidos almacenados en MySQL.</p></div>
    <nav class="acciones"><a href="<c:url value='/pedidos'/>">Volver a pedidos</a>
      <form method="post" action="<c:url value='/logout'/>"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"><button type="submit">Cerrar sesión</button></form></nav>
  </header>
  <c:if test="${not empty error}"><div class="alerta error"><c:out value="${error}"/></div></c:if>
  <section class="tarjeta"><h2>Resumen general</h2><div class="metricas">
    <div class="metrica">Total de pedidos<strong><c:out value="${resumen.totalPedidos}"/></strong></div>
    <div class="metrica">Unidades<strong><c:out value="${resumen.totalUnidades}"/></strong></div>
    <div class="metrica">Importe acumulado<strong>S/ <fmt:formatNumber value="${resumen.importeAcumulado}" minFractionDigits="2" maxFractionDigits="2"/></strong></div>
    <div class="metrica">Ticket promedio<strong>S/ <fmt:formatNumber value="${resumen.ticketPromedio}" minFractionDigits="2" maxFractionDigits="2"/></strong></div>
  </div></section>
  <section class="tarjeta"><h2>Búsqueda avanzada</h2><p class="nota">Filtra por coincidencia de cliente y por el total mínimo del pedido.</p>
    <form method="get" action="<c:url value='/reportes/pedidos'/>" class="form-reportes">
      <label>Cliente<input name="cliente" value="<c:out value='${clienteFiltro}'/>" placeholder="Ej. Ana" required></label>
      <label>Total mínimo (S/)<input name="totalMinimo" type="number" min="0" step="0.01" value="<c:out value='${totalMinimoFiltro}'/>" required></label>
      <input type="hidden" name="minPedidos" value="<c:out value='${minPedidos}'/>"/><button type="submit">Buscar</button>
    </form>
    <div class="tabla-scroll"><table><thead><tr><th>ID</th><th>Cliente</th><th>Producto</th><th>Cantidad</th><th>Total</th><th>Fecha</th></tr></thead><tbody>
      <c:forEach var="pedido" items="${pedidosBusqueda}"><tr><td><c:out value="${pedido.id}"/></td><td><c:out value="${pedido.cliente}"/></td><td><c:out value="${pedido.productoNombre}"/></td><td><c:out value="${pedido.cantidad}"/></td><td>S/ <fmt:formatNumber value="${pedido.total}" minFractionDigits="2" maxFractionDigits="2"/></td><td><c:out value="${pedido.fecha}"/></td></tr></c:forEach>
      <c:if test="${not empty clienteFiltro and empty pedidosBusqueda}"><tr><td colspan="6">No se encontraron pedidos con esos filtros.</td></tr></c:if>
    </tbody></table></div>
  </section>
  <section class="tarjeta"><h2>Clientes frecuentes</h2><p class="nota">Clientes con al menos <c:out value="${minPedidos}"/> pedidos.</p>
    <form method="get" action="<c:url value='/reportes/pedidos'/>" class="form-reportes"><label>Mínimo de pedidos<input name="minPedidos" type="number" min="1" value="<c:out value='${minPedidos}'/>" required></label><button type="submit">Actualizar</button></form>
    <div class="tabla-scroll"><table><thead><tr><th>Cliente</th><th>Total pedidos</th><th>Total unidades</th><th>Importe acumulado</th><th>Ticket promedio</th></tr></thead><tbody>
      <c:forEach var="cliente" items="${clientesFrecuentes}"><tr><td><c:out value="${cliente.cliente}"/></td><td><c:out value="${cliente.totalPedidos}"/></td><td><c:out value="${cliente.totalUnidades}"/></td><td>S/ <fmt:formatNumber value="${cliente.importeAcumulado}" minFractionDigits="2" maxFractionDigits="2"/></td><td>S/ <fmt:formatNumber value="${cliente.ticketPromedio}" minFractionDigits="2" maxFractionDigits="2"/></td></tr></c:forEach>
      <c:if test="${empty clientesFrecuentes}"><tr><td colspan="5">No hay clientes que alcancen el mínimo.</td></tr></c:if>
    </tbody></table></div>
  </section>
</div></body></html>
