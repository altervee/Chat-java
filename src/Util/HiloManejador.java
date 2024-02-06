package Util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class HiloManejador implements Runnable {
    private Socket cliente;
    private int numeroAdivinar;
    public  static ArrayList<String> listaNombres = new ArrayList<>();
    public HiloManejador(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        try {
            InputStream in = cliente.getInputStream();
            DataInputStream flujoEntrada = new DataInputStream(in);
            OutputStream out = cliente.getOutputStream();
            DataOutputStream flujoSalida = new DataOutputStream(out);

            while (true) {
                String intento = flujoEntrada.readUTF();

                if (listaNombres.contains(intento)) {

                    System.out.println("El intento está en la lista: " + listaNombres.contains(intento));
                    System.out.println(listaNombres.size());
                    flujoSalida.writeBoolean(false);
                } else {
                    listaNombres.add(intento);
                    flujoSalida.writeBoolean(true);

                }
            }
            //cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}