package mirrortorrent.main;

import java.io.IOException;
import java.util.Vector;

import mirrortorrent.torrents.*;

public class MirrorTorrentApplication {    
    public static void main(String[] args){

        // String url = "https://linuxmint.com/torrents/";
        String url = "https://torrents.artixlinux.org/torrents.php";
        // String url = "https://download.documentfoundation.org/libreoffice/stable/"; // depth 5

        try{
            Vector<String> links = ScrapeTorrents.getLinks(url);
            
            System.out.println(links);

            ScrapeTorrents.downloadFileList(links, "torrent");
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}
