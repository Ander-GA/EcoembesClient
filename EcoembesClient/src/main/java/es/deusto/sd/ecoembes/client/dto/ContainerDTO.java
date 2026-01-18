package es.deusto.sd.ecoembes.client.dto;

public class ContainerDTO {

    private Long id;
    private String direccion;
    private int codigoPostal;
    private double capacidad;

    public ContainerDTO() {
    }

    public ContainerDTO(Long id, String direccion, int codigoPostal, double capacidad) {
        this.id = id;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.capacidad = capacidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(double capacidad) {
        this.capacidad = capacidad;
    }
}