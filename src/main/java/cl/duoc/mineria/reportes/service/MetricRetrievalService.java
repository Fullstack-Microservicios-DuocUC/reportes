package cl.duoc.mineria.reportes.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cl.duoc.mineria.reportes.exception.MetricaNoDisponibleException;
import cl.duoc.mineria.reportes.model.CicloResponseDTO;
import cl.duoc.mineria.reportes.model.OrdenMantencionResponseDTO;

@Service
public class MetricRetrievalService {

    private final WebClient webClient;

    public MetricRetrievalService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    // Trae ÚNICAMENTE los ciclos de este turno específico (Puerto 8088)
    public List<CicloResponseDTO> obtenerCiclosPorTurno(Long turnoId) {
        try {
            CicloResponseDTO[] respuesta = this.webClient.get()
                .uri("http://127.0.0.1:8088/api/v1/ciclos-transporte/turno/{id}", turnoId)
                .retrieve()
                .bodyToMono(CicloResponseDTO[].class)
                .block();
            return respuesta != null ? Arrays.asList(respuesta) : List.of();
        } catch (Exception e) {
            throw new MetricaNoDisponibleException(
                "No se pudo obtener los ciclos de transporte del turno " + turnoId + ": " + e.getMessage());
        }
    }

    public List<OrdenMantencionResponseDTO> obtenerIncidentesTaller(Long turnoId) {
        try {
            OrdenMantencionResponseDTO[] respuesta = this.webClient.get()
                .uri("http://127.0.0.1:8089/api/v1/mantenciones/turno/{turnoId}", turnoId)
                .retrieve()
                .bodyToMono(OrdenMantencionResponseDTO[].class)
                .block();
            return respuesta != null ? Arrays.asList(respuesta) : List.of();
        } catch (Exception e) {
            throw new MetricaNoDisponibleException(
                "No se pudo obtener las mantenciones del turno " + turnoId + ": " + e.getMessage());
        }
    }

    // Verifica la existencia del Turno en el módulo Turnos (Puerto 8082)
    public boolean verificarTurnoExiste(Long turnoId) {
        try {
            Boolean existe = this.webClient.get()
                .uri("http://127.0.0.1:8082/api/v1/turnos/existe/{id}", turnoId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
            return existe != null && existe;
        } catch (Exception e) {
            System.out.println("[Reportes] Error al conectar con Turnos (8082): " + e.getMessage());
            return false;
        }
    }
}
