/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendFiletest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author mrahn
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5000);
        Socket s = ss.accept();
        File f = new File ("C:\\Users\\mrahn\\Desktop\\blank-icon-13.png");
        System.out.println(f.length());
        FileInputStream fr = new FileInputStream(f);
        byte data[] = new byte[20002];
        fr.read(data, 0, data.length);
        OutputStream os = s.getOutputStream();
        os.write(data,0,data.length);
    }
}
