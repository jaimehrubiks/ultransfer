# ultransfer

A file transfer protocol implementation in java using sockets. CLI program, client-server, options, multi-threaded. Functions as a directory-drop mode, where server will store everything received.

Usage: java -jar filesender.jar [-r|-s] [-p PORT] [-d DEST HOSTNAME | IP] [-o SAVE_PATH] [-x PASSWORD] [Files to send]

    > Ultransfer v0.9
    > 
    > Usage: java -jar ultransfer.jar -s|-r
    > [options] Files   Options:
    >     --block-size, -b
    >        Block size in bytes in which data is writen/read from TCP buffer and from/to disk [Optional, default: 5000]
    >        Default: 50000
    >     --destination, -d
    >        Host | Destination, required in --send|-s mode
    >        Default: null
    >     --help, -h
    >        Shows this help
    >        Default: false
    >     --invert, -i
    >        Reverse mode, here sender will listen on selected port and receiver will attempt to connect to it. Only one connection will be made.
    >        Default: false
    >     --out-directory, -o
    >        Directory to save files [Optional, default: ./ ]
    >        Default: ./
    >     --password, -x
    >        PasswordX, required not to receive malicious files [Optional, default: 12345]
    >        Default: 12345
    >     --port, -p
    >        Port to listen / connect [Optional, default: 7055]
    >        Default: 7055
    >     --progress --> NOT IMPLEMENTED
    >        Displays progress of each file. Useful as interface to other program via stdout
    >        Default: false
    >     --receive, -r
    >        Receiving files mode
    >        Default: false
    >     --send, -s
    >        Send a file
    >        Default: false


## Examples:

Receive files

>     java -jar ultransfer.jar -r 
>     java -jar ultransfer.jar -r -p 4000 -x 12345

 

Send files

>     java -jar ultransfer.jar -s -d 192.168.1.105 file1
>     java -jar ultransfer.jar -s —d 192.168.1.105 -p 4000 -x 12345 file1 file2 file3 file4…


## Inverse Mode:

Normally receiver must open ports. If "-i" is specified in both sides, then sender must open ports, and receiver must specify ip address.

## Download binaries:
"Compile from source for latest version"

Link (v0.9): https://github.com/jaimehrubiks/ultransfer/releases/download/v.0.9/Ultransfer_CLI_v_0_9.rar  
        
