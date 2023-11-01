

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class method {

	private ArrayList<Info_File> registryList = new ArrayList<Info_File>(); 
	public Map<String, String> toIP = new HashMap<>();
//	FileWriter writer = null;
	/*
	 *  Register file on server side
	 */
	public void registery(String peerName, String peerID, String lName, String fName){
		// TODO Auto-generated method stub
		registerThread register = new registerThread(peerName, peerID, lName, fName);
		//String[] parts = peerID.split(":");
		//String ipAddress = parts[0];
		// Map the peerName to the IP address
		//toIP.put(peerName, ipAddress);
		//System.out.println(peerName+" "+ipAddress+"\n");
		Thread thread = new Thread(register);
		thread.start();
		thread = null;
	}

	/*
	 *  registerThread
	 *  Used to implement multiusers to register files at the same time
	 */
	class registerThread implements Runnable{
		private String peerName;
		private String peerID;
		private String lName;
		private String fName;
		
		public registerThread(String peerName, String peerID, String lName, String fName){
			this.peerName = peerName;
			this.peerID = peerID;
			this.lName = lName;
			this.fName = fName;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(registryList.size()==0){
				
				try{
					
					FileWriter writer = new FileWriter("./serverLog.txt",true);
					// Add register file to the registery list
		            registryList.add(new Info_File(peerName,peerID,lName,fName));
					System.out.println("File:"+lName+" from "+"Client:"+peerName+" is registeried as " + fName);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = df.format(new Date());
					writer.write(time + "\t\tFile "+lName + " is registered on the index server as "+ fName +"\r\n");
					writer.close();		

				}catch(Exception e)
				{
					e.printStackTrace();
				}			
				
			}
			else{
				try{
					if(fileNotExist(peerID, lName)){	
						FileWriter writer = new FileWriter("./serverLog.txt",true);
						registryList.add(new Info_File(peerName,peerID,lName, fName));
						System.out.println("File:"+lName+" from "+"Client:"+peerName+" is registried as " + fName);
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time = df.format(new Date());
						writer.write(time + "\t\tFile "+lName + " is registered on the index server as "+ fName +"\r\n");
						writer.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}	
		}
		
	}
	private boolean fileNotExist(String peerID, String lName) {
		// TODO Auto-generated method stub
		for(int i=0;i<registryList.size();i++){
			if(registryList.get(i).getlName().equals(lName)&&
					registryList.get(i).getID().equals(peerID)){
				return false;
			}
		}
		return true;
		
	}
	
	

	/*
	 *  Unregister file on server side
	 */
	public void unregistery(String peerName, String peerID, String lName){
		// TODO Auto-generated method stub
		unregisteryThread unregister = new unregisteryThread(peerName, peerID, lName);
		Thread thread = new Thread(unregister);
		thread.start();
		thread = null;
		
	}

	/*
	 *  unregisterThread
	 *  Used to implement multiusers to unregister files at the same time
	 */
	class unregisteryThread implements Runnable{
		private String peerName;
		private String peerID;
		private String lName;
		
		public unregisteryThread(String peerName, String peerID, String lName){
			this.peerName = peerName;
			this.peerID = peerID;
			this.lName = lName;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			for(int i=0;i<registryList.size();i++){
				try{
					
					if(registryList.get(i).getlName().equals(lName)&&
							registryList.get(i).getID().equals(peerID)){
						FileWriter writer = new FileWriter("./serverLog.txt",true);
						registryList.remove(i);
						System.out.println("File:"+lName+" from "+"Client:"+peerName+" is removed!");
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time = df.format(new Date());
						writer.write(time + "\t\tFile "+lName + " is unregistered on the index server!\r\n");
						writer.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	/*
	 *  Search file on server side
	 */
//	public ArrayList<String> search(String fileName) {
//		//search file ID: IP + port
//		ArrayList<String> peerList = new ArrayList<String>();
//
//		for(int i=0;i<registryList.size();i++){
//			if(registryList.get(i).getName().equals(fileName)){
//				peerList.add(registryList.get(i).getID());
//				
//			}
//		}
//		return peerList;
//	}
	
	public ArrayList<String> search(String fName) {
		//search file ID: IP + port
		ArrayList<String> peerList = new ArrayList<String>();
		
		ExecutorService execPool = Executors.newCachedThreadPool();
		Callable<ArrayList<String>> call = new searchThread(fName);
		Future<ArrayList<String>> result = execPool.submit(call);
		
		try{
			if(result.get().size() != 0){
            	peerList.addAll(result.get());
			}
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(ExecutionException e){
			e.printStackTrace();
		}finally{
			execPool.shutdown();
		}
		
		return peerList;
	}
	
	class searchThread implements Callable<ArrayList<String>>{
		private ArrayList<String> peerList = new ArrayList<String>();
		private String fName;
		
		public searchThread(String fName){
			this.fName = fName;
		}
		@Override
		public ArrayList<String> call() throws Exception {
			// TODO Auto-generated method stub
			for(int i=0;i<registryList.size();i++){
				if(registryList.get(i).getfName().equals(fName)){
					peerList.add(registryList.get(i).getID()+":"+registryList.get(i).getlName());
					//System.out.println(registryList.get(i).getID()+registryList.get(i).getlName()+"\n");
				}
			}
			return peerList;
		}
		
	}

	public ArrayList<String> discoverFileList(String peerName) {
        ArrayList<String> fileList = new ArrayList<String>();

        for (Info_File file : registryList) {
            if (file.getName().equals(peerName)) {
                fileList.add(file.getlName() +" as "+ file.getfName());
            }
        }

        return fileList;
    }

	public boolean pingPeer(String ipAddress, int port) {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(ipAddress, port), 5000);
			if (socket.isConnected()) {
				socket.close();
				return true; // Peer is online
			} else {
				return false; // Peer is offline or unreachable
			}
		} catch (SocketTimeoutException e) {
			return false; // Connection timed out
		} catch (IOException e) {
			e.printStackTrace();
			return false; // Error occurred, treat as offline
		}
	}
}




