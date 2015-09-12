/* 
 * Copyright (C) 2015 Jaime Hidalgo García
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaime.ultransfer.main;

import com.jaime.ultransfer.exception.ConnectionException;
import com.jaime.ultransfer.network.NetOperations;
import com.jaime.ultransfer.network.TCPsocket;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaime Hidalgo García
 */
public class ClientMode {
    
    private TCPsocket   socket ;//= new TCPsocket(param.getHost(),param.getPort());
    
    public ClientMode(){
        
        try {
            if (!ParamParser.isInverse()) {
                socket = new TCPsocket( ParamParser.getHost() , ParamParser.getPort() );            
            } else {
                ServerSocket serverSocket = new ServerSocket( ParamParser.getPort() );
                socket = new TCPsocket( serverSocket.accept() );
            }                
                socket.setTCPwindowSize(50000, 50000);                
        } catch (IOException ex) {
            Logger.getLogger(ClientMode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void run(){
        
        /*
        AUTHENTICATION PROCESS
        */
        if ( socket.getByte() == NetOperations.PASSWORD_REQUIRED ){
            socket.sendString(ParamParser.getPassword());
        }else{
            System.exit(1);
        }
        
        boolean connected;
        
        if( socket.getByte() == NetOperations.PASSWORD_ACK ) connected = true;
        else {
                                                             connected = false;
                                                             System.out.println("Authentication failed. Check password.");
        }
        
        /*
        TRANSFERING FILES
        */
        while(connected){
            int fileNumber = ParamParser.getFiles().size();
            System.out.println("[?] "+"Authentication successful! Preparing to send "+fileNumber+" files.");
            socket.sendInt(fileNumber);
            File file;
            byte op;
            for(int i = 1 ; i <= fileNumber ; i++){
                file = new File( ParamParser.getFiles().get(i-1) );
                if( file.exists() && file.isFile() ){
                    try {
                        socket.sendByte(NetOperations.FILE_SOON);
                        socket.sendString( file.getName() );
                        socket.sendFile( file );
                        System.out.println("[o] "+i+"/"+fileNumber+" - File \""+file.getName()+"\" Sent.");
                    } catch (ConnectionException ex) {
                        System.out.println("[x] "+i+"/"+fileNumber+" - File \""+file.getName()+"\" ERROR.");
                        System.out.println(ex.getMessage());
                    }
                }else{
                    socket.sendByte(NetOperations.FILE_NOT_FOUND);
                    System.out.println("[x] "+i+"/"+fileNumber+" - File \""+file.getName()+"\" NOT FOUND.");
                }
            }
            connected = false;
        }
        
        /*
        CONNECTION MUST END PROPERLY
        */
        if (socket.getByte() == NetOperations.READY_TO_CLOSE){
            try {
                System.out.println("[?] Program run successfully");
                Thread.sleep(1000);
                socket.closeConnection();
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientMode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("[?] Error closing connection.");
            socket.closeConnection();
        }
        System.exit(0);
        
    }
    
    


    
}
