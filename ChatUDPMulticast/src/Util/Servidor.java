package Util;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

    private static final int PUERTO_MULTICAST = 5000;
    private static final String DIRECCION_MULTICAST = "230.0.0.1";

    private static Map<String, Map<InetAddress, Integer>> logsClientes = new HashMap<>();

    public static void main(String[] args) {
        try {
            MulticastSocket multicastSocket = new MulticastSocket(PUERTO_MULTICAST);
            InetAddress grupo = InetAddress.getByName(DIRECCION_MULTICAST);
            multicastSocket.joinGroup(grupo);
            System.out.println("Servidor activo");

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(paquete);
                String mensajeCliente = new String(paquete.getData(), 0, paquete.getLength());
                System.out.println(mensajeCliente);

                if (mensajeCliente.startsWith("[Log]")) {
                    // Procesar mensaje de registro
                    String tipoLog = obtenerTipoLog(mensajeCliente);
                    InetAddress direccionCliente = paquete.getAddress();

                    if (!logsClientes.containsKey(tipoLog)) {
                        logsClientes.put(tipoLog, new HashMap<>());
                    }

                    Map<InetAddress, Integer> clientes = logsClientes.get(tipoLog);

                    if (!clientes.containsKey(direccionCliente)) {
                        enviarRespuestaRegistro("[LogTrue]", paquete.getPort(), direccionCliente);
                        System.out.println("Es un nuevo cliente");
                        clientes.put(direccionCliente, paquete.getPort()); // Añadir al cliente al mapa de clientes
                    } else {
                        enviarRespuestaRegistro("[LogFalse]", paquete.getPort(), direccionCliente);
                        System.out.println("Pasa por false");
                    }
                } else {
                    // Procesar otros mensajes (multicast)
                    // Aquí colocarías el código para procesar los mensajes multicast
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String obtenerTipoLog(String mensaje) {
        return mensaje.substring(5); // Eliminar el prefijo "[Log]"
    }

    private static void enviarRespuestaRegistro(String mensaje, int puertoCliente, InetAddress direccionCliente) throws IOException {
        try {
            DatagramSocket socketRespuesta = new DatagramSocket();
            byte[] buffer = mensaje.getBytes();
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, direccionCliente, puertoCliente);
            socketRespuesta.send(paquete);
            socketRespuesta.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}