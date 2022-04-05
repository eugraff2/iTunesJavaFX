package cs1302.gallery;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/** The object for a menu bar on the gallery, which contains one menu item. */
public class FileBar extends MenuBar {

    Menu file;
    MenuItem exit;

    /** Default constructor. */
    public FileBar() {
        super();
        file = new Menu("File");
        exit = new MenuItem("Exit");
        exit.setOnAction((e) -> System.exit(0));
        file.getItems().add(exit);
        this.getMenus().add(file);
    } // FileBar


} // FileBar
