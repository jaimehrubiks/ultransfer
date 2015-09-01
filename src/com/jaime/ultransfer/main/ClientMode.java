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

/**
 *
 * @author Jaime Hidalgo García
 */
public class ClientMode {
    
    private ParamParser param;
    private TCPsocket   socket ;//= new TCPsocket(param.getHost(),param.getPort());
    
    public ClientMode(ParamParser param){
        this.param = param;
        socket = new TCPsocket(param.getHost(),param.getPort(),param.getBlockSize());

    }
    
    public void run(){
                //

        if ( socket.getByte() == NetOperations.PASSWORD_REQUIRED ){
            socket.sendString(param.getPassword());
        }else{
            System.exit(1);
        }
        
        boolean connected;
        
        if( socket.getByte() == NetOperations.PASSWORD_ACK ) connected = true;
        else {
                                                             connected = false;
                                                             System.out.println("Authentication failed. Check password.");
        }
        
        while(connected){
            int fileNumber = param.getFiles().size();
            System.out.println("[?] "+"Authentication successful! Preparing to send "+fileNumber+" files.");
            socket.sendInt(fileNumber);
            File file;
            for(int i = 1 ; i <= fileNumber ; i++){
                file = new File( param.getFiles().get(i-1) );
                if( file.exists() && file.isFile() ){
                    socket.sendByte(NetOperations.FILE_SOON);
                    socket.sendString( file.getName() );
                    socket.sendFile( file );
                    System.out.println("[o] "+i+"/"+fileNumber+" - File \""+file.getName()+"\" Sent.");
                }else{
                    socket.sendByte(NetOperations.FILE_NOT_FOUND);
                    System.out.println("[x] "+i+"/"+fileNumber+" - File \""+file.getName()+"\" NOT FOUND.");
                }
            }
            connected = false;
        }
        
        socket.closeConnection();
        System.exit(0);
        
    }
    
    


    
}
