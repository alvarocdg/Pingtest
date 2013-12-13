package de.zh32.pingtest;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    
    public static void main( String[] args ) {
        String[] split = args[0].split(":");
        check(new InetSocketAddress(split[0], Integer.valueOf(split[1])));        
    }
    
    public static void check(InetSocketAddress inetSocketAddress) {
        String responseString = "Response: %s %d/%d %s\n";
       
        System.out.println("Checking " + inetSocketAddress.toString() + "\n");
        
        System.out.println("SLP B1.8+");
        try {                
            ServerListPingB18 pingb18 = new ServerListPingB18();           
            pingb18.setAddress(inetSocketAddress);
            pingb18.fetchData();
            System.out.println(String.format(responseString, 
                    pingb18.getMotd(), 
                    pingb18.getPlayersOnline(), 
                    pingb18.getMaxPlayers(), 
                    "[>= Beta 1.8]"));

        } catch (Exception ex) {          
            ex.printStackTrace();
        }
        
        System.out.println("SLP 1.4+");
        try {                
            ServerListPing14 ping14 = new ServerListPing14();           
            ping14.setAddress(inetSocketAddress);
            ping14.fetchData();
            System.out.println(String.format(responseString, 
                    ping14.getMotd(), 
                    ping14.getPlayersOnline(), 
                    ping14.getMaxPlayers(), 
                    ping14.getGameVersion()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        System.out.println("SLP 1.6+");
        try {                
            ServerListPing16 ping16 = new ServerListPing16();           
            ping16.setAddress(inetSocketAddress);
            ping16.fetchData();
            System.out.println(String.format(responseString, 
                    ping16.getMotd(), 
                    ping16.getPlayersOnline(), 
                    ping16.getMaxPlayers(), 
                    ping16.getGameVersion()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
            
        System.out.println("SLP 1.7+");
        try {           
            ServerListPing17 ping17 = new ServerListPing17();
            ping17.setAddress(inetSocketAddress);
            ServerListPing17.StatusResponse response = ping17.fetchData();
            System.out.println(String.format(responseString, 
                    response.getDescription(), 
                    response.getPlayers().getOnline(), 
                    response.getPlayers().getMax(), 
                    response.getVersion().getName()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
