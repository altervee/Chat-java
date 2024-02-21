package Util;

import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClienteChat {

    private JTextField textField1;
    private JButton button1;
    private JTextArea textArea1;
    private JScrollBar scrollBar1;

    public static void main(String[] args) {
        if (args.length > 0) {
            // Llama al método para crear la interfaz gráfica, pasando el primer argumento como nombre
            crearInterfazGrafica(args[0]);
        } else {
            System.out.println("Debe proporcionar un argumento para el nombre del cliente.");
        }
    }

    // Método para crear la interfaz gráfica del cliente
    private static void crearInterfazGrafica(String nombre) {
        JFrame frame = new JFrame("Cliente Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana

        JPanel panel = new JPanel(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JTextField textField = new JTextField();
        panel.add(textField, BorderLayout.SOUTH);

        JButton button = new JButton("Enviar");
        button.addActionListener(e -> {
            try {
                // Establece la conexión con el servidor
                Socket sCliente = new Socket("127.0.0.1", 6001);
                System.out.println("Conectado");

                // Configura los flujos de salida para enviar datos al servidor
                OutputStream outaux = sCliente.getOutputStream();
                DataOutputStream flujo_salida = new DataOutputStream(outaux);

                // Envía el nombre del cliente y el texto ingresado al servidor
                String texto = nombre + ": " + textField.getText();
                flujo_salida.writeUTF(texto);

                // Cierra la conexión con el servidor
                sCliente.close();

            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println("Error al enviar mensaje al servidor.");
            }
        });
        panel.add(button, BorderLayout.EAST);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}