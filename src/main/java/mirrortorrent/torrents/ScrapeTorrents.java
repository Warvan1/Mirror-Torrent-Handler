package mirrortorrent.torrents;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.helper.ValidationException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapeTorrents {
    
    private static final int BUFFER_SIZE = 4096;

    public static HashSet<String> scrapeLinksDepth(String url, String filterSuffix, Vector<String> ignoreList, int depth){
        //if depth is 1 return the output for the url from scrapeLinks
        if(depth == 1){
            return scrapeLinks(url, filterSuffix, ignoreList);
        }

        //if depth is NOT 1 we have to search all pages until given depth
        HashSet<String> outputLinks = new HashSet<>();
        HashSet<String> currientLinks = new HashSet<>();
        currientLinks.add(url);

        for(int i = depth; i >= 1; i--){
            //base case where depth is 1
            if(i == 1){
                //retrieve all filtered links on each page and add them to outputLinks
                for(String link : currientLinks){
                    outputLinks.addAll(scrapeLinks(link, filterSuffix, ignoreList));
                }
            }
            else{
                //retrieve all links on each page and add them to new Links vector
                HashSet<String> newLinks = new HashSet<>();
                for(String link : currientLinks){
                    newLinks.addAll(scrapeLinks(link, "", ignoreList));
                }
                //add to outputlinks any links from newLinks that end in filterSuffix
                for(String l : newLinks){
                    if(checkSuffix(l, filterSuffix)){
                        outputLinks.add(l);
                    }
                }
                //replace currientLinks with newLinks
                currientLinks = newLinks;
            }
            System.out.println(currientLinks);
            System.out.println(currientLinks.size());
            System.out.println("--------------");
            System.out.println(outputLinks);
            System.out.println(outputLinks.size());
            System.out.println("--------------");
            System.out.println(i);
            System.out.println("--------------");
            System.out.println("--------------");

        }

        return outputLinks;
    }

    //use jsoup to get the list of links from a given webpage
    public static HashSet<String> scrapeLinks(String url, String filterSuffix, Vector<String> ignoreList){
        HashSet<String> link_set = new HashSet<>();

        try{
            //get the html from the page
            Document doc = Jsoup.connect(url).get();
            //get all the link elements on the page
            Elements links = doc.getElementsByTag("a");
            for (Element link : links){
                //get the href url for each link and add it to the output vector if the link ends in "filterSuffix"
                String l = link.attr("abs:href");
                if(link.text().toLowerCase().equals("parent directory")) continue;
                if(checkSuffix(l, filterSuffix) && !checkIgnoreList(l, filterSuffix, ignoreList)){
                    link_set.add(l);
                }
            }
        }
        catch(IOException | ValidationException e){
            return new HashSet<>();
        }

        return link_set;
    }

    public static boolean checkSuffix(String s, String filterSuffix){
        if(filterSuffix.equals("") || (s.length() > filterSuffix.length() && s.substring(s.length() - filterSuffix.length()).equals(filterSuffix))){
            return true;
        }
        return false;
    }

    public static boolean checkIgnoreList(String s, String filterSuffix, Vector<String> ignoreList){
        for(String l : ignoreList){
            if(s.contains(l) && !s.contains(".torrent") && !s.contains(".mirrorlist")){
                return true;
            }
        }
        return false;
    }

    //download files from a vector of urls into a given file location
    public static void downloadFileList(Vector<String> urls, String folder){
        for(String url : urls){
            //get the filename from the url
            String[] split_url = url.split("/");
            String filename = split_url[split_url.length - 1];

            System.out.println(filename);
            try{
                //download the file using the url, folder and calculated filename
                downloadFile(url, folder, filename);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    //downloads a file from a given url into a given file path and name
    public static void downloadFile(String url, String folder, String filename) throws IOException{
        //check to make sure that the file doesnt already exist
        File f = new File(folder + "/" + filename);
        if(f.exists()){
            return;
        }

        //create the folder if it doesnt exist
        File dir = new File(folder);
        if(!dir.exists()){
            dir.mkdir();
        }

        //open a http connection to the given url 
        HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
        int responseCode = httpConn.getResponseCode();

        //check to make sure that connection is established successfully
        if(responseCode == HttpURLConnection.HTTP_OK){
            //initialize input stream from the http connection
            InputStream inputStream = httpConn.getInputStream();

            //initialize output stream to the file
            FileOutputStream outputStream = new FileOutputStream(folder + "/" + filename);

            //read the bytes of the file from the http input stream and output them to the file output stream
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
        }
        else{
            throw new IOException("Failed to connect to " + url + " over http\nResponse Code: " + responseCode);
        }
    }
}
