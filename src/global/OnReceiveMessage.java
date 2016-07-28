package global;

import java.util.HashMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import global.RabbitMQUtil.MessagesEventsListener;
/**
 * An implementation for MessagesEventsListener (in global.RabbitMQUtil class)</br>
 */
public class OnReceiveMessage implements MessagesEventsListener {
	
	protected JsonUtil mJsonUtil = new JsonUtil();
	protected HashMap<String, Object> mResponse;
	private JsonParser mParser = new JsonParser();
	protected Object mWait = new Object();
	
	/**
	 * Receives the task message from rmq and parses it into map</br>
	 * Notifies the listener
	 */
	@Override
	public void onReceived(String aMsg) {
		System.out.println("onRecieved");
		JsonObject response = (JsonObject) (mParser).parse(aMsg);
		if (mJsonUtil != null) {
			mResponse = (HashMap<String, Object>) mJsonUtil.getJsonMap(response);
			System.out.println("Response:" + mResponse);
			synchronized (mWait) {
				System.out.println("notify");
				mWait.notifyAll();
			}
		}
	}
}
