package tcp_gateway;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by tal on 8/24/16.
 */
public class ManageTCPAgent {
    private static final String TAG = "TCPAgent";
    private static final int SOCKET_TIMEOUT = 10 * 1000;
    private static final int SOCKET_TIMEOUT_WATCHDOG = SOCKET_TIMEOUT - 5000;
    private static final int SOCKET_KEEP_ALIVE_LIMIT = 36;
    private long mLastSocketActivity = Long.MIN_VALUE;
    private int mSocketKeepAliveCounter = 0;
    private DataOutputStream mTCPOutputStream;
    private InputStream mTCPInputStream;
    private String url = "gwtcp.qa.gimso.net";
 

  

    public ManageTCPAgent() {
    }

    public boolean connectTcpSocket() {
  
        mSocketKeepAliveCounter = 0;
        return createNewTcpSocket(url);
    }

    

    public byte[] sendMessage(byte[] aBytes) {
        return sendMessage(aBytes, (byte) 0x00);
    }

    private byte[] sendMessage(byte[] aBytes, byte id) {
        byte[] buffer = new byte[256];

        try {
            int bytesRead;
            mTCPOutputStream.write(aBytes);

            mTCPOutputStream.flush();
            System.out.println( String.format("Sending request id:0x%02x request:%s", id, ConvertersUtil.byteArrayToHexString(aBytes)));

            bytesRead = mTCPInputStream.read(buffer);


            System.out.println( String.format("Received %d bytes for request id:0x%02x", bytesRead, id));
            return buffer;
        } catch (Exception aE) {
            System.out.println( String.format("Error: %s",aE));
            return null;
        }
    }


    
    public boolean createNewTcpSocket(String cloudUrl) {
    	return createNewTcpSocket(cloudUrl, 5151);
    }
    
   public boolean createNewTcpSocket (String cloudUrl, int port) {
	    return createNewTcpSocket(cloudUrl,port, true, 0);
    	
    }
   
   public boolean createNewTcpSocket (String cloudUrl, boolean closeSocket, long milliseconds) {
	   return createNewTcpSocket(cloudUrl, 5150, closeSocket, milliseconds);
   }

    public boolean createNewTcpSocket(String cloudUrl,int port, boolean closeSocket, long milliseconds) {
        try {
            Socket TCPSocket = new Socket();
            TCPSocket.bind(null);

            TCPSocket.setSoTimeout(SOCKET_TIMEOUT);
            TCPSocket.setTcpNoDelay(true);
            System.out.println( String.format("Connecting to server: %s", cloudUrl));

            TCPSocket.connect((new InetSocketAddress(cloudUrl, port)));

            mTCPOutputStream = new DataOutputStream((TCPSocket.getOutputStream()));
            mTCPInputStream = TCPSocket.getInputStream();
            System.out.println( String.format("Opened new TCP Socket"));
            
            
            if (closeSocket == true){
            	TCPSocket.close();
            	return true;
            }
            
            else{
            	try {
					Thread.sleep(milliseconds);
					TCPSocket.close();
					return true;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					TCPSocket.close();
					return false;
				}
            }
            
        } catch (IOException aE) {
            aE.printStackTrace();
            System.out.println( aE.getMessage());
            return false;
        }
    }
    
    
    
 
}
