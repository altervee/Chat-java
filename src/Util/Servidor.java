package Util;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private static List<String> nombresRegistrados = new ArrayList<>();

    public static void main(String[] args) {
        int puerto = 6001;
        ServerSocket servidor = null;

        try {
            servidor = new ServerSocket(puerto);
            System.out.println("Escuchando en el puerto " + puerto);

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Atendiendo petición del cliente");

                // Iniciar un hilo para manejar la comunicación con el cliente
                Thread t = new Thread(new HiloManejador(cliente));
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}