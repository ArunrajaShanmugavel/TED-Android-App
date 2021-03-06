/*
	Copyright 2011 Catch.com, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License
*/

package com.catchnotes.tedapp;

import com.tedx.logics.ArchiveLogic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ArchiveActivity extends Activity{

	private ListView mListArchive;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive);
        
        mListArchive = (ListView) findViewById(R.id.lstArchive);        
        mListArchive.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, this.getResources().getStringArray(R.array.TEDConferences)));
        
        mListArchive.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View row, int position, long id) {
				ArchiveLogic.SetConference(ArchiveActivity.this, position);
				finish();
			}
        });
    }
}
