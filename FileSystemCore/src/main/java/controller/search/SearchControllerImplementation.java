package controller.search;

import controller.filesystemobject.FilesController;
import enums.SortCriteria;
import enums.SortOrder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchControllerImplementation implements SearchController{

    private SortOrder sortOrder;
    private SortCriteria sortCriteria;

    public SearchControllerImplementation(SortCriteria sortCriteria, SortOrder sortOrder){
        this.sortCriteria = sortCriteria;
        this.sortOrder = sortOrder;
    }

    @Override
    public List<File> getAllFiles(String path) {

        File file = new File(path);
        File[] directories = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile();
            }
        });
        List<File> result =  new ArrayList<>();
        if(directories != null) {
            for (File directory : directories) {
                result.add(directory);
            }
        }

        return result;
    }


    @Override
    public List<File> getAllFilesFromSubdirectories(String path) {
        File file = new File(path);
        File[] directories = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return true;
            }
        });
        List<File> result = new ArrayList<>();
        for (File directory : directories) {

            if(directory.isDirectory()) {
                List<File> subResult = getAllFiles(path+directory.getName());

                for(File subDir : subResult) {
                    if(subDir.isFile()) {
                        result.add(subDir);
                    }
                }
            }else
                result.add(directory);
        }
        return result;
    }

    @Override
    public List<File> getAllFilesFromDirectory(String path) {
        List<File> result = new ArrayList<>();
        recursiveFileWalk( path, result,null);
        return result;
    }

    private void recursiveFileWalk( String path, List<File> list, String extension){

        File file = new File(path);
        File[] directories = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                File f =  new File(dir, name);
                if(getExtension(f.getPath()).equals(extension) || extension== null || f.isDirectory())
                    return true;
                else
                    return false;
            }
        });
        if( directories!=null){
            for (File directory : directories) {
                if(directory.isDirectory()) {
                    recursiveFileWalk(path+"/"+directory.getName(), list, extension);
                }else{
                    list.add(directory);
                }
            }
        }
    }
    /**
     * Get file extension
     *
     * @param path an absolute storage path to the target file
     * @return String - file extension
     */
    private String getExtension(String path){
        String[] pathComponents = path.split("\\.");

        if(pathComponents.length == 1){
            return "";
        }

        return pathComponents[pathComponents.length-1];
    }

    @Override
    public List<File> getAllFilesWithExtension(String path, String extension) {
        List<File> result = new ArrayList<>();
        recursiveFileWalk( path, result,extension);
        return result;

    }

    @Override
    public List<File> getAllFilesLikeName(String name) {
        return null;
    }

    @Override
    public boolean directoryContainsFile(String path, String name) {
        return false;
    }

    @Override
    public boolean directoryContainsFiles(String path, List<String> names, boolean strictMode) {
        return false;
    }

    @Override
    public String getFilePath(String name) {
        return null;
    }

    @Override
    public List<File> getFilesModifiedAt(String modifiedAt) {
        return null;
    }
}
