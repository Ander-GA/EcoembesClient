package es.deusto.sd.ecoembes.client.dto;

// Este DTO sirve para recibir la respuesta de "contenedores por zona"
public class EstadoContenedorDTO {
    private Long id;
    private String direccion;
    private double capacidad;
    private double nivelEnFecha; // El dato calculado por el server
    // private String colorEnFecha; // Opcional, si quieres mostrar el color

    public EstadoContenedorDTO() {}

    // Getters y Setters para todos los campos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public double getCapacidad() { return capacidad; }
    public void setCapacidad(double capacidad) { this.capacidad = capacidad; }
    public double getNivelEnFecha() { return nivelEnFecha; }
    public void setNivelEnFecha(double nivelEnFecha) { this.nivelEnFecha = nivelEnFecha; }
}