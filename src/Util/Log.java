package Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Log {
    private JFrame frame; // JFrame para contener los componentes
    private JButton logbtn;
    private JFormattedTextField textField;

    public static void main(String[] args) {
        Log log = new Log(); // Crear una instancia de la clase Log
        log.initialize(); // Inicializar la interfaz gráfica
    }

    public void initialize() {
        frame = new JFrame("Log"); // Crear un JFrame con el título "Log"
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Establecer la operación de cierre al cerrar la ventana

        // Inicializar el JButton y el JFormattedTextField
        logbtn = new JButton("Enviar");
        textField = new JFormattedTextField();

        // Establecer el tamaño preferido del cuadro de texto
        textField.setPreferredSize(new Dimension(200, 30)); // 200 pixels de ancho y 30 pixels de alto

        // Agregar ActionListener al botón
        logbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Socket cliente = new Socket("localhost", 6001); // AQUÍ TAMBIÉN PUEDE IR LA IP
                    InputStream in = cliente.getInputStream();
                    DataInputStream flujoEntrada = new DataInputStream(in);
                    OutputStream out = cliente.getOutputStream();
                    DataOutputStream flujoSalida = new DataOutputStream(out);
                    String textoIngresado = textField.getText();
                    flujoSalida.writeUTF("[Log]"+textoIngresado); // Enviar LO QUE INTRODUCE AL SERVIDOR
                    boolean respuesta = flujoEntrada.readBoolean(); // RECIBIR DEL SERVIDOR

                    if (!respuesta) {
                        JOptionPane.showMessageDialog(frame, "¡Ese nombre ya está en uso!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                    } else {

                        JOptionPane.showMessageDialog(frame, "¡Bienvenido " + textoIngresado + "!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                        ClienteChat clienteChat = new ClienteChat();
                        clienteChat.initialize(textoIngresado); // Llamar al método initialize() del cliente
                        //cliente.close();
                        frame.dispose();
                    }


                    //cliente.close(); // EN EL CASO CORRECTO
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        });

        // Crear un panel para colocar los botones y el campo de texto
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(logbtn);
        panel.add(textField);

        // Agregar el panel al contenedor
        frame.add(panel);

        // Establecer el tamaño y hacer visible el JFrame
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}