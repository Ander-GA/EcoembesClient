package es.deusto.sd.ecoembes.client.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import es.deusto.sd.ecoembes.client.EcoembesClientApplication;
import es.deusto.sd.ecoembes.client.dto.*;
import es.deusto.sd.ecoembes.client.proxies.IEcoembesServiceProxy;

public class SwingClientGUI extends JFrame {

    private SwingClientController controller;

    public SwingClientGUI(SwingClientController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setTitle("Ecoembes Client - Prototipo 3 (Final)");
        setSize(950, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // 1. Crear Contenedor
        tabs.addTab("1. Nuevo Contenedor", createPanelCrearContenedor());

        // 2. Consultar Zona
        tabs.addTab("2. Estado Zona", createPanelZona());
        
        // 3. Historial (Versión Porcentajes)
        tabs.addTab("3. Historial Llenado", createPanelHistorial());

        // 4. Asignación Masiva
        tabs.addTab("4. Asignar a Planta", createPanelAsignacion());
        
        // 5. Capacidad Planta (Nuevo)
        tabs.addTab("5. Consultar Capacidad", createPanelCapacidad());
        
        // Botón Logout
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.addActionListener(e -> {
            controller.logout();
            dispose();
            System.exit(0);
        });

        add(tabs, BorderLayout.CENTER);
        add(btnLogout, BorderLayout.SOUTH);
    }

    // --- PANELES ---

    private JPanel createPanelCrearContenedor() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        JTextField txtDir = new JTextField();
        JTextField txtCP = new JTextField();
        JTextField txtCap = new JTextField();
        JButton btnCrear = new JButton("Crear Contenedor");

        panel.add(new JLabel("Dirección:")); panel.add(txtDir);
        panel.add(new JLabel("Código Postal:")); panel.add(txtCP);
        panel.add(new JLabel("Capacidad (L):")); panel.add(txtCap);
        panel.add(new JLabel("")); panel.add(btnCrear);

        btnCrear.addActionListener(e -> {
            try {
                ContainerDTO c = new ContainerDTO();
                c.setDireccion(txtDir.getText());
                c.setCodigoPostal(Integer.parseInt(txtCP.getText()));
                c.setCapacidad(Double.parseDouble(txtCap.getText()));
                
                controller.crearContenedor(c);
                JOptionPane.showMessageDialog(this, "¡Contenedor creado con éxito!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        return panel;
    }

    private JPanel createPanelZona() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        JTextField txtCP = new JTextField(5);
        JTextField txtFecha = new JTextField("2026-01-20", 10);
        JButton btnBuscar = new JButton("Buscar Zona");
        
        top.add(new JLabel("CP:")); top.add(txtCP);
        top.add(new JLabel("Fecha:")); top.add(txtFecha);
        top.add(btnBuscar);

        JTextArea areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        
        btnBuscar.addActionListener(e -> {
            try {
                int cp = Integer.parseInt(txtCP.getText());
                List<EstadoContenedorDTO> lista = controller.getContenedoresPorZona(cp, txtFecha.getText());
                
                StringBuilder sb = new StringBuilder();
                if(lista.isEmpty()) sb.append("No hay contenedores en esta zona.");
                
                for (EstadoContenedorDTO dto : lista) {
                    sb.append("ID: ").append(dto.getId())
                      .append(" | Dir: ").append(dto.getDireccion())
                      .append(" | Nivel: ").append(String.format("%.2f", dto.getNivelEnFecha())).append(" L") // Aquí sigue siendo litros absolutos si no lo cambiaste en ese endpoint
                      .append("\n------------------\n");
                }
                areaResultados.setText(sb.toString());
            } catch (Exception ex) {
                areaResultados.setText("Error: " + ex.getMessage());
            }
        });
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaResultados), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        JTextField txtId = new JTextField(5);
        JTextField txtFInicio = new JTextField("2026-01-01", 8);
        JTextField txtFFin = new JTextField("2026-12-31", 8);
        JButton btnHistorial = new JButton("Ver Historial (%)");

        top.add(new JLabel("ID Cont:")); top.add(txtId);
        top.add(new JLabel("Desde:")); top.add(txtFInicio);
        top.add(new JLabel("Hasta:")); top.add(txtFFin);
        top.add(btnHistorial);

        JTextArea areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 13));

        btnHistorial.addActionListener(e -> {
            try {
                Long id = Long.parseLong(txtId.getText());
                // El servidor ahora devuelve DTOs con el PORCENTAJE calculado
                List<NivelLlenadoDTO> historial = controller.getHistorial(id, txtFInicio.getText(), txtFFin.getText());
                
                StringBuilder sb = new StringBuilder();
                if(historial.isEmpty()) sb.append("No hay datos para este rango.");
                
                for (NivelLlenadoDTO dato : historial) {
                    sb.append("📅 ").append(dato.getFecha().toString())
                      .append(" -> ")
                      // Ya no multiplicamos. Añadimos símbolo %
                      .append(String.format("%6.2f", dato.getNivelDeLlenado())).append("% de llenado")
                      .append("\n");
                }
                areaHistorial.setText(sb.toString());
            } catch (Exception ex) {
                areaHistorial.setText("Error: " + ex.getMessage());
            }
        });
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaHistorial), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPanelAsignacion() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(4, 1));
        
        JPanel p1 = new JPanel();
        JTextField txtPlantaId = new JTextField(5);
        p1.add(new JLabel("ID Planta Destino:")); p1.add(txtPlantaId);
        
        JPanel p2 = new JPanel();
        JTextField txtContenedores = new JTextField(20);
        p2.add(new JLabel("IDs Contenedores (ej: 1, 2, 5):")); p2.add(txtContenedores);
        
        JButton btnAsignar = new JButton("EJECUTAR ASIGNACIÓN");
        
        form.add(p1);
        form.add(p2);
        form.add(new JLabel("Se enviará notificación automática al finalizar."));
        form.add(btnAsignar);

        JTextArea areaLog = new JTextArea();
        areaLog.setEditable(false);

        btnAsignar.addActionListener(e -> {
            try {
                Long plantaId = Long.parseLong(txtPlantaId.getText());
                String[] idsString = txtContenedores.getText().split(",");
                List<Long> listaIds = new ArrayList<>();
                for(String s : idsString) listaIds.add(Long.parseLong(s.trim()));

                controller.asignarMasivamente(plantaId, listaIds);
                
                areaLog.append("✅ ASIGNACIÓN COMPLETADA CON ÉXITO.\n");
                areaLog.append("   -> Planta ID: " + plantaId + "\n");
                areaLog.append("   -> Contenedores procesados: " + listaIds + "\n");
                areaLog.append("--------------------------------------------\n");
            } catch (Exception ex) {
                areaLog.append("❌ Error: " + ex.getMessage() + "\n");
            }
        });
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaLog), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createPanelCapacidad() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        JTextField txtId = new JTextField(5);
        JTextField txtFecha = new JTextField("2026-01-20", 10);
        JButton btnConsultar = new JButton("Ver Capacidad Disponible");
        
        top.add(new JLabel("ID Planta:")); top.add(txtId);
        top.add(new JLabel("Fecha:")); top.add(txtFecha);
        top.add(btnConsultar);

        JTextArea areaInfo = new JTextArea();
        areaInfo.setEditable(false);
        areaInfo.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 14));

        btnConsultar.addActionListener(e -> {
            try {
                Long id = Long.parseLong(txtId.getText());
                double capacidad = controller.getCapacidadPlanta(id, txtFecha.getText());
                
                areaInfo.setText("");
                if (capacidad >= 0) {
                    areaInfo.append("\n  🏭 ESTADO PLANTA " + id + "\n");
                    areaInfo.append("  --------------------------\n");
                    areaInfo.append("  Espacio libre: " + String.format("%.2f", capacidad) + " litros\n");
                    
                    if(capacidad < 1000) areaInfo.append("\n  ⚠️ ALERTA: Poco espacio disponible.\n");
                    else areaInfo.append("\n  ✅ Operativa Normal.\n");
                } else {
                    areaInfo.append("❌ Error al consultar capacidad.");
                }
            } catch (Exception ex) {
                areaInfo.setText("Error: " + ex.getMessage());
            }
        });
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaInfo), BorderLayout.CENTER);
        return panel;
    }

    // --- MAIN ---
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(EcoembesClientApplication.class)
                .headless(false)
                .run(args);

        IEcoembesServiceProxy proxy = ctx.getBean(IEcoembesServiceProxy.class);
        SwingClientController controller = new SwingClientController(proxy);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridLayout(2, 2));
        JTextField userField = new JTextField("admin@ecoembes.es");
        JPasswordField passField = new JPasswordField("admin");
        loginPanel.add(new JLabel("Email:")); loginPanel.add(userField);
        loginPanel.add(new JLabel("Password:")); loginPanel.add(passField);

        int result = JOptionPane.showConfirmDialog(null, loginPanel, "Login Ecoembes", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            boolean loginOk = controller.login(userField.getText(), new String(passField.getPassword()));
            if (loginOk) {
                SwingUtilities.invokeLater(() -> {
                    SwingClientGUI gui = new SwingClientGUI(controller);
                    gui.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(null, "Login incorrecto.");
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }
}