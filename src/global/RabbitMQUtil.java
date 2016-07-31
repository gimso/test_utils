package global;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * A util for receiving or sending messages to/from RabbitMQ
 * @author Dana
 */
public class RabbitMQUtil {
	private static final String EXCHANGE_TYPE = "topic";
	private ConnectionFactory factory;
	private Connection conn;
	private Channel channel;
    private Consumer consumer;
   
	/**
	 * Get connection to rmq
	 * @param exchangeName
	 * @param queueName
	 * @param routingKey
	 * @param amqpUri
	 * @return connection
	 */
	public Connection getConnection(String exchangeName, String queueName, String routingKey, String amqpUri){
		factory = new ConnectionFactory();
			try {
				factory.setUri(amqpUri);
				conn = factory.newConnection();
				channel = conn.createChannel();
				channel.exchangeDeclare(exchangeName, EXCHANGE_TYPE, true);
				channel.queueDeclare(queueName, false, false, false, null);
				channel.queueBind(queueName, exchangeName, routingKey);
				//delete all previous messages in the queue
				channel.queuePurge(queueName);
				
				System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
			} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}		
		return conn;		
	}
	
	/**
	 * Reads message from queue
	 * @param queueName
	 * @param aListener
	 */
	public void getMessage(String queueName, MessagesEventsListener aListener){
		consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
			    System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");	
			    aListener.onReceived(message);
			    System.out.println("fired event" + aListener);
			}			
		};			
		try {
			channel.basicConsume(queueName, true, consumer);

		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * Close resources: connection and channel
	 */
	public void closeConnection()
	//(String queueName)
	{
		try {
			//channel.queueDelete(queueName);
			channel.close();
			conn.close();
			System.out.println("rmq connection and channel closed");
		} catch (IOException | TimeoutException e) {			
			e.printStackTrace();
		}	
	}
	
	
//	public void purgeQueue(String queue){
//		try {
//			channel.queuePurge(queue);
//		} catch (IOException e) {
//		}
//	}
	
	/**
	 * Message listener interface
	 */
	public interface MessagesEventsListener {		
		/**
		 * handle incoming messages from rmq
		 * @param aMsg
		 */
		void onReceived(String aMsg);
	}
}
