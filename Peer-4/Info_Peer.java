

import java.util.ArrayList;

public class Info_Peer {
	// Store local infomation
	public static class local{
		
		public static int serverPort = 9310;
		public static int clientPort = 8010;
		public static int downloadPort = 10310;
		public static int pingPort = 8510;
		public static String IP = "";	
		public static String serverIP = "";	
		public static String name = "";
		public static String ID = "";
		public static String path = "./Look";
		public static ArrayList<String> fileList = new ArrayList<String>();
	}
	
	// Store destination information
	public static class dest{
		public static ArrayList<String> destList = new ArrayList<String>();
		public static String destination = "127.0.0.1:8010";
		public static ArrayList<String> destPath = new ArrayList<String>();
		public static String path = "./Look";
		public static ArrayList<String> destLname = new ArrayList<String>();
		public static ArrayList<String> destPname = new ArrayList<String>();
	}
	
	// Initialization
	public void initial(){
		Info_Peer.dest.destination = "";
		Info_Peer.dest.destList = new ArrayList<String>();
		Info_Peer.dest.destPath = new ArrayList<String>();
		Info_Peer.dest.destLname= new ArrayList<String>();
		Info_Peer.dest.destPname= new ArrayList<String>();
	}
	
}
