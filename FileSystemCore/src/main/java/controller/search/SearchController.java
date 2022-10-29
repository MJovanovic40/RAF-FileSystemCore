package controller.search;

import java.io.File;
import java.util.Date;
import java.util.List;

public interface SearchController {
    List<File> getAllFiles(String path);
    List<File> getAllFilesFromSubdirectories(String path);
    List<File> getAllFilesFromDirectory(String path);
    List<File> getAllFilesWithExtension(String path, String extension);

    List<File> getAllFilesLikeName(String name);
    boolean directoryContainsFile(String path, String name);
    boolean directoryContainsFiles(String path, List<String> names, boolean strictMode);
    String getFilePath(String name);
    List<File> getFilesModifiedAt(String modifiedAt);
}
