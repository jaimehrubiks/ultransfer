# ultransfer

A file transfer protocol implementation in java using sockets. CLI program, client-server, options, multi-threaded. Functions as a directory-drop mode, where server will store everything received.

Usage: java -jar filesender.jar [-r|-s] [-p PORT] [-d DEST HOSTNAME | IP] [-o SAVE_PATH] [-x PASSWORD] [Files to send]

    > Ultransfer v0.9
    > 
    > Usage: java -jar ultransfer.jar -s|-r
    > [options] Files   Options:
    >     --block-size, -b
    >        Block size in bytes in which data is writen/read from TCP buffer
    > and from/to disk [Optional, default:
    > 5000]
    >        Default: 50000
    >     --destination, -d
    >        Host | Destination, required in --send|-s mode
    >        Default: null
    >     --help, -h
    >        Shows this help
    >        Default: false
    >     --out-directory, -o
    >        Directory to save files [Optional, default: ./ ]
    >        Default: ./
    >     --password, -x
    >        PasswordX, required not to receive malicious files [Optional,
    > default: 12345]
    >        Default: 12345
    >     --port, -p
    >        Port to listen / connect [Optional, default: 7055]
    >        Default: 7055
    >     --progress --> NOT IMPLEMENTED
    >        Displays progress of each file. Useful as interface to other program
    > via stdout
    >        Default: false
    >     --receive, -r
    >        Receiving files mode
    >        Default: false
    >     --send, -s
    >        Send a file
    >        Default: false




Receive files

>     java -jar ultransfer.jar -r 
>     java -jar ultransfer.jar -r -p 4000 -x 12345

 

Send files

>     java -jar ultransfer.jar -s file1
>     java -jar ultransfer.jar -s -p 4000 —d 192.168.1.105 -x 12345 file1 file2 file3 file4…



        
