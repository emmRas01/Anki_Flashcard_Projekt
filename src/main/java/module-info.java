module com.example.anki_flashcard_projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;


    opens com.example.anki_flashcard_projekt to javafx.fxml;
    exports com.example.anki_flashcard_projekt;
}