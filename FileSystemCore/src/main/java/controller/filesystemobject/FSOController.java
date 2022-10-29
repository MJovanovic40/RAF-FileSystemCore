package controller.filesystemobject;
//File System Object Controller
public interface FSOController {
    boolean upload(String targetPath, String uploadPath) throws Exception;
    boolean download(String targetPath, String downloadPath) throws Exception;
    boolean create(String name, String path) throws Exception;
    boolean delete(String path) throws Exception;
    boolean move(String targetPath, String movePath) throws Exception;
    boolean rename(String path, String name) throws Exception;
}
