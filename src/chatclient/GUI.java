/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
public class GUI extends JFrame implements ActionListener, KeyListener {

    private ChatClient chatClient;

    private JTextField port;
    private JTextField host;
    private JButton btnStart;

    private JTextArea chatArea;
    private JTextField chatText;
    private JButton btnSend;
    private JLabel userState;
    private JLabel lblClientsConnected;
    private JLabel lblTotalClientsConnected;

    /* CONSTRUCTOR ---------------------------------------------------------- */
    public GUI(ChatClient cc) {
        this.chatClient = cc;
        this.crearVentana();
    }

    /* METODOS PUBLICOS ----------------------------------------------------- */
    /* METODOS PRIVADOS ----------------------------------------------------- */
    //Añade los componentes a la ventana principal
    private JPanel addChatArea() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        this.userState = new JLabel("Disconnected");
        this.userState.setFont(new Font("Arial Black", 0, 14));
        this.userState.setForeground(Color.red);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        c.fill = GridBagConstraints.CENTER;
        p.add(this.userState, c);
       
        this.chatArea = new JTextArea(30, 40);
        JScrollPane scrollPane = new JScrollPane(this.chatArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatArea.setEditable(false);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        p.add(scrollPane, c);

        this.chatText = new JTextField();
        this.chatText.setEditable(false);
        this.chatText.addKeyListener(this);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 1.0d;

        p.add(this.chatText, c);

        this.btnSend = new JButton("Send");
        this.btnSend.setEnabled(false);
        this.btnSend.addActionListener(this);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 0.2d;

        p.add(this.btnSend, c);

        return p;
    }

    private JPanel addServerInfo() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 3, 10, 10));

        JLabel labelHost = new JLabel("HOST Server");
        labelHost.setFont(new Font("Arial Black", 0, 14));
        labelHost.setHorizontalAlignment(JLabel.CENTER);
        p.add(labelHost);

        this.host = new JTextField();
        p.add(this.host);

        p.add(new JPanel());

        JLabel labelPort = new JLabel("PORT Server");
        labelPort.setFont(new Font("Arial Black", 0, 14));
        labelPort.setHorizontalAlignment(JLabel.CENTER);
        p.add(labelPort);

        this.port = new JTextField();
        p.add(this.port);

        this.btnStart = new JButton("Start");
        this.btnStart.addActionListener(this);
        p.add(this.btnStart);

        return p;
    }
    
    private JPanel addStadistics(){
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 2));
        
        
        this.lblClientsConnected = new JLabel("Clients in your server: 0");
        this.lblClientsConnected.setFont(new Font("Arial Black", 0, 14));
        this.lblClientsConnected.setForeground(Color.blue);
        this.lblClientsConnected.setHorizontalAlignment(JLabel.CENTER);
        p.add(this.lblClientsConnected);

        this.lblTotalClientsConnected = new JLabel("Total clients in chat: 0");
        this.lblTotalClientsConnected.setFont(new Font("Arial Black", 0, 14));
        this.lblTotalClientsConnected.setForeground(Color.blue);
        this.lblTotalClientsConnected.setHorizontalAlignment(JLabel.CENTER);
        p.add(this.lblTotalClientsConnected);

        
        return p;
    }

    //Añade los componentes a la ventana
    private void addUIComponentes(Container panel) {
        panel.add(this.addServerInfo(), BorderLayout.NORTH);
        panel.add(this.addChatArea(), BorderLayout.CENTER);
        panel.add(this.addStadistics(),BorderLayout.SOUTH);
    }

    //Crea la interfaz principal
    private void crearVentana() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("2do DAM - Chat");
        this.setLayout(new BorderLayout());

        this.addUIComponentes(getContentPane());
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    //Coge el texto del textfield para enviar el mensaje
    private void takeMsgTextField() {
        if (!"".equals(this.chatText.getText())) {
            String msg = this.chatText.getText();
            this.chatText.setText("");
            this.chatClient.sendMsg(msg);
        }
    }

    public void showMsg(String msg) {
        this.chatArea.append(msg + "\n");
    }

    private void startOutServer() {
        this.chatText.setEditable(true);
        this.btnSend.setEnabled(true);
        this.btnStart.setEnabled(false);
        this.port.setEditable(false);
        this.host.setEditable(false);

        this.chatClient.startOutServer(this.host.getText(), Integer.parseInt(this.port.getText()));
    }

    public void errorServerLost() {
        JOptionPane.showMessageDialog(this, "Conexión con el servidor perdida", "Error", JOptionPane.ERROR_MESSAGE);
        this.chatText.setEditable(false);
        this.chatText.setText("");
        this.btnSend.setEnabled(false);
        this.btnStart.setEnabled(true);
        this.port.setEditable(true);
        this.host.setEditable(true);
        this.userState.setText("Disconnected");
        this.userState.setForeground(Color.red);
        this.lblClientsConnected.setText("Clients in your server: 0");
        this.lblTotalClientsConnected.setText("Total clients in chat: 0");
    }

    public void serverConected(String user) {
        this.userState.setText("Conncted as: " + user);
        this.userState.setForeground(Color.green);
    }
    
    public void setClientsServer(int stats) {
        this.lblClientsConnected.setText("Clients in your server: "+stats);
    }

    public void setTotalClientsServer(int stats) {
        this.lblTotalClientsConnected.setText("Total clients in chat: "+stats);
    }

    /* LISTENERS ------------------------------------------------------------ */
    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        if (str.equals("Start")) {
            if (!(this.port.getText().equals("") || this.host.getText().equals(""))) {
                String user = JOptionPane.showInputDialog(this, "Introduce tu nombre de usuario", "Pedir nombre", JOptionPane.QUESTION_MESSAGE);
                while (user == null || user.equals("")) {
                    JOptionPane.showMessageDialog(this, "Por favor, introduce un nombre de usuario", "Error", JOptionPane.ERROR_MESSAGE);
                    user = JOptionPane.showInputDialog(this, "Introduce un nombre de usuario valido", "Pedir nombre", JOptionPane.QUESTION_MESSAGE);
                }
                this.chatClient.setUsername(user);
                this.userState.setText("Connecting...");
                this.userState.setForeground(Color.orange);
                this.startOutServer();

            } else {
                JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (str.equals("Send")) {
            this.takeMsgTextField();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == this.chatText) {
            if (e.VK_ENTER == e.getKeyCode()) {
                this.takeMsgTextField();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }



}
