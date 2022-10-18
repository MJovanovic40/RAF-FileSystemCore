package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Configuration {

    /*
        Example .configuration file:
        size=10000000
        forbiddenExtensions=exe,pdf
        maximumNumberOfFiles=100
     */

    private int storageSize;
    private List<String> forbiddenExtensions;
    private int maximumNumberOfFiles;

    public Configuration(){
        this.storageSize = 100000000;
        this.forbiddenExtensions = new ArrayList<String>();
        this.forbiddenExtensions.add("exe");
        this.maximumNumberOfFiles = 100;
    }

    public Configuration(int storageSize, List<String> forbiddenExtensions, int maximumNumberOfFiles){
        this.storageSize = storageSize;
        this.forbiddenExtensions = forbiddenExtensions;
        this.maximumNumberOfFiles = maximumNumberOfFiles;
    }

    public void load(String path){
        File configurationFile = new File(path + "/.configuration");
        Map<String, String> configFileContents = new HashMap<>();
        if(configurationFile.exists()){
            try{
                Scanner sc = new Scanner(configurationFile);
                while(sc.hasNextLine()){
                    String line = sc.nextLine();
                    String[] lineComponents = line.split("=");
                    configFileContents.put(lineComponents[0], lineComponents[1]);
                }

                List<String> keys = new ArrayList(configFileContents.keySet());

                if(keys.size() < 3){
                    return;
                }

                for(int i = 0; i < keys.size(); i++){
                    switch(keys.get(i)){
                        case "size":
                            System.out.println(configFileContents.get(keys.get(i)));
                            this.storageSize = Integer.parseInt(configFileContents.get(keys.get(i)));
                            break;
                        case "forbiddenExtensions":
                            String[] extensions = configFileContents.get(keys.get(i)).split(",");
                            this.forbiddenExtensions = Arrays.asList(extensions);
                            break;
                        case "maximumNumberOfFiles":
                            this.maximumNumberOfFiles = Integer.parseInt(configFileContents.get(keys.get(i)));
                            break;
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
                return;
            }
        } else{
            try {
                if(configurationFile.createNewFile()){
                    FileWriter fw = new FileWriter(configurationFile);
                    fw.write("size=" + this.storageSize + "\n" +
                            "forbiddenExtensions=exe\n" +
                            "maximumNumberOfFiles=" + this.maximumNumberOfFiles);
                    fw.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public int getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(int storageSize) {
        this.storageSize = storageSize;
    }

    public int getMaximumNumberOfFiles() {
        return maximumNumberOfFiles;
    }

    public void setMaximumNumberOfFiles(int maximumNumberOfFiles) {
        this.maximumNumberOfFiles = maximumNumberOfFiles;
    }

    public List<String> getForbiddenExtensions() {
        return forbiddenExtensions;
    }

    public void setForbiddenExtensions(List<String> forbiddenExtensions) {
        this.forbiddenExtensions = forbiddenExtensions;
    }

    public String toString(){
        return "Storage Size: " + this.storageSize + ", Forbidden Extensions: " + this.forbiddenExtensions.toString() +
                " Maximum number of files: " + this.maximumNumberOfFiles;
    }
    
}
