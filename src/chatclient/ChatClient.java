/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Ramon
 */
public class ChatClient {

    private String username;

    private OutServer os;
    private Server server;
    private GUI gui;


    /* CONSTRUCTOR ---------------------------------------------------------- */
    public ChatClient() {
        gui = new GUI(this);
    }

    /* GETTERS Y SETTERS ---------------------------------------------------- */
    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    
    /* METODOS PUBLICOS ----------------------------------------------------- */
    public static void main(String[] args) {
        ChatClient cc = new ChatClient();
    }

    //Añade un servidor cuando se conecta a uno
    public void addServer(Socket serverSocket) {
        this.server = new Server(serverSocket, this);
        this.gui.serverConected(this.username);
        Thread t = new Thread(this.server);
        t.start();
    }

    //Muestra el mensaje en el area de chat
    public void showMsg(String msg) {
        this.gui.showMsg(msg);
    }

    //Crea y empieza el thread del Out Server
    public void startOutServer(String host, int port) {
        os = new OutServer(host, port, this);
        os.start();
    }

    /* METODOS PRIVADOS ----------------------------------------------------- */
    //Envía un mensaje al servidor
    public void sendMsg(String text) {
        String msg = this.username + ": " + text;
        this.showMsg(msg);
        this.server.sendMsg(msg);
    }
    
    public void errorServerLost(){
        this.gui.errorServerLost();
    }
    
    public void updateStats(String command){
        if (command.startsWith("cliSer")) {
            this.gui.setClientsServer(Integer.parseInt(command.substring(6, command.length())));
        }else if (command.startsWith("cliTot")) {
            this.gui.setTotalClientsServer(Integer.parseInt(command.substring(6, command.length())));
        }
    }

}
