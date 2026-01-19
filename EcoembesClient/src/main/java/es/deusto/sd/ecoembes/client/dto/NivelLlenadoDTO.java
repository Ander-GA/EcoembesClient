package es.deusto.sd.ecoembes.client.dto;

import java.time.LocalDate;

public class NivelLlenadoDTO {

    private Long id;
    private double nivelDeLlenado;
    private LocalDate fecha;

    public NivelLlenadoDTO() {
    }

    public NivelLlenadoDTO(Long id, double nivelDeLlenado, LocalDate fecha) {
        this.id = id;
        this.nivelDeLlenado = nivelDeLlenado;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getNivelDeLlenado() {
        return nivelDeLlenado;
    }

    public void setNivelDeLlenado(double nivelDeLlenado) {
        this.nivelDeLlenado = nivelDeLlenado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}