/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myexplorer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author Farhan
 */
public class GUIController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Path root;
    private Path currentDirectoryPath;
    public TextField currentDirectory;
    public TableView <FileData> tableView;
    private ObservableList <FileData> tableViewData;
    private ArrayList <FileData> filesArrayList;
    
    public TableColumn <FileData,String> icon;
    public TableColumn <FileData,String> name;
    public TableColumn <FileData,String> size;
    public TableColumn <FileData,String> dateModified;
    
    public TilePane tileView;
    public ScrollPane scrollPane;
    
    public TreeView treeView;
    
    public Button back;
    public Button switchView;
    
    
    private void showDirectory(Path givenPath) {
        String curDirValue = givenPath.toAbsolutePath().toString();
        currentDirectory.clear();
        currentDirectory.setText(curDirValue);

    }
    
    public ArrayList <FileData> getCurrentFiles(Path givenPath)
    {
        ArrayList <FileData>currentFiles=new ArrayList<>();
        currentFiles.clear();
        File currentFolder=new File(givenPath.toAbsolutePath().toString());
        File[] fileList;
        fileList = currentFolder.listFiles();
        
        if(fileList!=null)    {
            for (File file:fileList) {
                FileData tmpFile=new FileData(file);
                currentFiles.add(tmpFile);
            }
        }
        return currentFiles;
    }
    
    private void setCellFactory() {
        icon.setCellValueFactory(new PropertyValueFactory("fileIcon"));
        name.setCellValueFactory(new PropertyValueFactory("fileName"));
        size.setCellValueFactory(new PropertyValueFactory("fileSize"));
        dateModified.setCellValueFactory(new PropertyValueFactory("dateLastModified"));
    }
    
    public void showTableView(Path givenPath) {
        showDirectory(givenPath);
        filesArrayList=getCurrentFiles(givenPath);
        setCellFactory();
        tableView.getItems().clear();
        filesArrayList.stream().filter((file) -> (file.getIsDirectory())).forEach((file) -> {
            tableViewData.add(file);
        });
        filesArrayList.stream().filter((file) -> (!file.getIsDirectory())).forEach((file) -> {
            tableViewData.add(file);
        }); 
        tableView.setItems(tableViewData);

    }
    
    public void fileClickEvent(FileData file) throws IOException{
        if(file.getIsDirectory()){
            currentDirectoryPath=file.getFilePath();
            if(tileView.isDisabled())    showTableView(currentDirectoryPath);
            else    showTileView(currentDirectoryPath);
        }
        else Desktop.getDesktop().open(file.getThisFile());
    }
    @FXML
    public void selectFileTableView(MouseEvent event) throws IOException
    {
        if (event.getClickCount() == 2) //Checking double click
        {
            fileClickEvent(tableView.getSelectionModel().getSelectedItem());
        }
    }
    
    
    
    
    public void showTileView(Path givenPath) {
        showDirectory(givenPath);
        
        tileView.getChildren().clear();
        filesArrayList=getCurrentFiles(givenPath);
        
        filesArrayList.stream().map((file) -> {
            
            ImageView imageView=file.getFileIcon();
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            imageView.setPreserveRatio(true);
            Text text = new Text();
            if (file.getFileName().length() <= 15) text.setText(file.getFileName());
            else
            {
                String string;
                string = file.getFileName().substring(0,15);
                string += "...";
                text.setText(string);
            }
            VBox vBox = new VBox();
            vBox.getChildren().add(imageView);
            vBox.getChildren().add(text);
            vBox.setAlignment(Pos.CENTER);
            vBox.setOnMouseClicked((MouseEvent event) -> {
                if(event.getClickCount() == 2)
                {
                    try {
                        fileClickEvent(file);
                    } catch (IOException ex) {
                        Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return vBox;            
        }).forEach((vBox) -> {
            tileView.getChildren().add(new Group(vBox));
        });
    }
    
    
    
    
    
    @FXML
    public void ViewStyleSwitch(ActionEvent event)
    { 
        if(tileView.isDisabled())    {
            tileView.getChildren().clear();
            tableView.setDisable(true);
            tableView.setOpacity(0);
            scrollPane.setOpacity(1);
            tileView.setDisable(false);
            scrollPane.setDisable(false);
            showTileView(currentDirectoryPath);
        }
        else    {
            tableView.getItems().clear();
            tileView.setDisable(true);
            scrollPane.setDisable(true);
            scrollPane.setOpacity(0);
            tableView.setOpacity(1);
            tableView.setDisable(false);
            showTableView(currentDirectoryPath);
        }
    }
    
    @FXML
    public void GoBack(ActionEvent event)
    { 
        if(!currentDirectoryPath.equals(root))    {
            currentDirectoryPath=currentDirectoryPath.getParent();
            if(tileView.isDisabled())    showTableView(currentDirectoryPath);
            else    showTileView(currentDirectoryPath);
            
        }
    }
    
    public GUIController() {
        this.tableViewData = FXCollections.observableArrayList();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        root=Paths.get("C:\\");
        currentDirectoryPath=root;
        tileView.setDisable(true);
        scrollPane.setDisable(true);
        scrollPane.setOpacity(0);
        showTableView(currentDirectoryPath);
       
    }    
    
}
