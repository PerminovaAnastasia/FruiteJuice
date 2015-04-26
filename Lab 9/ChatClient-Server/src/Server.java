import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.UUID;

public class Server implements HttpHandler {
    private List<Message> history = new ArrayList<Message>();
    private MessageExchange messageExchange = new MessageExchange();

    public static void main(String[] args) {
        if (args.length != 1)
            System.out.println("Usage: java Server port");
        else {
            try {
                System.out.println("Server is starting...");
                Integer port = Integer.parseInt(args[0]);
                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                System.out.println("Server started.");
                String serverHost = InetAddress.getLocalHost().getHostAddress();
                System.out.println("Get list of messages: GET http://" + serverHost + ":" + port + "/chat?token={token}");
                System.out.println("Send message: POST http://" + serverHost + ":" + port + "/chat provide body json in format {\"message\" : \"{message}\"} ");

                server.createContext("/chat", new Server());
                server.setExecutor(null);
                server.start();
            } catch (IOException e) {
                System.out.println("Error creating http server: " + e);
            }
        }
    }
//
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";

        if ("GET".equals(httpExchange.getRequestMethod())) {
            response = doGet(httpExchange);
        }
        else if ("POST".equals(httpExchange.getRequestMethod())) {
            doPost(httpExchange);
        }
        else if("DELETE".equals(httpExchange.getRequestMethod()))
        {
            doDelete(httpExchange);
        }
        else if("PUT".equals(httpExchange.getRequestMethod()))
        {
            doPut(httpExchange);
        }
        else if ("OPTIONS".equals(httpExchange.getRequestMethod())) {
            response = "";
        }
        else {
            response = "Unsupported http method: " + httpExchange.getRequestMethod();
        }

        sendResponse(httpExchange, response);
    }

    private String doDelete(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        if (query != null) {
            Map<String, String> map = queryToMap(query);

            String idReceive = map.get("id").toString();
            int index = -1;
            for(int i = 0; i<history.size(); i++)
            {
                if(history.get(i).getId().compareTo(idReceive) == 0)
                {
                    index = i;
                    break;
                }
            }
            if(index!=-1)
            {
                if( history.get(index).isDelete() == false)
                {
                    Message temp = history.get(index);
                    temp.deleteMessage();
                    history.add(temp);
                }
                else{
                    System.out.println("You have just deleted this message");
                }
            }
            else {
                return "Token query parameter is absent in url: " + query;
            }
            //int idReceive = Integer.parseInt(map.get("id"));
            /*if (idReceive < history.size() && idReceive>=0) {

                if(history.get(idReceive).isDelete() == false)
                {
                    Message temp = history.get(idReceive);
                    temp.deleteMessage();
                    history.add(temp);
                }
                else
                    System.out.println("You have just deleted this message");
            } else {
                return "Token query parameter is absent in url: " + query;
            }*/
        }
        return  "Absent query in url";
    }

    private void doPut(HttpExchange httpExchange) {
        try {
            Message message = messageExchange.getClientMessage(httpExchange.getRequestBody());
            String idReceive = message.getId();
            int index = -1;
            for(int i = 0; i<history.size(); i++)
            {
                if(history.get(i).getId().compareTo(idReceive) == 0)
                {
                    index = i;
                    break;
                }
            }
            if(index!=-1)
            {
                if( history.get(index).isDelete() == true)
                {
                    System.out.println("You can not change it.");
                }
                else{
                    Message temp = history.get(index);
                    temp.redactMessage(message.getMess());
                    history.add(history.get(index));
                }
            }
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void doPost(HttpExchange httpExchange) {
        try {
            Message message = messageExchange.getClientMessage(httpExchange.getRequestBody());
            System.out.println("Get Message from User : " + message);
            message.setId(UUID.randomUUID().toString());
            history.add(message);
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private String doGet(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        if (query != null) {
            Map<String, String> map = queryToMap(query);
            String token = map.get("token");
            if (token != null && !"".equals(token)) {
                int index = messageExchange.getIndex(token);
                return messageExchange.getServerResponse(history, index);
            } else {
                return "Token query parameter is absent in url: " + query;
            }
        }
        return  "Absent query in url";
    }

    private void sendResponse(HttpExchange httpExchange, String response) {
        try {
            byte[] bytes = response.getBytes();
            Headers headers = httpExchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin","*");
            ///
            if("OPTIONS".equals(httpExchange.getRequestMethod())) {
                headers.add("Access-Control-Allow-Methods","PUT, DELETE, POST, GET, OPTIONS");
            }
            ///
            httpExchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = httpExchange.getResponseBody();
            os.write( bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
