package module;

import enums.SortCriteria;
import enums.SortOrder;

import java.io.File;
import java.util.List;

public interface FileSystemCore {
    boolean uploadFile(String targetPath, String uploadPath) throws Exception;
    boolean downloadFile(String targetPath, String downloadPath) throws Exception;
    boolean createFile(String name, String path) throws Exception;
    boolean deleteFile(String path) throws Exception;
    boolean moveFile(String targetPath, String movePath) throws Exception;
    boolean renameFile(String path, String name) throws Exception;

    List<File> getAllFilesLikeName(String name, SortCriteria sortCriteria, SortOrder sortOrder);
    boolean directoryContainsFile(String path, String name) throws Exception;
    boolean directoryContainsFiles(String path, List<String> names, boolean strictMode) throws Exception;
    String getFilePath(String name);
    List<File> getFilesModifiedAt(String path, long modifiedAt, SortCriteria sortCriteria, SortOrder sortOrder) throws Exception;
}
