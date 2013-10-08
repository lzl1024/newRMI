package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * This is the httpdownload server handler to make client to
 * download the .class file
 * @author zhuolinl dil1
 *
 */
public class HttpDownloadServer implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    	exchange.getResponseHeaders().set("Content-Type", "text/html;charset=UTF-8");

    	//get the url and ready for response
        String url = exchange.getRequestURI().getPath().substring(1);
        exchange.sendResponseHeaders(200, 0);       
        FileInputStream fs = new FileInputStream(url);
        OutputStream out = exchange.getResponseBody();

        //write files into inputstream
        byte[] buffer = new byte[1024];
        int byteNum = 0;
        while ((byteNum = fs.read(buffer)) != -1) {
        	out.write(buffer, 0, byteNum);
        }

        out.flush();
        out.close();
        
    }
}
