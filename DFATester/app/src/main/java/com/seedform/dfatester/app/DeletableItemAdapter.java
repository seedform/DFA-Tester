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

package com.seedform.dfatester.app;

import java.util.List;

import com.seedform.dfatester.R;
import com.seedform.dfatester.util.Consts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DeletableItemAdapter<T> extends BasicAdapter<T> {

    private OnDeleteCallback mCallback;

    public DeletableItemAdapter(List<T> data, Context context, OnDeleteCallback callback) {
        super(data, context);
        mCallback = callback;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView == null
                ? mInflater.inflate(R.layout.deletable_list_item, null) : convertView;
        TextView textRow1 = (TextView) row.findViewById(R.id.textview_list_item_1);
        TextView textRow2 = (TextView) row.findViewById(R.id.textview_list_item_2);
        Button deleteButton = (Button) row.findViewById(R.id.button_delete);

        T item = getItem(position);
        final int split = item.toString().indexOf(Consts.SPLIT);

        if (split > 0) {
            textRow1.setText(item.toString().substring(0, split));
            textRow2.setText(item.toString().substring(split + 1));
        } else {
            textRow1.setText(item.toString());
            ((ViewGroup) row).removeView(textRow2);
        }

        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setView(null)
                        .setPositiveButton(
                                mContext.getResources().getString(R.string.action_confirm_delete),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mCallback.onClickDelete(position);
                                        notifyDataSetChanged();
                                    }
                                })
                        .show();
            }
        });
        return row;
    }

    /**
     * The callback interface used by {@link DeleteableItemAdapter} to inform
     * its client about a delete action requested on a list item at its
     * position.
     */
    public static interface OnDeleteCallback {

        /**
         * Executes an action when the delete button is clicked.<br>
         * <tt>notifyDataSetChanged()</tt> is called immediately after this
         * method is executed.
         * @param index The index of the item to be deleted.
         */
        public void onClickDelete(int index);

    }

}
