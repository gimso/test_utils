package tcp_gateway;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Or on 8/31/16.
 */
public class TCPHandler {
    private static final int SOCKET_TIMEOUT = 10 * 1000;
    private DataOutputStream mTCPOutputStream;
    private InputStream mTCPInputStream;

 
    public TCPHandler() {
    }
   

    /**
     * Sends a Simgo protocol message over a TCP socket
     * @param aBytes
     * @return
     */
    public byte[] sendMessage(byte[] aBytes) {
        return sendMessage(aBytes, (byte) 0x00);
    }

    /**
     * Sends a Simgo protocol message over a TCP socket and keeps a request ID
     * @param aBytes
     * @param id   A request identifier
     * @return
     */
    private byte[] sendMessage(byte[] aBytes, byte id) {
        byte[] buffer = new byte[256];

        try {
            int bytesRead;
            mTCPOutputStream.write(aBytes);

            mTCPOutputStream.flush();
            System.out.println( String.format("Sending request id:0x%02x request:%s", id, global.Conversions.byteArrayToHexString(aBytes)));

            bytesRead = mTCPInputStream.read(buffer);


            System.out.println( String.format("Received %d bytes for request id:0x%02x", bytesRead, id));
            return buffer;
        } catch (Exception aE) {
            System.out.println( String.format("Error: %s",aE));
            return null;
        }
    }
   
    /** Creates a TCP Socket for a URL with default port
     * @param cloudUrl
     * @return
     */
    public boolean createNewTcpSocket(String cloudUrl) {
    	return createNewTcpSocket(cloudUrl, 5151);
    }
    
   public boolean createNewTcpSocket (String cloudUrl, int port) {
	    return createNewTcpSocket(cloudUrl,port, true, 0);
    	
    }
   
   /** Creates a TCP socket with a url and a port
 * @param cloudUrl
 * @param closeSocket
 * @param milliseconds
 * @return
 */
public boolean createNewTcpSocket (String cloudUrl, boolean closeSocket, long milliseconds) {
	   return createNewTcpSocket(cloudUrl, 5150, closeSocket, milliseconds);
   }

    /**
     * Creates a TCP socket given a URL and a port
     * @param cloudUrl  The cloudUrl instance to use
     * @param port  The TCP port to be used
     * @param closeSocket  If true - socket is closed immediately. If false - wait milliseconds before closing
     * @param milliseconds time to wait before closing the socket if closeSocket is true
     * @return  true if success
     */
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
            	return true;
            }
            
            else{
            	try {
					Thread.sleep(milliseconds);
					return true;
				} catch (InterruptedException e) {
					e.printStackTrace();
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
