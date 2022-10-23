package module;

public interface FileSystemCore {
    boolean uploadFile(String targetPath, String uploadPath);
    boolean downloadFile(String targetPath, String downloadPath);
    boolean createFile(String name, String path);
    boolean deleteFile(String path);
    boolean moveFile(String targetPath, String movePath);
    boolean renameFile(String path, String name);
}
