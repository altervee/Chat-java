package Util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Log {
    private JFrame frame;
    private JButton logbtn;
    private JTextField textField;

    private static final int PUERTO_MULTICAST = 5000;
    private static final String DIRECCION_MULTICAST = "230.0.0.1";

    public static void main(String[] args) {
        Log log = new Log();
        log.initialize();
    }

    public void initialize() {
        frame = new JFrame("Log");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logbtn = new JButton("Enviar");
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));

        logbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int puerto = 6005;

                try {
                    DatagramSocket socket = new DatagramSocket();
                    InetAddress servidorDireccion = InetAddress.getByName("localhost");

                    String textoIngresado = textField.getText();
                    byte[] buffer = ("[Log]" + textoIngresado).getBytes();
                    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, servidorDireccion, PUERTO_MULTICAST);
                    socket.send(paquete);

                    byte[] respuestaBuffer = new byte[1024];
                    DatagramPacket respuestaPaquete = new DatagramPacket(respuestaBuffer, respuestaBuffer.length);
                    socket.receive(respuestaPaquete);

                    String respuesta = new String(respuestaPaquete.getData(), 0, respuestaPaquete.getLength());
                    System.out.println("Respuesta del server al log: " + respuesta);

                    if (respuesta.contains("[LogTrue]")) {
                        JOptionPane.showMessageDialog(frame, "¡Bienvenido " + textoIngresado + "!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                        ClienteChat clienteChat = new ClienteChat();
                        clienteChat.inicializa(textoIngresado);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "¡Ese nombre ya está en uso!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                    }

                    socket.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(logbtn);
        panel.add(textField);

        frame.add(panel);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}