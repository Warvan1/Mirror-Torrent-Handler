package mirrortorrent.torrents;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapeTorrents {
    
    private static final int BUFFER_SIZE = 4096;

    //use jsoup to get the list of links from a given webpage
    public static Vector<String> getLinks(String url) throws IOException{
        Vector<String> link_vec = new Vector<>();

        //get the html from the page
        Document doc = Jsoup.connect(url).get();
        //get all the link elements on the page
        Elements links = doc.getElementsByTag("a");
        for (Element link : links){
            //get the href url for each link and add it to the output vector if it ends in .torrent
            String l = link.attr("abs:href");
            if(l.substring(l.length() - 8).equals(".torrent")){
                link_vec.add(l);
            }
        }

        return link_vec;
    }

    //download files from a vector of urls into a given file location
    public static void downloadFileList(Vector<String> urls, String folder){
        //create the folder if it doesnt exist
        File dir = new File(folder);
        if(!dir.exists()){
            dir.mkdir();
        }
        
        for(String url : urls){
            //get the filename from the url
            String[] split_url = url.split("/");
            String filename = split_url[split_url.length - 1];

            System.out.println(filename);
            try{
                //check if file already exists
                File f = new File(folder + "/" + filename);
                if(!f.exists()){
                    //download the file using the url, folder and calculated filename
                    downloadFile(url, folder + "/" + filename);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    //downloads a file from a given url into a given file path and name
    public static void downloadFile(String url, String fileName) throws IOException{
        //open a http connection to the given url 
        HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
        int responseCode = httpConn.getResponseCode();

        //check to make sure that connection is established successfully
        if(responseCode == HttpURLConnection.HTTP_OK){
            //initialize input stream from the http connection
            InputStream inputStream = httpConn.getInputStream();

            //initialize output stream to the file
            FileOutputStream outputStream = new FileOutputStream(fileName);

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
