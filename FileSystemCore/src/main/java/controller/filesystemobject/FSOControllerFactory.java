package controller.filesystemobject;

import model.Configuration;
import model.FolderConfiguration;

public class FSOControllerFactory {
    public static FSOController createFSOController(String type, String rootStorageLocation, Configuration configuration, FolderConfiguration folderConfiguration){
        if(type.equals("files")){
            return new FilesController(rootStorageLocation, configuration, folderConfiguration);
        } else if(type.equals("folders")){
            return new FoldersController(rootStorageLocation, configuration,folderConfiguration);
        } else {
            return null;
        }
    }
}
