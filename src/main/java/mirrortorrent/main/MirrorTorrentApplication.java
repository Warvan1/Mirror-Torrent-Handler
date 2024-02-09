package mirrortorrent.main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import mirrortorrent.torrents.*;

public class MirrorTorrentApplication {    
    public static void main(String[] args){

        // String url = "https://linuxmint.com/torrents/";
        // String url = "https://torrents.artixlinux.org/torrents.php";
        String url = "https://download.documentfoundation.org/libreoffice/stable/"; // depth 5

        String[] ignoreArray = {"apache", "mirrorbrain", "mailto", "?C=N;O=D", "?C=M;O=A", "?C=S;O=A", ".tar.gz", ".msi", ".asc", ".dmg", "exact: https://download.documentfoundation.org/libreoffice/", "exact: https://download.documentfoundation.org/libreoffice/stable/"};
        // String[] ignoreArray = {"apache", "mirrorbrain", "mailto"};
        Vector<String> ignoreList = new Vector<String>(Arrays.asList(ignoreArray));

        HashSet<String> links = ScrapeTorrents.scrapeLinksDepth(url, ".torrent", ignoreList, 5 );
        
        System.out.println(links);

        // ScrapeTorrents.downloadFileList(links, "torrent");

    }
}
