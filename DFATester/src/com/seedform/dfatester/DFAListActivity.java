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

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.seedform.dfatester.app.BaseActivity;
import com.seedform.dfatester.app.DeletableItemAdapter;
import com.seedform.dfatester.dfa.DFA;
import com.seedform.dfatester.util.Consts;
import com.seedform.dfatester.util.Data;
import com.seedform.dfatester.util.Tool;
import com.seedform.dfatester.viewer.DFAViewerActivity;

import java.util.ArrayList;

public class DFAListActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        DeletableItemAdapter.OnDeleteCallback {

    private ArrayList<DFA> mDFAs;
    private GridView mGridView;
    private DeletableItemAdapter<DFA> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfa_list);
        Tool.setAppContext(this);
        Data.loadData();
        mDFAs = Data.getDFAs();
        setUpUI();
    }

    private void setUpUI() {
        getSupportActionBar().setTitle(R.string.app_name);
        mGridView = (GridView) findViewById(R.id.gridview_dfa);
        mGridView.setEmptyView((View) findViewById(R.id.textview_empty_gridview_dfa));
        mGridView.setVerticalFadingEdgeEnabled(true);
        mAdapter = new DeletableItemAdapter<DFA>(mDFAs, this, this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dfa_list, menu);        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                openActivity(InfoActivity.class, true);
                return true;
            case R.id.action_add_dfa:
                DFA dfa = new DFA(mDFAs.isEmpty() ? 1 : mDFAs.get(mDFAs.size() - 1).getId() + 1);
                mDFAs.add(dfa);
                mAdapter.notifyDataSetChanged();
                mGridView.smoothScrollToPosition(mDFAs.size() - 1);
                Tool.createToast(this, R.string.msg_new_dfa, Toast.LENGTH_SHORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> aV, View v, final int position, long id) {
        Data.selectDFA(position);
        openActivity(DFAViewerActivity.class, true);
    }

    @Override
    public void onClickDelete(int index) {
        DFA removed = mDFAs.remove(index);
        String itemName = removed.toString().substring(0,
                removed.toString().indexOf(Consts.SPLIT));
        Tool.createToast(this,
                itemName + getResources().getString(R.string.msg_concat_deleted),
                Toast.LENGTH_SHORT);
    }

}
