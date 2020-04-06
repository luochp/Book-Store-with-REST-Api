package com.dslab2;
import static spark.Spark.*;
import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.*;

public class CatelogServer {
    public IP ip;
    private IP frontEndIP;
    private IP orderIP;

    private String dataURL;

    private List<Book> bookList;

    public HashMap<Integer, String> querySubject(String subject) throws Exception{
        HashMap<Integer, String> items = new HashMap<>();

        loadCSV();
        ListIterator<Book> list_Iter = bookList.listIterator(0);
        while(list_Iter.hasNext()){
            Book tempBook = list_Iter.next();
            if (tempBook.topic.equals(subject)){
                items.put( tempBook.itemNumber, tempBook.name );
            }
        }
        closeCSV();

        if(items.size() > 0){
            System.out.println("query subject " + subject + " success");
        } else{
            System.out.println("query subject " + subject + " failed");
        }
        return items;
    }

    public String queryItem(int itemNumber) throws Exception{
        loadCSV();
        ListIterator<Book> list_Iter = bookList.listIterator(0);
        Book tempBook = new Book(100,"100","100",100,100);
        while(list_Iter.hasNext()){
            tempBook = list_Iter.next();
            if (tempBook.itemNumber == itemNumber ){
                break;
            }
        }

        closeCSV();

        if ( tempBook.itemNumber == itemNumber ){
            System.out.println("query item " + itemNumber + " success");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson( tempBook );
        } else{
            System.out.println("query item " + itemNumber + " failed");
            return "Book Not Found";
        }
    }


    public Boolean requestItem(int itemNumber) throws Exception{
        Boolean result = false;
        Book tempBook = new Book(100,"100","100",100,100);
        loadCSV();
        ListIterator<Book> list_Iter = bookList.listIterator(0);
        while(list_Iter.hasNext()){
            tempBook = list_Iter.next();
            if ( tempBook.itemNumber == itemNumber && tempBook.stack > 0 ){
                result = true;
                tempBook.stack -= 1;
                break;
            }
        }
        closeCSV();

        if ( result == true ){
            System.out.println("request item " + itemNumber + " success, " + tempBook.stack + " left");
        } else{
            System.out.println("request item " + itemNumber + " fail");
        }

        return result;
    }

    public Boolean reloadItem(int itemNumber, int reloadAmount) throws Exception {
        Boolean result = false;
        Book tempBook = new Book(100,"100","100",100,100);
        loadCSV();
        ListIterator<Book> list_Iter = bookList.listIterator(0);
        while(list_Iter.hasNext()){
            tempBook = list_Iter.next();
            if ( tempBook.itemNumber == itemNumber ){
                result = true;
                tempBook.stack += reloadAmount;
                break;
            }
        }
        closeCSV();

        if ( result == true ){
            System.out.println("reload item " + itemNumber + " " + reloadAmount + " more, " + tempBook.stack + " left");
        } else{
            System.out.println("reload item " + itemNumber + " " + reloadAmount + " fail");
        }

        return result;
    }

    public Boolean updateCost(int itemNumber, int cost) throws Exception{
        Boolean result = false;
        loadCSV();
        ListIterator<Book> list_Iter = bookList.listIterator(0);
        while(list_Iter.hasNext()){
            Book tempBook = list_Iter.next();
            if ( tempBook.itemNumber == itemNumber ){
                result = true;
                tempBook.cost = cost;
                break;
            }
        }
        closeCSV();

        if ( result == true ){
            System.out.println("update item " + itemNumber + " cost to " + cost + " success");
        } else{
            System.out.println("update item " + itemNumber + " cost to " + cost + " fail");
        }

        return result;
    }

    public void loadCSV() throws Exception {
        bookList = new LinkedList<Book>();
        CSVReader reader = new CSVReader(new FileReader(dataURL),',','"',0);
        String[] nextLine;

        while( (nextLine = reader.readNext())!= null ){
            if (nextLine != null){
                bookList.add(new Book( Integer.parseInt(nextLine[0]), nextLine[1], nextLine[2], Integer.parseInt(nextLine[3]), Integer.parseInt(nextLine[4])  ));
            }
        }

        reader.close();
    }

    public void closeCSV() throws Exception{
        CSVWriter writer = new CSVWriter(new FileWriter(dataURL));

        ListIterator<Book> list_Iter = bookList.listIterator(0);
        while(list_Iter.hasNext()){
            Book thisBook = list_Iter.next();
            String [] record = thisBook.toString().split(",");
            writer.writeNext(record);
        }

        writer.close();
    }

    public CatelogServer withSelfIP(String addr){
        ip = new IP().withString(addr);
        return this;
    }

    public CatelogServer withFrontEndIP(String addr){
        frontEndIP = new IP().withString(addr);
        return this;
    }

    public CatelogServer withOrderIP(String addr){
        orderIP = new IP().withString(addr);
        return this;
    }

    public CatelogServer withDataURL(String url){
        dataURL = url;
        return this;
    }

}