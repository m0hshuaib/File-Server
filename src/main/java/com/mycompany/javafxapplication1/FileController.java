/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import static com.mycompany.javafxapplication1.PrimaryController.username_;
import com.mycompany.javafxapplication1.ScpTo;
import static com.mycompany.javafxapplication1.ScpTo.Containers;
import static com.mycompany.javafxapplication1.ScpTo.Numberofchunks;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileReader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;


/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class FileController  {
    
    @FXML 
    private TextField fileNameinput;
    
    @FXML 
    private TextField cloudinput;
    
    
    @FXML 
    private TextField receiverinput;
    
    
    @FXML 
    private TextField cloudinput2;

    @FXML
    private  TextArea output;

    @FXML
    private Button createbtn;

    @FXML
    private Button readbtn;

    @FXML
    private  Button updatebtn;

    @FXML
    private Button selectBtn;

    @FXML
    private Text fileText;

    @FXML
    private Button uploadBtn;
    
    @FXML
    private Button file2btn;
    
    
    @FXML
    private Button backButton;
    
    @FXML
    private Button backButton2;
    
    @FXML
    private Button selectCloudBtn;
    
    @FXML
    private Button selectCloudBtn2;
    
    @FXML
    private CheckBox myCheckBox;
   
    @FXML
    private TableView dataTableView;
    
    public static String selectedFilePath;
       
    private String checkBoxState = "RW";
    
    private String access;
    
    
    
    public static long fileSize;
    
   public static String[] chunkUUIDs = new String[4];
    
   static {
    Containers[0] = "comp20081-files-container1";
    Containers[1] = "comp20081-files-container2";
    Containers[2] = "comp20081-files-container3";
    Containers[3] = "comp20081-files-container4";
    }
    
    
    
    
   
    
   
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    // UI Navigation Functions
    
    
    //Switches the UI to the file2.fxml screen
     @FXML
    private void switchToFile2(){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) file2btn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("file2.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 900);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Download Share and Delete");
            FileController controller = loader.getController();
            controller.initialise2();
            secondaryStage.show();
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Goes back to the secondary.fxml home screen
    @FXML
    private void Backbuttonahndler(ActionEvent event){
        String getuser_ = username_.getUser();
        String getpass = username_.getPass();
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) backButton.getScene().getWindow();
        try {
            
            String[] credentials = new String[2];
            credentials[0] = getuser_;
            credentials[1] = getpass;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("secondary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 900);
            secondaryStage.setScene(scene);
            SecondaryController controller = loader.getController();
            controller.initialise(credentials);
            controller.initialise2();
            secondaryStage.setTitle("Home");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Goes back to the file.fxml screen
    @FXML
    private void Backbuttonahndler2(ActionEvent event){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) backButton2.getScene().getWindow();
        try {
            
        
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("file.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 900);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Files");
            FileController controller = loader.getController();
            controller.initialise2();
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////

// File Chunking and Merging Functions
    
   
     public static void splitFileIntoChunks(String filePath, String directoryPath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            File file = new File(filePath);
            fileSize = file.length();
            long chunkSize = fileSize / Numberofchunks;

            // Generate UUIDs as strings directly and store them in the array
            for (int i = 0; i < Numberofchunks; i++) {
                chunkUUIDs[i] = UUID.randomUUID().toString();
            }

            for (int i = 1; i <= Numberofchunks; i++) {
                try (FileOutputStream fos = new FileOutputStream(directoryPath + "/chunk" + i + ".bin")) {
                    byte[] buffer = new byte[(int) chunkSize];
                    int bytesRead = fis.read(buffer);

                    // If the last chunk is smaller, adjust the buffer size
                    if (i == Numberofchunks && bytesRead < chunkSize) {
                        buffer = Arrays.copyOf(buffer, bytesRead);
                    }

                    fos.write(buffer);

                    // Use chunkUUIDs in your file transfer logic
                    ScpTo.dockerConnect("chunk" + i + ".bin", "Vchunk" + chunkUUIDs[i - 1] + ".bin", Containers[i-1], "create");

                    deleteFile(directoryPath + "/chunk" + i + ".bin"); // Delete using UUID string
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void joinFiles(String directoryPath,String originalFileName, String outputPath) {
    try (FileOutputStream fos = new FileOutputStream(outputPath + originalFileName)) {
        for (int i = 1; i <= Numberofchunks; i++) {
            String virtualFileName = "Vchunk" + chunkUUIDs[i - 1] + ".bin";
            ScpTo.dockerConnect("chunk" + i + ".bin", virtualFileName, Containers[i-1], "get");
            
            try (FileInputStream fis = new FileInputStream(directoryPath + "/chunk" + i + ".bin")) {
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            
            deleteFile(directoryPath + "/chunk" + i + ".bin");
        }
    } catch (IOException e) {
        e.printStackTrace();
        edialogue("FILE JOIN FAILED", "An error occurred while joining files.");
    }
}
         
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    
//File selection functions
    
    @FXML
        private void selectBtnHandler(ActionEvent event) throws IOException {
            Stage primaryStage = (Stage) selectBtn.getScene().getWindow();
            primaryStage.setTitle("Select a File");

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if(selectedFile != null){
                selectedFilePath = selectedFile.getCanonicalPath();
                dialogue("", "FILE SELECTED");
                fileNameinput.setText("");
                cloudinput.setText("");
            }
        }
        
    
    @FXML
    private void selectCloudBtnHandler(ActionEvent event) throws IOException, ClassNotFoundException {
        DB myObj = new DB("fileMetaData");
        if (myObj.doesItemExist("fileName_", cloudinput2.getText(),  PrimaryController.username_.getUser(), "userName")){
             selectedFilePath = cloudinput2.getText();
             receiverinput.setText("");
             dialogue("", "FILE SELECTED");
        }
        else{
            edialogue("CANNOT SELECT", "FILE DOES NOT EXISTS");
        }
    }
    
    @FXML
    private void selectCloudBtnHandlerlocal(ActionEvent event) throws IOException, ClassNotFoundException {
        DB myObj = new DB("fileMetaData");
        String path = "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/";
        if(myObj.doesItemExist("fileName_", cloudinput.getText(),  PrimaryController.username_.getUser(), "userName")){
             selectedFilePath = cloudinput.getText();
             joinFiles(path,getFileName(selectedFilePath),path + "Files/");
             dialogue("", "FILE SELECTED");
        }
        else{
            edialogue("CANNOT SELECT", "FILE DOES NOT EXISTS");
        }
    }
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////     
// Core File Functions (Create, Delete, Update, Download, Upload, Read, Share)
    
    
    //Create
    
   @FXML
   private void createButtonHandler(ActionEvent event) throws ClassNotFoundException {
    

    String userCommand = fileNameinput.getText();
    DB myObj = new DB("fileMetaData");
    DB Log = new DB("auditTrail");

    if (myObj.doesItemExist("fileName_", userCommand, PrimaryController.username_.getUser(), "userName")) {
        // File with the same name already exists
        edialogue("CANNOT CREATE", "FILE ALREADY EXISTS ON THE CLOUD");
    } else {
        // File does not exist, proceed with creating
        cloudinput.setText("");
        output.setText("");
        fileCreating(userCommand);
        selectedFilePath = "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/Files/" + userCommand;
            
        if (!"".equals(userCommand)) {
            dialogue("CREATING FILE", "Successful!, You can now write and update your file");
            Log.addLog("User " + PrimaryController.username_.getUser() + " Created File: " + getFileName(selectedFilePath), "log");
        } else {
            edialogue("CANNOT CREATE", "ENTER FILE NAME");
        }
    }
}
   
    private String fileCreating(String name) {
        String output = "";
        String directoryPath = "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/Files/";
        
        // Combine the directory path and the file name
        String filePath = directoryPath + name;
        
        File myObj = new File(filePath);
        try {
            // Create the directories if they don't exist
            myObj.getParentFile().mkdirs();
            
            if (myObj.createNewFile()) {
                output += ("File created: " + myObj.getName());
            } else {
                output += ("File already exists.");
            }
        } catch (IOException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //Delete
    
     @FXML
    private void DeleteBtnHandler(ActionEvent event) throws IOException, ClassNotFoundException {
        DB myObj = new DB("fileMetaData");
        DB Log = new DB("auditTrail");
        if (selectedFilePath != null) {
            String[] chunkIds = myObj.getChunkIds(getFileName(selectedFilePath), PrimaryController.username_.getUser());
            for(int i = 1; i <= Numberofchunks; i++){
            ScpTo.dockerConnect("", "Vchunk" + chunkIds[i-1] + ".bin", Containers[i-1], "delete");
            }
            myObj.deleteRecord("fileName_",getFileName(selectedFilePath), PrimaryController.username_.getUser());
            Log.addLog("User " + PrimaryController.username_.getUser() + " Deleted File: " + getFileName(selectedFilePath), "log");
            initialise2();
            selectedFilePath = null;
        } else {
            edialogue("CANNOT DELETE", "NO FILE SELECTED");
        }
    }
    
 //////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //Update
    
      @FXML
    private void UpdateButtonHandler() throws ClassNotFoundException, InvalidKeySpecException {
    DB myobj = new DB("fileMetaData");
    DB Log = new DB("auditTrail");
    String path_ = "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/Files/";
    if(myobj.doesACLExist(PrimaryController.username_.getUser(), getFileName(selectedFilePath))){
       edialogue("Permission error", "You have read only permissions");
       return;
    }
   
    if(!(myobj.doesItemExist("fileName_", getFileName(selectedFilePath), PrimaryController.username_.getUser(), "userName"))){
    writeToFile(selectedFilePath, output.getText());
    dialogue("", "Upload to save changes");
    return;
    }
    
     if (myobj.doesItemExist("fileName_", getFileName(selectedFilePath), PrimaryController.username_.getUser(), "userName")) {
            writeToFile(path_ + selectedFilePath, output.getText());
            String[] chunkIds = myobj.getChunkIds(getFileName(selectedFilePath), PrimaryController.username_.getUser());
            for(int i = 1; i <= Numberofchunks; i++){
            ScpTo.dockerConnect("", "Vchunk" + chunkIds[i-1] + ".bin", Containers[i-1], "delete");
            }
            myobj.deleteRecord("fileName_",getFileName(selectedFilePath), PrimaryController.username_.getUser());
            splitFileIntoChunks(path_ + selectedFilePath, "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/");
            myobj.addDataTofileDB(PrimaryController.username_.getUser(), getFileName(selectedFilePath), fileSize, getFilePermissions(selectedFilePath), chunkUUIDs[0], chunkUUIDs[1], chunkUUIDs[2], chunkUUIDs[3], 123,12);
            initialise2();
            Log.addLog("User " + PrimaryController.username_.getUser() + " Updated file: " + getFileName(selectedFilePath), "log");
            selectedFilePath = null;
            cloudinput.setText("");
            output.setText("");
     }
    
    }
    
     public static void writeToFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
////////////////////////////////////////////////////////////////////////////////////////////////////////// 
    
    //Download
    
     @FXML
    private void downloadButtonHandler(ActionEvent event) throws ClassNotFoundException {
        DB myObj = new DB("fileMetaData");
        if (selectedFilePath != null & (myObj.doesItemExist("fileName_", selectedFilePath, PrimaryController.username_.getUser(), "userName"))) {
            DB Log = new DB("auditTrail");
            joinFiles("/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/", getFileName(selectedFilePath),"/home/ntu-user/Downloads/");
            Log.addLog("User " + PrimaryController.username_.getUser() + " Downloaded File: " + getFileName(selectedFilePath), "log");
            if(myObj.doesACLExist(PrimaryController.username_.getUser(), getFileName(selectedFilePath))){            
            makeFileReadOnly("/home/ntu-user/Downloads/" + getFileName(selectedFilePath) );
            dialogue("File dowloaded", "Check download directory");
            }
            
            selectedFilePath = null;
        } else {
            FileController.edialogue("CANNOT DOWNLOAD", "NO FILE SELECTED");
        }
    }
      
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //Upload
    
     @FXML
    private void uploadButtonHandler(ActionEvent event) throws InvalidKeySpecException, ClassNotFoundException {
        DB myObj = new DB("fileMetaData");
        DB Log = new DB("auditTrail");
        
    
    if (myObj.doesItemExist("fileName_", getFileName(selectedFilePath), PrimaryController.username_.getUser(), "userName")) {
        // File with the same name already exists
        edialogue("CANNOT UPLOAD", "TRY UPDATING INSTEAD");
        return;
    } 
        
        if (selectedFilePath != null & !(myObj.doesItemExist("fileName_", selectedFilePath, PrimaryController.username_.getUser(), "userName"))) {
             splitFileIntoChunks(selectedFilePath, "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/");
             myObj.addDataTofileDB(PrimaryController.username_.getUser(), getFileName(selectedFilePath), fileSize, getFilePermissions(selectedFilePath), chunkUUIDs[0], chunkUUIDs[1], chunkUUIDs[2], chunkUUIDs[3], 123,12);
             Log.addLog("User " + PrimaryController.username_.getUser() + " Uploaded File: " + getFileName(selectedFilePath), "log");
             initialise2();
             if(doesFileExist(getFileName(selectedFilePath))){
                deleteFile(selectedFilePath);
                selectedFilePath = null;
                fileNameinput.setText("");
                cloudinput.setText("");
                output.setText("");
             }

        } else {
            edialogue("CANNOT UPLOAD", "NO FILE SELECTED");
        }
    
    }
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////
   
    //Read
    
    @FXML
    private void readFileButtonHandler() throws ClassNotFoundException {
    DB myObj = new DB("fileMetaData");
    String path_ = "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/Files/";
     if((myObj.doesItemExist("fileName_", getFileName(selectedFilePath), PrimaryController.username_.getUser(), "userName"))){
    selectedFilePath = path_ + selectedFilePath;
    }
    
    
    File file = new File(selectedFilePath);
    if (selectedFilePath != null && !selectedFilePath.isEmpty()) {
        if (file.exists()) {
            readFileAndOutputToTextArea(file, output);
            selectedFilePath = getFileName(selectedFilePath);
        } else {
            System.err.println("File does not exist: " + selectedFilePath);
        }
    } else {
        System.err.println("Selected file path is null or empty.");
    }
}
    
    public static void readFileAndOutputToTextArea(File file, TextArea outputTextArea) {
        if (file == null || outputTextArea == null) {
            System.err.println("File or TextArea is null");
            return;
        }
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }
        
        outputTextArea.setText(content.toString());
    }
    
    
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //Share
    
     @FXML
    private void ShareFileHandler(ActionEvent event) throws IOException, ClassNotFoundException, InvalidKeySpecException {
        DB myObj = new DB("fileMetaData"); 
        DB user = new DB("Users");        
        String path = "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/";
        String receiver = receiverinput.getText();        
        if(!(user.doesItemExist("name", receiver, receiver, "name"))){
            edialogue("CANNOT SHARE", "USER DOES NOT EXISTS");
            return;
        }
        
        if (selectedFilePath != null & (myObj.doesItemExist("fileName_", selectedFilePath, PrimaryController.username_.getUser(), "userName"))) {  
            
            joinFiles(path,getFileName(selectedFilePath),path + "Files/");
            helper(receiver); 
            selectedFilePath = null;
            dialogue("", "File shared to user: " + receiver + "successfully!");
        } else {
            FileController.edialogue("CANNOT Share", "NO FILE SELECTED");
        }
     }
    
    //Access Controll Functions
    
     @FXML
    private void handleCheckBoxAction(ActionEvent event) {
        if (myCheckBox.isSelected()) {
            checkBoxState = "R";
        } else {
            checkBoxState = "RW"; 
        }
    }
    
    @FXML
    private void uncheckCheckBox() {
        myCheckBox.setSelected(false);
    }
    
    private void makeFileReadOnly(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.setReadOnly();
            System.out.println("File set to read-only: " + filePath);
        } else {
            System.out.println("File does not exist: " + filePath);
        }
    }
    
     private String getFilePermissions(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.canWrite()) {
                return "RW"; 
            } else {
                return "R"; 
            }
        } else {
            return "RW"; 
        }
    }
     
     // Main sharing logic
        private void helper(String person) throws InvalidKeySpecException, ClassNotFoundException {
        DB myObj = new DB("fileMetaData");
        DB Log = new DB("auditTrail");
        String path_ = "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/Files/";
        String originalFileName = getFileName(selectedFilePath);
        String newFileName = originalFileName;

        // Check if the file already exists in the receiver's database
        if (myObj.doesItemExist("fileName_", originalFileName, person, "userName")) {
            // Get the file extension (if any)
            String extension = "";
            int extensionIndex = originalFileName.lastIndexOf('.');
            if (extensionIndex != -1) {
                extension = originalFileName.substring(extensionIndex);
                originalFileName = originalFileName.substring(0, extensionIndex);
            }

            int counter = 1;
            // Keep incrementing the counter until a unique file name is found
            while (myObj.doesItemExist("fileName_", newFileName, person, "userName")) {
                newFileName = originalFileName + "(" + counter + ")" + extension;
                counter++;
            }
        }

        splitFileIntoChunks(path_ + selectedFilePath, "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/");
        if(myObj.doesACLExist(PrimaryController.username_.getUser(), getFileName(selectedFilePath))){
              access = "R";
         }
         else{
             access = getCheckBoxState();
         }

        myObj.addDataTofileDB(person, newFileName, fileSize, access,chunkUUIDs[0], chunkUUIDs[1], chunkUUIDs[2], chunkUUIDs[3], 123, 11);
        Log.addLog("User " + PrimaryController.username_.getUser() + " Shared File: " + newFileName + " to " + person, "log");
        initialise2();

        if (doesFileExist(getFileName(selectedFilePath))) {
            deleteFile(path_ + selectedFilePath);
            selectedFilePath = null;
            receiverinput.setText("");
            cloudinput2.setText("");
            uncheckCheckBox();
        }
}
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    
 //File Data Table
    
      public void initialise2() {
        DB myObj = new DB("fileMetaData");
        try {
            ObservableList<Map<String, String>> data = myObj.getDataFromTable2(PrimaryController.username_.getUser());

            // Create a TableColumn with the appropriate property name
            TableColumn<Map<String, String>, String> fileNameColumn = new TableColumn<>("Files");
            fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get("fileName_")));

            // Set the items and columns to the TableView
            dataTableView.getItems().clear(); // Clear existing data
            dataTableView.getColumns().clear(); // Clear existing columns
            dataTableView.setItems(data);
            dataTableView.getColumns().addAll(fileNameColumn);
            
            
            addContextMenu();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
            }   
       }
    
    
      
    public void addContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        // Create a menu item to copy the file name
        MenuItem copyFileNameMenuItem = new MenuItem("Copy File Name");
        copyFileNameMenuItem.setOnAction(event -> {
            Map<String, String> selectedRow = (Map<String, String>) dataTableView.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                String fileName = selectedRow.get("fileName_");

                // Use Clipboard to copy the file name to the system clipboard
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(fileName);
                clipboard.setContent(content);
            }
        });

        // Add the menu item to the context menu
        contextMenu.getItems().add(copyFileNameMenuItem);

        // Set the context menu for each row in the TableView
        dataTableView.setContextMenu(contextMenu);
    }  
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    
   //Helper Fucntions
    
      public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }
     
      
    public static boolean doesFileExist(String fileName) {
        // Construct the full path to the file
        
        String filePath = "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/Files/" + File.separator + fileName;

        // Create a File object for the specified file path
        File file = new File(filePath);

        // Check if the file exists 
        return file.exists();
    }
    
    
    public static void deleteFile(String filePath) {
        File File = new File(filePath);
        if (File.delete()) {
        } else {
            System.out.println("Failed to delete chunk file: " + File.getName());
        }
    }
    
   
    public String getCheckBoxState() {
        return checkBoxState;
    }
     
//////////////////////////////////////////////////////////////////////////////////////////////////////////

//UI Dialogs
    
    public static void dialogue(String headerMsg, String contentMsg) {
        Stage secondaryStage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);
        Optional<ButtonType> result = alert.showAndWait();
    }
    
    
    public static void edialogue(String headerMsg, String contentMsg) {
        Stage secondaryStage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);

        Optional<ButtonType> result = alert.showAndWait();
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////
      
}

    

    
    

