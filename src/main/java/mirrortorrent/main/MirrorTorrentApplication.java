package mirrortorrent.main;

import java.io.FileNotFoundException;
import java.lang.Thread;
import java.text.ParseException;

import org.lavajuno.lucidjson.JsonArray;
import org.lavajuno.lucidjson.JsonObject;

import mirrortorrent.torrents.*;

public class MirrorTorrentApplication {    
    public static void main(String[] args){
        //load mirrors.json
        try{
            JsonObject config = JsonObject.fromFile("configs/mirrors.json");

            Thread torrentScrapeThread = new Thread( new ScrapeTorrents((JsonArray) config.get("torrents")));
            torrentScrapeThread.start();

            Thread syncTorrentsThread = new Thread( new SyncTorrents((JsonObject) config.get("mirrors")));
            syncTorrentsThread.start();
            
        }
        catch(FileNotFoundException | ParseException e){
            e.printStackTrace();
        }
    }
}
