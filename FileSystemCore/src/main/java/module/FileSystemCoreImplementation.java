package module;

import model.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileSystemCoreImplementation implements FileSystemCore{

    private Path storagePath;

    private Configuration configuration;

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

    public FileSystemCoreImplementation(int storageSize, List<String> forbiddenExtensions, int maximumNumberOfFiles, String path){
        this.configuration = new Configuration(storageSize, forbiddenExtensions, maximumNumberOfFiles);
        this.storagePath = Paths.get(path);
        initialize();
    }


    private void initialize() {
        File storageFile = storagePath.toFile();
        if(!storageFile.exists()) {
            storageFile.mkdir();
        }

        this.configuration.load(this.storagePath.toString());

        System.out.println(this.configuration.toString());
    }
}
