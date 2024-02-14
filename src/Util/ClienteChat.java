package Util;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteChat {

    private JTextField textField1;
    private JButton button1;
    private JTextArea textArea1;
    private JScrollBar scrollBar1;

    public static void main(String[] args) {
        try {
            Socket sCliente = new Socket("127.0.0.1", 6001);
            System.out.println("Conectado");

            OutputStream outaux = sCliente.getOutputStream();
            DataOutputStream flujo_salida = new DataOutputStream(outaux);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Introduce el nombre del archivo: ");
            String nombre = scanner.nextLine();

            System.out.println("Mando nombre de archivo");
            flujo_salida.writeUTF(nombre);

            InputStream inaux = sCliente.getInputStream();
            DataInputStream flujo_entrada = new DataInputStream(inaux);
            int codigo = flujo_entrada.readInt();
            if (codigo == 200) {
                String linea;
                while ((linea = flujo_entrada.readUTF()) != null)
                    System.out.println(linea);
            } else {// en este caso recibe el 404
                System.out.println("El fichero no existe");
            }
            sCliente.close();
            scanner.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Final del fichero");
        }

    }
}