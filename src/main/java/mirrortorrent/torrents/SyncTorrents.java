package mirrortorrent.torrents;

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
        // Collection<String> projectList = mirrorConfig.getKeys();
        // System.out.println(projectList);

        for(String name : mirrorConfig.getKeys()){
            JsonObject projectConfig = (JsonObject) mirrorConfig.get(name);
            //skip if there are no torrent folders to check
            if(projectConfig.get("torrents") == null){
                continue;
            }

            System.out.println(projectConfig.get("torrents"));

            //TODO: Find all torrent files from the directory

        }

    }
}
