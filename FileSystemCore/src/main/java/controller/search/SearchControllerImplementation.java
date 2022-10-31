package controller.search;

import controller.filesystemobject.FilesController;
import enums.SortCriteria;
import enums.SortOrder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

@Setter
@Getter
public class SearchControllerImplementation implements SearchController{

    private SortOrder sortOrder;
    private SortCriteria sortCriteria;

    private final String rootStorageLocation;

    public SearchControllerImplementation(String rootStorageLocation, SortCriteria sortCriteria, SortOrder sortOrder){
        this.sortCriteria = sortCriteria;
        this.sortOrder = sortOrder;
        this.rootStorageLocation = rootStorageLocation;
    }

    @Override
    public List<File> getAllFiles(String path) {

        File file = new File(this.rootStorageLocation + path);
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
        return sortFiles(result);
    }

    @Override
    public List<File> getAllFilesLikeName(String name) {
        List<File> files = allFiles(new File(this.rootStorageLocation + "/"), null);
        List<File> result = new ArrayList<>();
        for(File file: files){

            if(file.getName().contains(name)){
                result.add(file);
            }
        }
        return sortFiles(result);
    }

    @Override
    public boolean directoryContainsFile(String path, String name) throws Exception {
        File targetDir = new File(this.rootStorageLocation + path);

        if(!targetDir.exists() || !targetDir.isDirectory()) {
            throw new Exception("Invalid input provided.");
        }

        File[] files = targetDir.listFiles();

        if(files == null) {
            throw new Exception("NullPointer Error");
        }

        for(File file: files) {
            if (file.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean directoryContainsFiles(String path, List<String> names, boolean strictMode) throws Exception {
        File targetDir = new File(this.rootStorageLocation + path);

        if(!targetDir.exists() || !targetDir.isDirectory()) {
            throw new Exception("Invalid input provided.");
        }

        File[] files = targetDir.listFiles();

        if(files == null) {
            throw new Exception("NullPointer Error");
        }

        Set<String> usedNames = new HashSet<>();

        for(File file: files) {
            for(String name: names){
                if(file.getName().equals(name)){
                    if(strictMode){
                        usedNames.add(name);
                    } else{
                        return true;
                    }
                }
            }
        }
        return strictMode && usedNames.size() == names.size();
    }

    @Override
    public String getFilePath(String name) {

        List<File> files = allFiles(new File(this.rootStorageLocation + "/"), null);

        for(File file: files){
            if(file.getName().equals(name)){
                return file.getAbsolutePath().replace(this.rootStorageLocation, "");
            }
        }

        return "";
    }

    @Override
    public List<File> getFilesModifiedAt(String path, long modifiedAt) throws Exception {
        File targetDir = new File(this.rootStorageLocation + path);

        if(!targetDir.exists() || !targetDir.isDirectory()) {
            throw new Exception("Invalid input provided.");
        }

        List<File> result = new ArrayList<>();

        File[] files = targetDir.listFiles();

        if(files == null){
            return result;
        }
        for(File file: files){
            if(file.isFile() && file.lastModified() >= modifiedAt*1000){
                result.add(file);
            }
        }

        return sortFiles(result);
    }

    public List<File> allFiles(File path, List<File> files){
        if(files == null){
            files = new ArrayList<>();
        }
        if(!path.exists()){
            return files;
        }

        File[] filesAndDirectories = path.listFiles();

        if(filesAndDirectories == null){
            return files;
        }

        for(File f: filesAndDirectories){
            if(f.isDirectory()){
                allFiles(f, files);
            } else{
                files.add(f);
            }
        }
        return files;

    }

    private List<File> sortFiles(List<File> files){
        switch(this.sortCriteria){
            case FILE_NAME:
                files.sort(Comparator.comparing(File::getName));
                break;
            case MODIFIED_AT:
                files.sort(Comparator.comparing(File::lastModified));
                break;
        }
        if(this.sortOrder == SortOrder.DESCENDING)
            Collections.reverse(files);

        return files;
    }

}
