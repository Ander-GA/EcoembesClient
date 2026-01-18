package es.deusto.sd.ecoembes.client.proxies;

import java.util.List;
import es.deusto.sd.ecoembes.client.dto.*;

public interface IEcoembesServiceProxy {
    // Autenticación
    String login(CredentialsDTO credentials);
    void logout(String token);

    // Contenedores
    ContainerDTO createContainer(ContainerDTO container, String token);
    List<NivelLlenadoDTO> getHistorial(Long id, String fechaInicio, String fechaFin); // Fechas como String para simplificar
    List<EstadoContenedorDTO> getContenedoresPorZona(int codigoPostal, String fecha);

    // Plantas
    void asignarContenedores(Long plantaId, AsignacionMasivaDTO asignacion);
}