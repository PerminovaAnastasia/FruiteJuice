/**
 * Created by Anastasia on 29.03.2015.
 */
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.util.IntSummaryStatistics;
import java.rmi.server.UID;

public class Message implements JSONAware {
    private String username;
    private String text;
    private int id;
    private boolean edit;
    private boolean delete;
    private String time;
    public Message() {
        username="";
        text="";
        id=0;
        edit=false;
        delete=false;
        time = "";
    }
    public static Message parse(Object obj){
        Message temp=new Message();
        temp.username=(String)((JSONObject)obj).get("username");
        temp.text=(String)((JSONObject)obj).get("text");
        temp.id= Integer.parseInt((((JSONObject) obj).get("id")).toString());
        temp.edit=(Boolean)(((JSONObject) obj).get("edit"));
        temp.delete=(Boolean)(((JSONObject) obj).get("delete"));
        temp.time = (String)((JSONObject)obj).get("time");
        return temp;
    }
    @Override
    public String toJSONString(){
        JSONObject obj = new JSONObject();
        obj.put("username", username);
        obj.put("text", text);
        obj.put("id", id);
        obj.put("edit", edit);
        obj.put("delete", delete);
        obj.put("time", time);
        return obj.toString();
    }
    public String toString(){
        return username+" : "+text;
    }
    public void setId(int idd){
        id=idd;
    }
    public void deleteMessage()
    {
        delete = true;
        text = "The message has been deleted";
        edit = false;
    }
    public boolean isEdit(){
        return edit;
    }
    public boolean isDelete(){
        return delete;
    }
    public int getId(){
        return id;
    }
    public void redactMessage(String mess){
        edit = true;
        text = mess;
    }
    public String getMess(){
        return text;
    }
}