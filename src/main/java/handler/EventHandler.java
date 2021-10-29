package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.DataAccessException;
import json.*;
import request.RequestException;
import result.EventIDResult;
import result.EventResult;
import service.EventIDService;
import service.EventService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles event requests
 */
public class EventHandler extends Handler implements HttpHandler {
    /**
     * Create new EventHandler object
     */
    public EventHandler() {}

    /**
     * Handle event request
     * @param exchange HTTP exchange containing AuthToken of current logged-in user
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                System.out.println("Event request received");
                Headers reqHeaders = exchange.getRequestHeaders();
                if (!reqHeaders.containsKey("Authorization")) {
                    throw new IOException("Error: Missing auth token");
                }
                String token = reqHeaders.getFirst("Authorization");
                String uri = exchange.getRequestURI().getPath();
                String eventID = parseID(uri);

                if (eventID != null) {
                    EventIDService eventIDService = new EventIDService(DB_PATH);
                    EventIDResult eventIDResult = eventIDService.eventID(token, eventID);

                    Encoder jsonEncoder = new Encoder();
                    String jsonData = jsonEncoder.encodeEventID(eventIDResult);
                    exchange.getRequestBody().close();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, jsonData.length());
                    writeResponseBody(exchange.getResponseBody(), jsonData);
                    exchange.getResponseBody().close();
                }
                else {
                    // Get all events
                    EventService eventService = new EventService(DB_PATH);
                    EventResult eventResult = eventService.event(token);

                    Encoder jsonEncoder = new Encoder();
                    String jsonData = jsonEncoder.encodeEvent(eventResult);
                    exchange.getRequestBody().close();
                    System.out.println("Event process complete");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, jsonData.length());
                    writeResponseBody(exchange.getResponseBody(), jsonData);
                    exchange.getResponseBody().close();
                }
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
                exchange.getRequestBody().close();
                exchange.getResponseBody().close();
                throw new IOException("Error: Invalid HTTP Request");
            }
        }
        catch (EncodeException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            throw new IOException(e.getMessage());
        }
        catch (RequestException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            throw new IOException(e.getMessage());
        }
        finally {
            exchange.getRequestBody().close();
            exchange.getResponseBody().close();
        }
    }
}
