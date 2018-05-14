

import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 1 TCP port
 * @author Group 58
 *
 */
public class HerCDN   {
	public static final int PORT = 40571;
	 private  static String movieFile = "/Users/FrancisParrilla/Desktop/CPS706/Project/file4.txt";
	 private final static String[] movieFiles = {"/Users/FrancisParrilla/Desktop/CPS706/Project/file1.txt",
												 "/Users/FrancisParrilla/Desktop/CPS706/Project/Bees.mp4",
												 "/Users/FrancisParrilla/Desktop/CPS706/Project/file3.txt",
												 "/Users/FrancisParrilla/Desktop/CPS706/Project/file4.txt",
												 "/Users/FrancisParrilla/Desktop/CPS706/Project/Snakes2.mp4"};
	 JTextArea log;
	  ServerSocket serverSock;
public HerCDN(int port){

	JFrame frame = new JFrame("HerCinema CDN");
	log = new JTextArea("");
	log.setEditable(false);
	JPanel panel = new JPanel();
	JScrollPane jsp = new JScrollPane(log);
	panel.setLayout(new GridLayout());
	panel.add(jsp);
	
	frame.add(panel);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(500,500);
	frame.setVisible(true);
	try {
		serverSock = new ServerSocket(port);//create TCP socket
	} catch (IOException e) {
		log.append(e+"\n");
	}
	
	new Thread(new server()).start();
}
class server implements Runnable {
	
	public void run() {

		 while (true) {
	         try{
			   ///
			   log.append("\n\nWaiting for request from client...\n");
	        	  Socket    cs = serverSock.accept();
	        	
	          
	            InputStream sis =cs.getInputStream();
	           
	            BufferedReader br = new BufferedReader(new InputStreamReader(sis));
				String fileNum = br.readLine(); // Now you get GET index.html HTTP/1.1`
				log.append("\n Client contacts content serve and requests: " +fileNum +"\n");
	           String n="";
	            for(int i= 1; i<6;i++){
	            	
	            	n="F"+i;
	            	if (fileNum.equals(n)){
	            		movieFile = movieFiles[i - 1];//assign the appropriate file from the requested url
	            		 log.append("\n"+ movieFile + "\n");
	            		 //break;
	            	}}
	            sis.close();
	            br.close();
	            cs.close();
	            Socket    connectionSocket = serverSock.accept(); //Accept client request for file
					log.append("\nherCDN accepts client's request...\n");
	            BufferedOutputStream    outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
	         

	            		if (outToClient != null) {
	            			File file = new File(movieFile);
	            				if (!file.exists()) {//Check if file exists
	            					log.append("\n Error 404 - File Not Found!");
	            				}
	                		byte[] mybytearray = new byte[(int) file.length()]; // set array length for file length
           
							FileInputStream fis = new FileInputStream(file);
							BufferedInputStream bis = new BufferedInputStream(fis);
							bis.read(mybytearray, 0, mybytearray.length); //read the file in the input stream
							outToClient.write(mybytearray, 0, mybytearray.length); //output to the clients input stream
							outToClient.flush();
							outToClient.close();
							connectionSocket.close();
							log.append("\n" +movieFile+"\n Application client contacts content server - ACCEPTED"+"\n Source Ip: "+
							connectionSocket.getInetAddress().toString()+" Destination IP: "+"her ip"+
							" \n Source port: "+ connectionSocket.getPort()+" Destination Port: "+PORT);
							fis.close();
						
						
							bis.close();
	                //break;
	              
	            }
	         } catch (IOException e) {
					log.append("\n" + "Failed to accept client request\n");
				}
	        }
	}
}

    public static void main(String args[] )  throws IOException {
    	HerCDN cdn = new HerCDN(PORT);	
    }

	

}