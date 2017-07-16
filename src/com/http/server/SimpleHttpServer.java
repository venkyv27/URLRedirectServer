package com.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/*
 * a simple static http server
*/
public class SimpleHttpServer{
    
    //public static ArrayList<String> listOfURL = new ArrayList<String>();
    
  public static void main(String[] args) throws Exception {
      
  //    for (int i = 0 ; i < args.length; i++){
 //         listOfURL.add(args[i]);
 //     }
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    //server.createContext("/trial", new MyHandler());
    server.createContext("/", new MyHandler());
    server.setExecutor(null); // creates a default executor
    server.start();
  }

  static class MyHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
      // String stringURL = t.getRequestURI().toString();
        String stringURL = "http://"+t.getRequestHeaders().getFirst("Host") + t.getRequestURI();

       System.out.println("URL passed in is "+ stringURL);
         
        DatabaseURL dbURL = new DatabaseURL();
       // Iterator iter = listOfURL.iterator();
      //  while(iter.hasNext()){
            
        //String origURL = dbURL.getMappedURL("http://localhost:8080/LSyC");
        String origURL = dbURL.getMappedURL(stringURL);
        System.out.println(origURL);
        URI uri = t.getRequestURI();
        String string = uri.toString();
        String reply = "Welcome" + string;
     
     //   t.getResponseHeaders().add("Location", "http://google.com");
        t.getResponseHeaders().add("Location", origURL);
        t.sendResponseHeaders(302, 0);
        t.close();        
    }
  }
}
