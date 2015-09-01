/*
 * JAIME HIDALGO.
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaime.ultransfer.main;

import com.jaime.ultransfer.network.NetOperations;
import com.jaime.ultransfer.network.TCPsocket;
import java.io.File;
import java.net.Socket;

/**
 *
 * @author Jaime Hidalgo García
 */
public class ServerInstance implements Runnable{
    
    private final TCPsocket socket;
    private ParamParser param;
    //public static TextFile log = new TextFile("iplog.txt");
    
    public ServerInstance(Socket accept, ParamParser param) {
        socket = new TCPsocket(accept,param.getBlockSize());
        this.param = param;
    }

//    @Override
    public void run() {
        
        boolean connected = true;
        
        socket.sendByte(NetOperations.PASSWORD_REQUIRED );
        if ( socket.getString().equals(param.getPassword()) )
            socket.sendByte(NetOperations.PASSWORD_ACK);
        else{
            socket.sendByte(NetOperations.PASSWORD_NACK);
            System.out.println("Client tried to connect but failed in authentication");
            connected = false;
        }
        
        if(connected){
            int fileNumber = socket.getInt();
            System.out.println("New client succesfully authenticated. "+fileNumber+" files are now in queue to be received.");
            String fileName;
            File file;
            for(int i = 1 ; i <= fileNumber ; i++){
                file = new File( param.getDirectory()+socket.getString() );
                //fileName = socket.getString();
                socket.getFile( file );
                System.out.println(i+"/"+fileNumber+" - File "+file.getName()+" Received");
            }
            
            connected = false;
            
        }
        System.out.println("Closing connection");
        socket.closeConnection();
        
        
    }
    
}
