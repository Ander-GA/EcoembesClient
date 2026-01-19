package es.deusto.sd.ecoembes.client.dto;


public class NivelLlenadoDTO {

    private Long id;
    private double nivelDeLlenado;
    private String fecha;

    public NivelLlenadoDTO() {
    }

    public NivelLlenadoDTO(Long id, double nivelDeLlenado, String fecha) {
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}