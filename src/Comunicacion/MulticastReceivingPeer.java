/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Comunicacion;
  
import GUIs.Rejilla;
import GUIs.ganadorDisp;
import RMI.Compute;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author JGUTIERRGARC
 */
public class MulticastReceivingPeer {
    	public static void main(String args[]) throws NotBoundException{ 
  	 
	MulticastSocket s =null;
        String ipLocal = "192.168.0.8";
        String ipServidor = "192.168.0.8";
        
        System.setProperty("java.security.policy","file:\\C:\\Users\\HP\\Documents\\NetBeansProjects\\JavaIPMulticast\\src\\RMI\\client.policy"); 
        
        if (System.getSecurityManager() == null) {
          System.setSecurityManager(new SecurityManager());
        } 
        String name = "Compute";
        Registry registry;
        String[] ipMulti = null;
        try {
            registry = LocateRegistry.getRegistry(ipServidor); // server's ip address
            Compute comp= (Compute) registry.lookup(name);
            ipMulti = comp.register("jose aznar", ipLocal);
        } catch (RemoteException ex) {
            Logger.getLogger(MulticastReceivingPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
    
   	 try {
                InetAddress group = InetAddress.getByName(ipMulti[0]); // destination multicast group 
	    	s = new MulticastSocket(Integer.parseInt(ipMulti[1]));
	   	s.joinGroup(group); 
                
                JFrame r;
                
                String aux;
                
                boolean t = true;
	    	byte[] buffer = new byte[1000];

                while(t) {                    
                    System.out.println("Waiting for messages");
                    DatagramPacket messageIn = 
			new DatagramPacket(buffer, buffer.length);
 		    s.receive(messageIn);
                    aux = new String(messageIn.getData());
                    
                    if(aux.charAt(0) == 'E'){
                        t =false;
                        JFrame g = new ganadorDisp(aux);
                        g.setVisible(true);
                    }
                    else{
                        System.out.println("parammm: " + aux.toCharArray()[0] + " aux: " + aux);
                        r = new Rejilla(aux, ipLocal);
                        r.setVisible(true);                        

                        System.out.println("Message: " + aux+ " from: "+ messageIn.getAddress());
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
                 // get messages from others in group
}
