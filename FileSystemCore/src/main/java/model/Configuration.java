package model;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
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
                            this.storageSize = Integer.parseInt(configFileContents.get(keys.get(i)));
                            break;
                        case "forbiddenExtensions":
                            if(configFileContents.get(keys.get(i)).equals("0")){
                                this.forbiddenExtensions = new ArrayList<>();
                                break;
                            }
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
                    StringBuilder extensions = new StringBuilder();
                    for(int i = 0; i < this.forbiddenExtensions.size(); i++){
                        if(i != 0){
                            extensions.append(',');
                        }
                        extensions.append(this.forbiddenExtensions.get(i));
                    }
                    if(extensions.toString().isEmpty()){
                        extensions.append("0");
                    }
                    fw.write("size=" + this.storageSize + "\n" +
                            "forbiddenExtensions=" + extensions + "\n" +
                            "maximumNumberOfFiles=" + this.maximumNumberOfFiles);
                    fw.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public String toString(){
        return "Storage Size: " + this.storageSize + ", Forbidden Extensions: " + this.forbiddenExtensions.toString() +
                " Maximum number of files: " + this.maximumNumberOfFiles;
    }
    
}
