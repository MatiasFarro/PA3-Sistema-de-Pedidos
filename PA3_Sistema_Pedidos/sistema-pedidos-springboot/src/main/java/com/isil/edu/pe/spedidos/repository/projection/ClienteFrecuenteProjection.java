package com.isil.edu.pe.spedidos.repository.projection;

import java.math.BigDecimal;

/** Resultado agregado de pedidos agrupados por cliente. */
public record ClienteFrecuenteProjection(
    String cliente,
    long totalPedidos,
    long totalUnidades,
    BigDecimal importeAcumulado,
    BigDecimal ticketPromedio
) { }
