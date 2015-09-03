/*
 * JAIME HIDALGO.
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaime.ultransfer.main;

import com.jaime.ultransfer.exception.ConnectionException;
import com.jaime.ultransfer.network.NetOperations;
import com.jaime.ultransfer.network.TCPsocket;
import java.io.File;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaime Hidalgo García
 */
public class ServerInstance implements Runnable{
    
    private final TCPsocket socket;
    private ParamParser param;

    /*
    STARTING SOCKETS AND CONFIGURING OPTIONS
    */
    public ServerInstance(Socket accept, ParamParser param) {
        socket = new TCPsocket(accept,param.getBlockSize());
        socket.setTCPwindowSize(50000, 50000);
        this.param = param;
    }

//    @Override
    public void run() {
        
        boolean connected = true;
        
        /*
        AUTHENTICATION PROCESS
        */
        socket.sendByte(NetOperations.PASSWORD_REQUIRED );
        if ( socket.getString().equals(param.getPassword()) )
            socket.sendByte(NetOperations.PASSWORD_ACK);
        else{
            socket.sendByte(NetOperations.PASSWORD_NACK);
            System.out.println("[?] "+"Client "+socket.getIp() +" tried to connect but failed in authentication");
            connected = false;
        }
        
        /*
        FILES TRANSFERING
        */
        if(connected){
            String fileName;
            File file;
            byte op;
            
            int fileNumber = socket.getInt();
            System.out.println("[?] "+"New client "+socket.getIp() +" succesfully authenticated. "
                               +fileNumber+" files are now in queue to be received.");

            for(int i = 1 ; i <= fileNumber ; i++){
                if( (op = socket.getByte()) == NetOperations.FILE_SOON){
                    file = new File( param.getDirectory()+"/"+socket.getString() );
                    try {
                        socket.getFile( file );
                        System.out.println("[o] "+i+"/"+fileNumber+" - File \""+file.getName()+"\" Received");
                    } catch (ConnectionException ex) {
                        System.out.println(ex.getMessage());
                        System.out.println("[x] Error receiving file "+i+"/"+fileNumber+" -IOerror-");
                    }
                }else if (op == NetOperations.FILE_NOT_FOUND){
                    System.out.println("[x] Error receiving file "+i+"/"+fileNumber+" -not found-");
                }
            }
            
            connected = false;
            
        }
        
        /*
        CONNECTION MUST END PROPERLY
        */
        System.out.println("[?] "+"Closing connection");
        socket.sendByte(NetOperations.READY_TO_CLOSE);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        socket.closeConnection();
        
        
    }
    
}
