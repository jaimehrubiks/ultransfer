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

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jaime Hidalgo García
 */
public class ParamParser {

    //@Parameter
    //private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"--help", "-h"}, description = "Shows this help", help = true)
    private static boolean help = false;
    
    //Server Client Mode Flags
    @Parameter(names = {"--send", "-s"}, description = "Send a file" )
    private static boolean send = false;

    @Parameter(names = {"--receive", "-r"}, description = "Receiving files mode")
    private static boolean receive = false;

    //Host and port
    @Parameter(names = {"--port","-p"}, description = "Port to listen / connect [Optional, default: 7055]",validateWith = portValidator.class)
    private static Integer port = 7055;

    @Parameter(names = {"--destination", "-d"}, description = "Host | Destination, required in --send|-s mode")
    private static String host = "null";

    //Configs
    @Parameter(names = {"--out-directory", "-o"}, description = "Directory to save files [Optional, default: ./ ]")
    private static String directory = "./";

//    @Parameter(names = {"--block-size", "-b"}, description = "Block size in bytes in which data is writen/read from TCP buffer and from/to disk [Optional, default: 5000]",validateWith = bufferValidator.class)
//    private static Integer blockSize = 50000;

    @Parameter(names = {"--password", "-x"}, description = "PasswordX, required not to receive malicious files [Optional, default: 12345]")
    private static String password = "12345";
    
    @Parameter(names = "--progress", description = "Displays progress of each file. Useful as interface to other program via stdout")
    private static boolean progress = false;
    
    @Parameter(names = {"--invert","-i"}, description = "Reverse mode, here sender will listen on selected port and receiver will attempt to connect to it. Only one connection will be made.")
    private static boolean inverse = false;
    


    //@Parameter(names = "-debug", description = "Debug mode")
    //private boolean debug = false;

    //Main parameter for Files to send
    @Parameter(description = "Files")//, converter = FileConverter.class)
    private static List<String> files = new ArrayList<>();

    
    
    /*
    
    GETTERS
    
    */
    
    public boolean getSendMode(){
        return send;
    }
    
    public static boolean getReceiveMode(){
        return receive;
    }
    
    public static int getPort(){
        return port; 
    }
    
    public static String getHost(){
        return String.valueOf( host );
    }
    
    public static String getDirectory(){
        return directory;
    }
    
    public static String getPassword(){
        return password;
    }
    
    public static List<String> getFiles(){
        return files;
    }
    
    public static boolean isHelp(){
        return help;
    }
    
    public static boolean isProgress(){
        return progress;
    }
    
    public static boolean isInverse(){
        return inverse;
    }
    /*
    
    CONVERSORS
    
    */
    
//    public class FileConverter implements IStringConverter<File> {
//        @Override
//        public File convert(String value) {
//            return new File(value);
//        }
//    }
    
    
    /*  
    
    VALIDATORS
    
    */
    
    @SuppressWarnings("empty-statement")
    public void validateMode() throws ParameterException {
        if(help){
            ;
        }
        else if ((send == true && receive == true) || (send == false && receive == false)) {
            throw new ParameterException("There must be *one* mode selected, either -s or -r");
        }
        else if( send == true && host.equals("null") ){
            throw new ParameterException("You need to specify the target IP|hostname.Example: -d 192.168.1.35");
        }
        else if( receive == true && host.equals("null") && inverse == true ){
            throw new ParameterException("You need to specify the target IP|hostname.Example: -d 192.168.1.35");
        }
    }
    
//    public static class modeValidator implements IParameterValidator {
//
//        @SuppressWarnings("empty-statement")
//        public void validate(String name, String value) throws ParameterException {
//        //boolean send = Boolean.parseBoolean(value);
//            if (help){
//                System.out.println("test");
//            }else{
//                System.out.println(help);
//                if ( (send == true && receive == true) || (send == false && receive == false) ) {
//                    throw new ParameterException("There must be *one* mode selected, either -s or -r");
//                }
//            }
//            
//        }
//    }
    
    public static class portValidator implements IParameterValidator {

        public void validate(String name, String value) throws ParameterException {
            int port = Integer.parseInt(value);
            //boolean reveive =
            if ( port <= 1024 || port >= 65535 ) {
                throw new ParameterException("Port number must be between 1024 and 65535. Found: "+value);
            }
        }
    }
    
    public static class bufferValidator implements IParameterValidator {

        public void validate(String name, String value) throws ParameterException {
            int block = Integer.parseInt(value);
            //boolean reveive =
            if ( block < 500 || block > 8000 ) {
                throw new ParameterException("Packet block size [-b] must be in the 500-8000 range Found: "+value);
            }
        }
    }
    
    
}
