import java.awt.GridLayout;
import java.io.*;
import java.net.*;

import java.awt.Desktop;
//import java.awt.*;
//import java.awt.event.ActionListener;
//import javax.swing.event.*;
//import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * The application Client
 * 2 TCP port
 * 1 UDP port
 * 
 * @author Group 58
 *
 */
public class ApplicationClient {
private static JTextArea log;
private static File movieF;
private final static String indexFile = "/Users/FrancisParrilla/Desktop/CPS706/Project/index.html";
private final static String videoNum = "2";
private static String hiscinemaURL; 
final static int Hiscinema_PORT = 40575;
final static String hisCinemaAddr= "192.168.0.27";
private static String hercdnIP = "";
private final static int hercdnPort = 40571;
private final static int localDNSPort = 40576;
//private final static String localDNSIP = "10.17.3.95";
private final static String movieFile = "/Users/FrancisParrilla/Desktop/CPS706/Project/herCDNmovie.mp4";

	public static void main(String argv[]) throws Exception
	{
		final int CLIENT_PORT = 40570;
		//final String movie = "1";
	    InetAddress localDNSIP = InetAddress.getByName("192.168.0.27");//local DNS IP Address
		
		JFrame frame = new JFrame("Client");
		log = new JTextArea("Close this window to close the server.");
		log.setEditable(false);
		/*
		JButton btn1 = new JButton("Movie 1");//buttons lets user choose the file tehy want
		btn1.setPreferredSize(new Dimension(40, 40));
		btn1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//movie = "1";
				try{
					webserver("1");
				}
				catch(Exception j){
					//log.append(j);
					System.out.println(j);
				}
			}
		});
		JButton btn2 = new JButton("Movie 2");
		btn2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//movie = "2";
				try{
					webserver("2");
				}
				catch(Exception j){
					//log.append(j);
					System.out.println(j);
				}
			}
		});
		JButton btn3 = new JButton("Movie 3");
		btn3.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//movie = "3";
				try{
					webserver("3");
				}
				catch(Exception j){
					//log.append(j);
					System.out.println(j);
				}
			}
		});
		JButton btn4 = new JButton("Movie 4");
		btn4.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//movie = "4";
				try{
					webserver("4");
				}
				catch(Exception j){
					//log.append(j);
					System.out.println(j);
				}
			}
		});
		JButton btn5 = new JButton("Movie 5");
		btn5.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//movie = "5";
				try{
					webserver("5");
				}
				catch(Exception j){
					//log.append(j);
					System.out.println(j);
				}
				
			}
		});
		*/
		JPanel panel = new JPanel();
		//JPanel btnPanel = new JPanel();
		JScrollPane jsp = new JScrollPane(log);
		panel.setLayout(new GridLayout());
		panel.add(jsp);
		/*
		btnPanel.add(btn1);
		btnPanel.add(btn2);
		btnPanel.add(btn3);
		btnPanel.add(btn4);
		btnPanel.add(btn5);
		*/
		//frame.add(btnPanel);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,800);
		frame.setVisible(true);

	getVideoURL(videoNum);
	log.append("\n www.hiscenima.com returns index.html file\n Source IP: " + hisCinemaAddr + " Source Port#: " + Hiscinema_PORT + "\n Destination IP: " + localDNSIP + " Destination Port#: " + CLIENT_PORT);
    try 
    {
                DatagramSocket client = new DatagramSocket(CLIENT_PORT);//create socket to contact local DNS
            

                byte[] sendbyte = new byte[1024];
                byte[] receivebyte = new byte[1024];
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            
                String video_url = hiscinemaURL;//Set the url to the url received from hiscinema web server
				sendbyte = video_url.getBytes();
				//log.append("\n IS THE ERROR HERE?\n");
				DatagramPacket send = new DatagramPacket(sendbyte,sendbyte.length,localDNSIP,40576); 
				log.append("\nSending url to local DNS..."+"\n");
				client.send(send);
				log.append("request sent to local DNS...IP: " +localDNSIP +" Port: "+localDNSPort+"\n");
                DatagramPacket receive=new DatagramPacket(receivebyte,receivebyte.length);
                client.receive(receive); //Send request and receive response (resolved url) from local dns
				log.append("\nRecieved response from local DNS...\n");
				String herIP=new String(receive.getData());
                log.append("\n" + herIP.trim()+ "Local dummy DNS replies to the client with resolved IP address of content server\n Source Ip: "+ receive.getAddress()+ " Destination IP: "+localDNSIP +"\n Source port: "+receive.getPort()+" Destination Port: "+CLIENT_PORT);
                hercdnIP = herIP.trim(); //Set the hercdn IP that was resolved in local dns
				log.append("\nIP address of herCDN: " + hercdnIP + "\n");
				client.close();
    }
    catch(Exception e)
    {
				System.out.println(e);
				log.append("\nCant send request to local DNS: " + e+"\n");
    }
    log.append("\n Requesting file from herCDN...\n");
    herfile();//request video from herCDN using IP returned by local DNS
    log.append("\n" +"Content server replies with the content. Whole file should be downloaded before playing it"+"\n Source Ip: "+hercdnIP+" Destination IP: "+localDNSIP+" \n Source port: "+ hercdnPort+" Destination Port: "+CLIENT_PORT);

	} 
	/**
	 * Returns the URL for the file from the web server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void getVideoURL(String fileNum) throws UnknownHostException, IOException{ //his cinema web server method
		String getRequest;
		Socket clientSocket = new Socket(hisCinemaAddr, Hiscinema_PORT); 
		log.append("\n Created new socket for hisCinema...\n");
		getRequest = "GET "+indexFile+" HTTP/1.1\r\n"+fileNum+"\r\n";//Get request which asks for index.html and file number
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

		log.append("\nSending file request to hisCinema...\n");
		outToServer.writeBytes(getRequest + '\n');//Send the request to Hiscinema web server
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		hiscinemaURL = inFromServer.readLine();// Read the returned url message from the web server

		log.append("\nhisCinema replies: " + hiscinemaURL + "\n");
		clientSocket.close();//Close the sockets
		outToServer.close();
		inFromServer.close();
	}
	/**
	 * Returns the file from the web server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void herfile() throws UnknownHostException, IOException{//HerCDN web server method
			byte[] b = new byte[1];
	        int bytesRead;
	        String fn = hiscinemaURL.substring(20,22);//get the filename from the url hiscinema sent
	        try {
	       		log.append("Connecting to herCDN...\n");
	        	Socket cs = new Socket( hercdnIP , hercdnPort);
	        	DataOutputStream outToServer =
	    				new DataOutputStream(cs.getOutputStream());
	        	
	    		log.append("\nSending request for video file to herCDN...\n");
	    		outToServer.writeBytes(fn+"");//Send the request to Hiscinema web server
	    	cs.close();
	    	outToServer.close();
				Socket    clientSocket= new Socket( hercdnIP , hercdnPort );
				log.append("\nWaiting for herCDN to stream file...\n");
	    		InputStream      is = clientSocket.getInputStream();//Accept the socket request
	        	
	        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            FileOutputStream fos = null;
	            BufferedOutputStream bos = null;
	          
	                fos = new FileOutputStream(movieFile);//Create the file that will be read into
	                bos = new BufferedOutputStream(fos);
	                bytesRead = is.read(b, 0, b.length);//Read from the input stream

	                do {
	                        baos.write(b);
	                        bytesRead = is.read(b);
	                } while (bytesRead != -1);

	                bos.write(baos.toByteArray());
	                bos.flush();
					bos.close();
					log.append("\n content of requested file written to:" + movieFile + "\n");
	                clientSocket.close();
	              
	          	  is.close();//Close the sockets
					baos.close();
					log.append("\nContent delivery successful!!!!\n");
					/*
					log.append("\n New window will open to play video\n");
					try{
					movieF = new File(movieFile);
					Desktop.open(movieF);
					}
					catch(Exception m){
						System.out.println(m);
					}
					*/
	            } catch (IOException ex) {
	            	 log.append("\n "+ex);
	            
	        }
	
	}
} 

