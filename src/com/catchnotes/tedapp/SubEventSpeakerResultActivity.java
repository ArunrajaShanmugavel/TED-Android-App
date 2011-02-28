package com.catchnotes.tedapp;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.catchnotes.tedapp.R;
import com.tedx.logics.SearchResultLogic;
import com.tedx.objects.SearchResult;
import com.tedx.activities.LazyActivity;

public class SubEventSpeakerResultActivity extends LazyActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
		mFrom = new String[] {
				SearchResult.NAME,
				SearchResult.TITLE,
				SearchResult.PHOTOURL
		};

		mTo = new int[] {
				android.R.id.text1,
				android.R.id.text2,
				android.R.id.icon
		};
		
		super.onCreate(savedInstanceState, R.layout.searchresults, R.layout.searchresultrow);
	}
	

	public void onItemClick(AdapterView<?> list, View row, int position, long id) {
		startActivityForPosition(SpeakerActivity.class, position);
	}

	@Override
	protected LoadTask newLoadTask() {
		return new LoadSearchResultTask();
	}

	@Override
	protected void setTaskActivity() {
		mLoadTask.activity = this;
	}

	protected static class LoadSearchResultTask extends LoadTask {
		@Override
		protected Boolean doInBackground(String... params) {
			SubEventSpeakerResultActivity activity = (SubEventSpeakerResultActivity) super.activity;

			int EventId = Integer.valueOf(activity.getResources().getString(R.string.subEventId));
			int ServerEventVersion = SearchResultLogic.getSearchResultVersionByEventId(activity.getResources(), EventId);
			JSONArray speakers;

			//check point to load from cache or web
			if(	ServerEventVersion != 0 &&
				SearchResultLogic.getCurrentVersionByEventIdFromCache(activity, EventId) != ServerEventVersion)
			{
				String Url = 
					com.tedx.logics.SearchResultLogic.getSearchResultsByCriteriaURL(
							activity.getResources(), EventId, activity.mPage);
				
				//Set the version
				SearchResultLogic.setCurrentVersionByEventId(activity, EventId, ServerEventVersion);
				
				try {
					speakers = SearchResultLogic.loadSpeakerSearchResultsFromWeb(activity, Url, EventId);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					speakers = null;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					speakers = null;
				}
			}
			else
			{
				try {
					speakers = SearchResultLogic.loadSpeakerSearchResultsFromCache(activity, EventId);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					speakers = null;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					speakers = null;
				}
			}
			return loadSpeakerResultsByCollection(speakers);	

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!result) {
				activity.showDialog(DIALOG_ERROR_LOADING);
			}
		}
	}

	@Override
	protected HashMap<String, String> loadJSON(JSONObject data) throws JSONException {
		HashMap<String, String> SearchResults = new HashMap<String, String>();
		
		SearchResults.put(SearchResult.NAME, data.getString("FirstName") + " " + data.getString("LastName") );
		SearchResults.put(SearchResult.TITLE, data.getString("Title"));
		SearchResults.put(SearchResult.EMAIL, data.getString("Email"));
		SearchResults.put(SearchResult.FACEBOOK, data.getString("Facebook"));
		SearchResults.put(SearchResult.PHOTOURL, data.getString("PhotoUrl"));
		SearchResults.put(SearchResult.SPEAKERID, data.getString("SpeakerId"));
		SearchResults.put(SearchResult.TWITTER, String.valueOf(data.getString("Twitter")));
		SearchResults.put(SearchResult.EMAIL, String.valueOf(data.getString("Email")));
		SearchResults.put(SearchResult.TOPIC, String.valueOf(data.getString("Topic")));
		SearchResults.put(SearchResult.DESCRIPTION, String.valueOf(data.getString("Description")));
		SearchResults.put(SearchResult.WEBSITE, String.valueOf(data.getString("WebSite")));
		SearchResults.put(SearchResult.SESSION, String.valueOf(data.getInt("Session")));

		return SearchResults;
	}
	
    //Back Button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    
    @Override
    public void onBackPressed() {
        // This will be called either automatically for you on 2.0
        // or later, or by the code above on earlier versions of the
        // platform.
		finish();
        return;
    }
}