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

import com.jaime.ultransfer.network.TCPsocket;
import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Jaime Hidalgo García
 */
public class ServerMode {
    
    //private ParamParser param;

    public ServerMode() {
    }

    public void run() {

        boolean listening = true;
        
        
        if (!ParamParser.isInverse()) {

            try (ServerSocket serverSocket = new ServerSocket(ParamParser.getPort())) {

                while (listening) {
                    //System.out.println("New connection");
                    Thread t = new Thread(new ServerInstance(serverSocket.accept()));
                    t.start();
                }

            } catch (IOException e) {
                System.err.println("Could not listen on port " + ParamParser.getPort()
                        + " . Check port availability and internet connection");
                //ErrorLogger.toFile("IOError", e.toString());
                System.exit(-1);
            }

        } else {
            TCPsocket socket = new TCPsocket( ParamParser.getHost() , ParamParser.getPort() );
            new ServerInstance(socket).run();
        }

    }
    
   
}
