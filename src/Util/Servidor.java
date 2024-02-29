package Util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6002);
            System.out.println("Servidor activo");

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + clienteSocket);// visualizzar los usuarios por terminal

                HiloManejador.agregarCliente(clienteSocket);

                Thread hilo = new Thread(new HiloManejador(clienteSocket));
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}