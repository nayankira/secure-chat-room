package chatclient1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class used in client side to parse and response to the JSON data package from the server.
 * 
 * @author Yan NA
 *
 */

public class clientread implements Runnable 
{
	Socket socket=null;
	String clientid;
	private boolean isConnected=false;
	clientread(Socket socketin)
	{
		this.socket=socketin;
		isConnected=true;
	}
	
	public void run() 
	{
		DataInputStream in = null;
		try {
			in = new DataInputStream(socket.getInputStream());
			while (isConnected) 
			{	
				JSONParser parser = new JSONParser();
				String response = in.readUTF();
				JSONObject object = null;
				try {
					object = (JSONObject) parser.parse(response);
//					System.out.println(object); // need to be removed finally
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String type =(String)object.get("type");
				if (type.equals("message"))
				{
					String senderid= (String) object.get("identity");
					String content = (String) object.get("content");
					System.out.println(senderid+": " + content);
				}
				else if(type.equals("newidentity"))
				{
					String formerid = (String) object.get("former");
					if(formerid.equals(""))
					{ 
						String newid= (String) object.get("identity");
						System.out.println("Connected to the localhost as "+ newid);
						clientid=newid;
					}
					else
					{
						String newid= (String) object.get("identity");
						System.out.println(formerid+" is now "+ newid);
						if(formerid.equals(clientid))
						{
							clientid=newid;
						}
					}
					
				}
				else if (type.equals("roomchange"))
				{
					if (((String) object.get("roomid")).equals("")
							&&clientid.equals((String)object.get("identity")))
					{	
						isConnected=false;
						break;
					}
					else if (((String) object.get("roomid")).equals("")
							&&!clientid.equals((String)object.get("identity")))
					{
						System.out.println((String) object.get("identity")+" leaves " 
					+ (String) object.get("former"));
					}
					else if (!(((String) object.get("former")).equals("")))
					{
					System.out.println((String) object.get("identity")+" moves from "
					+ (String) object.get("former") 
							+" to " + (String) object.get("roomid"));
					}
					else 
					{
					System.out.println((String) object.get("identity")+" moves to " 
					+ (String) object.get("roomid"));
					}
				}
				else if(type.equals("roomlist"))
				{
					JSONArray array = (JSONArray) object.get("rooms");
					for (int i = 0; i < array.size(); i++) 
					{
						JSONObject obj = (JSONObject) array.get(i);
						System.out.println(obj.get("roomid")+ ": "+ obj.get("count")+" guests");
					}
				}
				else if(type.equals("roomcontents"))
				{
					String roomid=(String) object.get("roomid");
					String owner=(String) object.get("owner");
					
					JSONArray arrayjs = (JSONArray) object.get("identities");
					ArrayList<String> memberid = new ArrayList<>();
					for (int i = 0; i < arrayjs.size(); i++) 
					{
						memberid.add((String) arrayjs.get(i));
					}
					
					
					int ownerno=memberid.indexOf(owner);
					if (!owner.equals("")&&ownerno!=-1)
					{
						memberid.set(ownerno, owner +"*");
					}
					System.out.print(roomid+ " owned by "+ owner +" contains ");
					for (int i=0; i<memberid.size();i++)
					{
						System.out.print(memberid.get(i) +" ");
					}
					System.out.print("\n");
				}
				else;
			}
			
			System.out.println("Disconnected from the server 1");
			in.close();
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			try {
				in.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			}
			System.out.println("Disconnected from the server 2");
//			e.printStackTrace();
		}
	}
	
	// Preparing sending and receiving streams
				
}
