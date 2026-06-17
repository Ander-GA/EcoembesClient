# Ecoembes Desktop Client ♻️💻

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white)](https://gradle.org/)

## 📝 Descripción del Proyecto

Este repositorio contiene el **Frontend de escritorio** para el ecosistema distribuido de gestión de infraestructuras de reciclaje[cite: 3]. Se trata de un cliente desarrollado en Java Swing que proporciona una interfaz gráfica (GUI) para interactuar con los datos de plantas, contenedores y asignaciones de forma remota[cite: 3].

> ⚙️ **Nota:** Esta aplicación es un cliente ligero. Requiere que la API REST del servidor (Backend) esté en ejecución para poder obtener y procesar los datos. Puedes encontrar el código del servidor en: [Ecoembes API Backend]([URL_DE_TU_REPO_BACKEND]).

## ⚙️ Arquitectura de Comunicación y Patrones

El cliente no procesa la lógica de negocio pesada, sino que está diseñado para consumir servicios web de forma eficiente mediante una arquitectura limpia:

*   **Patrón Proxy:** Se han implementado clases intermedias (`HttpServiceProxy`) que actúan como representantes locales del servidor remoto[cite: 3]. Esto encapsula toda la complejidad de las peticiones HTTP, aislando el código de la interfaz gráfica[cite: 3].
*   **Controladores de Interfaz (MVC):** El componente `SwingClientController` actúa como mediador estricto entre las vistas de Swing (`SwingClientGUI`) y los proxies de comunicación[cite: 3].
*   **Gestión de DTOs:** Recepción y deserialización de la información del servidor utilizando Data Transfer Objects (`ContainerDTO`, `PlantaDTO`, `NivelLlenadoDTO`, etc.) para renderizar los datos en las tablas de la interfaz[cite: 3].

## 🚀 Ejecución y Despliegue Local

1. Asegúrate de tener el [Servidor Backend](https://github.com/Ander-GA/EcoembesV2) clonado y en ejecución en tu máquina local.
2. **Clonar este cliente:**
```bash
   git clone [https://github.com/Ander-GA/EcoembesClient.git](https://github.com/Ander-GA/EcoembesClient.git)
```
3. **Compilar y Ejecutar:**
Utilizando el wrapper de Gradle incluido en el proyecto:
```bash
   ./gradlew bootRun
```
Desarrollado como proyecto de integración Cliente-Servidor e interfaces gráficas en Java.
