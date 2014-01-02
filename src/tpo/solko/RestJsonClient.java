package tpo.solko;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestJsonClient {
	
	public boolean isSiteAvailable(String url){
		try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL(url).openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1500); 
            urlc.connect();
            return true;
        } catch (IOException e) {
        
        }
    

    return false;
	}
	
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	
	public HttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
	
	public DefaultHttpClient CreateClient(){
		HttpParams httpParameters = new BasicHttpParams();
		
		/*
		 *  Set the timeout in milliseconds until a connection is established.
		 *  The default value is zero, that means the timeout is not used. 
		 */
		int timeoutConnection = 3000;
		
		
		
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		
		/* 
		 * Set the default socket timeout (SO_TIMEOUT) 
		 * in milliseconds which is the timeout for waiting for data.
		 */
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		
		
		return httpClient;
	}
	

	
	
	public HttpResponse getResponseSendingLogin(String url, String mEmail, String  mPassword){
		
		
		
		HttpClient httpClient = getNewHttpClient();
		
		HttpParams params = new BasicHttpParams();
		//params.setParameter("http.protocol.handle-redirects",false);
		HttpResponse response = null;
		URI address = null;
		try {
			address = new URI(url);
			
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		HttpPost httppost = new HttpPost(address);
		
		httppost.setParams(params);
		    // Add your data
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    nameValuePairs.add(new BasicNameValuePair("username", mEmail));
	    nameValuePairs.add(new BasicNameValuePair("password", mPassword));
	    
	    try {
	    	
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    //ugly ugly hack
	    //if (isSiteAvailable()){
			try {
				
				response = httpClient.execute(httppost);
				long entity = response.getEntity().getContentLength();
				if (entity == 0)
					return null;
			} catch (Exception e) {
				e.printStackTrace();
			} 
		
		return response;
		
	    
		    
	}
	
	public HttpResponse getResponseWithoutToken(String url, List<NameValuePair> j)
	{
		HttpClient httpClient = getNewHttpClient();
		
		HttpParams params = httpClient.getParams();
		params.setParameter("http.protocol.handle-redirects",false);
		HttpResponse response = null;
		URI address = null;
	
		try {
			address = new URI(url);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	
		HttpPost httppost = new HttpPost(address);
		httppost.setParams(params);
		
    	if (j!=null)
    		{
		    try {
		    	httppost.setEntity(new UrlEncodedFormEntity(j));
			} catch (UnsupportedEncodingException e) {
				
			}
    		}
	    
	    try {
			response = httpClient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		return response;
		
	}
	
	public HttpResponse getResponseWithToken(String url, String token){

		HttpClient httpClient = getNewHttpClient();
		
		HttpParams params = httpClient.getParams();
		params.setParameter("http.protocol.handle-redirects",false);
		HttpResponse response = null;
		
		URI address = null;
		try {
			address = new URI(url);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		HttpGet httpGet = new HttpGet(address);
		httpGet.setParams(params);
		
		if (token != null && token.length()>0){
    		httpGet.addHeader("Authorization", "Token " + token);

    	try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	}
	
	 
		return response;
		
	    
		    
	}
	
	
	public HttpResponse postTokenAndJsonObject(String url, String token, JSONObject jsonData){

		HttpClient httpClient = getNewHttpClient();
		
		HttpParams params = httpClient.getParams();
		params.setParameter("http.protocol.handle-redirects",false);
		HttpResponse response = null;
		URI address = null;
	
		try {
			address = new URI(url);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	
		HttpPost httppost = new HttpPost(address);
		httppost.setParams(params);
		
		if (token!=null && token.length()>0){
    		httppost.addHeader("Authorization", "Token " + token);
		}
		List<NameValuePair> postParams = new ArrayList<NameValuePair>(1);
    	postParams.add(new BasicNameValuePair("data", jsonData.toString()));
		
	    try {
	    	httppost.setEntity(new UrlEncodedFormEntity(postParams));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    
	    try {
			response = httpClient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		return response;
		
	}
			
	
	public String getToken(JSONObject req){
		try {
			String token = req.getString("token");
			return token;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	

	
	
	public JSONObject GetObjectFromResponse(HttpResponse response) {
		
		JSONObject jsonO = null;
		BufferedReader reader;

		String json;
		try {
			if (response != null)
			{
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
	
				json = reader.readLine();
				jsonO = new JSONObject(json);
				if (jsonO.has("data"))
				{
					String str = jsonO.getString("data");
					jsonO = new JSONObject(str);
				}
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonO;

	}
	
	public JSONArray GetArrayFromResponse(HttpResponse response) {
		
		JSONArray jsonA = null;
		String json;
		
	    HttpEntity entity = response.getEntity();
        InputStream content;
		try {
			content = entity.getContent();

	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));

	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	        	sb.append(line);
	        }

	        json = sb.toString();
	        jsonA = new JSONArray(json);
	        content.close();
			    
	        
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        
		return jsonA;
		
	
	}
   

}