package mirrortorrent.torrents;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.HashSet;

import org.lavajuno.lucidjson.JsonObject;

import io.github.cdimascio.dotenv.Dotenv;

public class SyncTorrents implements Runnable{

    //read torrent folder path from .env file
    private Dotenv dotenv = Dotenv.load();
    private String torrentFolder = dotenv.get("TorrentFolder");

    private JsonObject mirrorConfig;

    public SyncTorrents(JsonObject a){
        this.mirrorConfig = a;
    }

    public void run(){

        for(String name : mirrorConfig.getKeys()){
            JsonObject projectConfig = (JsonObject) mirrorConfig.get(name);
            //skip if there are no torrent folders to check
            if(projectConfig.get("torrents") == null){
                continue;
            }

            String glob = projectConfig.get("torrents").toString();
            System.out.println(glob);

            //Find all torrent files from the given glob
            try{
                System.out.println(GlobSearch(glob + "*.torrents", ""));
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        //test the globsearch function
        try{
            HashSet<String> testSet = GlobSearch("src/main/java/*/*/*.java", "");
            System.out.println(testSet);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    //given a glob string find all the files that match that string
    public HashSet<String> GlobSearch(String glob, String path) throws IOException{
        
        //create an array containing each part of the glob string
        String[] globParts = glob.split("/");
        //create a hashset to collect all the files we find into
        HashSet<String> output = new HashSet<>();
        
        if(globParts.length > 1){
            if(globParts[0].equals("*")){

                //remove the first element from the globParts array
                globParts = Arrays.copyOfRange(globParts, 1, globParts.length);
                //join globparts array back into glob
                glob = String.join("/", globParts);

                //call GlobSearch on every directory in the directory
                File dir = new File(path);
                File[] dirList = dir.listFiles();
                for(File f : dirList){
                    if(f.isDirectory()){
                        output.addAll(GlobSearch(glob , f.toString()));
                    }
                }

            }
            else{
                //add first element of array to path
                path += globParts[0] + "/";

                //check to make sure that the path exists
                File dir = new File(path);
                if(!dir.exists()){
                    return new HashSet<>();
                }

                //remove the first element from the globParts array
                globParts = Arrays.copyOfRange(globParts, 1, globParts.length);
 
                //recursivly call GlobSearch on the cut down array, joined into a string.
                output.addAll(GlobSearch(String.join("/", globParts), path));
            }
            // System.out.println(String.join("/", globParts));
            // System.out.println(path + "\n");
        }
        else{
            //base case
            //create a pathMatcher using the final glob
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:"+ path + "/" + globParts[0]);
            
            //loop over every file in the directory and test if it matches the matcher
            File dir = new File(path);
            File[] fileList = dir.listFiles();
            for(File f : fileList){
                if(f.isFile() && matcher.matches(Path.of(f.toString()))){
                    output.add(f.toString());
                }
            }
        }
        return output;
    }
}
