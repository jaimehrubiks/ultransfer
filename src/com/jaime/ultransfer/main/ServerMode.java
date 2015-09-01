/*
 * JAIME HIDALGO.
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaime.ultransfer.main;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Jaime Hidalgo García
 */
public class ServerMode {
    
    private ParamParser param;

    public ServerMode(ParamParser param) {
        this.param = param;
    }

    public void run() {

        boolean listening = true;
        
        try (ServerSocket serverSocket = new ServerSocket( param.getPort() )) {
            
            while (listening) {
                //System.out.println("New connection");
                Thread t = new Thread(new ServerInstance(serverSocket.accept(),param));
                t.start();
            }
            
        } catch (IOException e) {
            System.err.println("Could not listen on port " + param.getPort() + 
                              " . Check port availability and internet connection");
            //ErrorLogger.toFile("IOError", e.toString());
            System.exit(-1);
        }

    }
    
   
}
