package controller.filesystemobject;

import model.Configuration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FoldersController implements FSOController{
    private final String rootStorageLocation;
    private final Configuration configuration;

    public FoldersController(String rootStorageLocation, Configuration configuration){
        this.rootStorageLocation = rootStorageLocation;
        this.configuration = configuration;
    }
    @Override
    public boolean upload(String targetPath, String uploadPath) {

        File sourceFolder = new File(rootStorageLocation+targetPath);
        File destinationFolder = new File(rootStorageLocation + uploadPath);
        System.out.println(destinationFolder.toPath());
        if(!sourceFolder.exists() || !sourceFolder.isDirectory() || !destinationFolder.exists() || !destinationFolder.isDirectory()) {
            return false;
        }
        long folderSize;
        try {
            folderSize = Files.size(sourceFolder.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            if(Files.size(Paths.get(this.rootStorageLocation)) + folderSize > this.configuration.getStorageSize()){
                return false;
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] splitedFolders = targetPath.split("/");
        Path finalPath = Paths.get(destinationFolder.toPath() + "/"+ splitedFolders[splitedFolders.length-1]);

        if(finalPath.toFile().exists()){
            int c = 1;
            Path tempPath = finalPath;
            while(tempPath.toFile().exists()){
                tempPath = Paths.get(finalPath.toString() + "(" + c + ")");
                c++;
            }
            finalPath = tempPath;
        }
        try {

            FileUtils.copyDirectory(sourceFolder, finalPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;

    }

    @Override
    public boolean download(String targetPath, String downloadPath) {
        File targetFolder = new File(this.rootStorageLocation + targetPath);
        File downloadFolder = new File(downloadPath);

        if(!targetFolder.exists() || !targetFolder.isDirectory() || !downloadFolder.exists() || !downloadFolder.isDirectory()){
            return false;
        }
        Path finalPath = Paths.get(downloadFolder + "/" + targetFolder.getName());

        if(finalPath.toFile().exists()){
            int c = 1;
            Path tempPath = finalPath;
            while(tempPath.toFile().exists()){
                tempPath = Paths.get(finalPath.toString() + "(" + c + ")");
                c++;
            }
            finalPath = tempPath;
        }

        try {
            FileUtils.copyDirectory(targetFolder, finalPath.toFile());
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean create(String name, String path) {
        if(name.isEmpty() || path.isEmpty()){
            return false;
        }
        String newPath = this.rootStorageLocation + path + "/" + name;

        File newFolder = new File(newPath);
        if(newFolder.exists()){
            return false;
        }

       return newFolder.mkdir();
    }

    @Override
    public boolean delete(String path) {

        File targetFolder = new File(rootStorageLocation+path);
        if(!targetFolder.exists()){
            return false;
        }
        try {
            FileUtils.forceDelete(targetFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean move(String targetPath, String movePath) {

        File targetFolder = new File(this.rootStorageLocation + targetPath);
        File moveDir = new File(this.rootStorageLocation + movePath);

        if(!targetFolder.exists() || !targetFolder.isDirectory() || !moveDir.exists() || !moveDir.isDirectory()){
            return false;
        }
        File folderLoc = new File(moveDir.toPath() + "/" + targetFolder.getName());

        try {
            FileUtils.moveDirectory(targetFolder, folderLoc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean rename(String path, String name) {

        File targetFolder = new File(this.rootStorageLocation + path);
        if(!targetFolder.exists() || !targetFolder.isDirectory() || name.isEmpty()){
            return false;
        }
        File newPath = new File(targetFolder.toString().replace(targetFolder.getName(),name));
        return targetFolder.renameTo(newPath);
    }

}
