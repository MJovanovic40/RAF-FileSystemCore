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

    /**
     * Uploads a folder to the Storage
     *
     * @param targetPath an absolute path to the folder to be uploaded
     * @param uploadPath an absolute storage path where the folder will be uploaded
     * @return true if the folder is uploaded successfully
     * @throws Exception when the source folder and destination folder do not exist and when size exceeds given memory
     */
    @Override
    public boolean upload(String targetPath, String uploadPath) throws Exception {

        File sourceFolder = new File(rootStorageLocation+targetPath);
        File destinationFolder = new File(rootStorageLocation + uploadPath);
        System.out.println(destinationFolder.toPath());
        if(!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            throw new Exception("Source folder does not exist.");
        }
        if(!destinationFolder.exists() || !destinationFolder.isDirectory()){
            throw new Exception("Destination folder does not exist.");
        }
        long folderSize;
        try {
            folderSize = Files.size(sourceFolder.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            if(Files.size(Paths.get(this.rootStorageLocation)) + folderSize > this.configuration.getStorageSize()){
                throw new Exception("Exceeds given memory.");
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

    /**
     * Downloads a folder from the storage to a specified location
     *
     * @param targetPath an absolute storage path to the folder for download
     * @param downloadPath an absolute path to the download location (outside the storage)
     * @return true if the folder is downloaded
     * @throws Exception when target folder and download folder do not exist
     */
    @Override
    public boolean download(String targetPath, String downloadPath) throws Exception {
        File targetFolder = new File(this.rootStorageLocation + targetPath);
        File downloadFolder = new File(downloadPath);

        if(!targetFolder.exists() || !targetFolder.isDirectory()){
            throw new Exception("Target folder does not exist.");
        }
        if(!downloadFolder.exists() || !downloadFolder.isDirectory()){
            throw new Exception("Download folder does not exist.");
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

    /**
     * Creates a new folder
     *
     * @param name the name of the new folder
     * @param path an absolute storage path to the new file's directory
     * @return true if the file is created
     * @throws Exception when name or path do not exist and when the folder already exists
     */
    @Override
    public boolean create(String name, String path) throws Exception {
        if(name.isEmpty() || path.isEmpty()){
            throw new Exception("Invalid input arguments.");
        }
        String newPath = this.rootStorageLocation + path + "/" + name;

        File newFolder = new File(newPath);
        if(newFolder.exists()){
            throw new Exception("Folder already exists.");
        }

       return newFolder.mkdir();
    }

    /**
     * Deletes a folder from storage
     *
     * @param path an absolute storage path to the folder
     * @return true if file is deleted
     * @throws Exception when the given folder does not exist
     */
    @Override
    public boolean delete(String path) throws Exception {

        File targetFolder = new File(rootStorageLocation+path);
        if(!targetFolder.exists()){
            throw new Exception("Target folder does not exist.");
        }
        try {
            FileUtils.forceDelete(targetFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Moves a folder to another location within the storage
     *
     * @param targetPath an absolute storage path to the folder
     * @param movePath an absolute storage path to the target directory
     * @return true if the file is moved
     * @throws Exception when the target folder and target destination do not exist
     */
    @Override
    public boolean move(String targetPath, String movePath) throws Exception {

        File targetFolder = new File(this.rootStorageLocation + targetPath);
        File moveDir = new File(this.rootStorageLocation + movePath);

        if(!targetFolder.exists() || !targetFolder.isDirectory()){
            throw new Exception("Target folder does not exist.");
        }
        if(!moveDir.exists() || !moveDir.isDirectory()){
            throw new Exception("Target destination does not exist.");
        }
        File folderLoc = new File(moveDir.toPath() + "/" + targetFolder.getName());

        try {
            FileUtils.moveDirectory(targetFolder, folderLoc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Renames the provided folder to the provided name
     *
     * @param path an absolute storage path to the folder
     * @param name a new folder name (with extension)
     * @return true if the file is renamed
     * @throws Exception when the target folder does not exist
     */
    @Override
    public boolean rename(String path, String name) throws Exception {

        File targetFolder = new File(this.rootStorageLocation + path);
        if(!targetFolder.exists() || !targetFolder.isDirectory() || name.isEmpty()){
            throw new Exception("Target folder does not exist.");
        }
        File newPath = new File(targetFolder.toString().replace(targetFolder.getName(),name));
        return targetFolder.renameTo(newPath);
    }

}
