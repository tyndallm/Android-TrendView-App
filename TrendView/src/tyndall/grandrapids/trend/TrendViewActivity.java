package tyndall.grandrapids.trend;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tyndall.grandrapids.trend.PullToRefreshListView.OnRefreshListener;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TrendViewActivity extends ListActivity {
	String[] venueList = new String[10];
	private ArrayList<String> venues = new ArrayList<String>();
	private JSONArray trendingVenues;
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.pull_to_refresh);
	  
	  String[] listItems = { "1. ", "2. ", "3. ", "4. ", "5. ", "6. ", "7. ", "8. ", "9. ", "10." };
	  ArrayList<String> lst = new ArrayList<String>();
	  lst.addAll(Arrays.asList(listItems));
	  
	  final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, lst);
	  
	// Set a listener to be invoked when the list should be refreshed.
      ((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
          @Override
          public void onRefresh() {
        	  
        	  new ParseJsonRequestTask(new ParseJsonRequestTask.Callback() {
     	         public void onComplete(String content) {
     	             // do whatever needs to be done with the content, such as:
     	             try {
     	            	trendingVenues = new JSONArray(content);
     	            	adapter.clear();
     	            	
     	            	for(int i = 0; i < trendingVenues.length(); i ++){
     	            		JSONObject object = trendingVenues.getJSONObject(i);
     	            		String currVenue = (i + 1) + ". " + object.getString("venueName") + " - " + object.getString("totalHitScore");
     	            		venueList[i] = currVenue;
     	            		adapter.add(venueList[i]);
     	            		Log.i("CityTrend", currVenue);
     	            	}
     	            	
//     	            	adapter.addAll(venueList);  Not available in Android 10
     	            	adapter.notifyDataSetChanged();
     	            	((PullToRefreshListView) getListView()).onRefreshComplete();

     					
     				} catch (JSONException e) {
     					// TODO Auto-generated catch block
     					e.printStackTrace();
     				}
     	         }
     	    }).execute("http://fierce-mist-5830.herokuapp.com/venues.json"); //Heroku Django app API
        	  
          }
      });
	  	  	  
	  setListAdapter(adapter);

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	    	
	      // When clicked, show a toast with the TextView text
	      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
	          Toast.LENGTH_SHORT).show();
	    }
	  });
	}
	
	

	
	
}