package com.mycompany.javafxapplication1;

import static com.mycompany.javafxapplication1.PrimaryController.username_;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class SecondaryController {
    
    @FXML
    private TextField userTextField;
    
    @FXML
    private Text fileText;
    
    @FXML
    private TableView dataTableView;

    @FXML
    private Button secondaryButton;
    
    @FXML
    private Button fileBtn;
    
    @FXML
    private Button terminalbtn;
    
    @FXML
    private Button manageaccountbtn;
    
    @FXML
    private void terminalbtnHandler(ActionEvent event){
        
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) terminalbtn.getScene().getWindow();
        try{
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(getClass().getResource("terminal.fxml"));
            
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 1250, 900);
            
            secondaryStage.setScene(scene);
            
            secondaryStage.setTitle("Terminal");
            
            secondaryStage.show();
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    };
    
    @FXML
    private void manageAccountBtnHandler(ActionEvent event){
        
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) manageaccountbtn.getScene().getWindow();
        try{
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(getClass().getResource("manageuser.fxml"));
            
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 1250, 900);
            
            secondaryStage.setScene(scene);
            
            ManageUsersController controller = loader.getController();
            controller.initialise();
            
            secondaryStage.setTitle("Update User");
            
            secondaryStage.show();
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    };
   
    
    @FXML
     private void switchToPrimary(){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) secondaryButton.getScene().getWindow();
        String directoryPath = "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/Files";
        try {
            
        
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 900);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");
            secondaryStage.show();
            primaryStage.close();
            deleteDirectory(new File(directoryPath));
            createDirectory(directoryPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialise(String[] credentials) {
        //userTextField.setText(credentials[0]);
        DB myObj = new DB("Users");
        ObservableList<User> data;
        try {
            data = myObj.getDataFromTable();
            TableColumn user = new TableColumn("User");
        user.setCellValueFactory(
        new PropertyValueFactory<>("user"));

        TableColumn pass = new TableColumn("Pass");
        pass.setCellValueFactory(
            new PropertyValueFactory<>("pass"));
        dataTableView.setItems(data);
        dataTableView.getColumns().addAll(user, pass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
     public void initialise2(){
     String getuser_ = username_.getUser();
     fileText.setText(getuser_);
     }
    
    @FXML
    private void switchToFile(){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) secondaryButton.getScene().getWindow();
        try {
            
        
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("file.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 900);
            secondaryStage.setScene(scene);
            FileController controller = loader.getController();
            controller.initialise2();
            secondaryStage.setTitle("Files");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            // Finally, delete the directory itself
            directory.delete();
        } else {
            System.err.println("Directory does not exist: " + directory.getAbsolutePath());
        }
    }
    
    private static void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
            } else {
                System.err.println("Failed to create directory.");
            }
        }
    }

}
