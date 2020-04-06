package com.dslab2;
import static spark.Spark.*;
import java.util.*;
import com.google.gson.*;

public class FrontEndService {

    public FrontEndService(FrontEndServer feServer){
        port(feServer.ip.getPort());


        get("search/:topic", (req, res) -> {
            String topic = req.params(":topic");
            return new Gson().toJson( feServer.search(topic) );
        });

        get("lookup/:itemNumber", (req, res) -> {
            Integer itemNumber = Integer.parseInt(req.params(":itemNumber"));
            return new Gson().toJson( feServer.lookUp(itemNumber) );
        });

        get("buy/:itemNumber", (req, res) -> {
            Integer itemNumber = Integer.parseInt(req.params(":itemNumber"));
            return new Gson().toJson( feServer.buy(itemNumber) );
        });
    }

    public static void main(String[] args) {
        // Check if enter all ip address
        if(args.length < 3){
            System.out.println("Please enter FrontEnd, Catelog and Order IPaddrss:port");
            return;
        }

        FrontEndServer feServer = new FrontEndServer()
                       .withSelfIP(args[0])
                       .withCatelogIP(args[1])
                       .withOrderIP(args[2]);

        new FrontEndService(feServer);
        System.out.println("Front End Service Running");
    }

}