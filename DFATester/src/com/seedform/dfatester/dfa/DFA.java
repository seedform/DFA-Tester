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
import java.util.Collections;

public class DFA implements Serializable {
	
	private static final long serialVersionUID = -4672071582913351182L;
	
	private int mId;
	private ArrayList<Character> mAlphabet;
	private ArrayList<State> mStates;
	
	/**
	 * Constructs a <tt>DFA</tt> with an empty <tt>Alphabet</tt>.
	 */
	public DFA(int id) {
		mId = id;
		mAlphabet = new ArrayList<Character>();
		mStates = new ArrayList<State>();
	}
	
	/**
	 * Adds a symbol to the alphabet of this <tt>DFA</tt>. Sorted automatically.
	 * @param symbol The symbol to be added to the alphabet of this <tt>DFA</tt>.
	 * @return <tt>true</tt> if the symbol was added, <tt>false</tt> otherwise;
	 */
	public boolean addSymbol(char symbol) {
		if (mAlphabet.contains(symbol)) return false;
		mAlphabet.add(symbol);
		for(State state : mStates) {
		    state.addTransition(symbol);
		}
		Collections.sort(mAlphabet);
		return true;
	}

    /**
     * Removes the symbol at the specified index in the alphabet of this
     * <tt>DFA</tt>.
     * 
     * @param index The symbol to be removed.
     * @return The removed symbol.
     */
    public Character removeSymbol(int index) {
        // remove this transition from each state
        for (State state : mStates) {
            state.removeTransition(mAlphabet.get(index));
        }
        return mAlphabet.remove(index);
    }

    /**
     * Replaces a symbol with the specified symbol.
     * 
     * @param oldSymbol The symbol to replace.
     * @param newSymbol The new symbol.
     * @return <tt>true</tt> if the symbol was replaced, <tt>false</tt> if the
     *         new symbol already exists within the alphabet.
     */
    public boolean replaceSymbol(Character oldSymbol, Character newSymbol) {
        if (mAlphabet.contains(newSymbol)) {
            return false;
        } else {
            addSymbol(newSymbol);
            for (State s : mStates) {
                State next = s.getNextState(oldSymbol);
                s.removeTransition(oldSymbol);
                s.setTransition(newSymbol, next);
            }
            mAlphabet.remove(oldSymbol);
            return true;
        }
    }
	
	/**
	 * Adds a <tt>State</tt> to this <tt>DFA</tt>.
	 */
	public void addState() {
		//int id = mStates.isEmpty() ? 0 : mStates.get(mStates.size() - 1).getId() + 1;
    	mStates.add(new State(mStates.size(), false, getAlphabet()));
	}

    /**
     * Removes a <tt>State</tt> from this <tt>DFA</tt> at the given index.
     * 
     * @param index The index of the <tt>State</tt> to be removed.
     * @return The removed <tt>State</tt>.
     */
	public State removeState(int index) {
		State removed = mStates.remove(index);
		for (int i = 0; i < mStates.size(); i++) {
			State state = mStates.get(i);
			state.setId(i);
			
			// fix transitions that referred to the removed state
			for (char symbol: mAlphabet) {
				if (state.getNextState(symbol).getId() == removed.getId()){
					state.setTransition(symbol, state);
				}
			}
		}
		return removed;
	}
	
    /**
     * Tests a given <tt>String</tt> on this <tt>DFA</tt>.
     * 
     * @param test The <tt>String</tt> to test.
     * @return A <tt>DFATraversalLog</tt> containing all info about the test.
     */
	public DFATraversalLog testString(String test) {
		DFATraversalLog log = new DFATraversalLog();
		log.startRecord();
		if (test.length() == 0){
			log.setAccepted(mStates.get(0).isAcceptingState());
		} else {		
			char nextChar;
			int index = 0;
			State currentState = mStates.get(index);
			while(currentState!= null && index < test.length()) {
				nextChar = test.charAt(index);
				currentState = currentState.getNextState(nextChar);
				if (currentState != null) {
				    log.recordTransition(nextChar, currentState);
	                index++;
				} else{
				    log.setBadTransition(nextChar);
				}
			}
			if (currentState != null) log.setAccepted(currentState.isAcceptingState());
		}
		log.finishRecord();
		return log;
	}
	
	/**
	 * @return The list of <tt>States</tt> used by this <tt>DFA</tt>.
	 */
	public ArrayList<State> getStates() {
		return mStates;
	}
	
	/**
	 * @return The symbols in the alphabet of this <tt>DFA</tt>.
	 */
	public ArrayList<Character> getAlphabet() {
		return mAlphabet;
	}
	
	/**
	 * @return the id of this <tt>DFA</tt>
	 */
	public int getId() {
		return mId;
	}
	
	@Override
	public String toString() {
		return String.format("DFA %d|%d state%s", mId, mStates.size(), 
		        mStates.size() == 1 ? "" : "s");
	}

}
