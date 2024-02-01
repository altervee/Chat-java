package Hilos;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainHilos implements Runnable {

    private Socket cliente;
    private DataInputStream flujo_entrada = null;
    private DataOutputStream flujo_salida = null;

    public MainHilos(Socket cliente) {
        this.cliente = cliente;
        InputStream inaux = null;
        OutputStream outaux = null;
        try {
            inaux = cliente.getInputStream();
            outaux = cliente.getOutputStream();
            flujo_entrada = new DataInputStream(inaux);
            flujo_salida = new DataOutputStream(outaux);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        System.out.println("Atendiendo petición del cliente " +
                cliente.getInetAddress() + ":" + cliente.getPort());

        String nombreArchivo = null;
        try {
            nombreArchivo = flujo_entrada.readUTF();
            System.out.println("Archivo: " + nombreArchivo);

            File archivo = new File(nombreArchivo);
            if (archivo.exists()) {
                flujo_salida.writeInt(200);
                FileReader fr = new FileReader(archivo);
                BufferedReader br = new BufferedReader(fr);
                String linea;
                while ((linea = br.readLine()) != null) {
                    flujo_salida.writeUTF(linea);
                    Thread.sleep(1000);
                }
                br.close();
            } else {
                System.out.println("Fichero no encontrado");
                flujo_salida.writeInt(404);
                cliente.close(); // Cerrar la conexión con el cliente si el archivo no existe
            }
            flujo_salida.close();
            cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



