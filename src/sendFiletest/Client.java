/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendFiletest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 *
 * @author mrahn
 */
public class Client {
    public static void main(String[] args) throws IOException {
          Socket s = new Socket("localhost",5000);
          InputStream is = s.getInputStream();
          byte data[] = new byte[100000];
//          Byte bt = 
          FileOutputStream fr = new FileOutputStream("H:\\meme.jpg");
          is.read(data,0, data.length);
          fr.write(data,0,data.length);
    
    }
}
