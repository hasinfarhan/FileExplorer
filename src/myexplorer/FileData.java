/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myexplorer;

import java.io.File;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Farhan
 */
public class FileData {
    private String fileName;
    private String fileSize;
    private String dateLastModified;
    private ImageView fileIcon;
    private Boolean isDirectory;
    private Path filePath;
    private File parentFile;
    private File thisFile;
    
    public FileData(File file)
    {
        thisFile=file;
        filePath=file.toPath();
        parentFile=thisFile.getParentFile();
        fileName=thisFile.getName();
        isDirectory=thisFile.isDirectory();
        dateLastModified=new SimpleDateFormat("dd/MM/yyyy").format(new Date(thisFile.lastModified()));
        fileSize=calculateSize(thisFile.length());
        if(isDirectory)    fileIcon=new ImageView(new Image("file:fileicon.jpg"));
        else    fileIcon=new ImageView(new Image("file:File-512.png"));
        fileIcon.setFitHeight(30);
        fileIcon.setFitWidth(30);
        fileIcon.setPreserveRatio(true); 
        
    }
    public String calculateSize(long size)
    {
        DecimalFormat df;
        df = new DecimalFormat("0.00");
        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTb = sizeGb * sizeKb;
        if(size < sizeKb)
            return df.format(size)+ " b";
        else if(size < sizeMb)
            return df.format(size / sizeKb)+ " Kb";
        else if(size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if(size < sizeTb)
            return df.format(size / sizeGb) + " Gb";
        return "";
    }
    
    
     private javax.swing.Icon getJSwingIconFromFileSystem(File file) {
        FileSystemView view = FileSystemView.getFileSystemView();
        javax.swing.Icon icon = view.getSystemIcon(file);
        return icon;
    }    
    
    
    public String getFileName()
    {
        return fileName;
    }
    public void setFileName(String name)
    {
        fileName=name;
    }
    public Path getFilePath()
    {
        return filePath;
    }
    public void setFile(Path path)
    {
        filePath=path;
    }
    public String getDateLastModified()
    {
        return dateLastModified;
    }
    public void setdateLastModified(String date)
    {
        dateLastModified=date;
    }
    public String getFileSize()
    {
        return fileSize;
    }
    public void setFileSize(String size)
    {
        fileSize=size;
    }
    public ImageView getFileIcon()
    {
        return fileIcon;
    }
    public void setFileIcon(ImageView icon)
    {
        fileIcon=icon;
    }
    public Boolean getIsDirectory()
    {
        return isDirectory;
    }
    public void setIsDirectory(Boolean isDir)
    {
        isDirectory=isDir;
    }
    public File getParentFile()
    {
        return parentFile;
    }
    public void setParentFile(File pFile)
    {
        parentFile=pFile;
    }
    public File getThisFile()
    {
        return thisFile;
    }
    public void setThisFile(File file)
    {
        thisFile=file;
    }
    
    
}
