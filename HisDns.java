import java.awt.GridLayout;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * Hiscinema DNS
 * 1 UDP port
 * @author Group 58
 *
 */
public class HisDns
{
            public static void main(String args[])
            {
            try
            {
                 
            JTextArea log;
        		JFrame frame = new JFrame("HisCinema DNS");
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
            	DatagramSocket server=new DatagramSocket(40578);
               
            	while(true)
                        {
                                    log.append("Waiting for request from local DNS...\n");
            						            byte[] sendbyte=new byte[1024];
                                    byte[] receivebyte=new byte[1024];
                                    DatagramPacket receiver=new DatagramPacket(receivebyte,receivebyte.length);
                                    server.receive(receiver);
                                    String str=new String(receiver.getData());
                                    String address=str.trim();
                                    log.append("local DNS wants to resolve: " + str + "\n");
                                    InetAddress clientAddress=receiver.getAddress();
                                    int port=receiver.getPort();
                                    /*
                                     * DNS records Table
                                     */
                                    String name[]={"video.hiscinema.com/F1","video.hiscinema.com/F2","video.hiscinema.com/F3","video.hiscinema.com/F4", "video.hiscinema.com/F5"};
                                    String value[]={"herCDN.com/F1","herCDN.com/F2","herCDN.com/F3","herCDN.com/F4", "herCDN.com/F5"};
                                    String type[]={"R","R","R","R", "R"};
                                    log.append("\n"+ "Local dummy DNS contact dummy DNS for hiscinema.com - ACCEPTED"+"\n Source Ip: "+ receiver.getAddress().toString()+ " Destination IP: Her IP\n Source port: "+receiver.getPort()+" Destination Port: "+"40161");

                               
                                 
                                    
                                     for(int i=0;i<value.length;i++)
                                      {
                                                 if(address.equals(name[i])) //match the name with the value
                                               {
                                                    log.append("\nReply to local DNS: " + value[i] +"\n");
                                                	  sendbyte=value[i].getBytes();
                                                    DatagramPacket sender=new DatagramPacket(sendbyte,sendbyte.length,clientAddress,port);
                                                    server.send(sender);//send the name to local dns
                                                    //server.close();
                                                    //break;
                                    

                                               }
                                                     
                                      
                                      }
                                     //break;
                        }
           
            }
            catch(Exception e)
            {
                        System.out.println(e);
            }
            }
}