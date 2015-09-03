/*
 * JAIME HIDALGO.
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    private Integer port = 7055;

    @Parameter(names = {"--destination", "-d"}, description = "Host | Destination, required in --send|-s mode")
    private static String host = "null";

    //Configs
    @Parameter(names = {"--out-directory", "-o"}, description = "Directory to save files [Optional, default: ./ ]")
    private String directory = "./";

    @Parameter(names = {"--block-size", "-b"}, description = "Block size in bytes in which data is writen/read from TCP buffer and from/to disk [Optional, default: 5000]",validateWith = bufferValidator.class)
    private Integer blockSize = 50000;

    @Parameter(names = {"--password", "-x"}, description = "PasswordX, required not to receive malicious files [Optional, default: 12345]")
    private String password = "12345";
    
    @Parameter(names = "--progress", description = "Displays progress of each file. Useful as interface to other program via stdout")
    private static boolean progress = false;
    


    //@Parameter(names = "-debug", description = "Debug mode")
    //private boolean debug = false;

    //Main parameter for Files to send
    @Parameter(description = "Files")//, converter = FileConverter.class)
    private List<String> files = new ArrayList<>();

    
    
    /*
    
    GETTERS
    
    */
    
    public boolean getSendMode(){
        return send;
    }
    
    public boolean getReceiveMode(){
        return receive;
    }
    
    public int getPort(){
        return port; 
    }
    
    public String getHost(){
        return String.valueOf( host );
    }
    
    public String getDirectory(){
        return directory;
    }
    
    public int getBlockSize(){
        return blockSize;
    }
    
    public String getPassword(){
        return password;
    }
    
    public List<String> getFiles(){
        return files;
    }
    
    public boolean isHelp(){
        return help;
    }
    
    public static boolean isProgress(){
        return progress;
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
