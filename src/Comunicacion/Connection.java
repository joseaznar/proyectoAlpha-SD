/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Comunicacion;

/**
 *
 * @author HP
 */
import java.net.*;
import java.io.*;


public class Connection extends Thread{
    DataInputStream in; //Entrada y salida para ese cliente
    DataOutputStream out;
    Socket clientSocket; //Para establecer la comunicaciÃ³n del otro lado
    String data;
    public Connection (Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());    
         } catch(IOException e)  {System.out.println("Connection:"+e.getMessage());}
    }

    public String correr(){
        try {			                 // an echo server
            data = in.readUTF(); //Leo algo que recibi de cliente   

            //Lo recibÃ­ de...
            System.out.println("Message " + data + "received from: " + clientSocket.getRemoteSocketAddress()); 

        } 
        catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } 
        catch(IOException e) {
            System.out.println("IO:"+e.getMessage());
        } finally {
            try {
                //Una vez que se terminan de comunicar, se debe de cerrar el puerto
                clientSocket.close();  
                
            } catch (IOException e){
                System.out.println(e);
            }
            }
        return data;
        }
    public static void main (String args[]) {
	
   
}


    
	
}



