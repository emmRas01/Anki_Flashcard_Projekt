package com.example.anki_flashcard_projekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("flashcardAppLayout.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Flashcards");
        stage.setScene(scene);
        stage.show();

        // Når brugeren lukker vinduet -> gemmes data i filerne
        Controller controller = fxmlLoader.getController(); // Finder vores controller
        stage.setOnCloseRequest(e ->
        {
            try
            {
                controller.gemData(); // Alt gemmes
            }
            catch (Exception ex) // Hvis der sker fejl, så får brugeren besked
            {
                ex.printStackTrace(); // Udskriver fejlen
            }
        });
    }
}