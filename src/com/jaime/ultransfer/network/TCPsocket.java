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
package com.jaime.ultransfer.network;

import com.jaime.ultransfer.exception.ConnectionException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * JAIME HIDALGO.
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jaime Hidalgo García
 */
public class TCPsocket {

    private Socket              tcpSocket;
    private ServerSocket        lsSocket;

    private DataOutputStream    tx;
    private DataInputStream     rx;
    
    private int                 buffSize = 4096;

//    public TCPsocket(String host, int port, int buffSize) {
//        this(host,port);
//        this.buffSize = buffSize;
//    }
    
    public TCPsocket(String host, int port) {

        try {
            tcpSocket = new Socket(host, port);
            //tx = new DataOutputStream(tcpSocket.getOutputStream()); // Unbuffered Alternative - Direct link
            tx = new DataOutputStream(new BufferedOutputStream(tcpSocket.getOutputStream()));
            //rx = new DataInputStream(tcpSocket.getInputStream());
            rx = new DataInputStream(new BufferedInputStream(tcpSocket.getInputStream()));
        } catch (Exception e) {
            System.out.println("[!] SocketError. Can't stablish connection. " + e.toString());
        }

    }
    
//    public TCPsocket(Socket comSocket) {
//        this(comSocket);
//        //this.buffSize = buffSize;
//    }
    

    public TCPsocket(Socket comSocket) {
        
        try {
            this.tcpSocket = comSocket;
            //tx = new DataOutputStream(tcpSocket.getOutputStream()); // // Unbuffered Alternative - Direct link
            tx = new DataOutputStream(new BufferedOutputStream(tcpSocket.getOutputStream()));
            //rx = new DataInputStream(tcpSocket.getInputStream());
            rx = new DataInputStream(new BufferedInputStream(tcpSocket.getInputStream()));
        } catch (UnknownHostException uhe) {
            System.out.println("[!]  SocketError. Can't stablish connection. " + uhe.toString());
        } catch (IOException ex) {
            System.out.println("[!]  SocketError. Can't stablish connection. " + ex.toString());
        }
        
    }
    
    public void setTCPwindowSize(int send, int get){
        try {
            tcpSocket.setReceiveBufferSize(get);
            tcpSocket.setSendBufferSize(send);
        } catch (SocketException ex) {
            Logger.getLogger(TCPsocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendFile(File path) throws ConnectionException {
        
        long total  =     path.length();
        int count;
        byte[] buffer = new byte[buffSize];
        //System.out.println("Length: "+total);
        try (FileInputStream fis = new FileInputStream(path)) {

            tx.writeLong(total);
            while ( (count = fis.read(buffer)) > 0 ){
                tx.write(buffer, 0, count);
                //System.out.println(total);
            }       
            tx.flush();
            if ( getByte() != NetOperations.FILE_ACK) throw new ConnectionException("[!] Error sending file. Undet");
            
        } catch (Exception e) {
            //System.out.println("[!] Error sending file. " + e.toString());
            throw new ConnectionException("[!] Error sending file. " + e.toString());
        }
    }

    public void getFile(File path) throws ConnectionException {
        
        long total;
        int count;
        byte[] buffer = new byte[buffSize];
        
        try (FileOutputStream fos = new FileOutputStream(path)) {

            total = rx.readLong();
            //System.out.println("Length: "+total);

            while ( total > 0 ){
                count = rx.read(buffer);
                fos.write(buffer, 0, count);
                total -= count;
                //System.out.println(total);
            }       
            tx.flush();
            sendByte(NetOperations.FILE_ACK);

        } catch (Exception e) {
            //System.out.println("[!] Error receiving file. " + e.toString());
            throw new ConnectionException("[!] Error receiving file. " + e.toString());
        }
    
    }
    
 
    

    public void sendByte(byte nt) {

        try {
            tx.writeByte(nt);
            tx.flush();
        } catch (Exception e) {
            System.out.println("[!] Error sending byte." + e.toString());
        }

    }

    public byte getByte() {

        byte nt;

        try {
            nt = rx.readByte();
        } catch (IOException ex) {
            Logger.getLogger(TCPsocket.class.getName()).log(Level.SEVERE, null, ex);
            nt = 0;
        }

        return nt;
    }
    
    
    public void sendInt(int nt) {

        try {
            tx.writeInt(nt);
            tx.flush();
        } catch (Exception e) {
            System.out.println("[!] Error sending integer. " + e.toString());
        }

    }

    public int getInt() {

        int nt;

        try {
            nt = rx.readInt();
        } catch (Exception e) {
            System.out.println("[!] Error receiving integer. " + e.toString());
            nt = 0;
        }

        return nt;
    }
    
    public void sendLong(long nt) {

        try {
            tx.writeLong(nt);
            tx.flush();
        } catch (Exception e) {
            System.out.println("[!] Error sending long. " + e.toString());
        }

    }

    public long getLong() {

        long nt;

        try {
            nt = rx.readLong();
        } catch (Exception e) {
            System.out.println("[!] Error receiving long. " + e.toString());
            nt = 0;
        }

        return nt;
    }
    
    public void sendString(String str) { 

        try {
            byte[] buffer = str.getBytes("UTF-8");
            tx.writeInt(buffer.length);
            tx.write(buffer);
            
            //tx.writeUTF(str);
            tx.flush();
        } catch (Exception e) {
            System.out.println("[!] Error sending String (coding error?)." + e.toString());
        }

    }

    public String getString() {

        String str;

        try {
            int length = rx.readInt();
            byte[] buffer = new byte[length];
            rx.read(buffer, 0, length);
            str = new String(buffer,"UTF-8");
            
            //str = rx.readUTF();
        } catch (Exception e) {
            System.out.println("[!] Error receiving String (decoding error?)." + e.toString());
            str = "FileNameLost.unknown";
        }

        return str;
    }
    
    

    public void closeConnection() {

        try {
            tcpSocket.shutdownInput();
        } catch (Exception e) {
        }

        try {
            tcpSocket.shutdownOutput();
        } catch (Exception e) {
        }

        try{
            if(rx!=null) rx.close();
        }catch(Exception e){}
        
        try{
            if(tx!=null) tx.close();
        }catch(Exception e){}
        
        try{
            if(tcpSocket!=null)tcpSocket.close();
        }catch(Exception e){}
    }
    
    public String getIp(){
        return tcpSocket.getInetAddress().toString();
    }
    

    


    
}
