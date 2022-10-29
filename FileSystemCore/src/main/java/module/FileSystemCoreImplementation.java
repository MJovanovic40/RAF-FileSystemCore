package module;

import controller.filesystemobject.FSOController;
import controller.filesystemobject.FilesController;
import model.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileSystemCoreImplementation implements FileSystemCore{

    private final Path storagePath;

    private final Configuration configuration;

    private FSOController filesController;

    public FileSystemCoreImplementation(){
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        String storageFolderName = "raf_storage";

        this.configuration = new Configuration();
        this.storagePath = Paths.get(desktopPath + "/" + storageFolderName); // Default path je na desktop-u
        initialize();


    }
    public FileSystemCoreImplementation(String path){
        this.configuration = new Configuration();
        this.storagePath = Paths.get(path);
        initialize();
    }

    public FileSystemCoreImplementation(int storageSize, List<String> forbiddenExtensions, int maximumNumberOfFiles){
        this.configuration = new Configuration(storageSize, forbiddenExtensions, maximumNumberOfFiles);
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        String storageFolderName = "raf_storage";
        this.storagePath = Paths.get(desktopPath + "/" + storageFolderName); // Default path je na desktop-u
        initialize();
    }

    public FileSystemCoreImplementation(String path, int storageSize, List<String> forbiddenExtensions, int maximumNumberOfFiles){
        this.configuration = new Configuration(storageSize, forbiddenExtensions, maximumNumberOfFiles);
        this.storagePath = Paths.get(path);
        initialize();
    }


    private void initialize() {
        File storageFile = storagePath.toFile();
        if(!storageFile.exists()) {
            if(!storageFile.mkdir()){
                return;
            }
        }

        this.configuration.load(this.storagePath.toString());

        this.filesController = new FilesController(this.storagePath.toString(), this.configuration);

        System.out.println(this.configuration);
    }

    @Override
    public boolean uploadFile(String targetPath, String uploadPath) throws Exception {
        return this.filesController.upload(targetPath, uploadPath);
    }

    @Override
    public boolean downloadFile(String targetPath, String downloadPath) throws Exception {
        return this.filesController.download(targetPath, downloadPath);
    }

    @Override
    public boolean createFile(String name, String path) throws Exception {
        return this.filesController.create(name, path);
    }

    @Override
    public boolean deleteFile(String path) throws Exception {
        return this.filesController.delete(path);
    }

    @Override
    public boolean moveFile(String targetPath, String movePath) throws Exception {
        return this.filesController.move(targetPath, movePath);
    }

    @Override
    public boolean renameFile(String path, String name) throws Exception {
        return this.filesController.rename(path, name);
    }
}
