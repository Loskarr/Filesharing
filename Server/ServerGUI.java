
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.util.Scanner;
class indexServer{
	public ServerSocket serversocket;
	public int port;
	
	public indexServer()throws IOException{
		port = 8010;
		serversocket = new ServerSocket(port);
		
	}
	
	public indexServer(int port)throws IOException{
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

class ServerGUI extends JFrame{
	private JList<String> notificationList;
    private DefaultListModel<String> notificationListModel;
    private JList<String> noteList; // notification list of notification panel
    private static DefaultListModel<String> noteListModel; // notification list model of notification panel
   
    private JTextField peerNameTextField;
    private JTextField terminalTextInput;
    private JTextArea terminalTextOutput;

    private JButton pingButton;
    private JButton discoverButton;
	private JButton clearNotificationPanel;
    private JButton clearCommandPanel;
    private JButton refresh_Userlist;

    private JTable userListTable;
	
	private Boolean isRunning = true;
    method serverfunction = new method(this);
    
    public ServerGUI() {
		ImageIcon img = new ImageIcon("hcmut.png");
        ImageIcon icon_Notification = new ImageIcon("NoteIcon.jpg");
        ImageIcon icon_Command = new ImageIcon("Command.jpg");
        ImageIcon icon_UserIcon = new ImageIcon("User.jpg");

// Resize the icons
        Image image_Notification = icon_Notification.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        Image image_Command = icon_Command.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        Image image_UserIcon = icon_UserIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);

// Create new ImageIcon objects with the resized images
        ImageIcon resizedIcon_Notification = new ImageIcon(image_Notification);
        ImageIcon resizedIcon_Command = new ImageIcon(image_Command);
        ImageIcon resizedIcon_UserIcon = new ImageIcon(image_UserIcon);

// Use the resized icons in your code
// ...
        setIconImage(img.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("BK File Sharing");
         setSize(1000, 1000);
        JTabbedPane tabbedPane = new JTabbedPane();

        //Notification Panel handle
        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new BorderLayout());
        noteListModel = new DefaultListModel<>();
        noteList = new JList<>(noteListModel);
        JScrollPane noteScrollPane = new JScrollPane(noteList);
        notificationPanel.add(noteScrollPane, BorderLayout.CENTER);
        clearNotificationPanel = new JButton("Clear");
        clearNotificationPanel.setBackground(Color.CYAN);
        notificationPanel.add(clearNotificationPanel, BorderLayout.SOUTH);
        clearNotificationPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noteListModel.clear();
            }
        });


        //command panel handle
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new BorderLayout());

        notificationListModel = new DefaultListModel<>();
        notificationList = new JList<>(notificationListModel);
        JScrollPane notificationScrollPane = new JScrollPane(notificationList);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel peerNameLabel = new JLabel("Enter peer name:");
        peerNameTextField = new JTextField(10);
        pingButton = new JButton("Ping");
        pingButton.setBackground(Color.green);
        discoverButton = new JButton("Discover");
        discoverButton.setBackground(Color.yellow);
        clearCommandPanel = new JButton("Clear");
        clearCommandPanel.setBackground(Color.cyan);
        inputPanel.add(peerNameLabel);
        inputPanel.add(peerNameTextField);
        inputPanel.add(pingButton);
        inputPanel.add(discoverButton);
        inputPanel.add(clearCommandPanel);

        commandPanel.add(notificationScrollPane, BorderLayout.CENTER);
        commandPanel.add(inputPanel, BorderLayout.SOUTH);

    pingButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String peerName = peerNameTextField.getText();
        if (peerName != null && !peerName.isEmpty()) {
            boolean isUserExist = false;
            try {
                File file = new File("User.txt");
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains(peerName)) {
                        isUserExist = true;
                        break;
                    }
                }
                scanner.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            if(isUserExist == false){
                String notification = "Peer " + peerName + " is not registered.";
                notificationListModel.addElement(notification);
                return;
            }
            // Read the IP address from the User.txt file
            String termIpAddress = null;
            try {
                File file = new File("User.txt");
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains(peerName)) {
                        termIpAddress = line.split(" ")[1];
                        break;
                    }
                }
                scanner.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            String ipAddress = serverfunction.toIP.get(peerName);
            if (ipAddress == null) {
                String notification = "Peer " + peerName + " at IP "+termIpAddress+" is not online.";
                notificationListModel.addElement(notification);
                return;
            }
            int defaultPingPort = 8510;
            boolean isOnline = serverfunction.pingPeer(ipAddress, defaultPingPort);
            if (isOnline == true) {
                String notification = "Peer " + peerName + " at IP " + ipAddress + " is online.";
                notificationListModel.addElement(notification);
            } else {
                String notification = "Peer " + peerName + " at IP " + ipAddress + " is offline.";
                notificationListModel.addElement(notification);
            }
        } else {
            // Handle the case when the peerNameTextField is empty or null
            
        }
        peerNameTextField.setText("");
        }
    });

    discoverButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String peerName = peerNameTextField.getText();
            ArrayList<String> fileList = serverfunction.discoverFileList(peerName);
            if(fileList.size() == 0){
                String notification = "Peer " + peerName + " has no files.";
                notificationListModel.addElement(notification);
                peerNameTextField.setText("");
                return;
            }
            String notification = "Peer " + peerName + " has the following files: ";
            notificationListModel.addElement(notification);
            for (String file : fileList) {
                notificationListModel.addElement(file);
            }
            peerNameTextField.setText("");
        }
    });
    clearCommandPanel.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            notificationListModel.clear();
        }
    });
        //Userlist Panel handle
        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BorderLayout());
        refresh_Userlist = new JButton("Refresh");
        refresh_Userlist.setBackground(Color.CYAN);
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Name", "IP Address"}, 0);
        userListTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(userListTable);
        userListPanel.add(tableScrollPane, BorderLayout.CENTER);
        userListPanel.add(refresh_Userlist, BorderLayout.SOUTH);
        refresh_Userlist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                // for (String name : serverfunction.toIP.keySet()) {
                //     String ipAddress = serverfunction.toIP.get(name);
                //     tableModel.addRow(new Object[]{name, ipAddress});
                // }
                // Get the user list from User.txt
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("User.txt"));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(" ");
                        String name = parts[0];
                        String ipAddress = parts[1];
                        tableModel.addRow(new Object[]{name, ipAddress});
                    }
                    reader.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        JPanel terminalPanel = new JPanel();
        terminalPanel.setLayout(new BorderLayout());
        terminalTextOutput = new JTextArea();
        terminalTextOutput.setEditable(false);
        terminalTextInput = new JTextField(50);
        JLabel terminalLabel = new JLabel("Enter command:");
        JPanel termPanel = new JPanel();
        termPanel.setLayout(new FlowLayout());
        termPanel.add(terminalLabel);
        termPanel.add(terminalTextInput);
        terminalPanel.add(termPanel, BorderLayout.NORTH);
        JScrollPane terminalScrollPane = new JScrollPane(terminalTextOutput);
        terminalPanel.add(terminalScrollPane, BorderLayout.CENTER);
        terminalPanel.add(terminalTextOutput, BorderLayout.CENTER);
        terminalTextInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = terminalTextInput.getText();
                terminalTextInput.setText("");
                if(command.startsWith("ping")){
                    if (command.length() < 5) {
                        terminalTextOutput.append("@@ Invalid command.\n");
                        return;
                    }
                    String peerName = command.substring(5).trim();
                    
                    //check have the peer name
                    if(peerName.isEmpty()){
                        terminalTextOutput.append(" :)) Peer name cannot be empty, type command again!\n");
                        return;
                    }
                    // check if the user name is already in the user.txt
                    boolean isUserExist = false;
                    try {
                        File file = new File("User.txt");
                        Scanner scanner = new Scanner(file);
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if (line.contains(peerName)) {
                                isUserExist = true;
                                break;
                            }
                        }
                        scanner.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    String ipAddress = serverfunction.toIP.get(peerName);
                    String termIpAddress = null;
                    if ( (isUserExist == false)) {
                        terminalTextOutput.append(" :)) Peer " + peerName + " is not registered.\n");
                        return;
                    }
                    // Read the IP address from the User.txt file
                    try {
                        File file = new File("User.txt");
                        Scanner scanner = new Scanner(file);
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if (line.contains(peerName)) {
                                String[] parts = line.split(" ");
                                termIpAddress = parts[1];
                                // Use the ipAddress variable as needed
                                break;
                            }
                        }
                        scanner.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    if (ipAddress == null) {
                        terminalTextOutput.append(" -> Peer " + peerName + " at IP " + termIpAddress+" is offline.\n");
                        return;
                    }
                    terminalTextOutput.append(" :v Pinging " + peerName + "...\n");
                    int defaultPingPort = 8510;
                    boolean isOnline = serverfunction.pingPeer(ipAddress, defaultPingPort);
                    //sleep for a second
                    if (isOnline == true) {
                        terminalTextOutput.append("-> Peer " + peerName + " at IP " + ipAddress + " is online.\n");
                    } else {
                        terminalTextOutput.append("-> Peer " + peerName + " at IP " + ipAddress + " is offline.\n");
                    }

                }
                else if(command.startsWith("discover")){
                    if (command.length() < 9) {
                        terminalTextOutput.append("@@ Invalid command.\n");
                        return;
                    }
                    String peerName = command.substring(9).trim();
                    if(peerName.isEmpty()){
                        terminalTextOutput.append(" :)) Peer name cannot be empty.\n");
                        return;
                    }
                    terminalTextOutput.append(":v Discovering "+peerName+" ...\n");
                    boolean isUserExist = false;
                    try {
                        File file = new File("User.txt");
                        Scanner scanner = new Scanner(file);
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if (line.contains(peerName)) {
                                isUserExist = true;
                                break;
                            }
                        }
                        scanner.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    if(isUserExist == false){
                        terminalTextOutput.append(" :)) Peer " + peerName + " is not registered.\n");
                        return;
                    }
                    ArrayList<String> fileList = serverfunction.discoverFileList(peerName);
                    if(fileList.size() == 0){
                        terminalTextOutput.append("-> Peer " + peerName + " has no files.\n");
                        return;
                    }
                    terminalTextOutput.append("-> Peer " + peerName + " has the following files: \n");
                    for (String file : fileList) {
                        terminalTextOutput.append("+ "+file + "\n");
                    }
                    
                }
                else if (command.startsWith("clear")){
                    terminalTextOutput.setText("");
                }  
                else {
                    terminalTextOutput.append("Unknown command: " + command + "\n");
                }
            }
        });
        

        tabbedPane.addTab("Notification",resizedIcon_Notification, notificationPanel);
        tabbedPane.addTab("Command", resizedIcon_Command, commandPanel);
        tabbedPane.addTab("User List", resizedIcon_UserIcon, userListPanel);
        tabbedPane.addTab("Terminal", terminalPanel);
        getContentPane().add(tabbedPane);

        pack();
        setVisible(true);
	}
	public void addNotification(String notification) {
        noteListModel.addElement(notification);
    }
	public void service() throws IOException{
		
		ServerSocket serverSocket;
		indexServer indexserver = new indexServer();
		serverSocket = indexserver.serversocket;
		this.addNotification("Server started.");
		try {
			Socket socket = null;
			while (isRunning) {
				socket = serverSocket.accept();
				//System.out.println("New connection accepted!");
				new SThread(socket, serverfunction,this);
				
			}
		} catch (Exception e) {
			if (!(e instanceof SocketException && e.getMessage().equalsIgnoreCase("Socket closed"))) {
				e.printStackTrace();
			}
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	
	public static void main(String[] args)throws IOException{
		new ServerGUI().service();
	}
}