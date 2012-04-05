package tyndall.grandrapids.trend;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class ParseJsonRequestTask extends AsyncTask<String, Void, String>{
	
	private Callback mCallback;
	
	public ParseJsonRequestTask(Callback callback){
		mCallback = callback;
	}
	
	@Override
	protected String doInBackground(String... urls) {
		String response = "";
		for (String url : urls) {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			try {
				HttpResponse execute = client.execute(httpGet);
				InputStream content = execute.getEntity().getContent();

				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(content));
				String s = "";
				while ((s = buffer.readLine()) != null) {
					response += s;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
	public interface Callback {
		public void onComplete(String result);
	}

	@Override
	protected void onPostExecute(String result) {
		mCallback.onComplete(result);
	}
	


}
