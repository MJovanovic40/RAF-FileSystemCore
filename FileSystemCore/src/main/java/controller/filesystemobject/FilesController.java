package controller.filesystemobject;

public class FilesController implements FSOController{
    @Override
    public boolean upload(String targetPath, String uploadPath) {
        return false;
    }

    @Override
    public boolean download(String targetPath, String downloadPath) {
        return false;
    }

    @Override
    public boolean create(String name, String path) {
        return false;
    }

    @Override
    public boolean delete(String path) {
        return false;
    }

    @Override
    public boolean move(String targetPath, String movePath) {
        return false;
    }

    @Override
    public boolean rename(String path, String name) {
        return false;
    }
}
