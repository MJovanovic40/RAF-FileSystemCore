package controller.filesystemobject;

import model.Configuration;
import model.FolderConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesController implements FSOController{

    private final String rootStorageLocation;
    private final Configuration configuration;
    private final FolderConfiguration folderConfiguration;

    public FilesController(String rootStorageLocation, Configuration configuration, FolderConfiguration folderConfiguration){
        this.rootStorageLocation = rootStorageLocation;
        this.configuration = configuration;
        this.folderConfiguration=folderConfiguration;
    }

    /**
        Uploads a file to the Storage

        @param targetPath an absolute path to the file to be uploaded
        @param uploadPath an absolute storage path where the file will be uploaded
        @return true if the file is uploaded successfully, false otherwise
     */
    @Override

    public boolean upload(String targetPath, String uploadPath) throws Exception {
        File targetFile = new File(targetPath);
        File targetDirectory = new File(rootStorageLocation + uploadPath); // Predpostavljamo da ce uploadPath imati "/" na pocetku

        if(this.folderConfiguration.getFilesCount(targetDirectory) >= this.folderConfiguration.getFolderCountLimit(rootStorageLocation+uploadPath)){
            throw new Exception("File count limit exceeded.");
        }
        if(!targetFile.exists() || !targetFile.isFile() || !targetDirectory.exists() || !targetDirectory.isDirectory()) { //Validate input

            throw new Exception("Invalid input provided.");
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
            throw new Exception("Extension not allowed.");
        }

        try {
            if(Files.size(Paths.get(this.rootStorageLocation)) + fileSize > this.configuration.getStorageSize()){
                throw new Exception("Storage size exceeded.");
            }

            if(getFilesCount(new File(this.rootStorageLocation)) >= this.configuration.getMaximumNumberOfFiles()){
                throw new Exception("Number of files exceeded.");
            }
        } catch (IOException e) {
            throw new Exception("Error.");
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
            throw new Exception("Error.");
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
    public boolean download(String targetPath, String downloadPath) throws Exception {
        File targetFile = new File(this.rootStorageLocation + targetPath);
        File downloadFile = new File(downloadPath);

        if(!targetFile.exists() || !targetFile.isFile() || !downloadFile.exists() || !downloadFile.isDirectory()){ //Validate input
            throw new Exception("Invalid input provided.");
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
    public boolean create(String name, String path) throws Exception {
        if(name.isEmpty() || path.isEmpty()){
            throw new Exception("Invalid Input Provided.");
        }

        String newPath = this.rootStorageLocation + path + "/" + name;

        String extension = getExtension(newPath);

        //Check configuration
        //We don't need to check for file size because a newly created file will always be 0 bytes
        if(extension.isEmpty() || this.configuration.getForbiddenExtensions().contains(extension)){ // Check extension
            throw new Exception("Extension not allowed.");
        }

        if(getFilesCount(new File(this.rootStorageLocation)) >= this.configuration.getMaximumNumberOfFiles()){ // Check if the file is allowed to be created
            throw new Exception("Number of files exceeded.");
        }

        if(this.folderConfiguration.getFilesCount(new File( this.rootStorageLocation + path)) >= this.folderConfiguration.getFolderCountLimit( this.rootStorageLocation + path)){
            throw new Exception("File count limit exceeded.");
        }

        File newFile = new File(newPath);

        if(newFile.exists()){ //Check if file already exists
            throw new Exception("File already exists.");
        }

        try {
            return newFile.createNewFile();
        } catch (IOException e) {
            throw new Exception("Error.");
        }
    }

    /**
     * Deletes a file from storage
     *
     * @param path an absolute storage path to the file
     * @return true if file is deleted, false otherwise
     */
    @Override
    public boolean delete(String path) throws Exception {

        File targetFile = new File(this.rootStorageLocation + path);

        if(!targetFile.exists()) // Validate input
            throw new Exception("Target file doesen't exist.");

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
    public boolean move(String targetPath, String movePath) throws Exception {

        File targetFile = new File(this.rootStorageLocation + targetPath);
        File moveDir = new File(this.rootStorageLocation + movePath);


        if(!targetFile.exists() || !targetFile.isFile() || !moveDir.exists() || !moveDir.isDirectory()){ // Validate input
            throw new Exception("Invalid input.");
        }

        if(this.folderConfiguration.getFilesCount(new File( this.rootStorageLocation + movePath)) >= this.folderConfiguration.getFolderCountLimit( this.rootStorageLocation + movePath)){
            throw new Exception("File count limit exceeded.");
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
    public boolean rename(String path, String name) throws Exception {

        File targetFile = new File(this.rootStorageLocation + path);

        if(!targetFile.exists() || !targetFile.isFile() || name.isEmpty()){ // Validate input
            throw new Exception("Invalid input.");
        }

        String newPath = targetFile.toString().replace(targetFile.getName(), name); // Create a new path with the new file name

        if(this.configuration.getForbiddenExtensions().contains(getExtension(newPath))){ // Check if the new name (mainly extension) fits the configuration settings
            throw new Exception("Extension not allowed.");
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
