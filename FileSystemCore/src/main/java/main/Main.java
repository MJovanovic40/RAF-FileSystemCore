package main;

import controller.filesystemobject.FoldersController;
import model.Configuration;
import module.FileSystemCore;
import module.FileSystemCoreImplementation;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<String> ext = new ArrayList<>();
        FileSystemCore fileSystemCore = new FileSystemCoreImplementation(100000, ext, 55);

        //fileSystemCore.uploadFile("D:\\RuDok.zip","/");
        //fileSystemCore.deleteFile("/RuDok.zip");
        //fileSystemCore.moveFile("/RuDok.zip", "/test");
        //fileSystemCore.renameFile("/RuDok.zip", "RuDok2.zip");
        //fileSystemCore.createFile("test.txt", "/");
        //fileSystemCore.downloadFile("/RuDok2.zip", "C:\\Users\\Milan\\Desktop");
        String rootLocation = "C:\\Users\\pc\\Desktop\\raf_storage\\";
        FoldersController fc = new FoldersController(rootLocation, new Configuration());
        //System.out.println(fc.upload("B","A"));
        //System.out.println(fc.rename("/A", "Test"));
        //System.out.println(fc.upload("B/D/E","A/B"));
        //System.out.println(fc.delete("B"));
        //System.out.println(fc.move("B","A"));
        //System.out.println(fc.create("TestFolder", "A"));
        System.out.println(fc.download("A", "B"));

    }
}
