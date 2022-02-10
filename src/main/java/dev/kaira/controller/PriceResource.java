package dev.kaira.controller;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;


@Path("/service")
public class PriceResource {

    @Inject
    @Channel("price-create")
    Emitter<Double> priceEmitter;


    @POST
    @Path("/prices")
    @Consumes(MediaType.TEXT_PLAIN)
    public void addPrice(Double price) {
        System.out.println("-------------------------------Post data: " + price);
        CompletionStage<Void> ack = priceEmitter.send(price);
    }
}