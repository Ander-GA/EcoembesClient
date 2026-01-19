package es.deusto.sd.ecoembes.client.proxies;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.deusto.sd.ecoembes.client.dto.*;

@Service
public class HttpServiceProxy implements IEcoembesServiceProxy {

    // Asegúrate de que este puerto coincide con el de tu servidor (EcoembesV2_SD-1)
    private static final String SERVER_URL = "http://localhost:9000";
    
    private final RestTemplate restTemplate;

    public HttpServiceProxy() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String login(CredentialsDTO credentials) {
        try {
            String url = SERVER_URL + "/auth/login";
            return restTemplate.postForObject(url, credentials, String.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error de login: " + e.getStatusCode());
        }
    }

    @Override
    public void logout(String token) {
        String url = SERVER_URL + "/auth/logout";
        restTemplate.postForObject(url, token, Void.class);
    }

    @Override
    public ContainerDTO createContainer(ContainerDTO container, String token) {
        // En este prototipo enviamos el objeto tal cual. 
        // Si tu servidor valida token en headers, habría que añadirlo, 
        // pero tu controlador actual solo valida el objeto ContainerDTO.
        String url = SERVER_URL + "/contenedores";
        return restTemplate.postForObject(url, container, ContainerDTO.class);
    }

    @Override
    public List<NivelLlenadoDTO> getHistorial(Long id, String fechaInicio, String fechaFin) {
        String url = SERVER_URL + "/contenedores/" + id + "/historial?fechaInicio=" + fechaInicio + "&fechaFin=" + fechaFin;
        
        // RestTemplate devuelve un Array, lo convertimos a Lista
        NivelLlenadoDTO[] response = restTemplate.getForObject(url, NivelLlenadoDTO[].class);
        
        if (response == null) {
            return List.of();
        }
        return Arrays.asList(response);
    }

    @Override
    public List<EstadoContenedorDTO> getContenedoresPorZona(int codigoPostal, String fecha) {
        String url = SERVER_URL + "/contenedores?codigoPostal=" + codigoPostal + "&fecha=" + fecha;
        
        EstadoContenedorDTO[] response = restTemplate.getForObject(url, EstadoContenedorDTO[].class);
        
        if (response == null) {
            return List.of();
        }
        return Arrays.asList(response);
    }

    @Override
    public void asignarContenedores(Long plantaId, AsignacionMasivaDTO asignacion) {
        String url = SERVER_URL + "/plantas/" + plantaId + "/asignar";
        restTemplate.postForObject(url, asignacion, Void.class);
    }
    @Override
    public double getCapacidadPlanta(Long plantaId, String fecha) {
        // GET /plantas/{id}/capacidad?fecha=YYYY-MM-DD
        String url = SERVER_URL + "/plantas/" + plantaId + "/capacidad?fecha=" + fecha;
        
        // Recibimos un Map<String, Object> del servidor
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        if (response != null && response.containsKey("capacidadDisponible")) {
            // Convertimos el objeto a double con cuidado
            return Double.parseDouble(response.get("capacidadDisponible").toString());
        }
        return -1.0; // Código de error si falla
    }
}