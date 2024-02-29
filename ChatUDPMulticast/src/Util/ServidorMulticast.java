package Util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ServidorMulticast {

    private static final int PUERTO_MULTICAST = 6006;
    private static final String DIRECCION_MULTICAST = "230.0.0.1";
    private static final int CAPACIDAD_MAXIMA_MENSAJES = 10;

    public static void main(String[] args) {
        List<String> listaMensajes = new ArrayList<>();

        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress grupo = InetAddress.getByName(DIRECCION_MULTICAST);

            byte[] buffer = new byte[1024];

            while (true) {
                // Escucha para recibir nuevos mensajes
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                socket.receive(paquete);

                // Obtener el mensaje recibido
                String mensajeRecibido = new String(paquete.getData(), 0, paquete.getLength());

                // Agregar el mensaje a la lista de mensajes
                agregarMensaje(listaMensajes, mensajeRecibido);

                // Enviar el último mensaje a todos los clientes suscritos al grupo multicast
                enviarMensajeMulticast(listaMensajes, grupo, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void agregarMensaje(List<String> listaMensajes, String mensaje) {
        // Agregar el mensaje a la lista
        listaMensajes.add(mensaje);

        // Verificar si la lista excede la capacidad máxima
        if (listaMensajes.size() > CAPACIDAD_MAXIMA_MENSAJES) {
            // Si excede, eliminar el mensaje más antiguo
            listaMensajes.remove(0);
        }
    }

    private static void enviarMensajeMulticast(List<String> listaMensajes, InetAddress grupo, DatagramSocket socket) throws IOException {
        if (!listaMensajes.isEmpty()) {
            // Obtener el último mensaje de la lista
            String ultimoMensaje = listaMensajes.get(listaMensajes.size() - 1);

            // Convertir el mensaje en bytes
            byte[] buffer = ultimoMensaje.getBytes();

            // Crear un nuevo paquete para enviar el último mensaje a todos los clientes suscritos al grupo multicast
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, grupo, PUERTO_MULTICAST);

            // Enviar el último mensaje a todos los clientes suscritos al grupo multicast
            socket.send(paquete);
        }
    }
}