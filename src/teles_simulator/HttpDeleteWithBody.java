package teles_simulator;

import java.io.IOException;
import java.net.URI;
import org.apache.http.Header;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author Or
 *  The sole purpose of this class is to enable an HTTP Delete request that contains a body using the apahce HTTP client (this is
 *  not supported by default, as usually delete requests do not contain a body, but it was thus implemented on the python teles simulator
 *  (wrongly), which we do not wish to replace currently.  
 */
public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

	  public static final String METHOD_NAME = "DELETE";
	  
	    public String getMethod() {
	        return METHOD_NAME;
	    }
	 
	    public HttpDeleteWithBody(final String uri) {
	        super();
	        setURI(URI.create(uri));
	    }
	 
	    public HttpDeleteWithBody(final URI uri) {
	        super();
	        setURI(uri);
	    }
	 
	    public HttpDeleteWithBody() {
	        super();
	    }
}
