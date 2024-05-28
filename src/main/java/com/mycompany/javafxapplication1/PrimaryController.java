package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class PrimaryController {

    @FXML
    private Button registerBtn;

    @FXML
    private TextField userTextField;

    @FXML
    private PasswordField passPasswordField;
    
     public static User username_;

    @FXML
    private void registerBtnHandler(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        DB myObj = new DB("Users");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 900);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Register a new User");
            secondaryStage.show();
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    @FXML
    private void switchToSecondary() {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            DB myObj = new DB("Users");
            String[] credentials = {userTextField.getText(), passPasswordField.getText()};
            if(myObj.validateUser(userTextField.getText(), passPasswordField.getText())){
                username_ = new User(userTextField.getText(), passPasswordField.getText());
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("secondary.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 1250, 900);
                secondaryStage.setScene(scene);
                SecondaryController controller = loader.getController();
                controller.initialise(credentials);
                controller.initialise2();
                secondaryStage.setTitle("Home");
                String msg="some data sent from Primary Controller";
                secondaryStage.setUserData(msg);
                secondaryStage.show();
                primaryStage.close();
            }
            else{
                FileController.edialogue("Invalid User Name / Password","Please try again!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
