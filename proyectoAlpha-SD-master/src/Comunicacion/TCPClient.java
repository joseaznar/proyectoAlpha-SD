/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Comunicacion;

import java.net.*;
import java.io.*;


public class TCPClient {
    
    public void ejecutaCliente(String dato, String ip) {
        
        Socket s = null;
        
   
        try {
            
            int serverPort = 7896;
            
                s = new Socket(ip, serverPort);  

                DataOutputStream out = new DataOutputStream( s.getOutputStream());
                out.writeUTF(dato);          
        } 
        catch (UnknownHostException e) {
            System.out.println("Sock:"+e.getMessage()); 
        }
        catch (EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } 
        catch (IOException e) {
            System.out.println("IO:"+e.getMessage());
        } finally {
            if(s!=null) 
                try {
                    s.close();
                } catch (IOException e){
                System.out.println("close:"+e.getMessage());}
        }
        

    }
    
}

