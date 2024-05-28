/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import static com.mycompany.javafxapplication1.FileController.edialogue;
import static com.mycompany.javafxapplication1.PrimaryController.username_;
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

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class RegisterController {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button registerBtn;

    @FXML
    private Button backLoginBtn;

    @FXML
    private PasswordField passPasswordField;

    @FXML
    private PasswordField rePassPasswordField;

    @FXML
    private TextField userTextField;
    

    @FXML
    private void registerBtnHandler(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            DB myObj = new DB("Users");
             
            
            if(myObj.nameExists(userTextField.getText())){
                edialogue("NAME ERROR","Username Taken");
                return;
            }

            if (passPasswordField.getText().equals(rePassPasswordField.getText())) {
                myObj.addDataToDB(userTextField.getText(), passPasswordField.getText());
                username_ = new User(userTextField.getText(), passPasswordField.getText());
                FileController.dialogue("Adding information to the database", "Successful!");
                String[] credentials = {userTextField.getText(), passPasswordField.getText()};
                loader.setLocation(getClass().getResource("secondary.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 1250, 900);
                secondaryStage.setScene(scene);
                SecondaryController controller = loader.getController();
                secondaryStage.setTitle("Home");
                controller.initialise(credentials);
                controller.initialise2();
                String msg = "some data sent from Register Controller";
                secondaryStage.setUserData(msg);
            } else {
                loader.setLocation(getClass().getResource("register.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 1250, 900);
                secondaryStage.setScene(scene);
                secondaryStage.setTitle("Register a new User");
            }
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backLoginBtnHandler(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) backLoginBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 900);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
