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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaime Hidalgo García
 */
public class ServerInstance implements Runnable{
    
    private final TCPsocket socket;
    private ParamParser     param;
    private static int      seq;

    /*
    STARTING SOCKETS AND CONFIGURING OPTIONS
    */
    public ServerInstance(Socket accept, ParamParser param) {
        socket = new TCPsocket(accept,param.getBlockSize());
        socket.setTCPwindowSize(50000, 50000);
        this.param = param;
        seq++;
    }

//    @Override
    public void run() {
        
        boolean connected = true;
        String seqs = "["+seq+"]";
        /*
        AUTHENTICATION PROCESS
        */
        socket.sendByte(NetOperations.PASSWORD_REQUIRED );
        if ( socket.getString().equals(param.getPassword()) )
            socket.sendByte(NetOperations.PASSWORD_ACK);
        else{
            socket.sendByte(NetOperations.PASSWORD_NACK);
            System.out.println("[?] "+seqs+" Client "+socket.getIp() +" tried to connect but failed in authentication");
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
            System.out.println("[?] "+seqs+" New client "+socket.getIp() +" succesfully authenticated. "
                               +fileNumber+" files are now in queue to be received.");

            for(int i = 1 ; i <= fileNumber ; i++){
                if( (op = socket.getByte()) == NetOperations.FILE_SOON){
                    file = new File( param.getDirectory()+"/"+socket.getString() );
                    try {
                        socket.getFile( file );
                        System.out.println("[o] "+seqs+" "+i+"/"+fileNumber+" - File \""+file.getName()+"\" Received");
                    } catch (ConnectionException ex) {
                        System.out.println(ex.getMessage());
                        System.out.println("[x] "+seqs+" Error receiving file "+i+"/"+fileNumber+" -IOerror-");
                    }
                }else if (op == NetOperations.FILE_NOT_FOUND){
                    System.out.println("[x] "+seqs+" Error receiving file "+i+"/"+fileNumber+" -not found-");
                }
            }
            
            connected = false;
            
        }
        
        /*
        CONNECTION MUST END PROPERLY
        */
        System.out.println("[?] "+seqs+" Closing connection");
        socket.sendByte(NetOperations.READY_TO_CLOSE);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        socket.closeConnection();
        
        
    }
    
}
