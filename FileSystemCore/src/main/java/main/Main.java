package main;

import controller.filesystemobject.FilesController;
import controller.filesystemobject.FoldersController;
import controller.search.SearchControllerImplementation;
import model.Configuration;
import model.FolderConfiguration;
import module.FileSystemCore;
import module.FileSystemCoreImplementation;

import java.io.File;
import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<String> ext = new ArrayList<>();
        FileSystemCore fileSystemCore = new FileSystemCoreImplementation(100000, ext, 55);

        //fileSystemCore.uploadFile("D:\\RuDok.zip","/");
        //fileSystemCore.deleteFile("/RuDok.zip");
        //fileSystemCore.moveFile("/RuDok.zip", "/test");
        //fileSystemCore.renameFile("/RuDok.zip", "RuDok2.zip");
        //fileSystemCore.createFile("test.txt", "/");
        //fileSystemCore.downloadFile("/RuDok2.zip", "C:\\Users\\Milan\\Desktop");

        String rootLocation = "C:\\Users\\pc\\Desktop\\raf_storage\\";
        FolderConfiguration folderConfig = new FolderConfiguration();


        FoldersController fc = new FoldersController(rootLocation, new Configuration(),folderConfig);
        FilesController filesController = new FilesController(rootLocation, new Configuration(),folderConfig);
        //System.out.println(fc.upload("B","A"));
        //System.out.println(fc.rename("/A", "Test"));
        //System.out.println(fc.upload("B/D/E","A/B"));
        //System.out.println(fc.delete("B"));
        //System.out.println(fc.move("B","A"));
        //System.out.println(fc.create("TestFolder", "A"));
        //System.out.println(fc.download("A", "C:/Users/pc/Desktop/raf_storage/B"));
        SearchControllerImplementation searchControllerImplementation = new SearchControllerImplementation(rootLocation,null,null);
//        for(File f : searchControllerImplementation.getAllFiles(rootLocation)){
//               System.out.println(f.getName());
//            }
//        System.out.println("Fajlovi i prvi podfolderi");
//        for(File f : searchControllerImplementation.getAllFilesFromSubdirectories(rootLocation)){
//            System.out.println(f.getName());
//        }

//        for(File f : searchControllerImplementation.getAllFilesFromDirectory(rootLocation+"")){
//               System.out.println(f.getName());
//            }
//        System.out.println("--------------------------------------------------------");
//        for(File f : searchControllerImplementation.getAllFilesWithExtension(rootLocation,"bmp")){
//            System.out.println(f.getName());
//        }
     folderConfig.addFolderLimit(rootLocation+"A\\B",3);
      //  folderConfig.printMap();
      //  filesController.upload(rootLocation+"A.txt","A\\B");
        filesController.move("D.txt","A\\B");

    }
}