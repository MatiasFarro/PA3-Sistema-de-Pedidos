
package com.isil.edu.pe.spedidos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.isil.edu.pe.spedidos.repository.PedidoReporteJdbcRepository;

/**
 * Pruebas de las funcionalidades agregadas en PA3.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class Pa3ReportesFeatureTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PedidoReporteJdbcRepository repository;

    @BeforeEach
    void prepararDatos() {
        jdbcTemplate.update("DELETE FROM pedido");

        insertar("Ana Torres", "LAP-1", "Laptop", "1000.00", 1, "1000.00");
        insertar("Ana Torres", "MOU-1", "Mouse", "50.00", 2, "100.00");
        insertar("Ana Torres", "MON-1", "Monitor", "400.00", 1, "400.00");
        insertar("Luis Rojas", "TEC-1", "Teclado", "80.00", 3, "240.00");
    }

    @Test
    @DisplayName("PA3: filtra pedidos por cliente y total mínimo usando JDBC")
    void filtraPedidosPorClienteYTotalMinimo() {
        var resultado = repository.buscarPedidosPorClienteYTotalMinimo(
                "Ana", new BigDecimal("500.00"));

        assertEquals(1, resultado.size());
        assertEquals("Ana Torres", resultado.get(0).getCliente());
        assertEquals(
                0,
                new BigDecimal("1000.00").compareTo(resultado.get(0).getTotal())
        );
    }

    @Test
    @DisplayName("PA3: agrupa clientes frecuentes y calcula sus indicadores")
    void agrupaClientesFrecuentesYCalculaIndicadores() {
        var resultado = repository.listarClientesFrecuentes(2);

        assertEquals(1, resultado.size());
        assertEquals("Ana Torres", resultado.get(0).cliente());
        assertEquals(3L, resultado.get(0).totalPedidos());
        assertEquals(4L, resultado.get(0).totalUnidades());

        assertEquals(
                0,
                new BigDecimal("1500.00").compareTo(
                        resultado.get(0).importeAcumulado())
        );

        assertEquals(
                0,
                new BigDecimal("500.00").compareTo(
                        resultado.get(0).ticketPromedio())
        );
    }

    @Test
    @DisplayName("PA3: responde 400 cuando el total mínimo es negativo")
    void responde400CuandoTotalMinimoEsNegativo() throws Exception {
        mockMvc.perform(get("/api/v1/pedidos/busqueda-avanzada")
                        .param("cliente", "Ana")
                        .param("totalMinimo", "-1")
                        .with(user("consulta").roles("CONSULTA")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PA3: permite a CONSULTA visualizar la vista JSP de reportes")
    void permiteConsultaVisualizarVistaDeReportes() throws Exception {
        mockMvc.perform(get("/reportes/pedidos")
                        .with(user("consulta").roles("CONSULTA")))
                .andExpect(status().isOk())
                .andExpect(view().name("reportes-pedidos"));
    }

    @Test
    @DisplayName("PA3: redirige al login cuando un usuario anónimo abre reportes")
    void redirigeAnonimoAlLoginCuandoAbreReportes() throws Exception {
        mockMvc.perform(get("/reportes/pedidos"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("PA3: responde 400 cuando el cliente está vacío")
    void responde400CuandoClienteEstaVacio() throws Exception {
        mockMvc.perform(get("/api/v1/pedidos/busqueda-avanzada")
                        .param("cliente", " ")
                        .param("totalMinimo", "0")
                        .with(user("consulta").roles("CONSULTA")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PA3: responde 400 cuando minPedidos es menor que uno")
    void responde400CuandoMinPedidosEsCero() throws Exception {
        mockMvc.perform(get("/api/v1/pedidos/clientes-frecuentes")
                        .param("minPedidos", "0")
                        .with(user("consulta").roles("CONSULTA")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PA3: devuelve JSON de clientes frecuentes para el rol CONSULTA")
    void devuelveJsonDeClientesFrecuentesParaConsulta() throws Exception {
        mockMvc.perform(get("/api/v1/pedidos/clientes-frecuentes")
                        .param("minPedidos", "2")
                        .with(user("consulta").roles("CONSULTA")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cliente").value("Ana Torres"))
                .andExpect(jsonPath("$[0].totalPedidos").value(3));
    }

    private void insertar(String cliente, String codigo, String nombre,
                          String precio, int cantidad, String total) {

        jdbcTemplate.update("""
                INSERT INTO pedido (
                    cliente,
                    producto_codigo,
                    producto_nombre,
                    precio_unitario,
                    cantidad,
                    total,
                    fecha
                )
                VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                """,
                cliente,
                codigo,
                nombre,
                new BigDecimal(precio),
                cantidad,
                new BigDecimal(total)
        );
    }
}
