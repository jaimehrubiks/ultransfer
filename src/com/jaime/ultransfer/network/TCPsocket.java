package com.jaime.ultransfer.network;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


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
    
    private int                 buffSize = 2000;

    public TCPsocket(String host, int port, int buffSize) {
        this(host,port);
        this.buffSize = buffSize;
    }
    
    public TCPsocket(String host, int port) {

        try {
            tcpSocket = new Socket(host, port);
            //tx = new DataOutputStream(tcpSocket.getOutputStream()); // Unbuffered Alternative - Direct link
            tx = new DataOutputStream(new BufferedOutputStream(tcpSocket.getOutputStream()));
            rx = new DataInputStream(tcpSocket.getInputStream());
        } catch (Exception e) {
            System.out.println("[!] SocketError. Can't stablish connection. " + e.toString());
        }

    }
    
    public TCPsocket(Socket comSocket, int buffSize) {
        this(comSocket);
        this.buffSize = buffSize;
    }
    

    public TCPsocket(Socket comSocket) {
        
        try {
            this.tcpSocket = comSocket;
            //tx = new DataOutputStream(tcpSocket.getOutputStream()); // // Unbuffered Alternative - Direct link
            tx = new DataOutputStream(new BufferedOutputStream(tcpSocket.getOutputStream()));
            rx = new DataInputStream(tcpSocket.getInputStream());
        } catch (UnknownHostException uhe) {
            System.out.println("[!]  SocketError. Can't stablish connection. " + uhe.toString());
        } catch (IOException ex) {
            System.out.println("[!]  SocketError. Can't stablish connection. " + ex.toString());
        }
        
    }

    public void sendFile(File path) {

        //File file   =     new File(nombre);
        long total  =     path.length();
        
        try (FileInputStream fis = new FileInputStream(path)) {

            int rest = (int) total % buffSize;
            tx.writeLong(total);
            byte[] buffer = new byte[buffSize];
            for (int i = 0; i < total - rest; i += buffSize) {
                fis.read(buffer, 0, buffSize);
                tx.write(buffer, 0, buffSize);
            }
            fis.read(buffer, 0, rest);
            tx.write(buffer, 0, rest);
            tx.flush();

        } catch (Exception e) {
            System.out.println("[!] Error sending file. " + e.toString());
        }

    }

    public void getFile(File path) {

        try (FileOutputStream fos = new FileOutputStream(path)) {

            long total = rx.readLong();
            int rest = (int) (total % buffSize);

            byte[] buffer = new byte[buffSize];
            
            for (int i = 0; i < total - rest; i += buffSize) {
                rx.read(buffer, 0, buffSize);
                fos.write(buffer, 0, buffSize);
            }
            rx.read(buffer, 0, rest);
            fos.write(buffer, 0, rest);

        } catch (Exception e) {
            System.out.println("[!] Error receiving file. " + e.toString());
        }

    }
    

    public void sendByte(int nt) {

        try {
            tx.writeByte(nt);
            tx.flush();
        } catch (Exception e) {
            System.out.println("[!] Error sending byte." + e.toString());
        }

    }

    public int getByte() {

        byte nt;

        try {
            nt = rx.readByte();
        } catch (Exception e) {
            System.out.println("[!] Error receiving byte." + e.toString());
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
    
//    public void enviarArrayBytes(byte[] array) {
//
//        try {
//            tx.writeInt(array.length);
//            tx.write(array);
//
//            tx.flush();
//        } catch (Exception e) {
//            System.out.println("Error al enviar el array." + e.toString());
//        }
//
//    }
//
//    public byte[] recibirArrayBytes() {
//
//        byte[] array = null;
//        int nt;
//
//        try {
//            nt = rx.readInt();
//            if (nt > 0) {
//                array = new byte[nt];
//                rx.readFully(array, 0, nt);
//            }
//
//        } catch (Exception e) {
//            System.out.println("Error al recibir el array." + e.toString());
//        }
//
//        return array;
//
//    }
//
//    public void enviarArrayInt(int[] array) {
//
//        try {
//            tx.writeInt(array.length);
//            for (int nt : array) {
//                tx.writeInt(nt);
//            }
//            tx.flush(); //Si wrapeamos un buffer tenemos que hacer flush en algunos casos como este
//        } catch (Exception e) {
//            System.out.println("Error al enviar array." + e.toString());
//        }
//
//    }
//
//    public int[] recibirArrayInt() {
//
//        int[] array = null;
//
//        try {
//            int length = rx.readInt();
//            array = new int[length];
//            for (int i = 0; i < length; i++) {
//                array[i] = rx.readInt();
//            }
//        } catch (Exception e) {
//            System.out.println("Error al recibir el array." + e.toString());
//        }
//
//        return array;
//
//    }
//
//    public void enviarArrayBidInt(int[][] array) {
//
//        try {
//
//            tx.writeInt(array.length);      //Filas
//            tx.writeInt(array[0].length);   //Columnas
//
//            for (int[] fila : array) {
//                for (int elemento : fila) {
//                    tx.writeInt(elemento);
//                }
//            }
//
//            tx.flush();
//
//        } catch (Exception e) {
//            System.out.println("Error al enviar el array." + e.toString());
//        }
//
//    }
//
//    public int[][] recibirArrayBidInt() {
//
//        int[][] array = null;
//
//        try {
//
//            int filas = rx.readInt();
//            int columnas = rx.readInt();
//            array = new int[filas][columnas];
//
//            for (int[] fila : array) {
//                for (int i = 0; i < columnas; i++) {
//                    fila[i] = rx.readInt();
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println("Error al recibir el array." + e.toString());
//        }
//
//        return array;
//
//    }

    



}
