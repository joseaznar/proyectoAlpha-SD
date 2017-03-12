/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Comunicacion;

import RMI.Compute;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JGUTIERRGARC
 */
public class MulticastSenderPeer implements Compute {
        private static int[] suma = new int[1000];
        private static ArrayList ips = new ArrayList();
        private static ArrayList names = new ArrayList();
        
        public MulticastSenderPeer() throws RemoteException {
            super();
        } 
        @Override
        public String[] register ( String name, String ip ){
            ips.add(ip);
            names.add(name);
            String[] resp = new String[3];
            resp[0] = "228.5.6.7"; 
            resp[1] = "6789";
            return resp; 
        }
        public static void runRMIServer(){
            try {
                // TODO code application logic here
                System.setProperty("java.security.policy","file:\\C:\\Users\\HP\\Documents\\NetBeansProjects\\JavaIPMulticast\\src\\RMI\\server.policy");

                if (System.getSecurityManager() == null) {
                    System.setSecurityManager(new SecurityManager());
                }
                LocateRegistry.createRegistry(1099);

                String name = "Compute";
                MulticastSenderPeer engine = new MulticastSenderPeer();
                Compute stub = (Compute) UnicastRemoteObject.exportObject(engine, 0);
                Registry registry = LocateRegistry.getRegistry();
                registry.rebind(name, stub); 

            } catch (RemoteException ex) {
                Logger.getLogger(MulticastSenderPeer.class.getName()).log(Level.SEVERE, null, ex);
            }      
        }
    	public static void main(String args[]){ 
  	int max = 0;
        int maximo = 0;
        boolean tempi = false;
        //String[] ips = new String[1000];
	MulticastSocket s =null;
        ServerSocket listenSocket = null;
        int serverPort = 7896; 
        
        //ips[0] = "192.168.0.8";        
        
   	 try {
                runRMIServer();
                long time = System.currentTimeMillis();
                while(System.currentTimeMillis() - time<=10000)
                    System.out.println("dif: " + (time - System.currentTimeMillis()));
                boolean t = true;
                String[] mens = new String[15];
                for(int i=0;i<=9;i++){
                    mens[i] = (i+"");
                }
                mens[9] = "A"; mens[10] = "B"; mens[11] = "C";
                
                InetAddress group = InetAddress.getByName("228.5.6.7"); // destination multicast group 
	    	s = new MulticastSocket(6789);
	   	s.joinGroup(group);
                int altcont = 0;
                boolean f = true;
                time = System.currentTimeMillis();
                while(t)
                {
                    System.out.println("timeee: " + (System.currentTimeMillis()-time));
                    if(System.currentTimeMillis()-time>=10000 || altcont==0){
                        if(f){
                            altcont++;
                            f=!f;
                        }
                        time = System.currentTimeMillis();
                        int temp = (int)Math.round(Math.random()*11);
                        String myMessage = mens[temp];
                        System.out.println("mensaje: " + temp);
                        byte [] m = myMessage.getBytes();
                        DatagramPacket messageOut = 
                                new DatagramPacket(m, m.length, group, 6789);
                        s.send(messageOut); 
                        tempi = true;
                        try{                            
                            // Creo un socket del servidor(indicandole el puerto)
                            if(listenSocket == null)
                                listenSocket = new ServerSocket(serverPort);                        
                            System.out.println("Waiting for messages...");
                            // -->BLOQUEA HASTA QUE ALGUIEN NO ESTABLEZCA COMUNICACIÃ“N 
                            //Listens for a connection to be made to this socket and accepts it. 
                            //The method blocks until a connection is made. 
                            Socket clientSocket = listenSocket.accept();  
                            Connection c = new Connection(clientSocket); //Es un hilo
                            String data = c.correr();
                            System.out.println("ip: " + clientSocket.getRemoteSocketAddress().toString().substring(1, 12));
                            int index = ips.indexOf(clientSocket.getRemoteSocketAddress().toString().substring(1, 12));
                            if(data!=null && index>=0 && tempi){
                                tempi = false;
                                suma[index]++;
                                if(suma[index]>max){
                                    max = suma[index];
                                    maximo = index;
                                }
                                System.out.println("cont(i): " + suma[index]);                                   
                            }
                        } catch(IOException e) {System.out.println("Listen :"+ e.getMessage());}                        
                    }
                    if(max == 3)
                    {                        
                        String myMessage = "El ganador es: " + names.get(maximo);
                        System.out.println(myMessage);
                        byte [] m = myMessage.getBytes();
                        DatagramPacket messageOut = 
                                new DatagramPacket(m, m.length, group, 6789);
                        s.send(messageOut); 
                        t = false; 
                    }
                }
	    	s.leaveGroup(group);		
 	    }
         catch (SocketException e){
             System.out.println("Socket: " + e.getMessage());
	 }
         catch (IOException e){
             System.out.println("IO: " + e.getMessage());
         }
	 finally {
            if(s != null) s.close();
        }
    }		     
}
