/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and openConection the template in the editor.
 */
package chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramon
 */
public class OutServer extends Thread {

    private final int PORT;
    private final String HOST;
    private final ChatClient chatClient;

    private Socket sock;


    /* CONSTRUCTOR ---------------------------------------------------------- */
    public OutServer(String h, int p, ChatClient cc) {
        PORT = p;
        HOST = h;
        chatClient = cc;
    }

    /* METODOS PUBLICOS ----------------------------------------------------- */
    @Override
    public void run() {
        this.chatClient.showMsg("Connecting...");

        try {
            while (this.chatClient.getServer() == null) {
                makeContact();
                Thread.sleep(2000);
            }
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(OutServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /* METODOS PRIVADOS ----------------------------------------------------- */
    //Intenta contactar con el servidor
    private void makeContact() {
        try {
            System.out.println("Connecting...");
            this.sock = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
            sendInfo(out);
        } catch (IOException e) {
            this.chatClient.showMsg("Error de conexión con el servidor. Reconectando en breves...");
            System.out.println("Error de conexión");
            System.out.println(e);
            System.out.println("Volviendo a conectar en breves");
        }

    }

    //Si consigue contactar con el servidor le informa de que es un cliente
    private void sendInfo(PrintWriter out) {
        try {
            String info = "&"+this.chatClient.getUsername();
            out.println(info);
            this.chatClient.showMsg("Conectado con el servidor "+HOST+":"+PORT);
            this.chatClient.addServer(sock);
        } catch (Exception ex) {
            this.chatClient.showMsg("Error al enviar información");
            System.out.println("Problem sending info \n");
            System.out.println(ex);
        }
    }

}
