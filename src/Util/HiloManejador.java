package Util;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HiloManejador implements Runnable {
    private Socket cliente;
    private static List<Socket> clientesConectados = new ArrayList<>();
    private static List<String> listaNombres = new ArrayList<>();
    private static List<String> mensajes = new ArrayList<>();
    private DataOutputStream flujoSalida;

    public HiloManejador(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        try {
            InputStream in = cliente.getInputStream();
            DataInputStream flujoEntrada = new DataInputStream(in);
            OutputStream out = cliente.getOutputStream();
            flujoSalida = new DataOutputStream(out);

            while (true) {
                String mensajeCliente = flujoEntrada.readUTF();
                if (mensajeCliente.startsWith("[Log]")) {
                    if (listaNombres.contains(mensajeCliente)) {
                        System.out.println("El intento está en la lista: " + listaNombres.contains(mensajeCliente));
                        System.out.println(listaNombres.size());
                        flujoSalida.writeBoolean(false);
                    } else {
                        listaNombres.add(mensajeCliente);
                        flujoSalida.writeBoolean(true);
                    }
                } else {
                    // Agregar mensaje a la lista de mensajes
                    System.out.println(mensajeCliente);
                    mensajes.add(mensajeCliente);
                    transmitirMensajeATodos(mensajeCliente);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void transmitirMensajeATodos(String mensaje) {
        for (Socket cliente : clientesConectados) {
            try {
                DataOutputStream flujoSalidaCliente = new DataOutputStream(cliente.getOutputStream());
                flujoSalidaCliente.writeUTF(mensaje);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void agregarCliente(Socket cliente) {
        clientesConectados.add(cliente);
    }
}