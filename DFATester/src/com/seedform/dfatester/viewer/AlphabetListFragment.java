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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.seedform.dfatester.R;
import com.seedform.dfatester.app.DeletableItemAdapter;
import com.seedform.dfatester.dfa.DFA;
import com.seedform.dfatester.util.Data;
import com.seedform.dfatester.util.Tool;

public class AlphabetListFragment extends Fragment implements AdapterView.OnItemClickListener,
        DeletableItemAdapter.OnDeleteCallback {

    private DeletableItemAdapter<Character> mAdapter;
    private GridView mGridView;
    private DFA mDFA;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDFA = Data.getSelectedDFA();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alphabet_list, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.alphabet_listview);
        mGridView.setEmptyView((View) rootView.findViewById(R.id.empty_alphabet_listview_text));
        mGridView.setVerticalFadingEdgeEnabled(true);
        mAdapter = new DeletableItemAdapter<Character>(mDFA.getAlphabet(), getActivity(), this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_alphabet_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add_symbol:
                openAddSymbolDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAddSymbolDialog() {
        final EditText input = new EditText(getActivity());
        input.setHint(R.string.hint_symbol_entry);
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_symbol_entry)
                .setView(input)
                .setPositiveButton(R.string.action_add_symbol,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inputText = input.getText().toString();
                                if (inputText.length() != 0) {
                                    for (char c : inputText.toCharArray()) {
                                        mDFA.addSymbol(c);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    Tool.createToast(getActivity(), R.string.msg_symbols_added,
                                            Toast.LENGTH_SHORT);
                                }
                            }
                        })
                .show();
    }

    @Override
    public void onClickDelete(int index) {
        Character removed = mDFA.removeSymbol(index);
        Tool.createToast(getActivity(), String.format(
                "\"%c\"" + getResources().getString(R.string.msg_concat_deleted),
                removed), Toast.LENGTH_SHORT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Character oldChar = mDFA.getAlphabet().get(position);
        final EditText input = new EditText(getActivity());
        input.setHint(getResources().getString(R.string.hint_concat_symbol_replacement) + "\""
                + oldChar.toString() + "\"");
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        input.setFilters(new InputFilter[] {
            new InputFilter.LengthFilter(1)
        });

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_symbol_replacement)
                .setView(input)
                .setPositiveButton(R.string.action_replace_symbol,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inputText = input.getText().toString();
                                String message;

                                if (inputText.length() != 0) {
                                    if (mDFA.replaceSymbol(oldChar, inputText.charAt(0))) {
                                        message = "\""
                                                + oldChar
                                                + "\""
                                                + getResources().getString(
                                                        R.string.msg_concat_symbol_replaced);
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        message = "\""
                                                + inputText
                                                + "\""
                                                + getResources().getString(
                                                        R.string.msg_concat_symbol_exists);
                                    }
                                    Tool.createToast(getActivity(), message, Toast.LENGTH_SHORT);
                                }
                            }
                        })
                .show();
    }

}
