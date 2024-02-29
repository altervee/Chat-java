package Util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ClienteChat {

    private JFrame frame;
    private JTextField textField1;
    private JButton button1;
    private JTextArea textArea1;
    private JScrollBar scrollBar1;
    private JLabel lblinea; // Agregar la etiqueta para mostrar los nombres de usuario
    private Socket cliente;
    private boolean solicitudEnviada = false;
    private HashMap<String, Thread> hilosUsuarios = new HashMap<>(); // Mapa para mantener los hilos de los usuarios

    public static void main(String[] args) {
        ClienteChat chat = new ClienteChat();
        chat.initialize("nombre");
    }

    public void initialize(String nombre) {
        frame = new JFrame(nombre);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        textArea1 = new JTextArea();
        textArea1.setEditable(false);
        textArea1.append("¡Bienvenido al chat, " + nombre + "!\n");
        scrollBar1 = new JScrollBar();
        JScrollPane scrollPane = new JScrollPane(textArea1);
        scrollPane.setVerticalScrollBar(scrollBar1);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        textField1 = new JTextField();
        textField1.setPreferredSize(new Dimension(200, 30));

        button1 = new JButton("Enviar");

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (this) {
                    try {
                        if (!solicitudEnviada) {
                            DataOutputStream flujoSalida = new DataOutputStream(cliente.getOutputStream());
                            flujoSalida.writeUTF("[SolicitarMensajes]");
                            solicitudEnviada = true;
                        }

                        Socket cliente = new Socket("localhost", 6002);
                        InputStream in = cliente.getInputStream();
                        DataInputStream flujoEntrada = new DataInputStream(in);
                        OutputStream out = cliente.getOutputStream();
                        DataOutputStream flujoSalida = new DataOutputStream(out);
                        String textoIngresado = textField1.getText();
                        flujoSalida.writeUTF(nombre+": "+textoIngresado);
                        String respuesta = flujoEntrada.readUTF();
                        System.out.println(respuesta);
                        textField1.setText("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(textField1);
        inputPanel.add(button1);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        lblinea = new JLabel(" "); // Inicializar la etiqueta
        mainPanel.add(lblinea, BorderLayout.NORTH);

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);

        try {
            cliente = new Socket("localhost", 6002);
            Thread hiloRecepcion = new Thread(this::recibirMensajes);
            hiloRecepcion.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recibirMensajes() {
        try {
            DataInputStream flujoEntrada = new DataInputStream(cliente.getInputStream());
            while (true) {
                String mensajeRecibido = flujoEntrada.readUTF();
                textArea1.append(mensajeRecibido + "\n");

                // Actualizar la etiqueta con los nombres de usuario
                actualizarEtiquetaUsuarios();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                frame.dispose();
                cliente.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Método para actualizar la etiqueta con los nombres de usuario
    private void actualizarEtiquetaUsuarios() {
        StringBuilder usuariosConectados = new StringBuilder("Usuarios conectados: ");
        for (String nombreUsuario : hilosUsuarios.keySet()) {
            Thread hiloUsuario = hilosUsuarios.get(nombreUsuario);
            if (hiloUsuario.isAlive()) {
                usuariosConectados.append(nombreUsuario).append(", ");
            } else {
                // Eliminar el usuario del mapa si el hilo no está vivo
                hilosUsuarios.remove(nombreUsuario);
            }
        }
        // Eliminar la última coma
        if (usuariosConectados.length() > 0) {
            usuariosConectados.deleteCharAt(usuariosConectados.length() - 1);
        }
        lblinea.setText(usuariosConectados.toString());
    }
}
