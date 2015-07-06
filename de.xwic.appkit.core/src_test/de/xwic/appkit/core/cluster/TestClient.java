/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
/**
 * 
 */
package de.xwic.appkit.core.cluster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


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

		new TestClient().start(args);

	}
	
	public void start (String[] args) {
		
		try {
			
			int port = 11150;
			if (args.length > 0) {
				port = Integer.parseInt(args[0]);
			}
			Socket s = new Socket("localhost", port);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			
			
			out.writeObject(new Message(Message.CMD_IDENTIFY_CLIENT, "Console"));
			Response res = (Response)in.readObject();
			System.out.println(res);
			
			
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

					long start = System.currentTimeMillis();
					System.out.println("Sending " + msg);
					out.writeObject(msg);
					Response response = (Response)in.readObject();
					
					System.out.println("[" + (System.currentTimeMillis() - start) + "]  >> " + response);
					if (response.getData() != null) {
						System.out.println(response.getData());
					}
					
					
				}
			} catch (IOException e) {
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
