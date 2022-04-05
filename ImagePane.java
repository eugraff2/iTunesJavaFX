package cs1302.gallery;

import java.util.Scanner;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.net.URLEncoder;
import java.io.InputStreamReader;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.animation.Timeline;
import java.time.LocalTime;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.geometry.Pos;

/** The gallery of images retrieved from the iTunes API. */
public class ImagePane extends GridPane {

    int numResults = 0;
    Text errorText = new Text("Error, number of results was less than 21. Try a new query.");
    Text progressText = new Text("Images provided by a courtesy of iTunes");
    JsonArray imageArray;
    KeyFrame keyFrame;
    Timeline timeline;

    ProgressBar loadBar = new ProgressBar();
    HBox progressBox = new HBox(loadBar, progressText);
    double progress;

    EventHandler<ActionEvent> imageUpdater = (e) -> {
        int removeIndex = (int)(Math.random() * 19);
        int addIndex = (int)(Math.random() * 19) + (numResults - 20);
        JsonObject removedImg = imageArray.get(removeIndex).getAsJsonObject();
        imageArray.set(removeIndex, imageArray.get(addIndex));
        imageArray.set(addIndex, removedImg);
        this.constructGrid(imageArray);
    };


    /**
     * Default constructor, uses "rock" as a default query.
     * Also sets the timeline for the changing of one image every two seconds
     */
    public ImagePane() {
        super();
        imageArray = this.getJsonArray("rock");
        this.constructGrid(imageArray);
        keyFrame = new KeyFrame(Duration.seconds(2), imageUpdater);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    } // ImagePane

    /**
     * Constructor for the image, will use a url to
     * retrieve images and store them in panes within the grid.
     * Also sets the timeline for the changing of one image every two seconds
     *
     * @param query the query String passed in from the search bar
     * that will be made into a url
     */
    public ImagePane(String query) {
        super();
        imageArray = this.getJsonArray(query);
        if (numResults >= 21) {
            Platform.runLater(() -> this.constructGrid(imageArray));
            keyFrame = new KeyFrame(Duration.seconds(2), imageUpdater);
            timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        } else { // numResults < 21
            TilePane errorPane = new TilePane(errorText);
            errorPane.setAlignment(Pos.CENTER);
            this.getChildren().addAll(errorPane);
        } // if
    } // ImagePane

    /**
     * Uses the query to create a url and the use that url to
     * make / retrieve an array of Json results.
     *
     * @return JsonArray the array of Json results
     * @param query the search terms passed in from the default constructor
     */
    public JsonArray getJsonArray(String query) {
        String sUrl = "https://itunes.apple.com/search?term=";
        Scanner queryScanner = new Scanner(query);
        while (queryScanner.hasNext()) {
            sUrl += queryScanner.next() + "+";
        } // while
        sUrl += "&limit=50&media=music";

        JsonArray results = new JsonArray();
        try {
            URL rUrl = new URL(sUrl);
            InputStreamReader reader = new InputStreamReader(rUrl.openStream());
            JsonElement je = JsonParser.parseReader(reader);
            JsonObject root = je.getAsJsonObject(); // root of response
            results = root.getAsJsonArray("results");
            this.numResults = results.size();
        } catch (java.io.IOException ioe) {
            System.out.println(ioe);
        } // try
        return results;
    } // getJsonArray

    /**
     * Runs the process given in a separate thread.
     *
     * @param r the runnable process
     */
    public void runNow(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
    } // runQuery

    /**
     * Creates the grid from a provided array of Json elements.
     * Uses the elements to pull an image url and add image panes to this class
     * (which is a child of {@link GridPane}
     *
     * @param imgArray the JsonArray containing Json elements.
     */
    public void constructGrid(JsonArray imgArray) {
        int elementIndex = 0;
        progress = 0.0;
        for (int i = 0; i < 5; i++) {
            Platform.runLater(() -> {
                progress += .2;
                loadBar.setProgress(progress);
            });
            for (int j = 0; j < 4; j++) {
                JsonObject result = imgArray.get(elementIndex).getAsJsonObject();
                JsonElement artworkUrl = result.get("artworkUrl100");
                String artStr = artworkUrl.getAsString();
                Image img = new Image(artStr, 100, 100, false, true);
                ImageView imgView = new ImageView(img);
                TilePane imgPane = new TilePane(imgView);
                imgPane.setAlignment(Pos.CENTER);
                imgPane.setPrefTileWidth(20);
                this.add(imgPane, i, j);
                elementIndex++;
            } // for j
        } // for i
    } // constructGrid

} // ImagePane
