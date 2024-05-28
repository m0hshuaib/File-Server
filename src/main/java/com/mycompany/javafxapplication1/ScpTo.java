package com.mycompany.javafxapplication1;

import com.jcraft.jsch.*;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;

import java.util.Scanner;

public class ScpTo {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "ntu-user";
    private static final int REMOTE_PORT = 22;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;
    public static  int Numberofchunks = 4;
    public static String[] Containers = new String[4];


    public static void dockerConnect(String localFile, String remoteFile, String remoteHost, String Process) {
        Session jschSession = null;

        try {
            JSch jsch = new JSch();
            jsch.setKnownHosts("/home/mkyong/.ssh/known_hosts");

            jschSession = jsch.getSession(USERNAME, remoteHost, REMOTE_PORT);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);

            jschSession.setPassword(PASSWORD);

            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");

            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            
            
            if ("create".equals(Process)) {
                channelSftp.put("ProgramDirectories/" + localFile, remoteFile);
                System.out.println("Remote file created.");
            } 
            
            if ("delete".equals(Process)) {
                channelSftp.rm(remoteFile);
                System.out.println("Remote file deleted.");
            } 
            
            if ("update".equals(Process)) {
                channelSftp.put("ProgramDirectories/" + localFile, remoteFile);
                System.out.println("Remote file updated.");
            } 
            if ("get".equals(Process)) {
                channelSftp.get(remoteFile, "ProgramDirectories/" + localFile);
                System.out.println("Remote file retrieved.");
            } 

            channelSftp.exit();

        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }

    }
}

/*
   What the code does:

1. It uses the JSch library to establish an SSH connection to a remote host (a docker container)

2. It opens an SFTP channel over that SSH connection to enable file transfers

3. It has various configurations set up - username, password, ports, timeouts etc. to facilitate the connection

4. It exposes a dockerConnect method that allows carrying out CRUD operations on files:

 - Create - Upload a local file to the remote docker container
 - Read - Download a file from the remote docker container 
 - Update - Overwrite an existing remote file with an updated local file
 - Delete - Delete a file on the remote docker container

5. The remoteHost parameter allows it to connect to different docker containers like there are 4 containers defined that store distributed chunks of files

6. It handles exceptions appropriately during the SSH/SFTP processes
*/