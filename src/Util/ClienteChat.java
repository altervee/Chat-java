package Util;


import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClienteChat {

    private JTextField textField1;
    private JButton button1;
    private JTextArea textArea1;
    private JScrollBar scrollBar1;

    public static void main(String[] args) {
        // Llama al método para crear la interfaz gráfica
        crearInterfazGrafica();
    }

    // Método para crear la interfaz gráfica del cliente
    private static void crearInterfazGrafica() {
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

                // Configura los flujos de entrada y salida para comunicarse con el servidor
                OutputStream outaux = sCliente.getOutputStream();
                DataOutputStream flujo_salida = new DataOutputStream(outaux);

                // Envía el nombre del archivo al servidor
                String nombre = textField.getText();
                flujo_salida.writeUTF(nombre);

                // Recibe y muestra los mensajes del servidor
                InputStream inaux = sCliente.getInputStream();
                DataInputStream flujo_entrada = new DataInputStream(inaux);
                int codigo = flujo_entrada.readInt();
                if (codigo == 200) {
                    String linea;
                    while ((linea = flujo_entrada.readUTF()) != null)
                        System.out.println(linea);
                } else {
                    System.out.println("El fichero no existe");
                }

                // Cierra la conexión con el servidor
                sCliente.close();

            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println("Final del fichero");
            }
        });
        panel.add(button, BorderLayout.EAST);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
