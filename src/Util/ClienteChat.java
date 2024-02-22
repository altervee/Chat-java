package Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;



public class ClienteChat {

    private JFrame frame;
    private JTextField textField1;
    private JButton button1;
    private JTextArea textArea1;
    private JScrollBar scrollBar1;

    private Socket cliente;

    public static void main(String[] args) {
        ClienteChat chat = new ClienteChat();
        chat.initialize("nombre"); // Aquí debes proporcionar el nombre
    }

    public void initialize(String nombre) {
        frame = new JFrame("Cliente Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        textArea1 = new JTextArea();
        textArea1.setEditable(false);
        textArea1.append("¡Bienvenido al chat, " + nombre + "!\n"); // Mensaje de bienvenida con el nombre proporcionado
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
                        Socket cliente = new Socket("localhost", 6001); // AQUÍ TAMBIÉN PUEDE IR LA IP
                        InputStream in = cliente.getInputStream();
                        DataInputStream flujoEntrada = new DataInputStream(in);
                        OutputStream out = cliente.getOutputStream();
                        DataOutputStream flujoSalida = new DataOutputStream(out);
                        String textoIngresado = textField1.getText();
                        flujoSalida.writeUTF(nombre+": "+textoIngresado); // Enviar LO QUE INTRODUCE AL SERVIDOR
                        String respuesta = flujoEntrada.readUTF(); // RECIBIR DEL SERVIDOR
                        System.out.println(respuesta);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JPanel inputPanel = new JPanel(new FlowLayout()); // JPanel para colocar los componentes de entrada
        inputPanel.add(textField1);
        inputPanel.add(button1);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);

        // Establecer conexión con el servidor y comenzar el hilo de recepción de mensajes
        try {
            cliente = new Socket("localhost", 6001);
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
                textArea1.append(mensajeRecibido + "\n"); // Mostrar el mensaje en el área de texto
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}