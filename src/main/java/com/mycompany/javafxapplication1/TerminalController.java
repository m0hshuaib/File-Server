package com.mycompany.javafxapplication1;

import static com.mycompany.javafxapplication1.PrimaryController.username_;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.out;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user 
 */

public class TerminalController {
    
    private static final String[] COMMANDS = {"mv", "cp", "ls", "mkdir", "ps", "whoami", "tree", "terminator"};

   @FXML
   private TextField commandInput;
    
   @FXML
   private Button enterbtn;
   
   @FXML
   private TextArea commandDisplay;
   
   @FXML
   private Button backButton;
        
    /**
     * Initializes the controller class $ terminator -e nano
     * @param event
     * @throws java.io.IOException
     */
    
    // To execute a command when the user presses the "Execute" button
    @FXML
    public void onEnterButtonClicked(ActionEvent event) throws IOException {
    var processBuilder = new ProcessBuilder();

    if ("nano".equals(commandInput.getText())) {
        // Use terminator to run nano
        processBuilder.command("terminator", "-e", "nano");
        System.out.println("Works");
    } else {
        processBuilder.command(commandInput.getText().split(" "));
    }

    var process = processBuilder.start();

    try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line;

        while ((line = reader.readLine()) != null) {
            commandDisplay.appendText(line + "\n");
        }
    }
}
  
    private static boolean isValidCommand(String cmd) {
        return Arrays.asList(COMMANDS).contains(cmd); 
    }

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
}
