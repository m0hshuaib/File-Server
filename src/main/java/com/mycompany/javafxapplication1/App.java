package com.mycompany.javafxapplication1;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import static javafx.application.Application.launch;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Stage secondaryStage = new Stage();
        DB myObj = new DB("Users");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 900);
            
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Primary View");
            secondaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
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

    public static void main(String[] args) {
        String directoryPath = "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/";
        deleteDirectory(new File(directoryPath));
        
        // Create a new directory
        createDirectory(directoryPath);
        
        
        launch();
        
        
    }

}

