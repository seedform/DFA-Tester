/***************************************************************************\
 * Copyright 2013 Shudmanul Chowdhury
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
\***************************************************************************/

package com.seedform.dfatester;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.seedform.dfatester.app.BaseActivity;
import com.seedform.dfatester.util.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfoActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private static String KEY_TITLE = "title";
    private static String KEY_SUMMARY = "summary";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setUpUI();
    }
    
    private void setUpUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        List<HashMap<String, String>> listViewData = new ArrayList<HashMap<String, String>>();
        String[] titles = getResources().getStringArray(R.array.info_item_titles);
        String[] summaries = getResources().getStringArray(R.array.info_item_summaries);
        
        for(int i = 0; i < titles.length; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put(KEY_TITLE, titles[i]);
            item.put(KEY_SUMMARY, summaries[i]);
            listViewData.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listViewData,
                android.R.layout.simple_list_item_2, new String[] {
                        KEY_TITLE, KEY_SUMMARY
                }, new int[] {
                        android.R.id.text1, android.R.id.text2
                });

        ListView listView = (ListView) findViewById(R.id.listview_info_items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
            case 0:
                showStoreAppPage();
                break;
            case 1:
                showStorePubPage();
                break;
            case 2:
                showContactDev();
                break;
            case 3:
                showPage(R.string.url_about, R.string.title_about);
                break;
            case 4:
                showPage(R.string.url_licenses, R.string.title_licenses);
                break;
        }
    }

    private void showStoreAppPage() {
        Intent appPageIntent = new Intent(Intent.ACTION_VIEW);
        appPageIntent.setData(Uri.parse(getResources().getString(R.string.url_market_app_link)));
        startActivity(appPageIntent);
    }

    private void showStorePubPage() {
        Intent pubPageIntent = new Intent(Intent.ACTION_VIEW);
        pubPageIntent.setData(Uri.parse(getResources().getString(R.string.url_market_pub_link)));
        startActivity(pubPageIntent);
    }

    private void showContactDev() {
        String emailBody = getResources().getString(R.string.email_body)
                + Tool.getSystemInfoAsFormattedString();
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.setType("message/rfc822"); // use from live device
        mailIntent.putExtra(Intent.EXTRA_EMAIL, getResources().getString(R.string.email_address));
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        mailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
        startActivity(Intent.createChooser(mailIntent,
                getResources().getString(R.string.action_select_email_app)));
    }
    
    private void showPage(int urlResId, int titleResId) {
        WebView webView = new WebView(this);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(getResources().getString(urlResId));
        new AlertDialog.Builder(this)
                .setTitle(titleResId)
                .setView(webView)
                .setNegativeButton(R.string.action_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .show();
    }

}
