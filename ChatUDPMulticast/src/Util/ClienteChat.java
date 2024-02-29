package Util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ClienteChat {

    private JFrame frame;
    private JTextField textField1;
    private JButton button1;
    private JTextArea textArea1;
    private MulticastSocket multicastSocket;
    private InetAddress grupo;

    public static void main(String[] args) {
        ClienteChat chat = new ClienteChat();
        chat.inicializa("nombre");
    }

    public void inicializa(String nombre) {
        frame = new JFrame(nombre);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        textArea1 = new JTextArea();
        textArea1.setEditable(false);
        textArea1.append("¡Bienvenido al chat, " + nombre + "!\n");
        JScrollPane scrollPane = new JScrollPane(textArea1);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        textField1 = new JTextField();
        textField1.setPreferredSize(new Dimension(200, 30));

        button1 = new JButton("Enviar");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    enviarMensaje(nombre+": "+textField1.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(textField1);
        inputPanel.add(button1);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);

        try {
            multicastSocket = new MulticastSocket(6006);
            grupo = InetAddress.getByName("230.0.0.1");
            multicastSocket.joinGroup(grupo);
            Thread hiloRecepcion = new Thread(this::recibirMensajes);
            hiloRecepcion.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarMensaje(String mensaje) throws IOException {
        byte[] buffer = mensaje.getBytes();
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, grupo, 6006);
        DatagramSocket socket = new DatagramSocket();
        socket.send(paquete);
        socket.close();
    }

    private void recibirMensajes() {
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(paquete);
                String mensajeRecibido = new String(paquete.getData(), 0, paquete.getLength());
                textArea1.append(mensajeRecibido + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
