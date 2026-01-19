package es.deusto.sd.ecoembes.client.swing;

import java.util.List;

import es.deusto.sd.ecoembes.client.dto.*;
import es.deusto.sd.ecoembes.client.proxies.IEcoembesServiceProxy;

public class SwingClientController {

    private IEcoembesServiceProxy serviceProxy;
    private String token; 

    public SwingClientController(IEcoembesServiceProxy serviceProxy) {
        this.serviceProxy = serviceProxy;
    }

    public boolean login(String email, String password) {
        try {
            this.token = serviceProxy.login(new CredentialsDTO(email, password));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        if (token != null) {
            serviceProxy.logout(token);
            token = null;
        }
    }

    public String getToken() {
        return token;
    }

    // --- MÉTODOS DE NEGOCIO ---

    public void crearContenedor(ContainerDTO container) {
        serviceProxy.createContainer(container, token);
    }

    public List<EstadoContenedorDTO> getContenedoresPorZona(int cp, String fecha) {
        return serviceProxy.getContenedoresPorZona(cp, fecha);
    }

    public List<NivelLlenadoDTO> getHistorial(Long id, String fechaInicio, String fechaFin) {
        return serviceProxy.getHistorial(id, fechaInicio, fechaFin);
    }
    
    public void asignarMasivamente(Long plantaId, List<Long> contenedores) {
        AsignacionMasivaDTO dto = new AsignacionMasivaDTO();
        dto.setToken(token);
        dto.setContainerIds(contenedores);
        serviceProxy.asignarContenedores(plantaId, dto);
    }
    public double getCapacidadPlanta(Long plantaId, String fecha) {
        return serviceProxy.getCapacidadPlanta(plantaId, fecha);
    }
}