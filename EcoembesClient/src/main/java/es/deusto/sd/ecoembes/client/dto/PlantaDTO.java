package es.deusto.sd.ecoembes.client.dto;

public class PlantaDTO {

    private Long id;
    private String nombre; 
    private String direccion;
    private int codigoPostal;
    private double capacidad;

    public PlantaDTO() {
    }

    public PlantaDTO(Long id, String nombre, String direccion, int codigoPostal, double capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.codigoPostal=codigoPostal;
        this.capacidad = capacidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    

    public int getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(int codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
	    this.direccion = direccion;
	}

	public double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(double capacidad) {
        this.capacidad = capacidad;
    }
}