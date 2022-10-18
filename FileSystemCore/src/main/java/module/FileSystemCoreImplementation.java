package module;

import model.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemCoreImplementation implements FileSystemCore{

    private Path storagePath;

    private Configuration configuration;

    public FileSystemCoreImplementation(){
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        String storageFolderName = "raf_storage";

        this.storagePath = Paths.get(desktopPath + "/" + storageFolderName); // Default path je na desktop-u
        System.out.println(storagePath.toString());
        initialize();
    }
    public FileSystemCoreImplementation(String path){
        this.storagePath = Paths.get(path);
        initialize();
    }

    private void initialize() {
        this.configuration = new Configuration();
        File storageFile = storagePath.toFile();
        if(!storageFile.exists()) {
            storageFile.mkdir();
        }

        this.configuration.load(this.storagePath.toString());

        System.out.println(this.configuration.toString());
    }
}
