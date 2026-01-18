package es.deusto.sd.ecoembes.client.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
        setTitle("Ecoembes Client - Prototipo 3");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // Pestaña 1: Crear Contenedor
        tabs.addTab("Nuevo Contenedor", createPanelCrearContenedor());

        // Pestaña 2: Consultar Zona
        tabs.addTab("Estado Zona", createPanelZona());
        
        // Botón de Logout abajo
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.addActionListener(e -> {
            controller.logout();
            dispose(); // Cierra ventana
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
        JButton btnCrear = new JButton("Crear");

        panel.add(new JLabel("Dirección:")); panel.add(txtDir);
        panel.add(new JLabel("Código Postal:")); panel.add(txtCP);
        panel.add(new JLabel("Capacidad:")); panel.add(txtCap);
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
        JTextField txtFecha = new JTextField("2024-01-20", 10);
        JButton btnBuscar = new JButton("Buscar");
        
        top.add(new JLabel("CP:")); top.add(txtCP);
        top.add(new JLabel("Fecha (YYYY-MM-DD):")); top.add(txtFecha);
        top.add(btnBuscar);

        JTextArea areaResultados = new JTextArea();
        
        btnBuscar.addActionListener(e -> {
            try {
                int cp = Integer.parseInt(txtCP.getText());
                List<EstadoContenedorDTO> lista = controller.getContenedoresPorZona(cp, txtFecha.getText());
                
                StringBuilder sb = new StringBuilder();
                for (EstadoContenedorDTO dto : lista) {
                    sb.append("ID: ").append(dto.getId())
                      .append(" - Dir: ").append(dto.getDireccion())
                      .append(" - Nivel: ").append(dto.getNivelEnFecha())
                      .append("\n");
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

    // --- MAIN ---
    public static void main(String[] args) {
        // 1. Arrancar el contexto de Spring (en modo headless false para permitir GUI)
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(EcoembesClientApplication.class)
                .headless(false)
                .run(args);

        // 2. Obtener el Proxy del contexto de Spring
        IEcoembesServiceProxy proxy = ctx.getBean(IEcoembesServiceProxy.class);
        
        // 3. Crear Controlador
        SwingClientController controller = new SwingClientController(proxy);

        // 4. Mostrar Login (Simple JOptionPane)
        JPanel loginPanel = new JPanel(new GridLayout(2, 2));
        JTextField userField = new JTextField("admin@ecoembes.es"); // Valor por defecto para probar rápido
        JPasswordField passField = new JPasswordField("admin");
        
        loginPanel.add(new JLabel("Email:")); loginPanel.add(userField);
        loginPanel.add(new JLabel("Password:")); loginPanel.add(passField);

        int result = JOptionPane.showConfirmDialog(null, loginPanel, "Login Ecoembes", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            boolean loginOk = controller.login(userField.getText(), new String(passField.getPassword()));
            
            if (loginOk) {
                // 5. Si Login OK -> Abrir Ventana Principal
                SwingUtilities.invokeLater(() -> {
                    SwingClientGUI gui = new SwingClientGUI(controller);
                    gui.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(null, "Login incorrecto. Adiós.");
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }
}