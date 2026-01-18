// Generado con gemini para implementar la asignacion de mas de un contenedor a la vez a una planta.

package es.deusto.sd.ecoembes.client.dto;

import java.util.List;

public class AsignacionMasivaDTO {
    private List<Long> containerIds; // Lista de contenedores
    private String token;            // Token de autenticación

    public AsignacionMasivaDTO() {}

    public List<Long> getContainerIds() { return containerIds; }
    public void setContainerIds(List<Long> containerIds) { this.containerIds = containerIds; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
