/**
 * 
 */
package de.xwic.appkit.cluster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import de.xwic.appkit.cluster.comm.Message;
import de.xwic.appkit.cluster.comm.Response;

/**
 * @author lippisch
 *
 */
public class TestClient {

	private ObjectOutputStream out;
	private ObjectInputStream in;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new TestClient().start();

	}
	
	public void start () {
		
		try {
			
			Socket s = new Socket("localhost", 11150);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			
			
			
			
			
			System.out.println("Enter commands or 'exit' to exit.");
			BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));
			String line;
			try {
				while (true) {
					System.out.println();
					System.out.print(":");
					line = bis.readLine();
					
					if (line.equals("exit")) {
						break;
					}
					
					Message msg;
					int idx = line.indexOf(' ');
					if (idx != -1) {
						String cmd = line.substring(0, idx);
						String arg = line.substring(idx + 1);
						msg = new Message(cmd, arg);
					} else {
						msg = new Message(line);
					}

					System.out.println("Sending " + msg);
					out.writeObject(msg);
					Response response = (Response)in.readObject();
					
					System.out.println(">> " + response);
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			out.close();
			in.close();
			s.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
