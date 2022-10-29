package module;

public interface FileSystemCore {
    boolean uploadFile(String targetPath, String uploadPath) throws Exception;
    boolean downloadFile(String targetPath, String downloadPath) throws Exception;
    boolean createFile(String name, String path) throws Exception;
    boolean deleteFile(String path) throws Exception;
    boolean moveFile(String targetPath, String movePath) throws Exception;
    boolean renameFile(String path, String name) throws Exception;
}
