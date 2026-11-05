package com.isil.edu.pe.spedidos.controller;

import com.isil.edu.pe.spedidos.service.PedidoReporteService;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

/** Controlador MVC que prepara el modelo para la vista JSP de reportes. */
@Controller
@RequestMapping("/reportes")
public class PedidoReporteWebController {

  private final PedidoReporteService reporteService;

  public PedidoReporteWebController(PedidoReporteService reporteService) {
    this.reporteService = reporteService;
  }

  @GetMapping("/pedidos")
  public String mostrarReportes(
      @RequestParam(required = false) String cliente,
      @RequestParam(required = false) BigDecimal totalMinimo,
      @RequestParam(defaultValue = "2") int minPedidos,
      Model model, HttpServletResponse response) {
    try {
      model.addAttribute("resumen", reporteService.obtenerResumen());
      model.addAttribute("clientesFrecuentes", reporteService.listarClientesFrecuentes(minPedidos));
      model.addAttribute("minPedidos", minPedidos);
      model.addAttribute("clienteFiltro", cliente == null ? "" : cliente);
      model.addAttribute("totalMinimoFiltro", totalMinimo == null ? "" : totalMinimo);
      model.addAttribute("pedidosBusqueda", java.util.List.of());
      if (cliente != null || totalMinimo != null) {
        if (cliente == null || totalMinimo == null) {
          throw new IllegalArgumentException("Ingresa cliente y total mínimo para realizar la búsqueda.");
        }
        model.addAttribute("pedidosBusqueda",
            reporteService.buscarPedidosPorClienteYTotalMinimo(cliente, totalMinimo));
      }
      return "reportes-pedidos";
    } catch (IllegalArgumentException exception) {
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      model.addAttribute("error", exception.getMessage());
      model.addAttribute("pedidosBusqueda", java.util.List.of());
      model.addAttribute("clientesFrecuentes", java.util.List.of());
      model.addAttribute("minPedidos", minPedidos);
      if (exception.getMessage() != null && exception.getMessage().contains("mínimo de pedidos")) {
        model.addAttribute("errorFrecuentes", exception.getMessage());
      }
      return "reportes-pedidos";
    } catch (RuntimeException exception) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "No fue posible cargar los reportes.", exception);
    }
  }
}
