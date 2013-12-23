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

package com.seedform.dfatester.util;

import com.seedform.dfatester.dfa.DFA;
import com.seedform.dfatester.dfa.State;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * A simple and fast way to share <tt>DFA</tt> data between fragments and
 * activities throughout the application.
 * 
 * @author Shudmanul Chowdhury
 */
public final class Data {

    private static final String DATA_FILE_NAME = "dfa.db";

    private static ArrayList<DFA> sDFAs;
    private static DFA sDFA;
    private static State sState;
    private static File sSaveFile;

    /**
     * Opens and loads previously saved instances of <tt>DFA</tt>.
     */
    @SuppressWarnings("unchecked")
    public static void loadData() {
        sSaveFile = new File(Tool.getAppContext().getFilesDir(), DATA_FILE_NAME);
        try {
            FileInputStream fileIn = new FileInputStream(sSaveFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            sDFAs = (ArrayList<DFA>) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception e) {
            sDFAs = new ArrayList<DFA>();
        }
    }

    /**
     * Saves all instances of <tt>DFA</tt> to a file.
     */
    public static void saveData() {
        try {
            FileOutputStream fileOut = new FileOutputStream(sSaveFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(sDFAs);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return All instances of <tt>DFA</tt>, or <tt>null</tt> if
     *         <tt>loadData()</tt> has not been called.
     */
    public static ArrayList<DFA> getDFAs() {
        return sDFAs;
    }

    /**
     * @return The current <tt>DFA</tt>.
     */
    public static DFA getSelectedDFA() {
        return sDFA;
    }

    /**
     * @return The current <tt>State</tt>.
     */
    public static State getSelectedState() {
        return sState;
    }

    /**
     * Sets the <tt>DFA</tt> at the specified index to be the current
     * <tt>DFA</tt>.
     * 
     * @param index The index of the new current <tt>DFA</tt>.
     */
    public static void selectDFA(int index) {
        sDFA = sDFAs.get(index);
    }

    /**
     * Sets the <tt>State</tt> at the specified index to be the current
     * <tt>State</tt>.
     * 
     * @param index The index of the new current <tt>State</tt>.
     */
    public static void selectState(int index) {
        sState = sDFA.getStates().get(index);
    }

}
