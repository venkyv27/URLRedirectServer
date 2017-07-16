package com.http.server;

import java.sql.SQLException;
import java.util.Random;

public class URLShortener {
    public static final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final int base = alphabet.length();
    public int num;
    //public static final String domain = "http://mini.me";
    public static final String domain = "http://localhost:8080";
   // public static String origURL = "http://yahoo.com";
    //public static String shortURL = null;
    public String origURL = null;
    public int lastAutoIncrId;
    
    //This takes db returned auto increment int, but for now fake it with random number
    
    
    //Take an argument for now, but RESTify it soon
    public static void main (String[] args){
               
        URLShortener tinyURL = new URLShortener();
        tinyURL.origURL = args[0];
        System.out.println("Original URL is " + tinyURL.origURL);
        DatabaseURL dbURL = new DatabaseURL();
        
        try {
            tinyURL.lastAutoIncrId = dbURL.insertIntoDB(tinyURL.origURL);
            System.out.println("Auto incremented id is " + tinyURL.lastAutoIncrId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String encodedValue = encode(tinyURL.lastAutoIncrId);
        String shortURL = tinyURL.shortenURL(tinyURL.origURL, encodedValue);
        try {
            dbURL.setMapping(tinyURL.origURL, shortURL, tinyURL.lastAutoIncrId);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       // String originalURL = dbURL.getMappedURL("http://localhost:8000/LSyC");
        //System.out.println("Original URL as shown in URLShortener is " + originalURL);
        tinyURL.decode(encodedValue);
        
    }
  
    
    
    
    public String shortenURL(String URL, String encodedValue){   
        String shortURL = domain + "/" + encodedValue;
        System.out.println("Short URL to test is " + shortURL);
        return shortURL;
    }
    
       
    public static String encode(int num) {
        StringBuilder sb = new StringBuilder();
        while ( num > 0 ) {
            sb.append( alphabet.charAt( num % base ) );
            num /= base;
        }
        String str = sb.reverse().toString(); 
        System.out.println("The generated shortURL is " + str);
        return str;
    }

    
    public int decode(String URL){
        int num = 0;
        for ( int i = 0; i < URL.length(); i++ ){
            num = num * base + alphabet.indexOf(URL.charAt(i));
        }
        System.out.println("The decoded original int is " + num);
        return num;
    }    
        }

