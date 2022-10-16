package module;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemCoreImplementation implements FileSystemCore{

    private Path storagePath;
    private String storageFolderName;

    public FileSystemCoreImplementation(){
        this.storagePath = Paths.get(System.getProperty("user.home") + "/Desktop"); // Default path je na desktop-u
        this.storageFolderName = "raf_storage";

        System.out.println(storagePath.toString());

    }

    public FileSystemCoreImplementation(String path){
        this.storagePath = Paths.get(path);
        this.storageFolderName = "raf_storage";
    }

    @Override
    public void initialize() {

    }
}
