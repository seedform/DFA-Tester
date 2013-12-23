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

package com.seedform.dfatester.viewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.seedform.dfatester.R;
import com.seedform.dfatester.app.BaseActivity;
import com.seedform.dfatester.app.DeletableItemAdapter;
import com.seedform.dfatester.dfa.DFA;
import com.seedform.dfatester.dfa.State;
import com.seedform.dfatester.util.Consts;
import com.seedform.dfatester.util.Data;
import com.seedform.dfatester.util.Tool;

import java.util.ArrayList;

public class StateListFragment extends Fragment implements AdapterView.OnItemClickListener,
        DeletableItemAdapter.OnDeleteCallback {

    private ArrayList<State> mStates;
    private DFA mDFA;
    private DeletableItemAdapter<State> mAdapter;
    private GridView mGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDFA = Data.getSelectedDFA();
        mStates = Data.getSelectedDFA().getStates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_state_list, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.state_listview);
        mGridView.setEmptyView((View) rootView.findViewById(R.id.empty_state_listview_text));
        mGridView.setVerticalFadingEdgeEnabled(true);
        mAdapter = new DeletableItemAdapter<State>(mStates, getActivity(), this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_state_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_state:
                mDFA.addState();
                mAdapter.notifyDataSetChanged();
                mGridView.smoothScrollToPosition(mStates.size() - 1);
                Tool.createToast(getActivity(), R.string.msg_new_state, Toast.LENGTH_SHORT);
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (mDFA.getAlphabet().size() == 0) {
            Tool.createToast(getActivity(), R.string.msg_alpha_config_incomplete,
                    Toast.LENGTH_LONG);
        } else {
            Data.selectState(position);
            ((BaseActivity) getActivity()).openActivity(TransitionSetterActivity.class, false);
        }
    }

    @Override
    public void onClickDelete(int index) {
        State removed = mDFA.removeState(index);
        String itemName = removed.toString().substring(0,
                removed.toString().indexOf(Consts.SPLIT));
        Tool.createToast(getActivity(),
                itemName + getResources().getString(R.string.msg_concat_deleted),
                Toast.LENGTH_SHORT);
    }

}
