package cs1302.gallery;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Priority;
import javafx.animation.Animation;
import javafx.animation.Timeline;

/**
 * Represents an iTunes GalleryApp.
 */
public class GalleryApp extends Application {

    VBox window;
    Scene scene;
    Stage stage;
    ImagePane imgPane;
    HBox toolBar;
    Button updateImages;
    Button pausePlay;
    TextField searchBar;

    /**
     * Start method for the Gallery JFX application. Contains the code that will construct the
     * primary stage and scene for the app.
     *
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) {
        FileBar fileBar = new FileBar();
        this.createToolbar();
        imgPane = new ImagePane();
        window = new VBox(8, fileBar, toolBar, imgPane, imgPane.progressBox);

        scene = new Scene(window);
        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);
        stage.setTitle("GalleryApp!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    } // start

    /** Creates the tool bar with its components. */
    public void createToolbar() {
        updateImages = new Button("Update Images");
        pausePlay = new Button("Pause");
        searchBar = new TextField("rock");
        toolBar = new HBox(8, pausePlay, searchBar, updateImages);

        pausePlay.setOnAction(ppHandler);
        updateImages.setOnAction(updateHandler);
    } // createToolbar

    EventHandler<ActionEvent> updateHandler = event -> {
        window.getChildren().remove(imgPane);
        window.getChildren().remove(imgPane.progressBox);
        imgPane = new ImagePane(searchBar.getText());
        imgPane.progress = 0;
        window.getChildren().addAll(imgPane, imgPane.progressBox);
    };

    EventHandler<ActionEvent> ppHandler = event -> {
        if (imgPane.timeline.getStatus() == Animation.Status.RUNNING) {
            imgPane.timeline.pause();
            pausePlay.setText("Play");
        } else if (imgPane.timeline.getStatus() == Animation.Status.PAUSED) {
            imgPane.timeline.play();
            pausePlay.setText("Pause");
        } // if
    };

} // GalleryApp
