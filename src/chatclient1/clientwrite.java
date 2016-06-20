package chatclient1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Class used in client side represent information on clients' interface (command line).
 * 
 * @author Yan NA
 *
 */

public class clientwrite implements Runnable
{
	private Socket socket =null;
	private boolean connected=false;
	
	public clientwrite (Socket socket)
	{
		this.socket=socket;
		connected=true;
	}

	@Override
	public void run() 
	{
		DataOutputStream out = null;
//		// TODO Auto-generated method stub
		try {
			out = new DataOutputStream(
					socket.getOutputStream());  

			Scanner cmdin = new Scanner(System.in);
			String second = null;
			while (connected) 
			{
				try 
				{
					String msg = cmdin.nextLine();
					String[] splitedMeg = msg.split(" ");
					String first=splitedMeg[0];
					JSONObject obj = new JSONObject();
					if (msg.equals(""))
					{
						obj.put("type","message");
						obj.put("content","");
						out.writeUTF(obj.toJSONString());
						out.flush();

					}
					else if (msg.charAt(0)!='#')
					{
						obj.put("type","message");
						obj.put("content",msg);
						out.writeUTF(obj.toJSONString());
						out.flush();				
						
					}
					else if (first.equals("#identitychange")&&!splitedMeg[1].isEmpty())
					{   
						String format = "[a-zA-Z][a-zA-Z0-9]{2,15}";
						if(splitedMeg[1].matches(format))
						{
						obj.put("type","identitychange");
						obj.put("identity",msg.substring(16));
						out.writeUTF(obj.toJSONString());
						out.flush();
						}
				
					}
					else if (first.equals("#list"))
					{
						obj.put("type","list");
						out.writeUTF(obj.toJSONString());
						out.flush();
					
					}
					else if (first.equals("#createroom")&&!splitedMeg[1].isEmpty())
					{
						String format = "[a-zA-Z][a-zA-Z0-9]{2,31}";
						if(splitedMeg[1].matches(format))
						{
							obj.put("type","createroom");
							obj.put("roomid",splitedMeg[1]);
							out.writeUTF(obj.toJSONString());
							out.flush();
						}		
					}
					else if (first.equals("#join"))
					{
						obj.put("type", "join");
						obj.put("roomid",splitedMeg[1]);
						out.writeUTF(obj.toJSONString());
						out.flush();
					}
					else if (first.equals("#who"))
					{
						obj.put("type", "who");
						obj.put("roomid",splitedMeg[1]);
						out.writeUTF(obj.toJSONString());
						out.flush();
					}
					else if(first.equals("#delete"))
					{
						obj.put("type", "delete");
						obj.put("roomid", splitedMeg[1]);
						out.writeUTF(obj.toJSONString());
						out.flush();
					}
					else if(first.equals("#kick")&&!splitedMeg[1].isEmpty()
							&&!splitedMeg[2].isEmpty()&&!splitedMeg[3].isEmpty())
					{
						obj.put("type", "kick");
						obj.put("roomid", splitedMeg[2]);
						obj.put("time", Integer.parseInt(splitedMeg[3]));
						obj.put("identity",splitedMeg[1]);
						out.writeUTF(obj.toJSONString());
						out.flush();
					}
					else if (first.equals("#quit"))
					{
						obj.put("type", "quit");				
						out.writeUTF(obj.toJSONString());
						out.flush();
						cmdin.close();
						connected=false;
					}
					else if (first.equals("#signup")&&!splitedMeg[1].isEmpty()
							&&!splitedMeg[2].isEmpty())
					{
						String format = "[a-zA-Z][a-zA-Z0-9]{2,16}";
						if(splitedMeg[1].matches(format))
						{
							obj.put("type", "signup");
							obj.put("newidentity",splitedMeg[1]);
							obj.put("password", splitedMeg[2]);
							out.writeUTF(obj.toJSONString());
							out.flush();
						}
					}
					else if (first.equals("#login")&&!splitedMeg[1].isEmpty()
							&&!splitedMeg[2].isEmpty())
					{
						String format = "[a-zA-Z][a-zA-Z0-9]{2,31}";
						String format2 = "[a-zA-Z0-9]{2,31}";
						if(splitedMeg[1].matches(format)&&splitedMeg[2].matches(format2))
						{
							obj.put("type", "login");
							obj.put("identity",splitedMeg[1]);
							obj.put("password", splitedMeg[2]);
							out.writeUTF(obj.toJSONString());
							out.flush();
						}
					}
					else
					{
						continue;
					}
					
					out.flush();
					// forcing TCP to send data immediatel
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					continue;
				}
				
			}		
		}
		catch (IOException e) 
		{
//			try {
//				out.close();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			// TODO Auto-generated catch block
		}

		// Reading from console
		
	}

	
	
}
