/*
 * JAIME HIDALGO.
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaime.ultransfer.main;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author Jaime Hidalgo García
 */
public class Ultransfer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here   
//        String test = "-s -h 192.168.1.1 archivo1 archivo2";
//        String[] testt = test.split(" ");
        
        ParamParser param = new ParamParser();
        
        try{
            JCommander jc = new JCommander(param,args);
            param.validateMode();
        } catch(ParameterException pe){
            System.out.println(pe.getMessage());
            System.out.println("Use --help|-h to see detailed instructions.");
            System.exit(1);
        }
        
        if(param.getReceiveMode()==true) new ServerMode(param).run();
        else                             new ClientMode(param).run();

    }
    
}
