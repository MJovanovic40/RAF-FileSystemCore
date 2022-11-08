package model;

import java.io.File;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class FolderConfiguration {
    private Map<String, Integer> maximumFilesCount = new HashMap<String, Integer>();
    private int defaultLimit;
    public FolderConfiguration(){
        this.defaultLimit=10;
    }
    public void printMap(){
        for( String key : maximumFilesCount.keySet()){
            System.out.println(key + "  " + maximumFilesCount.get(key));
        }
    }
    public FolderConfiguration(int defaultLimit){
        this.defaultLimit=defaultLimit;
    }

    public void addFolderLimit( String folderLocation, int limit){
        maximumFilesCount.put(folderLocation,limit);
    }
    public int getFolderCountLimit(String folderLocation){
        if(!maximumFilesCount.containsKey(folderLocation))
            return this.defaultLimit;
        else
            return maximumFilesCount.get(folderLocation);
    }
    /**
     * Counts only files (and not directories) from provided directory
     *
     * @param directory a file to the directory from where to count files
     * @return int - number of files from the provided directory
     */
    public int getFilesCount(File directory){
        File[] files = directory.listFiles();
        int count = 0;
        if(files == null){
            return count;
        }
        for (File f : files)
            if (!f.isDirectory())
                count++;
        return count;

    }


}