package Servidor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Hilos.MainHilos;

public class Servidor {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int puerto=6001;
        ServerSocket servidor=null;
        try {
            servidor=new ServerSocket(puerto);
            System.out.println("escucho en el puerto"+puerto);
            while(true) {
                Socket cliente1=servidor.accept();
                Thread hilo=new Thread(new MainHilos(cliente1));
                hilo.start();
            }//fin while
            //servidor.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}