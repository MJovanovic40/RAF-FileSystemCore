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
        @return true if the file is uploaded successfully, false otherwise
     */
    @Override

    public boolean upload(String targetPath, String uploadPath) {
        File targetFile = new File(targetPath);
        File targetDirectory = new File(rootStorageLocation + uploadPath); // Predpostavljamo da ce uploadPath imati "/" na pocetku

        if(!targetFile.exists() || !targetFile.isFile() || !targetDirectory.exists() || !targetDirectory.isDirectory()) { //Validate input
            return false;
        }

        //Check configuration
        String extension = this.getExtension(targetPath);
        long fileSize;
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

    /**
     * Downloads a file from the storage to a specified location
     *
     * @param targetPath an absolute storage path to the file for download
     * @param downloadPath an absolute path to the download location (outside the storage)
     * @return true if the file is downloaded, false otherwise
     */
    @Override
    public boolean download(String targetPath, String downloadPath) {
        File targetFile = new File(this.rootStorageLocation + targetPath);
        File downloadFile = new File(downloadPath);

        if(!targetFile.exists() || !targetFile.isFile() || !downloadFile.exists() || !downloadFile.isDirectory()){ //Validate input
            return false;
        }

        Path finalPath = Paths.get(downloadFile + "/" + targetFile.getName());

        if(finalPath.toFile().exists()){ // Workaround to support uploading files with the same name
            int c = 1;
            String extension = getExtension(finalPath.toString());
            Path tempPath = finalPath;
            while(tempPath.toFile().exists()){
                tempPath = Paths.get(finalPath.toString().substring(0, finalPath.toString().length() - (extension.length() + 1)) + "(" + c + ")." + extension);
                c++;
            }
            finalPath = tempPath;
        }

        try {
            Files.copy(targetFile.toPath(), finalPath); // Download
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new file
     *
     * @param name the name of the new file
     * @param path an absolute storage path to the new file's directory
     * @return true if the file is created, false otherwise
     */
    @Override
    public boolean create(String name, String path) {
        if(name.isEmpty() || path.isEmpty()){
            return false;
        }

        String newPath = this.rootStorageLocation + path + "/" + name;

        String extension = getExtension(newPath);

        //Check configuration
        //We don't need to check for file size because a newly created file will always be 0 bytes
        if(extension.isEmpty() || this.configuration.getForbiddenExtensions().contains(extension)){ // Check extension
            return false;
        }

        if(getFilesCount(new File(this.rootStorageLocation)) >= this.configuration.getMaximumNumberOfFiles()){ // Check if the file is allowed to be created
            return false;
        }

        File newFile = new File(newPath);

        if(newFile.exists()){ //Check if file already exists
            return false;
        }

        try {
            return newFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a file from storage
     *
     * @param path an absolute storage path to the file
     * @return true if file is deleted, false otherwise
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
     * @return true if the file is moved, false otherwise
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
     * @return true if the file is renamed, false otherwise
     */
    @Override
    public boolean rename(String path, String name) {

        File targetFile = new File(this.rootStorageLocation + path);

        if(!targetFile.exists() || !targetFile.isFile() || name.isEmpty()){ // Validate input
            return false;
        }

        String newPath = targetFile.toString().replace(targetFile.getName(), name); // Create a new path with the new file name

        if(this.configuration.getForbiddenExtensions().contains(getExtension(newPath))){ // Check if the new name (mainly extension) fits the configuration settings
            return false;
        }

        return targetFile.renameTo(new File(newPath));
    }

    /**
     * Get file extension
     *
     * @param path an absolute storage path to the target file
     * @return String - file extension
     */
    private String getExtension(String path){
        String[] pathComponents = path.split("\\.");

        if(pathComponents.length == 1){
            return "";
        }

        return pathComponents[pathComponents.length-1];
    }

    /**
     * Counts files (and not directories) from provided directory and all subdirectories
     *
     * @param directory a file to the directory from where to count files
     * @return int - number of files from the provided directory and all subdirectories
     */
    private static int getFilesCount(File directory) {
        File[] files = directory.listFiles();
        int count = 0;
        if(files == null){
            return count;
        }
        for (File f : files)
            if (f.isDirectory())
                count += getFilesCount(f);
            else
                count++;

        return count;
    }
}
