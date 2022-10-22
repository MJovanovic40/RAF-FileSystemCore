package controller.filesystemobject;

import model.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesController implements FSOController{

    private final String rootStorageLocation;
    private final Configuration configuration;

    public FilesController(String rootStorageLocation, Configuration configuration){
        this.rootStorageLocation = rootStorageLocation;
        this.configuration = configuration;
    }

    /**
        Uploads a file to the Storage

        @param targetPath an absolute path to the file to be uploaded
        @param uploadPath an absolute storage path where the file will be uploaded
        @return true if the file is uploaded successfully - false if the file is not uploaded
     */
    @Override

    public boolean upload(String targetPath, String uploadPath) {
        File targetFile = new File(targetPath);
        File targetDirectory = new File(rootStorageLocation + uploadPath); // Predpostavljamo da ce uploadPath imati "/" na pocetku

        if(!targetFile.exists() || !targetFile.isFile() || !targetDirectory.exists() || !targetDirectory.isDirectory()) { //Validate input
            return false;
        }

        //Check configuration
        String extension = this.getExtenstion(targetPath);
        long fileSize = 0;
        try {
            fileSize = Files.size(targetFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(this.configuration.getForbiddenExtensions().contains(extension)){
            return false;
        }

        try {
            if(Files.size(Paths.get(this.rootStorageLocation)) + fileSize > this.configuration.getStorageSize()){
                return false;
            }

            if(getFilesCount(new File(this.rootStorageLocation)) >= this.configuration.getMaximumNumberOfFiles()){
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path finalPath = Paths.get(targetDirectory.toPath() + "/" + targetFile.toPath().getFileName());
        if(finalPath.toFile().exists()){ // Workaround to support uploading files with the same name
            int c = 1;
            Path tempPath = finalPath;
            while(tempPath.toFile().exists()){
                tempPath = Paths.get(finalPath.toString().substring(0, finalPath.toString().length() - (extension.length() + 1)) + "(" + c + ")." + extension);
                c++;
            }
            finalPath = tempPath;
        }


        try {
            Files.copy(targetFile.toPath(), finalPath); // Upload file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean download(String targetPath, String downloadPath) {
        return false;
    }

    @Override
    public boolean create(String name, String path) {
        return false;
    }

    /**
     * Deletes a file from storage
     *
     * @param path an absolute storage path to the file
     * @return true if file is deleted - false if file is not deleted
     */
    @Override
    public boolean delete(String path) {

        File targetFile = new File(this.rootStorageLocation + path);

        if(!targetFile.exists()) // Validate input
            return false;

        return targetFile.delete();
    }

    /**
     * Moves a file to another location within the storage
     *
     * @param targetPath an absolute storage path to the file
     * @param movePath an absolute storage path to the target directory
     * @return true if the file is moved - false if the file fails to move
     */
    @Override
    public boolean move(String targetPath, String movePath) {

        File targetFile = new File(this.rootStorageLocation + targetPath);
        File moveDir = new File(this.rootStorageLocation + movePath);


        if(!targetFile.exists() || !targetFile.isFile() || !moveDir.exists() || !moveDir.isDirectory()){ // Validate input
            return false;
        }

        File finalFile = new File(moveDir.toPath() + "/" + targetFile.getName());

        try {
            Files.move(targetFile.toPath(), finalFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    /**
     * Renames the provided file to the provided name
     *
     * @param path an absolute storage path to the file
     * @param name a new file name (with extension)
     * @return true if the file is renamed - false if the file fails to be renamed
     */
    @Override
    public boolean rename(String path, String name) {

        File targetFile = new File(this.rootStorageLocation + path);

        if(!targetFile.exists() || !targetFile.isFile() || name.isEmpty()){ // Validate input
            return false;
        }

        String newPath = targetFile.toString().replace(targetFile.getName(), name); // Create a new path with the new file name

        if(this.configuration.getForbiddenExtensions().contains(getExtenstion(newPath))){ // Check if the new name (mainly extension) fits the configuration settings
            return false;
        }

        return targetFile.renameTo(new File(newPath));
    }


    private String getExtenstion(String path){
        String[] pathComponents = path.split("\\.");
        return pathComponents[pathComponents.length-1];
    }

    private static int getFilesCount(File directory) {
        File[] files = directory.listFiles();
        int count = 0;
        for (File f : files)
            if (f.isDirectory())
                count += getFilesCount(f);
            else
                count++;

        return count;
    }
}
