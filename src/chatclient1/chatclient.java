package chatclient1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * Class used as client to connect to the server
 * 
 * @author Yan NA
 *
 */

public class chatclient {
	public static void main(String[] args) {

//		Socket socket = null;
		try {
			String thehost=args[0];

			int port;
		
			try
			{
				String[] theport = {args[1],args[2]};
				CommandLineValues values = new CommandLineValues();
				CmdLineParser parser = new CmdLineParser(values);	
				// parse the command line options with the args4j library
				try {
					parser.parseArgument(theport);
				} catch (CmdLineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("The port number is "+values.getPort());
				// print values of the command line options
				port=values.getPort();
			}
			catch (NullPointerException ne)
			{
				port=4444;
			}
			catch (ArrayIndexOutOfBoundsException ae)
			{
				port=4444;
			}
		
//			//connect to a server listening on port 4444 on localhost 
//			socket = new Socket(args[0], port);
//			System.out.println("Client Connected...");
//			
//			clientread cr = new clientread(socket);
//			clientwrite cw = new clientwrite(socket);
//			
//			Thread crt=new Thread (cr);
//			Thread cwt = new Thread (cw);
//			
//			crt.start();
//			cwt.start();
			
			// You can hardcode the values of the JVM variables as follows:
			System.setProperty("javax.net.ssl.trustStore", "zn.keystore");
//			System.setProperty("javax.net.ssl.trustStore", "zn.keystore");
			System.setProperty("javax.net.ssl.trustStorePassword","123456");
			
			// Create SSL socket factory, which creates SSLSocket instances
			SocketFactory factory= SSLSocketFactory.getDefault();
			
			// Use the factory to instantiate SSLSocket
			Socket socket = factory.createSocket(args[0], port);

				System.out.println("Client Connected to " + args[0]+" at port "+port);
				
				clientread cr = new clientread(socket);
				clientwrite cw = new clientwrite(socket);
				
				Thread crt=new Thread (cr);
				Thread cwt = new Thread (cw);
				
				crt.start();
				cwt.start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
