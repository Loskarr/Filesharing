


import javax.management.monitor.Monitor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.lang.foreign.AddressLayout;
import java.io.*;
import java.net.*;
import java.nio.file.AccessDeniedException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.table.DefaultTableModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Peer {
	
	/* 
	 *  and register all files in the local file list.
	 */
	private JFrame nframe;
	private JFrame frame;
    private JTable table;
    private static DefaultTableModel tableModel;
    private JList<String> notificationList;
    private static DefaultListModel<String> notificationListModel;
    private JButton ClearButton;

	private JLabel peerNameLabel;
    private JTextField peerNameTextField;
    private JButton loginButton;

	private JButton btnDownload;
	private static DefaultListModel<String> downloadListModel;


    private JButton btnRegister;
    private JButton btnMonitorFile;
    private JButton btnSearchFile;
	private JButton btnSearchFile2;
	private JButton signup;
    private JTextField searchTextField;
	private JTextField searchTextField2;
    private JTextField filenameTextField;
    private JTextField serverFileNameTextField;
	private JTextField signupTextField;
	private JTextField downloadTextField;

	public String peerName;
	//register all files in the local file list.
	public static void Monitor_file(String path, String pname, procedure peerfunction){
		 File file = new File(path);
		// //Get all files in the path
		File[] files = file.listFiles();
		String test[];
		test = file.list();
		for(int i = 0; i<files.length; i++){	
			String fileType = getFileType(files[i]);
			long fileSize = files[i].length();
			long fileSizeKB = fileSize / 1024;
			String fileName = files[i].getName();
			String localFileName = fileName;
			String serverFileName = fileName.substring(0, fileName.lastIndexOf('.'));
			tableModel.addRow(new Object[]{fileName,fileName, fileSizeKB, fileType});
	}
		
		
		if(test.length!=0){
			for(int i = 0; i<test.length; i++){
			Thread_for_register(test[i],test[i],pname,peerfunction);

			}
		}
	}
	//Create a GUI for peer
	public  void initializeGUI(procedure peerfunction) {
		nframe = new JFrame("Peer Application");
		nframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nframe.setTitle("BK File Sharing");
        nframe.setSize(1000, 1000);
		ImageIcon img = new ImageIcon("hcmut.png");
		ImageIcon icon_Notification = new ImageIcon("NoteIcon.jpg");
		ImageIcon uploadIcon = new ImageIcon("Upload.png");
		ImageIcon downloadIcon = new ImageIcon("download.png");
		Image image_Notification = icon_Notification.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		Image uploadImage = uploadIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		Image downloadImage = downloadIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon_Notification = new ImageIcon(image_Notification);
		ImageIcon uploadResizedIcon = new ImageIcon(uploadImage);
		ImageIcon downloadResizedIcon = new ImageIcon(downloadImage);
		nframe.setIconImage(img.getImage());
		
		
		signup = new JButton("Signup");
		signupTextField = new JTextField(20);
        btnRegister = new JButton("Register a file");
        filenameTextField = new JTextField(20);
		Border filenameBorder = BorderFactory.createTitledBorder("Input Server File Name");
		filenameTextField.setBorder(filenameBorder);
		serverFileNameTextField = new JTextField(20);
		Border serverFileNameBorder = BorderFactory.createTitledBorder("Input Server File Name");
		serverFileNameTextField.setBorder(serverFileNameBorder);
		btnMonitorFile = new JButton("Register All Files");
		btnSearchFile = new JButton("Search File");
		searchTextField = new JTextField(20);
		Border searchBorder = BorderFactory.createTitledBorder("Input File Name");
		searchTextField.setBorder(searchBorder);

		btnDownload = new JButton("Download recently searched file");
		downloadTextField = new JTextField(20);
		Border downloadBorder = BorderFactory.createTitledBorder("Search File Name to Download");
		downloadTextField.setBorder(downloadBorder);
		btnSearchFile2 = new JButton("Search File to Download");
		searchTextField2 = new JTextField(20);

        JTabbedPane tabbedPane = new JTabbedPane();
		JPanel loginPanel = new JPanel(new BorderLayout());
        peerNameLabel = new JLabel("Peer Name: ");
        peerNameTextField = new JTextField(20);
        loginButton = new JButton("Login");
		loginPanel.add(peerNameLabel, BorderLayout.WEST);
        loginPanel.add(peerNameTextField, BorderLayout.CENTER);
        loginPanel.add(loginButton, BorderLayout.SOUTH);

		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String downloadFileName = searchTextField2.getText();
				if( downloadFileName.isEmpty() ) {
					return;
				}
				else {
					download(downloadFileName, peerfunction, 0);
				
				}
				searchTextField2.setText("");
			}
		});
		btnMonitorFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Get Info_Peer.local.path
				// How can i get the path from Info_Peer.local.path?
				// 				//String path = filenameTextField.getText(); // Assume this is the path to monitor
				try {
					Monitor_file(Info_Peer.local.path, peerName, peerfunction); // Assume peerName is your peer's name
					
				} catch (Exception ex) {
				}
			}
		});
		btnRegister.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String localFileName="";
				String serverFileName="";
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("./"));
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String fileType = getFileType(file);
                    long fileSize = file.length();
                    long fileSizeKB = fileSize / 1024;
                    String fileName = file.getName();
					localFileName = fileName;
					//Server file name is local file name without extension
					//if serverfilenametextfield is empty, server file name is local file name without extension else server file name is serverfilenametextfield
					if(serverFileNameTextField.getText().isEmpty()){
					serverFileName = fileName.substring(0, fileName.lastIndexOf('.'));}
					else{serverFileName = serverFileNameTextField.getText();}
                    // Add file details to the table
                    tableModel.addRow(new Object[]{fileName,serverFileName, fileSizeKB, fileType});}

				// String localFileName = filenameTextField.getText();
				// String serverFileName = serverFileNameTextField.getText();
				//Get localFileName and serverFileName from the FileChooser Example file 40.txt has localFileName = 40.txt and serverFileName = 40					 
						serverFileNameTextField.setText("");
						filenameTextField.setText("");
				try {
					Publish_file(localFileName, serverFileName, peerName, peerfunction); // Assume peerName is your peer's name
				} catch (Exception ex) {
				}
			}
		});
		btnSearchFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//catch the fault here if searchTextField.getText() is empty

				String searchFileName = searchTextField.getText();
				if( searchFileName.isEmpty() ) {
					return;
				}
				else {
				boolean found = searchThread(searchFileName, peerfunction);
				if (found) {
				} else { addNotification(searchFileName + " is not found");
				}}
				
			}
		});
		btnSearchFile2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//catch the fault here if searchTextField.getText() is empty


				String searchFileName = searchTextField2.getText();
				if( searchFileName.isEmpty() ) {
					return;
				}
				else {
				boolean found = searchThread(searchFileName, peerfunction);
				if (found) {
				} else { addNotification(searchFileName + " is not found");
				}}
				
			}
		});
		JPanel UploadPanel = new JPanel();
        UploadPanel.setLayout(new BorderLayout());
        // Create table with columns: Type, Size, Name, and Tick
		String[] columnNames = {"Filename","Serverfilename", "Size(kB)", "Type"};
        
        
        tableModel = new DefaultTableModel(columnNames, 0);

        table = new JTable(tableModel);

    
        // Create button for file upload
       // JButton uploadButton = new JButton("Upload File");
        // uploadButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         JFileChooser fileChooser = new JFileChooser();
        //         int result = fileChooser.showOpenDialog(frame);
        //         if (result == JFileChooser.APPROVE_OPTION) {
        //             File file = fileChooser.getSelectedFile();
        //             String fileType = getFileType(file);
        //             long fileSize = file.length();
        //             long fileSizeKB = fileSize / 1024;
        //             String fileName = file.getName();
    
        //             // Add file details to the table
        //             tableModel.addRow(new Object[]{fileName, fileSizeKB, fileType});
        //         }
        //     }
        // });
		JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
      //  panel.add(uploadButton, BorderLayout.SOUTH);
    
        // Create buttons for "Register" and "Clear"
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for registering the selected files
            }
        });
    
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for clearing the table
                tableModel.setRowCount(0); // Remove all rows from the table
            }
        });
    
        // Create panel to hold the "Register" and "Clear" buttons
		JPanel buttonContainerPanel = new JPanel();
        buttonContainerPanel.setLayout(new BoxLayout(buttonContainerPanel, BoxLayout.Y_AXIS));
		JPanel buttonPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel = new JPanel();
        buttonPanel2.add(clearButton);
		buttonPanel1.add(btnMonitorFile);
		buttonPanel1.add(btnSearchFile);
		buttonPanel1.add(searchTextField);
		buttonPanel2.add(btnRegister);
		//buttonPanel.add(filenameTextField);
		buttonPanel2.add(serverFileNameTextField);
		buttonContainerPanel.add(buttonPanel1);
		//buttonContainerPanel.add(buttonPanel);
		buttonContainerPanel.add(buttonPanel2);

		JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonContainerPanel, BorderLayout.SOUTH);
        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new BorderLayout());
        notificationListModel = new DefaultListModel<>();
        notificationList = new JList<>(notificationListModel);
		 JPanel downloadPanel = new JPanel();
		// // Let the button be on the left and the north the layout
		// downloadPanel.setLayout(new BorderLayout());
		// JPanel downloadContainerPanel = new JPanel();
		// downloadContainerPanel.setLayout(new BoxLayout(downloadContainerPanel, BoxLayout.Y_AXIS));
		 downloadPanel.add(searchTextField2, BorderLayout.NORTH);
		 downloadPanel.add(btnSearchFile2);
		 downloadPanel.add(btnDownload, BorderLayout.CENTER);

		// downloadPanel.add(downloadContainerPanel, BorderLayout.CENTER);
		
        JScrollPane noteScrollPane = new JScrollPane(notificationList);
        notificationPanel.add(noteScrollPane, BorderLayout.CENTER);
        ClearButton = new JButton("Clear");
        ClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notificationListModel.clear();
            }
        });
        notificationPanel.add(ClearButton, BorderLayout.SOUTH);

        tabbedPane.addTab("Notification",resizedIcon_Notification ,notificationPanel);
        tabbedPane.addTab("Upload", uploadResizedIcon,mainPanel);
		tabbedPane.addTab("Download", downloadResizedIcon,downloadPanel);
		nframe.getContentPane().add(loginPanel);

		loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for registering the selected files
                peerName = peerNameTextField.getText();
				Thread_for_signup(peerName, peerfunction);
                if (peerName != null && !peerName.isEmpty()) {
                    nframe.remove(loginPanel);
                    nframe.revalidate();
                    nframe.repaint();
                    nframe.setSize(800, 800);
                    nframe.getContentPane().add(tabbedPane);
                }
            }
        });
		nframe.pack();
        nframe.setVisible(true);



	}
	/*
	 *  Register the file to the index server
	 */ public static String getFileType(File file) {
        String name = file.getName();
        int lastIndexOfDot = name.lastIndexOf(".");
        if (lastIndexOfDot != -1 && lastIndexOfDot != name.length() - 1) {
            return name.substring(lastIndexOfDot + 1);
        }
        return "";
    }
	public static void addNotification(String notification) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				notificationListModel.addElement(notification);
			}
		});
		
        // ... Add other components to panel 
	}
	
	
	public static void Publish_file(String fileName, String serverName, String pname, procedure peerfunction){
		File file = new File(Info_Peer.local.path + File.separator + fileName);
		if (!file.exists()) {
			System.out.println("File '" + fileName + "' not exist.");
			return;
		}
		Thread_for_register(fileName, serverName, pname, peerfunction);
		//Add notification like File 14.txt is Do_registered as 14
			}

	public static void Thread_for_register(String fileName, String serverName, String pname, procedure peerfunction){
		Socket socket = null;
		StringBuffer sb = new StringBuffer("register ");
		try{			
			peerSocket peersocket = new peerSocket();
			socket = peersocket.socket;
			BufferedReader br = peersocket.getReader(socket);
			PrintWriter pw = peersocket.getWriter(socket);
			// register to the local file
			
			peerfunction.Do_register(Info_Peer.local.ID, fileName , serverName);
			
			// register to the server end
			// Send register message
			sb.append(pname);
			sb.append(" "+Info_Peer.local.ID);
			sb.append(" "+fileName);
			sb.append(" "+serverName);
			pw.println(sb.toString());

		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally{
			try{
				if(socket!=null){
					socket.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		addNotification("File "+fileName+" is registered as "+serverName) ;	
	}

	public static void Thread_for_signup(String name, procedure peerfunction){
		Socket socket = null;
		StringBuffer sb = new StringBuffer("signup ");
		try{			
			peerSocket peersocket = new peerSocket();
			socket = peersocket.socket;
			BufferedReader br = peersocket.getReader(socket);
			PrintWriter pw = peersocket.getWriter(socket);
			sb.append(name);
			sb.append(" "+Info_Peer.local.IP);
			sb.append(" request");
			sb.append(" connect");
			pw.println(sb.toString());
			System.out.println(br.readLine());

		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally{
			try{
				if(socket!=null){
					socket.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}


	public void do_it(procedure peerfunction)throws IOException{
		
		boolean exit = false;
		// Store file name
		String fileName = null;
		String serverName = null;
		String pname = null;
		String pass = null;
		
		BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter peer name:");
		pname = localReader.readLine();
		//System.out.println(Info_Peer.local.name+" "+Info_Peer.local.IP);
		Thread_for_signup(pname, peerfunction);
		//Usage Interface
		while(!exit)
		{
			System.out.println("\n1 Register all file\n2 Search a file\n3 Register a file\n4 Exit");
			
			switch(Integer.parseInt(localReader.readLine()))
			{
			case 1:
				{	
					 Monitor_file(Info_Peer.local.path,pname,peerfunction);				
						
				break;					
				}
			
			case 2:
				{					
					boolean find;
					System.out.println("Enter the file name:");
					fileName = localReader.readLine();
					// Search file through index server
					find = searchThread(fileName, peerfunction);
					
					if(find){
						System.out.println("\n1 Download the file\n2 Cancel and back");
						switch(Integer.parseInt(localReader.readLine())){
						case 1:					
							{
								System.out.println("\nChoose source number (0-...)");
								int pn = Integer.parseInt(localReader.readLine());
								download(fileName, peerfunction, pn);					
								break;
							}
						default:
							break;			
						}
					}
					break;
				}
			
			case 3:
				{
					System.out.println("Enter the local file name:");
					fileName = localReader.readLine();
					// Search file through index server
					System.out.println("Enter the server file name:");
					serverName = localReader.readLine();
					Publish_file( fileName, serverName, pname, peerfunction);
					break;
				}
			case 4:
				{
					exit = true;
					System.exit(0);
					break;
				}
			default:
				break;
			}
			
		}	
	}
	


	public static void Thread_for_unregister(String fileName, procedure peerfunction){
		Socket socket = null;
		StringBuffer sb = new StringBuffer("unregister ");
		try{			
			peerSocket peersocket = new peerSocket();
			socket = peersocket.socket;
			BufferedReader br = peersocket.getReader(socket);
			PrintWriter pw = peersocket.getWriter(socket);
			// Unregister to the local file
			
			peerfunction.unDo_register(Info_Peer.local.ID, fileName);
			
			// Unregister to the server end
			// Send unregister message
			sb.append("at");
			sb.append(" "+Info_Peer.local.ID);
			sb.append(" "+fileName);
			
			pw.println(sb.toString());

		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if(socket!=null){
					socket.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	
	public boolean searchThread(String fileName, procedure peerfunction){
		
		Info_Peer peerinfo = new Info_Peer();
		peerinfo.initial();
		Socket socket = null;
		StringBuffer sb = new StringBuffer("search ");
		boolean find = false;
		// Store file ID
		String findID = null;
		
		try{			
			peerSocket peersocket = new peerSocket();
			socket = peersocket.socket;
			BufferedReader br = peersocket.getReader(socket);
			PrintWriter pw = peersocket.getWriter(socket);
			
			// Send search message
			sb.append("request");
			sb.append(" "+Info_Peer.local.ID);
			sb.append(" "+fileName);
			pw.println(sb.toString());
			
			// Get peer list
			while(!("bye".equals(findID = br.readLine()))){
				Info_Peer.dest.destList.add(findID);							
			}
			
			// If find file in some peers, output their address
			if((find = peerfunction.search(fileName))== true){
				for(int i=0; i<Info_Peer.dest.destPath.size(); i++){
					System.out.println(Info_Peer.dest.destLname.get(i) +" with name "+ fileName+" is found on "+Info_Peer.dest.destPath.get(i)+" ("+i+")");
				   addNotification(Info_Peer.dest.destLname.get(i) +" with name "+ fileName+" is found on "+Info_Peer.dest.destPath.get(i)+" ("+i+")");				}
			}

		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if(socket!=null){
					socket.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		return find;
		
	}
	
	/*
	 *  Used to download file from other clients
	 */
	public void download(String fileName, procedure peerfunction, int pn){
		String IP = null;
		String folder = null;
		int port = 0;
		int serverPort = 0;
		
		String address = Info_Peer.dest.destPath.get(pn);
		
		String[] info = address.split("\\:");
		IP = info[0];
		port = Integer.parseInt(info[1]);
		folder = info[2];
		/*
		 *  Set up serverPort
		 */
		serverPort = Info_Peer.local.downloadPort;
		// Set up a server socket to receive file
		new DThread(serverPort,Info_Peer.dest.destLname.get(pn));
		
		// Set up a socket connection to the peer destination
		Socket socket = null;
		
		StringBuffer sb = new StringBuffer("download ");
		try{			
			peerSocket peersocket = new peerSocket(IP, port);
			socket = peersocket.socket;
			
			BufferedReader br = peersocket.getReader(socket);
			PrintWriter pw = peersocket.getWriter(socket);
						
			// Send download message
			sb.append(Info_Peer.dest.destLname.get(pn));
			sb.append(" " + serverPort);
			sb.append(" " + Info_Peer.local.IP);
			pw.println(sb.toString());
   
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if(socket!=null){
					socket.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	
	}
	
	
	
	public static void main(String args[])throws IOException{

		SwingUtilities.invokeLater(new Runnable() {

            public void run() {
				try {

	    procedure peerfunction = new procedure();
	    peerfunction.initialize();
	 // Monitor_file(Info_Peer.local.path,peerfunction);
	 Peer peerInstance = new Peer();
	 peerInstance.initializeGUI(peerfunction);
	    ServerSocket server = null;
		PingClient pingclient = new PingClient(Info_Peer.local.pingPort);
		pingclient.start();
		WrThread wrThread = new WrThread(Info_Peer.local.path, peerfunction);
		wrThread.start();
	    try{
	    	server = new ServerSocket(Info_Peer.local.serverPort);
	    	System.out.println("\n Peer  started!");
//	    	System.out.println(server);
	    	new PThread(server);
	    }catch(IOException e){
	    	e.printStackTrace();
	    }
		//new Peer().do_it(peerfunction);
	} catch (Exception e) {
		System.err.println("Error setting up the server: " + e.getMessage());
		e.printStackTrace();
		System.exit(1); // Thoát nếu không thể start server
	}
 
}
});

	}
}

/*
 *   Used to receive file from file client
 *   Step 1. Set up a server socket
 *   Step 2. Waiting for input data 
 */
class DThread extends Thread{
	int port;
	String fileName;
	public DThread(int port,String fileName){
		this.port = port;
		this.fileName = fileName;
		start();
	}
	
	public void run(){
		try {
			ServerSocket server = new ServerSocket(port);
			//while(true){
				Socket socket = server.accept();  
                receiveFile(socket,fileName);  
                socket.close();
                server.close();
			//}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void receiveFile(Socket socket, String fileName) throws IOException{
		byte[] inputByte = null;  
        int length = 0;  
        DataInputStream dis = null;  
        FileOutputStream fos = null;  
        String filePath = "./Look/" + fileName;  
        try {  
            try {  
                dis = new DataInputStream(socket.getInputStream());  
                File f = new File("./Look");  
                if(!f.exists()){  
                    f.mkdir();    
                }  

                fos = new FileOutputStream(new File(filePath));      
                inputByte = new byte[1024];     
                System.out.println("\nStart receiving..."); 
                System.out.println("display file " + fileName);
				//Print to GUI
				Peer.addNotification("Start receiving file " + fileName);
				Peer.addNotification("display file " + fileName);

                while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {  
                    fos.write(inputByte, 0, length);  
                    fos.flush();      
                }  
                System.out.println("Finish receive:"+filePath);  
				Peer.addNotification("Finish receive:"+filePath); 
            } finally {  
                if (fos != null)  
                    fos.close();  
                if (dis != null)  
                    dis.close();  
                if (socket != null)  
                    socket.close();   
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
}


class PingClient extends Thread {
    private int port;

    public PingClient(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Received ping request from Server");

                    // Handle the ping request here (e.g., respond or perform some action)
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/*
 *  Watch file
 *  Listening to the local file folder
 *  When there is a change, register or 
 *  unregister file in the local list.
 */
class WrThread extends Thread {
	String path = null;
	procedure peerfunction = null;

	public WrThread(String path,procedure peerfunction){
		this.path = path;
		this.peerfunction = peerfunction;
	}
	
	public void run(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(Info_Peer.local.fileList.size()!=0){
					for(int i = 0; i < Info_Peer.local.fileList.size(); i++){
						File file = new File(path + File.separator +
								Info_Peer.local.fileList.get(i));
						if(!file.exists()){
							System.out.println(Info_Peer.local.fileList.get(i)+" was removed!");
							Peer.Thread_for_unregister(Info_Peer.local.fileList.get(i),peerfunction);
							
						}
					}
				}
			}
			
		}, 2000, 200);
		     
	}
}
class peerServer{
	public ServerSocket serversocket;
	public int port;
	
	public peerServer()throws IOException{
		port = Info_Peer.local.serverPort;
		serversocket = new ServerSocket(port);
	}
	
	public peerServer(int port)throws IOException{
		this.port = port;
		serversocket = new ServerSocket(port);
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

class peerSocket{
    
	public Socket socket=null;
	private procedure pf = new procedure();

	public peerSocket()throws IOException{
		pf.initialize();
		socket = new Socket(Info_Peer.local.serverIP,Info_Peer.local.clientPort);
	}
	
	public peerSocket(String IP, int port)throws IOException{
		pf.initialize();
		Info_Peer.local.clientPort = port;
		socket = new Socket(IP,Info_Peer.local.clientPort);
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

