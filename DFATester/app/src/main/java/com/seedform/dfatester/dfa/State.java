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

package com.seedform.dfatester.dfa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author Shudmanul Chowdhury
 */
public class State implements Serializable {

    private static final long serialVersionUID = -3582751102544837830L;

    private boolean mIsAcceptingState;
    private int mId;
    private TreeMap<Character, State> mTransitions;

    /**
     * Constructs a new DFA <tt>State</tt>.
     * 
     * @param id The id of this <tt>State</tt>.
     * @param accepting <tt>true</tt> if this state is an accepting state,
     *            <tt>false</tt> otherwise.
     * @param alphabet The alphabet of the DFA.
     */
    protected State(int id, boolean isAcceptingState, ArrayList<Character> alphabet) {
        mId = id;
        mIsAcceptingState = isAcceptingState;
        mTransitions = new TreeMap<Character, State>();

        for (char s : alphabet) {
            addTransition(s);
        }
    }

    /**
     * Adds a new possible transition to this <tt>State</tt>. Called when a new
     * symbol is added to the alphabet.
     * 
     * @param symbol The new added symbol.
     */
    protected void addTransition(char symbol) {
        mTransitions.put(symbol, this);
    }
    
    /**
     * Removes the specified transition from this <tt>State</tt>.
     * @param symbol The symbol of the transition to remove.
     */
    protected void removeTransition(char symbol) {
        mTransitions.remove(symbol);
    }

    /**
     * Returns the next state given the symbol for the transition.
     * 
     * @param symbol The next symbol in the test <tt>String</tt>.
     * @return The corresponding <tt>State</tt> of the given symbol from this
     *         <tt>State</tt>, <tt><null</tt> if the transition doesn't exist.
     */
    public State getNextState(Character symbol) {
        return mTransitions.get(symbol);
    }

    /**
     * Sets a specified id for this <tt>State</tt>.
     * 
     * @param name
     */
    public void setId(int id) {
        mId = id;
    }

    /**
     * @return The id of this <tt>State</tt>.
     */
    public int getId() {
        return mId;
    }

    /**
     * @return <tt>true</tt> if this state is an accepting state, <tt>false</tt>
     *         otherwise.
     */
    public boolean isAcceptingState() {
        return mIsAcceptingState;
    }

    /**
     * Adds a transition for this <tt>State</tt>.
     * 
     * @param symbol The symbol that leads to the next <tt>State</tt>.
     * @param nextState The destination <tt>State</tt>.
     */
    public void setTransition(Character symbol, State nextState) {
        mTransitions.put(symbol, nextState);
    }

    /**
     * @param accepting <tt>true</tt> if this <tt>State</tt> is an accepting
     *            state, <tt>false</tt> otherwise.
     */
    public void setAsAcceptingState(boolean accepting) {
        mIsAcceptingState = accepting;
    }

    @Override
    public String toString() {
        return String.format("State %d|%s%s",
                mId,
                mId == 0 ? "Initial" : "",
                mIsAcceptingState ? (mId == 0 ? ", " : "") + "Accepting" : (mId == 0 ? "" : "-"));
    }

}
