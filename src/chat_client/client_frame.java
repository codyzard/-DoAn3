package chat_client;

import java.awt.Image;
import java.awt.Insets;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import test.NewJFrame;
public class client_frame extends javax.swing.JFrame 
{
    String username, address = "localhost";
    ArrayList<String> users = new ArrayList();
    int port = 1050;
    Boolean isConnected = false;
    
    Socket sock;
    BufferedReader reader;
    ObjectOutputStream oout;
    OutputStream os;
    InputStream is;
    PrintWriter writer;
    static String replacePath = "╰☆☆ĨMÁĞĔ☆☆╮";
    //--------------------------//
    
    public void ListenThread() 
    {
         Thread IncomingReader = new Thread(new IncomingReader());
         IncomingReader.start();
    }
    
    //--------------------------//
    
    public void appendMessage(JTextPane msgPanel, String msg){
        msgPanel.setContentType("text/html");
        String sender = msg.split(":")[0];
        String message = msg.substring(msg.indexOf(":")+1);
        if(sender.equals(username))
        {   
            msg = "<h3 align='right'><span style='background-color: #ffc0cb;'>"+ message +"</span></h3>"; //  xoa ten nguoi gui          style='background-color: #ffc0cb;'
        }
        else{
            Icon avatar = new ImageIcon(client_frame.class.getResource("/icon/user.png"));
            msg = "<p> " 
                    + " <img src='"+avatar+"'></img>"
                    + "<span><b>"+ sender +": </b>" + message +"</span>"
               + "</p>";
        }
        HTMLDocument doc =(HTMLDocument) msgPanel.getDocument();
        HTMLEditorKit editorKit = (HTMLEditorKit)msgPanel.getEditorKit();
        try {
          editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
          msgPanel.setCaretPosition(doc.getLength());

        } catch(Exception e){
          e.printStackTrace();
        }
    }
    public void setIcon(String sender, String icon){
        String getIconStr = icon.substring(1,icon.length()-1);
        Icon iconImg = new ImageIcon(client_frame.class.getResource("/icon/"+ getIconStr+ ".png"));
        String tagImg = sender+ ": " +"<img src='"+iconImg +"'></img>";
        appendMessage(ta_chat, tagImg);
    }
    public void appendImage(JTextPane msgPanel, String msg){
        msgPanel.setContentType("text/html");
        String sender = msg.split(":")[0];
        String icon = msg.split(":")[1].trim();
        Icon iconImg = new ImageIcon(client_frame.class.getResource("/image/"+ icon));
        msg = "<div style='width: 20%'><img  src='"+iconImg+"'></img></div>";
        if(sender.equals(username))
        {   
            msg = "<h3 align='right'>"+ msg +"</h3>"; //  xoa ten nguoi gui          style='background-color: #ffc0cb;'
        }
        else{
            Icon avatar = new ImageIcon(client_frame.class.getResource("/icon/user.png"));
            msg = "<p> " 
                    + " <img src='"+avatar+"'></img>"
                    + "<span><b>"+ sender +": </b>" + msg +"</span>"
               + "</p>";
        }
        
        HTMLDocument doc =(HTMLDocument) msgPanel.getDocument();
        HTMLEditorKit editorKit = (HTMLEditorKit)msgPanel.getEditorKit();
        try {
          editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
          msgPanel.setCaretPosition(doc.getLength());

        } catch(Exception e){
          e.printStackTrace();
        }
    }
    public void userAdd(String data) 
    {
         users.add(data);
    }
    
    //--------------------------//
    
    public void userRemove(String data) 
    {
//         ta_chat.append(data + " is now offline.\n");
        appendMessage(ta_chat, data);
    }
    
    //--------------------------//
    
    public void writeUsers() 
    {
         String[] tempList = new String[(users.size())];
         users.toArray(tempList);
         for (String token:tempList) 
         {
             //users.append(token + "\n");
         }
    }
    
    //--------------------------//
    
    public void sendDisconnect() 
    {
        String bye = (username + ": :Disconnect");
        try
        {
            writer.println(bye); 
            writer.flush(); 
        } catch (Exception e) 
        {
//            ta_chat.append("Could not send Disconnect message.\n");
            String error = "Could not send Disconnect message.\n";
            appendMessage(ta_chat, error);
        }
    }

    //--------------------------//
    
    public void Disconnect() 
    {
        try 
        {
//          ta_chat.append("Disconnected.\n");
            String disconnect = "Disconnected.\n";
            appendMessage(ta_chat, disconnect);
            sock.close();
        } catch(Exception ex) {
//            ta_chat.append("Failed to disconnect. \n");
            String disconnectFailed = "Failed to disconnect. \n";
            appendMessage(ta_chat, disconnectFailed);
        }
        isConnected = false;
        tf_username.setEditable(true);

    }
    
    public client_frame() 
    {
        initComponents();
        ta_chat.setMargin(new Insets(15,15,15,15));   
    }
    
    //--------------------------//
    
    public class IncomingReader implements Runnable
    {
        @Override
        public void run() 
        {
            String[] data;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat", image ="Image", file = "File";
            String serverDis = "ServerDisconnect";
            String fileReceive;
            try 
            {
                while ((stream = reader.readLine()) != null) 
                {
                     data = stream.split(":");
                     if (data[2].equals(chat)) 
                     {
                        switch(data[1]){
                            case "<smile>":
                                setIcon(data[0],data[1]);
                                break;
                            case "<smile_big>":
                                setIcon(data[0],data[1]);
                                break;
                            case "<heart_eye>":
                                setIcon(data[0],data[1]);
                                break;
                            case "<crying>":
                                setIcon(data[0],data[1]);
                                break;
                            case "<sad>":
                                setIcon(data[0],data[1]);
                                break;
                            case "<smile_cry>":
                                setIcon(data[0],data[1]);
                                break;
                            case "<scared>":
                                setIcon(data[0],data[1]);
                                break;
                            default: 
                                String MsgAndSender = data[0] + ": " + data[1] + "\n";
                                appendMessage(ta_chat, MsgAndSender);
                                break;
                        }
                     } 
                     else if(data[2].equalsIgnoreCase(image)){
                         synchronized(this){
                            String MsgAndSender = data[0] + ": " + data[1] + "\n";
                            appendImage(ta_chat, MsgAndSender);
                         }
                     }
                     else if(data[2].equalsIgnoreCase(file) ){
//                         && !data[0].equals(username)
                         System.out.println("File receive");
                         String ObjButtons[] = {"Yes","No"};
                         int result = JOptionPane.showOptionDialog(null,data[0]+ " send file to you, receive?", "Send file", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
                         byte dataByte[] = new byte[is.available()];
                         is.read(dataByte,0,dataByte.length);
                         if(result == JOptionPane.YES_OPTION){
                             FileOutputStream fr = new FileOutputStream("C:\\Users\\mrahn\\Desktop\\Nam 4\\Do an 3\\nhanfile\\"+data[1]);
                             fr.write(dataByte,0,dataByte.length);
                             fr.flush();
                             fr.close();
                             dataByte = null;
                             String MsgAndSender = data[0] + ": Send file " + data[1] + "\n";
                             appendMessage(ta_chat, MsgAndSender );
                         }
                          
//                         if(result == JOptionPane.YES_OPTION ){
//                             FileOutputStream fr = new FileOutputStream("C:\\Users\\mrahn\\Desktop\\Nam 4\\Do an 3\\nhanfile\\"+data[1]);
//                             fr.write(dataByte,0,dataByte.length);
//                             fr.flush();
////                             fr.close();
//                         }
                
                     }
                     else if (data[2].equals(connect))
                     {
                        ta_chat.removeAll();
                        userAdd(data[0]);
                     } 
                     else if (data[2].equals(disconnect)) 
                     {
                         userRemove(data[0]);
                     }
                     else if (data[2].equals(serverDis)){
                         System.out.println("serverdis");
                     }
                     else if (data[2].equals(done)) 
                     {
                        //users.setText("");
//                        writeUsers();
                        users.clear();
                     }
                }
           }catch(Exception ex) { }
        }
    }

    //--------------------------//
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb_address = new javax.swing.JLabel();
        tf_address = new javax.swing.JTextField();
        lb_port = new javax.swing.JLabel();
        tf_port = new javax.swing.JTextField();
        lb_username = new javax.swing.JLabel();
        tf_username = new javax.swing.JTextField();
        lb_password = new javax.swing.JLabel();
        tf_password = new javax.swing.JTextField();
        b_connect = new javax.swing.JButton();
        b_disconnect = new javax.swing.JButton();
        b_anonymous = new javax.swing.JButton();
        lb_name = new javax.swing.JLabel();
        jpIcon = new javax.swing.JPanel();
        icon_smile = new javax.swing.JLabel();
        smile_big = new javax.swing.JLabel();
        tf_chat = new javax.swing.JTextField();
        b_send = new javax.swing.JButton();
        crying = new javax.swing.JLabel();
        sad = new javax.swing.JLabel();
        smile_cry = new javax.swing.JLabel();
        scared = new javax.swing.JLabel();
        heart_eye = new javax.swing.JLabel();
        send_img = new javax.swing.JLabel();
        send_file = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Client's frame");
        setName("client"); // NOI18N
        setResizable(false);

        lb_address.setText("Address : ");

        tf_address.setText("localhost");
        tf_address.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_addressActionPerformed(evt);
            }
        });

        lb_port.setText("Port :");

        tf_port.setText("2222");
        tf_port.setEnabled(false);
        tf_port.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_portActionPerformed(evt);
            }
        });

        lb_username.setText("Username :");

        tf_username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_usernameActionPerformed(evt);
            }
        });

        lb_password.setText("Password : ");

        tf_password.setEnabled(false);

        b_connect.setText("Connect");
        b_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_connectActionPerformed(evt);
            }
        });

        b_disconnect.setText("Disconnect");
        b_disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_disconnectActionPerformed(evt);
            }
        });

        b_anonymous.setText("Anonymous Login");
        b_anonymous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_anonymousActionPerformed(evt);
            }
        });

        lb_name.setText("Hoang Tu Le");
        lb_name.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        icon_smile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/smile.png"))); // NOI18N
        icon_smile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                icon_smileMouseClicked(evt);
            }
        });

        smile_big.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/smile_big.png"))); // NOI18N
        smile_big.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                smile_bigMouseClicked(evt);
            }
        });

        tf_chat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tf_chatKeyPressed(evt);
            }
        });

        b_send.setText("SEND");
        b_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_sendActionPerformed(evt);
            }
        });

        crying.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/crying.png"))); // NOI18N
        crying.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cryingMouseClicked(evt);
            }
        });

        sad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/sad.png"))); // NOI18N
        sad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sadMouseClicked(evt);
            }
        });

        smile_cry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/smile_cry.png"))); // NOI18N
        smile_cry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                smile_cryMouseClicked(evt);
            }
        });

        scared.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/scared.png"))); // NOI18N
        scared.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scaredMouseClicked(evt);
            }
        });

        heart_eye.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/heart_eye.png"))); // NOI18N
        heart_eye.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                heart_eyeMouseClicked(evt);
            }
        });

        send_img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/blank_icon.png"))); // NOI18N
        send_img.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                send_imgMouseClicked(evt);
            }
        });

        send_file.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/attachment.png"))); // NOI18N
        send_file.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                send_fileMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpIconLayout = new javax.swing.GroupLayout(jpIcon);
        jpIcon.setLayout(jpIconLayout);
        jpIconLayout.setHorizontalGroup(
            jpIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpIconLayout.createSequentialGroup()
                .addGroup(jpIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpIconLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(icon_smile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(smile_big)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(heart_eye)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(crying)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(smile_cry)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scared))
                    .addComponent(tf_chat, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jpIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpIconLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addComponent(b_send, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                    .addGroup(jpIconLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(send_img)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(send_file))))
        );
        jpIconLayout.setVerticalGroup(
            jpIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpIconLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(scared, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(smile_big, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(icon_smile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(crying, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(heart_eye, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(smile_cry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(send_img, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(send_file))
                .addGap(21, 21, 21)
                .addGroup(jpIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_chat, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_send, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(ta_chat);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lb_username, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(lb_address, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tf_address, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(tf_username))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lb_password, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb_port, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tf_password)
                            .addComponent(tf_port, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(b_connect)
                                .addGap(2, 2, 2)
                                .addComponent(b_disconnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(b_anonymous, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(lb_name)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jpIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_address)
                    .addComponent(tf_address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_port)
                    .addComponent(tf_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_anonymous))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tf_username)
                    .addComponent(tf_password)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lb_username)
                        .addComponent(lb_password)
                        .addComponent(b_connect)
                        .addComponent(b_disconnect)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lb_name))
        );

        jpIcon.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tf_addressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_addressActionPerformed
       
    }//GEN-LAST:event_tf_addressActionPerformed

    private void tf_portActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_portActionPerformed
   
    }//GEN-LAST:event_tf_portActionPerformed

    private void tf_usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_usernameActionPerformed
    
    }//GEN-LAST:event_tf_usernameActionPerformed

    private void b_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_connectActionPerformed
        if (isConnected == false) 
        {
            username = tf_username.getText();
            tf_username.setEditable(false);

            try 
            {
                sock = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                is = sock.getInputStream();
                os = sock.getOutputStream();
                writer = new PrintWriter(sock.getOutputStream());
                writer.println(username + ":has connected.:Connect");
                writer.flush(); 
                isConnected = true; 
            } 
            catch (Exception ex) 
            {
//                ta_chat.append("Cannot Connect! Try Again. \n");
                String connectedFailed = "Cannot Connect! Try Again. \n";
                appendMessage(ta_chat, connectedFailed);
                tf_username.setEditable(true);
            }
            
            ListenThread();
            
        } else if (isConnected == true) 
        {
//            ta_chat.append("You are already connected. \n");
              String connected = "You are already connected. \n";
              appendMessage(ta_chat, connected);
        }
    }//GEN-LAST:event_b_connectActionPerformed

    private void b_disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_disconnectActionPerformed
        sendDisconnect();
        Disconnect();
    }//GEN-LAST:event_b_disconnectActionPerformed

    private void b_anonymousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_anonymousActionPerformed
        tf_username.setText("");
        if (isConnected == false) 
        {
            String anon="anon";
            Random generator = new Random(); 
            int i = generator.nextInt(999) + 1;
            String is=String.valueOf(i);
            anon=anon.concat(is);
            username=anon;
            
            tf_username.setText(anon);
            tf_username.setEditable(false);

            try 
            {
                sock = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                this.is = sock.getInputStream();
                os = sock.getOutputStream();
                writer = new PrintWriter(sock.getOutputStream());
                writer.println(anon + ":has connected.:Connect");
                writer.flush(); 
                isConnected = true; 
            } 
            catch (Exception ex) 
            {
//                ta_chat.append("Cannot Connect! Try Again. \n");
                String connectFailed = "Cannot Connect! Try Again. \n";
                appendMessage(ta_chat, connectFailed);
                tf_username.setEditable(true);
            }
            
            ListenThread();
            
        } else if (isConnected == true) 
        {
//            ta_chat.append("You are already connected. \n");
            String connected = "You are already connected. \n";
            appendMessage(ta_chat, connected);
        }        
    }//GEN-LAST:event_b_anonymousActionPerformed

    private void b_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_sendActionPerformed
        String nothing = "";
        if ((tf_chat.getText()).equals(nothing)) {
            tf_chat.setText("");
            tf_chat.requestFocus();
        } else {
            try {
               writer.println(username + ":" + tf_chat.getText() + ":" + "Chat");
               writer.flush(); // flushes the buffer
            } catch (Exception ex) {
//                ta_chat.append("Message was not sent. \n");
               String failed =  "Message was not sent. \n";
               appendMessage(ta_chat, failed);
            }
            tf_chat.setText("");
            tf_chat.requestFocus();
        }

        tf_chat.setText("");
        tf_chat.requestFocus();
    }//GEN-LAST:event_b_sendActionPerformed

    private void icon_smileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon_smileMouseClicked
        try {
           writer.println(username + ":" + "<smile>" + ":" + "Chat");
           writer.flush(); // flushes the buffer
        } catch (Exception ex) {
           String failed =  "Message was not sent. \n";
           appendMessage(ta_chat, failed);
        }
    }//GEN-LAST:event_icon_smileMouseClicked

    private void smile_bigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_smile_bigMouseClicked
        try {
           writer.println(username + ":" + "<smile_big>" + ":" + "Chat");
           writer.flush(); // flushes the buffer
        } catch (Exception ex) {
           String failed =  "Message was not sent. \n";
           appendMessage(ta_chat, failed);
        }
    }//GEN-LAST:event_smile_bigMouseClicked

    private void heart_eyeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_heart_eyeMouseClicked
        try {
           writer.println(username + ":" + "<heart_eye>" + ":" + "Chat");
           writer.flush(); // flushes the buffer
        } catch (Exception ex) {
           String failed =  "Message was not sent. \n";
           appendMessage(ta_chat, failed);
        }
    }//GEN-LAST:event_heart_eyeMouseClicked

    private void cryingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cryingMouseClicked
        try {
           writer.println(username + ":" + "<crying>" + ":" + "Chat");
           writer.flush(); // flushes the buffer
        } catch (Exception ex) {
           String failed =  "Message was not sent. \n";
           appendMessage(ta_chat, failed);
        }
    }//GEN-LAST:event_cryingMouseClicked

    private void sadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sadMouseClicked
        try {
           writer.println(username + ":" + "<sad>" + ":" + "Chat");
           writer.flush(); // flushes the buffer
        } catch (Exception ex) {
           String failed =  "Message was not sent. \n";
           appendMessage(ta_chat, failed);
        }
    }//GEN-LAST:event_sadMouseClicked

    private void smile_cryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_smile_cryMouseClicked
        try {
           writer.println(username + ":" + "<smile_cry>" + ":" + "Chat");
           writer.flush(); // flushes the buffer
        } catch (Exception ex) {
           String failed =  "Message was not sent. \n";
           appendMessage(ta_chat, failed);
        }
    }//GEN-LAST:event_smile_cryMouseClicked

    private void scaredMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scaredMouseClicked
        try {
           writer.println(username + ":" + "<scared>" + ":" + "Chat");
           writer.flush(); // flushes the buffer
        } catch (Exception ex) {
           String failed =  "Message was not sent. \n";
           appendMessage(ta_chat, failed);
        }
    }//GEN-LAST:event_scaredMouseClicked

    private void tf_chatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_chatKeyPressed
        if(evt.getKeyCode() == 10){
            b_send.doClick();
        }
    }//GEN-LAST:event_tf_chatKeyPressed

    private void send_imgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_send_imgMouseClicked
        JFileChooser chooser= new JFileChooser();
        int choice = chooser.showOpenDialog(b_send);
        if (choice != JFileChooser.APPROVE_OPTION) return;
        File chosenFile = chooser.getSelectedFile();
        try {
            Image img = ImageIO.read(chosenFile);
            if(img == null){
                JOptionPane.showMessageDialog(null, "Khong phai hinh anh");
            }
            else{                
                String filepath = chosenFile.getAbsolutePath();
                String[] filePathSpilit = filepath.split(":");
                String nameFile = chosenFile.getName();
                 try {
                    String send = username + ":" + nameFile + ":" + "Image";
                    writer.println(send);
                    writer.flush(); // flushes the buffer
                    FileInputStream fis = new FileInputStream(chosenFile);
                    byte data[]= new byte[1000000];
                    fis.read(data,0,data.length);
                    os = sock.getOutputStream();
                    os.write(data,0,data.length);
                    os.flush();
                 } catch (Exception ex) {
                    String failed =  "Message was not sent. \n";
                    appendMessage(ta_chat, failed);
                 }
            }
        } catch (IOException ex) {
            Logger.getLogger(client_frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_send_imgMouseClicked

    private void send_fileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_send_fileMouseClicked
        JFileChooser chooser= new JFileChooser();
        int choice = chooser.showOpenDialog(b_send);
        if (choice != JFileChooser.APPROVE_OPTION) return;
        File chosenFile = chooser.getSelectedFile();
        try {
           String send = username + ":" + chosenFile.getName() + ":" + "File";
           writer.println(send);
           writer.flush(); // flushes the buffer
           FileInputStream fis = new FileInputStream(chosenFile);
           byte data[]= new byte[1000000];
           fis.read(data,0,data.length);
           os = sock.getOutputStream();
           os.write(data,0,data.length);
           os.flush();
           data = null;
        } catch (Exception ex) {
           String failed =  "Message was not sent. \n";
           appendMessage(ta_chat, failed);
        }
    }//GEN-LAST:event_send_fileMouseClicked

    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new client_frame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_anonymous;
    private javax.swing.JButton b_connect;
    private javax.swing.JButton b_disconnect;
    private javax.swing.JButton b_send;
    private javax.swing.JLabel crying;
    private javax.swing.JLabel heart_eye;
    private javax.swing.JLabel icon_smile;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpIcon;
    private javax.swing.JLabel lb_address;
    private javax.swing.JLabel lb_name;
    private javax.swing.JLabel lb_password;
    private javax.swing.JLabel lb_port;
    private javax.swing.JLabel lb_username;
    private javax.swing.JLabel sad;
    private javax.swing.JLabel scared;
    private javax.swing.JLabel send_file;
    private javax.swing.JLabel send_img;
    private javax.swing.JLabel smile_big;
    private javax.swing.JLabel smile_cry;
    private javax.swing.JTextPane ta_chat;
    private javax.swing.JTextField tf_address;
    private javax.swing.JTextField tf_chat;
    private javax.swing.JTextField tf_password;
    private javax.swing.JTextField tf_port;
    private javax.swing.JTextField tf_username;
    // End of variables declaration//GEN-END:variables
}
