package module;

import controller.filesystemobject.FSOController;
import controller.filesystemobject.FilesController;
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

        //FSOController fsoController = new FilesController(this.storagePath.toString(), this.configuration);

        //boolean isUploaded = fsoController.upload("D:\\RuDok.zip","/");
        //boolean isDeleted = fsoController.delete("/RuDok.zip");
        //boolean isMoved = fsoController.move("/RuDok.zip", "/test");
        //boolean isRenamed = fsoController.rename("/Rudok2.pdf", "RuDok.zip");
        //boolean isCreated = fsoController.create("test.txt", "/");
        //boolean isDownloaded = fsoController.download("/RuDok.zip", "C:\\Users\\Milan\\Desktop");
        //System.out.println(isUploaded);
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
