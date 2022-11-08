package module;

import controller.filesystemobject.FSOController;
import controller.filesystemobject.FSOControllerFactory;
import controller.filesystemobject.FilesController;
import controller.search.SearchController;
import controller.search.SearchControllerFactory;
import enums.SortCriteria;
import enums.SortOrder;
import model.Configuration;
import model.FolderConfiguration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileSystemCoreImplementation implements FileSystemCore{

    private final Path storagePath;

    private final Configuration configuration;

    private FSOController filesController;

    private FolderConfiguration folderConfiguration;
    public FileSystemCoreImplementation(){
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        String storageFolderName = "raf_storage";

        this.configuration = new Configuration();
        this.folderConfiguration = new FolderConfiguration();
        this.storagePath = Paths.get(desktopPath + "/" + storageFolderName); // Default path je na desktop-u
        initialize();


    }
    public FileSystemCoreImplementation(String path){
        this.configuration = new Configuration();
        this.folderConfiguration = new FolderConfiguration();
        this.storagePath = Paths.get(path);
        initialize();
    }

    public FileSystemCoreImplementation(int storageSize, List<String> forbiddenExtensions, int maximumNumberOfFiles){
        this.configuration = new Configuration(storageSize, forbiddenExtensions, maximumNumberOfFiles);
        this.folderConfiguration = new FolderConfiguration();
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        String storageFolderName = "raf_storage";
        this.storagePath = Paths.get(desktopPath + "/" + storageFolderName); // Default path je na desktop-u
        initialize();
    }

    public FileSystemCoreImplementation(String path, int storageSize, List<String> forbiddenExtensions, int maximumNumberOfFiles){
        this.configuration = new Configuration(storageSize, forbiddenExtensions, maximumNumberOfFiles);
        this.folderConfiguration = new FolderConfiguration();
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

        this.filesController = FSOControllerFactory.createFSOController("files", this.storagePath.toString(), this.configuration, this.folderConfiguration);

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

    @Override
    public List<File> getAllFilesLikeName(String name, SortCriteria sortCriteria, SortOrder sortOrder) {
        SearchController searchController = SearchControllerFactory.createSearchController(this.storagePath.toString(), sortCriteria, sortOrder);
        return searchController.getAllFilesLikeName(name);
    }

    @Override
    public boolean directoryContainsFile(String path, String name) throws Exception {
        SearchController searchController = SearchControllerFactory.createSearchController(this.storagePath.toString(), SortCriteria.FILE_NAME, SortOrder.ASCENDING);
        return searchController.directoryContainsFile(path, name);
    }

    @Override
    public boolean directoryContainsFiles(String path, List<String> names, boolean strictMode) throws Exception {
        SearchController searchController = SearchControllerFactory.createSearchController(this.storagePath.toString(), SortCriteria.FILE_NAME, SortOrder.ASCENDING);
        return searchController.directoryContainsFiles(path, names, strictMode);
    }

    @Override
    public String getFilePath(String name) {
        SearchController searchController = SearchControllerFactory.createSearchController(this.storagePath.toString(), SortCriteria.FILE_NAME, SortOrder.ASCENDING);
        return searchController.getFilePath(name);
    }

    @Override
    public List<File> getFilesModifiedAt(String path, long modifiedAt, SortCriteria sortCriteria, SortOrder sortOrder) throws Exception {
        SearchController searchController = SearchControllerFactory.createSearchController(this.storagePath.toString(), sortCriteria, sortOrder);
        return searchController.getFilesModifiedAt(path, modifiedAt);
    }
}
