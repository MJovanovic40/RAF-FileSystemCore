package controller.filesystemobject;
//File System Object Controller
public interface FSOController {
    boolean upload(String targetPath, String uploadPath);
    boolean download(String targetPath, String downloadPath);
    boolean create(String name, String path);
    boolean delete(String path);
    boolean move(String targetPath, String movePath);
    boolean rename(String path, String name);
}
