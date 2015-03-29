/**
 * Created by Anastasia on 29.03.2015.
 */
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.util.IntSummaryStatistics;

public class Message implements JSONAware {
    private String username;
    private String text;
    private int ID;
    private boolean edit;
    private boolean delete;
    Message() {
        username="user1";
        text="";
        ID=(int)(Math.random()*1000);
        edit=false;
        delete=false;
    }
    Message(String mess,String name){
        username=name;
        text=mess;
        ID=(int)(Math.random()*1000000);
        edit=false;
        delete=false;
    }
    public static Message parse(Object obj){
        Message temp=new Message();
        temp.username=(String)((JSONObject)obj).get("UserName");
        temp.text=(String)((JSONObject)obj).get("Text");
        temp.ID= Integer.parseInt((((JSONObject) obj).get("ID")).toString());
        temp.edit=(Boolean)(((JSONObject) obj).get("Edit"));
        temp.delete=(Boolean)(((JSONObject) obj).get("Delete"));
        return temp;
    }
    @Override
    public String toJSONString(){
        JSONObject obj = new JSONObject();
        obj.put("UserName", username);
        obj.put("Text", text);
        obj.put("ID", ID);
        obj.put("Edit", edit);
        obj.put("Delete", delete);
        return obj.toString();
    }
    public String toString(){
        return username+" : "+text;
    }
    public void setId(int idd){
        ID=idd;
    }
    public void deleteMessage()
    {
        delete = true;
        text = "The message has been deleted";
        edit = false;
    }
    public boolean getEdit(){
        return edit;
    }
    public boolean getDelete(){
        return delete;
    }
    public int getId(){
        return ID;
    }
    public void redactMessage(String mess){
        edit = true;
        text = mess;
    }
    public String getMess(){
        return text;
    }
}