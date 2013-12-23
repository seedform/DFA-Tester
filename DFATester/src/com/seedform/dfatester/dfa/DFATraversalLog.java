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

public class DFATraversalLog {

    private boolean mAccepted;
    private String mMessage;
    private String mLog;

    protected DFATraversalLog() {
        mAccepted = false;
        mMessage = "REJECTED";
        mLog = "";
    }

    protected void setAccepted(boolean accepted) {
        mAccepted = accepted;
        mMessage = accepted ? "ACCEPTED" : mMessage;
    }

    protected void setBadTransition(char c) {
        mMessage = "ERROR";
        mLog = String.format("ERROR: \"%c\" is not in the alphabet!\n\n%s\t --( %c )--> [ERROR]\n",
                c, mLog, c);
    }

    protected void startRecord() {
        mLog += "START: State 0\n";
    }

    protected void finishRecord() {
        mLog += "END";
    }

    protected void recordTransition(char transitionChar, State destState) {
        mLog += String.format("\t --( %c )--> State %d\n", transitionChar, destState.getId());
    }

    public boolean isAccepted() {
        return mAccepted;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getTransitionHistory() {
        return mLog;
    }

}
