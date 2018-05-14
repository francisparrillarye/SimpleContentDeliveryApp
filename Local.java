 import java.awt.GridLayout;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Local DNS 
 * 2 UDP ports 
 * @author Group 58
 *
 */
public class Local {


	public static void main(String args[])
    {
		final int UDP_PORT1 = 40576;
		final int UDP_PORT2 = 40577;
    try
    {
    	JTextArea log;
		JFrame frame = new JFrame("Local Dns");
		log = new JTextArea("");
		log.setEditable(false);
		JPanel panel = new JPanel();
		JScrollPane jsp = new JScrollPane(log);
		panel.setLayout(new GridLayout());
		panel.add(jsp);
		
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,500);
		frame.setVisible(true);
    	
    	DatagramSocket server=new DatagramSocket(UDP_PORT1);
    	DatagramSocket server2=new DatagramSocket(UDP_PORT2);
    	while(true)
                {
                            byte[] sendbyte = new byte[1024];
                            byte[] receivebyte = new byte[1024];
                            log.append("\n\nWaiting to get request from client...\n");
                            DatagramPacket receiver = new DatagramPacket(receivebyte,receivebyte.length);
                            log.append("Created datagram to recieve from client...\n");
                            server.receive(receiver);//Receive request from Client
                            String str=new String(receiver.getData()); 
                            String response=str.trim();
                            log.append("Recieved request from client: "+str+"\n");
                            InetAddress clientAddress=receiver.getAddress();
                            int port=receiver.getPort();
                            /*
                             * DNS records Table
                             */
                            String name[]={"herCDN.com",	"NSherCDN.com",	"hisCinema.com",	"NShisCinema.com"};
                            String value[]={"NSherCDN.com", "192.168.0.27",		"NShisCinema.com",	"192.168.0.27"};
                            String type[]={"NS",			"A",			"NS",				"A"};
                            log.append("\n" + "Client selects the content URL and contacts local dummy DNS - ACCEPTED \n Source Ip: "+clientAddress.toString()+" Destination IP: "+"Local ip"+" \n Source port: "+receiver.getPort()+"Destination Port: "+UDP_PORT1+"\n");
                            InetAddress hisaddress = InetAddress.getByName(value[3]);
                            //hisaddress = InetAddress.getByName("");
                            InetAddress heraddress = InetAddress.getByName(value[1]);
                            //Ask hisdns for hercdn url 
                            sendbyte=response.getBytes();
                            DatagramPacket sender=new DatagramPacket(sendbyte,sendbyte.length,hisaddress,40578);//send request to hisDNS
                            server2.send(sender);
                            log.append("Sending request to hisDNS...\n");
                            sendbyte=new byte[1024];
                            receivebyte=new byte[1024];
                            receiver=new DatagramPacket(receivebyte,receivebyte.length);
                            server2.receive(receiver);
                            str=new String(receiver.getData());
                            log.append("Recieved reply from hisDNS: " + str + "\n");
                            response=str.trim();
                            System.out.println(response);
                            log.append("\n" + "DNS server for hiscinea.com sends reply: "+ response+ "\n Source Ip: "+value[3]+" Destination IP: "+"local ip"+" \n Source port: "+receiver.getPort()+"Destination Port: "+UDP_PORT2+"\n");
                          	  
                            //Ask herdns for the ip address of hercdn.com
                            sendbyte=response.getBytes();
                            log.append("\nCreating datagram to send to herDNS...\n");
                            sender=new DatagramPacket(sendbyte,sendbyte.length,heraddress,40579);
                            log.append("Asking herDNS to resolve: " + str + "\n");
                            server2.send(sender);
                            sendbyte=new byte[1024];
                            receivebyte=new byte[1024];
                            receiver=new DatagramPacket(receivebyte,receivebyte.length);
                            server2.receive(receiver);
                            str=new String(receiver.getData());
                            response=str.trim();
                            log.append("herDNS resolved request... replied: " + str + "\n");
                            log.append("\n" + "Dummy DNS for herCDN.com replies to local dummy DNS - ACCEPTED\n Source Ip: "+value[3]+" Destination IP: "+value[1]+" \n Source port: "+receiver.getPort()+"Destination Port: "+UDP_PORT2);   
                            //Send the ip address of hercdn.com to the client     
                            sendbyte=response.getBytes();
                            sender=new DatagramPacket(sendbyte,sendbyte.length,clientAddress,port);
                            log.append("\nSending client IP address of herCDN: " + str + "\n");
                            server.send(sender);
                            //break;
                             
                
               }
    	//server.close();//Close sockets
    	//server2.close();
    }
    catch(Exception e)
    {
                System.out.println(e);
    }
    }

	
}