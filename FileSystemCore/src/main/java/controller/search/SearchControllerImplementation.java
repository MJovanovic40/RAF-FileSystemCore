package controller.search;

import enums.SortCriteria;
import enums.SortOrder;

import java.io.File;
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

        return null;
    }

    @Override
    public List<File> getAllFilesFromSubdirectories(String path) {
        return null;
    }

    @Override
    public List<File> getAllFilesFromDirectory(String path) {
        return null;
    }

    @Override
    public List<File> getAllFilesWithExtension(String path, String extension) {
        return null;
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
