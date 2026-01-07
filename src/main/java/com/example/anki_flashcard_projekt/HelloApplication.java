package com.example.anki_flashcard_projekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
                controller.gemData(); // Alle objekterne gemmes
            }
            catch (IOException ex) // Hvis der sker fejl, så får brugeren besked
            {
                e.consume(); // Forhindre vinduet i at lukke, så brugeren kan se fejlmeddelelsen
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fejl");
                alert.setHeaderText("Kunne ikke gemme data");
                alert.setContentText("En fejl skete i forbindelse med at gemme data");
                alert.showAndWait();
            }
        });
    }
}