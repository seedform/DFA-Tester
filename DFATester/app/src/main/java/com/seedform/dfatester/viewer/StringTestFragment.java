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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.seedform.dfatester.R;
import com.seedform.dfatester.dfa.DFA;
import com.seedform.dfatester.dfa.DFATraversalLog;
import com.seedform.dfatester.util.Data;
import com.seedform.dfatester.util.Tool;

public class StringTestFragment extends Fragment implements View.OnClickListener {

    private EditText mTestStringEditText;
    private TextView mLogTextView;
    private TextView mMessageTextView;
    private ScrollView mLogScrollView;
    private Button mStringTestButton;
    private DFA mDFA;
    private DFATraversalLog mLog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDFA = Data.getSelectedDFA();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_string_test, container, false);
        mTestStringEditText = (EditText) rootView.findViewById(R.id.edittext_test_string);
        mLogTextView = (TextView) rootView.findViewById(R.id.textview_test_log);
        mMessageTextView = (TextView) rootView.findViewById(R.id.textview_test_message);
        mLogScrollView = (ScrollView) rootView.findViewById(R.id.scrollview_test_log);
        mLogScrollView.setVerticalFadingEdgeEnabled(true);
        mStringTestButton = (Button) rootView.findViewById(R.id.button_test_string);
        mStringTestButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_string_test, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View view) {
        if (mDFA.getStates().size() == 0 || mDFA.getAlphabet().size() == 0) {
            Tool.createToast(getActivity(),
                    R.string.msg_state_or_alpha_config_incomplete,
                    Toast.LENGTH_LONG);
        } else {
            Tool.createToast(getActivity(),
                    R.string.msg_running_test,
                    Toast.LENGTH_LONG);
            new StringTestTask().execute(mTestStringEditText.getText().toString());
        }

        Tool.hideSoftKeyboard(getActivity());
    }

    private class StringTestTask extends AsyncTask<String, Integer, Long> {

        @Override
        protected Long doInBackground(String... testString) {
            mLog = mDFA.testString(testString[0]);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            int resultColor = mLog.isAccepted()
                    ? getResources().getColor(R.color.test_string_accepted)
                    : getResources().getColor(R.color.test_string_rejected);
            mMessageTextView.setText(mLog.getMessage());
            mMessageTextView.setTextColor(resultColor);
            mLogTextView.setText(mLog.toString());
            mLogTextView.setTextColor(resultColor);
            mLogScrollView.fullScroll(ScrollView.FOCUS_UP);
            Tool.createToast(getActivity(), R.string.msg_test_complete, Toast.LENGTH_LONG);
        }

    }

}
