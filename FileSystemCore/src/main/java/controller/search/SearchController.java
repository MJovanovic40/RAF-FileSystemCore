package controller.search;

import java.io.File;
import java.util.List;

public interface SearchController {
    List<File> getAllFiles(String path);
    List<File> getAllFilesFromSubdirectories(String path);
    List<File> getAllFilesFromDirectory(String path);
    List<File> getAllFilesWithExtension(String path, String extension);

    List<File> getAllFilesLikeName(String name);
    boolean directoryContainsFile(String path, String name) throws Exception;
    boolean directoryContainsFiles(String path, List<String> names, boolean strictMode) throws Exception;
    String getFilePath(String name);
    List<File> getFilesModifiedAt(String path, long modifiedAt) throws Exception;
}
