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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author Jaime Hidalgo García
 */
public class Ultransfer {

    private ParamParser     param = new ParamParser();;
    private JCommander      jc;
    
    public Ultransfer(String[] args){
        
        try{
            jc = new JCommander(param,args);
            param.validateMode();
            
        } catch(ParameterException pe){
            System.out.println(pe.getMessage());
            System.out.println("Use --help|-h to see detailed instructions.");
            System.exit(1);
        }
    }
    
    public void run(){
        if(param.getReceiveMode()==true) new ServerMode(param).run();
        else                             new ClientMode(param).run();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here   
//        String test = "-s -h 192.168.1.1 archivo1 archivo2";
//        String[] testt = test.split(" ");
        
        ParamParser param = new ParamParser();
        JCommander jc = null;
         
        try{
            jc = new JCommander(param,args);
            param.validateMode();
            
        } catch(ParameterException pe){
            System.out.println(pe.getMessage());
            System.out.println("Use --help|-h to see detailed instructions.");
            System.exit(1);
        }
        
        if(param.isHelp()) {
            jc.setProgramName("java -jar ultransfer.jar -s|-r");
            jc.setColumnSize(200);
            jc.usage();
            System.exit(0);
        }
        
        if(param.getReceiveMode()==true) new ServerMode(param).run();
        else                             new ClientMode(param).run();

    }
    
}
