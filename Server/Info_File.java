

public class Info_File {
	
	private String peerID;
	private String lName;
	private String fName;
	
	public Info_File(){
		
	}
	
	public Info_File(String peerID, String lName, String fName){
		this.peerID = peerID;
		this.lName = lName;
		this.fName = fName;
	}
	
	public String getID(){
		return peerID;
	}
	
	public String getlName(){
		return lName;
	}
	public String getfName(){
		return fName;
	}
	
	public void setID(String peerID){
		this.peerID = peerID;
	}
	
	public void setName(String lName, String fName){
		this.lName = lName;
		this.fName = fName;
	}

}
