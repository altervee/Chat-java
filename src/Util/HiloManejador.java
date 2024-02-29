package Util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HiloManejador implements Runnable {
    private Socket cliente;
    private static List<Socket> clientesConectados = new ArrayList<>();
    private static List<String> listaNombres = new ArrayList<>();
    private static List<String> mensajes = new ArrayList<>();
    private DataOutputStream flujoSalida;
    private static HashMap<String, Thread> hilosUsuarios = new HashMap<>(); // Mapa para mantener los nombres de usuario y los hilos correspondientes

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
                if (mensajeCliente.equals("[SolicitarMensajes]")) {
                    // Enviar todos los mensajes almacenados al cliente
                    for (String mensaje : mensajes) {
                        flujoSalida.writeUTF(mensaje);
                    }
                    // Enviar los nombres de usuario al cliente
                    enviarNombresUsuarios();
                } else if (mensajeCliente.startsWith("[Log]")) {
                    if (listaNombres.contains(mensajeCliente)) {
                        System.out.println("El intento está en la lista: " + listaNombres.contains(mensajeCliente));
                        System.out.println(listaNombres.size());
                        flujoSalida.writeBoolean(false);
                    } else {
                        listaNombres.add(mensajeCliente);
                        flujoSalida.writeBoolean(true);
                        // Agregar el nombre de usuario y su hilo correspondiente al mapa
                        hilosUsuarios.put(mensajeCliente, Thread.currentThread());
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

    private void enviarNombresUsuarios() throws IOException {
        StringBuilder nombres = new StringBuilder();
        for (String nombre : hilosUsuarios.keySet()) {
            Thread hiloUsuario = hilosUsuarios.get(nombre);
            if (hiloUsuario.isAlive()) {
                nombres.append(nombre).append(",");
            }
        }
        // Eliminar la última coma
        if (nombres.length() > 0) {
            nombres.deleteCharAt(nombres.length() - 1);
        }
        flujoSalida.writeUTF(nombres.toString());
    }

    public static void agregarCliente(Socket cliente) {
        clientesConectados.add(cliente);
    }
}