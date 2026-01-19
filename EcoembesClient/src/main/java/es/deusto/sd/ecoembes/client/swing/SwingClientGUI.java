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
        setTitle("Ecoembes Client - Prototipo 3 (Completo)");
        setSize(900, 700); // Un poco más grande para que quepa todo
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // Pestaña 1: Crear Contenedor
        tabs.addTab("1. Nuevo Contenedor", createPanelCrearContenedor());

        // Pestaña 2: Consultar Zona
        tabs.addTab("2. Estado Zona", createPanelZona());
        
        // Pestaña 3: Historial (¡NUEVA!)
        tabs.addTab("3. Historial", createPanelHistorial());

        // Pestaña 4: Asignación Masiva (¡NUEVA!)
        tabs.addTab("4. Asignar a Planta", createPanelAsignacion());
        
        // Botón de Logout abajo
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.addActionListener(e -> {
            controller.logout();
            dispose();
            System.exit(0);
        });

        add(tabs, BorderLayout.CENTER);
        add(btnLogout, BorderLayout.SOUTH);
    }

    // --- PANELES EXISTENTES ---

    private JPanel createPanelCrearContenedor() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        JTextField txtDir = new JTextField();
        JTextField txtCP = new JTextField();
        JTextField txtCap = new JTextField();
        JButton btnCrear = new JButton("Crear Contenedor");

        panel.add(new JLabel("Dirección:")); panel.add(txtDir);
        panel.add(new JLabel("Código Postal:")); panel.add(txtCP);
        panel.add(new JLabel("Capacidad (litros):")); panel.add(txtCap);
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
        JTextField txtFecha = new JTextField("2025-01-20", 10);
        JButton btnBuscar = new JButton("Buscar por Zona");
        
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
                if(lista.isEmpty()) sb.append("No hay contenedores en esa zona.");
                
                for (EstadoContenedorDTO dto : lista) {
                    sb.append("ID: ").append(dto.getId())
                      .append(" | Dir: ").append(dto.getDireccion())
                      .append(" | Nivel: ").append(String.format("%.2f", dto.getNivelEnFecha() * 100)).append("%")
                      .append("\n--------------------------\n");
                }
                areaResultados.setText(sb.toString());
            } catch (Exception ex) {
                areaResultados.setText("Error al buscar: " + ex.getMessage());
            }
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaResultados), BorderLayout.CENTER);
        return panel;
    }

    // --- PANELES NUEVOS ---

    private JPanel createPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel();
        JTextField txtId = new JTextField(5);
        JTextField txtFInicio = new JTextField("2025-01-01", 8);
        JTextField txtFFin = new JTextField("2025-01-31", 8);
        JButton btnHistorial = new JButton("Ver Historial");

        top.add(new JLabel("ID Contenedor:")); top.add(txtId);
        top.add(new JLabel("Desde:")); top.add(txtFInicio);
        top.add(new JLabel("Hasta:")); top.add(txtFFin);
        top.add(btnHistorial);

        JTextArea areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);

        btnHistorial.addActionListener(e -> {
            try {
                Long id = Long.parseLong(txtId.getText());
                List<NivelLlenadoDTO> historial = controller.getHistorial(id, txtFInicio.getText(), txtFFin.getText());
                
                StringBuilder sb = new StringBuilder();
                if(historial.isEmpty()) sb.append("No hay datos para ese rango de fechas.");
                
                for (NivelLlenadoDTO dato : historial) {
                    sb.append("📅 Fecha: ").append(dato.getFecha())
                      .append(" -> Nivel: ").append(String.format("%.2f", dato.getNivelDeLlenado() * 100)).append("%")
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
        
        // Formulario superior
        JPanel form = new JPanel(new GridLayout(4, 1));
        
        JPanel p1 = new JPanel();
        JTextField txtPlantaId = new JTextField(5);
        p1.add(new JLabel("ID Planta Reciclaje:")); p1.add(txtPlantaId);
        
        JPanel p2 = new JPanel();
        JTextField txtContenedores = new JTextField(20);
        p2.add(new JLabel("IDs Contenedores (separados por coma):")); p2.add(txtContenedores);
        // Ejemplo visual: "1, 2, 5"
        
        JButton btnAsignar = new JButton("Realizar Asignación Masiva");
        
        form.add(p1);
        form.add(p2);
        form.add(new JLabel("Ejemplo IDs: 1, 2, 5"));
        form.add(btnAsignar);

        JTextArea areaLog = new JTextArea();
        areaLog.setEditable(false);

        btnAsignar.addActionListener(e -> {
            try {
                Long plantaId = Long.parseLong(txtPlantaId.getText());
                String[] idsString = txtContenedores.getText().split(",");
                List<Long> listaIds = new ArrayList<>();
                
                for(String s : idsString) {
                    listaIds.add(Long.parseLong(s.trim()));
                }

                controller.asignarMasivamente(plantaId, listaIds);
                areaLog.append("✅ Asignación enviada correctamente a Planta " + plantaId + "\n");
                areaLog.append("Contenedores procesados: " + listaIds + "\n");
                
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Error: Revisa que los IDs sean números.");
            } catch (Exception ex) {
                areaLog.append("❌ Error en asignación: " + ex.getMessage() + "\n");
            }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaLog), BorderLayout.CENTER);
        return panel;
    }


    // --- MAIN ---
    public static void main(String[] args) {
        // Arrancar Spring
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(EcoembesClientApplication.class)
                .headless(false)
                .run(args);

        // Obtener Proxy
        IEcoembesServiceProxy proxy = ctx.getBean(IEcoembesServiceProxy.class);
        SwingClientController controller = new SwingClientController(proxy);

        // Login Rapido
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
                JOptionPane.showMessageDialog(null, "Credenciales incorrectas.");
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }
}