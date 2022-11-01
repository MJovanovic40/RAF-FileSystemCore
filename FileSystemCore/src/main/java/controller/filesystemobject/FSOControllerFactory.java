package controller.filesystemobject;

import model.Configuration;

public class FSOControllerFactory {
    public static FSOController createFSOController(String type, String rootStorageLocation, Configuration configuration){
        if(type.equals("files")){
            return new FilesController(rootStorageLocation, configuration);
        } else if(type.equals("folders")){
            return new FoldersController(rootStorageLocation, configuration);
        } else {
            return null;
        }
    }
}
