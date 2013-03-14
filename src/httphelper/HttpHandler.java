package httphelper;

import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import android.util.Log;

public class HttpHandler
{

	private static String TAG = "HttpHandler";
	
	public String executeGet(String url)
	{
		String result = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet( url );
		try
		{
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			result = httpClient.execute(getMethod, responseHandler);
			
		}
		catch( Exception e )
		{
			//LOG.error( "Error while excecuting HTTP method. URL is: " + URL, e );
			//throw new RuntimeException( "Error while excecuting HTTP method.  URL is: " + url + ", Cause: " + e.getMessage(), e);
		}
		
		return result;
	}
	
	public static HttpResponse Post(String URL, List<NameValuePair> valuePairs, String user, String pw) throws Exception
	{
		
		final HttpPost httpPost = new HttpPost(URL);
		
		if (valuePairs != null)
		{
	        try {
	            httpPost.getParams().setBooleanParameter( "http.protocol.expect-continue", false ); 
	            httpPost.setEntity(new UrlEncodedFormEntity(valuePairs, HTTP.UTF_8));
	        } catch (final UnsupportedEncodingException ex) {
	        	Log.e(TAG, ex.getMessage());
	            throw ex;
	        }
		}
		
        try {
        	DefaultHttpClient client = new DefaultHttpClient();
        	
        	if (user.trim().length() > 0 && pw.trim().length() > 0)
        	{
        		//X509Certificate cert = new x509Certificate();
        		client.getCredentialsProvider().setCredentials(new AuthScope(null, -1), 
        			new UsernamePasswordCredentials(user, pw));
        	}
        	
        	HttpResponse response = client.execute(httpPost);
        	return response;
        }
        catch (final Exception ex)
        {
        	Log.e(TAG, ex.getMessage());
        	throw ex;
        }
		
	}
	
} 