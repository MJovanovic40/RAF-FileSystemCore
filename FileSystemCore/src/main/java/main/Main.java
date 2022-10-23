package main;

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


    }
}
