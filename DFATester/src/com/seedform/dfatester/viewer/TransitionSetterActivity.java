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

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.seedform.dfatester.R;
import com.seedform.dfatester.app.BaseActivity;
import com.seedform.dfatester.app.BasicAdapter;
import com.seedform.dfatester.dfa.DFA;
import com.seedform.dfatester.dfa.State;
import com.seedform.dfatester.util.Data;

import java.util.List;

public class TransitionSetterActivity extends BaseActivity {

    private State mState;
    private DFA mDFA;
    private List<State> mStates;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Workaround for floating activity crashing on some pre-4.0 devices.
        if (Build.VERSION.SDK_INT < 15) {
            setTheme(R.style.Theme_Holored);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_setter);
        
        mDFA = Data.getSelectedDFA();
        mState = Data.getSelectedState();
        mStates = mDFA.getStates();
        
        setUpUI();
    }

    private void setUpUI() {
        Button closeButton = (Button) findViewById(R.id.button_close);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.textview_state_transitions_title);
        title.setText(String.format(getResources().getString(R.string.title_transition_setter)
                + " %d", mState.getId()));
        ListView listView = (ListView) findViewById(R.id.listview_state_transitions);
        listView.setVerticalFadingEdgeEnabled(true);
        listView.setAdapter(new TransitionSetterAdapter(mDFA.getAlphabet(), this));

        CheckBox checkbox = (CheckBox) findViewById(R.id.checkbox_accepting_state);
        checkbox.setChecked(mState.isAcceptingState());
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mState.setAsAcceptingState(isChecked);
            }
        });
    }

    private class TransitionSetterAdapter extends BasicAdapter<Character> {

        private ArrayAdapter<String> mAdapter;

        public TransitionSetterAdapter(List<Character> data, Context context) {
            super(data, context);
            mAdapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_dropdown_item);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            for (State s : mStates) {
                mAdapter.add(s.toString().substring(0, s.toString().indexOf("|")));
            }
        }

        @Override
        public View getView(final int charPos, View convertView, ViewGroup parent) {

            final Character c = mData.get(charPos);
            ViewHolder vh;

            if (convertView == null || convertView.getTag() == null) {
                convertView = mInflater.inflate(R.layout.transition_setter_item, null);
                vh = new ViewHolder();
                vh.textView = (TextView) convertView.findViewById(R.id.textview_transition_symbol);
                vh.spinner = (Spinner) convertView.findViewById(R.id.spinner_state_chooser);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            vh.textView.setText(String.format("\"%c\" \u2192", c));
            vh.spinner.setAdapter(mAdapter);
            vh.spinner.setSelection(mState.getNextState(c).getId());
            vh.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> aV, View v, int statePos, long id) {
                    mState.setTransition(c, mStates.get(statePos));
                }

                @Override
                public void onNothingSelected(AdapterView<?> aV) {
                }

            });

            if (charPos % 2 == 0) {
                convertView.setBackgroundColor(getResources().getColor(R.color.app_grey_bg));
            } else {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }

            return convertView;
        }
    }

    private class ViewHolder {
        public TextView textView;
        public Spinner spinner;
    }

}
