


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class procedure {

	public void initialize(){
		try{
			
			Info_Peer.local.clientPort = 8010;
			Info_Peer.local.serverPort = 9310;
			Info_Peer.local.downloadPort = 10310;
			
			// Set the specific IP address
			Info_Peer.local.IP = "192.168.1.6";
			Info_Peer.local.serverIP = "192.168.1.6";

			// You can keep the following lines to set the hostname and ID
			//Info_Peer.local.name = InetAddress.getByName(Info_Peer.local.IP).getHostName();
			Info_Peer.local.ID = Info_Peer.local.IP + ":" + Info_Peer.local.serverPort;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 *  Register file to local list
	 */
	public void Do_register(String ID, String filename, String servername){
		FileWriter writer = null;

		try{
		
			writer = new FileWriter("./peerLog.txt",true);
			
			Info_Peer.local.fileList.add(filename);
			System.out.println("File "+filename + " is Do_registered as " + servername );
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			writer.write(time + "\t\tFile "+filename + " is Do_registered as "+ servername + "on the index server!\r\n");
			writer.close();		

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 *  UnDo_register file from local list
	 */
	public void unDo_register(String ID, String filename){
		FileWriter writer = null;
		try{
		
			writer = new FileWriter("./peerLog.txt",true);
			Info_Peer.local.fileList.remove(filename);		
			System.out.println("File "+filename + " is unregistered!");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			writer.write(time + "\t\tFile "+filename + " is unregistered on the index server!\r\n");
			writer.close();	

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public boolean search(String filename){
		boolean found = false;
		FileWriter writer = null;
		try{
			writer = new FileWriter("./peerLog.txt",true);
			
			if(Info_Peer.dest.destList.size()!=0){
				
				for(int i=0; i<Info_Peer.dest.destList.size(); i++){
					String destination = Info_Peer.dest.destList.get(i);
					String[] info = destination.split("\\:");
					String IP = info[0];
					String port = info[1];
					String lname = info[2];
					String name = info[3];
					Info_Peer.dest.destination = info[0] +":"+ info[1] + ":Look";
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = df.format(new Date());
					writer.write(time + "\t\tFile "+filename + " is found on Client: "+name+" as " + lname + "!\r\n");
					Info_Peer.dest.destPath.add(Info_Peer.dest.destination);
					Info_Peer.dest.destLname.add(lname);
					Info_Peer.dest.destPname.add(name); 
				}
								
				found = true;
			}else{
				System.out.println("File "+filename + " is not found!");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = df.format(new Date());
				writer.write(time + "\t\tFile "+filename + " is not found!\r\n");
			}
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return found;
	}
	
	public PrintWriter getWriter(Socket socket)throws IOException{
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut, true);
		
	}
	
	public BufferedReader getReader(Socket socket)throws IOException{
		InputStream socketIn = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
		
	}
	
}
