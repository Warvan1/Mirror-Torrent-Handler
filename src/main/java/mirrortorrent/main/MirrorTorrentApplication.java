package mirrortorrent.main;

import java.io.File;
import java.util.HashSet;

import mirrortorrent.torrents.*;

public class MirrorTorrentApplication {    
    public static void main(String[] args){

        // String url = "https://linuxmint.com/torrents/";
        String url = "https://torrents.artixlinux.org/torrents.php";
        String url2 = "https://download.documentfoundation.org/libreoffice/stable/"; // depth 5

        File dir = new File("torrent");
        if(!dir.exists()){
            dir.mkdir();
        }

        HashSet<String> links = ScrapeTorrents.scrapeLinks(url, ".torrent");
        
        System.out.println(links);
        System.out.println(links.size());

        ScrapeTorrents.downloadFileList(links, "torrent/artixLinux");

        HashSet<String> links2 = ScrapeTorrents.scrapeLibreOfficeTorrentLinks(url2, 5);
        
        System.out.println(links2);
        System.out.println(links2.size());

        ScrapeTorrents.downloadFileList(links2, "torrent/libreOffice");

    }
}
