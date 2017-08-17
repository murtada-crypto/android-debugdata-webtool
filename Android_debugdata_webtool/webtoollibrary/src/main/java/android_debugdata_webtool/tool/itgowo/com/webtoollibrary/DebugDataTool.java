/*
 *
 *  *    Copyright (C) 2016 Amit Shekhar
 *  *    Copyright (C) 2011 Android Open Source Project
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package android_debugdata_webtool.tool.itgowo.com.webtoollibrary;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

import android_debugdata_webtool.tool.itgowo.com.webtoollibrary.server.ClientServer;
import android_debugdata_webtool.tool.itgowo.com.webtoollibrary.utils.NetworkUtils;

/**
 * Created by amitshekhar on 15/11/16.
 */

public class DebugDataTool {

    private static final String TAG = DebugDataTool.class.getSimpleName();
    private static final int DEFAULT_PORT = 8080;
    private static ClientServer clientServer;
    private static String addressLog = "not available";
    private static onDebugToolListener mToolListener;

    private DebugDataTool() {
    }

    public static String ObjectToJson(Object mO) {
        if (mO != null) {
            if (mToolListener == null) {
                new Throwable("initialize:onDebugToolListener = null").printStackTrace();
                return "";
            }
            return mToolListener.onObjectToJson(mO);
        }
        return "";
    }

    public static <T> T JsonToObject(String mJsonString, Class<T> mClass) {
        if (mJsonString != null && mClass != null) {
            if (mToolListener == null) {
                new Throwable("initialize:onDebugToolListener = null").printStackTrace();
                return null;
            }
            return mToolListener.onJsonStringToObject(mJsonString, mClass);
        }
        return null;
    }

    public static void onRequest(String mS,String url) {
        if (mS == null || mS.equals("")) {
            return;
        }
        if (mToolListener == null) {
            new Throwable("initialize:onDebugToolListener = null").printStackTrace();
        } else {
            mToolListener.onGetRequest(mS,url);
        }
    }

    public static void onResponse(String mS) {
        if (mToolListener == null) {
            new Throwable("initialize:onDebugToolListener = null").printStackTrace();
        } else {
            mToolListener.onResponse(mS);
        }
    }

    public static void initialize(Context context, int mPortNumber, onDebugToolListener mOnDebugToolListener) {
        mToolListener = mOnDebugToolListener;
        int portNumber;
        if (mPortNumber < 10) {
            portNumber = DEFAULT_PORT;
        } else {
            portNumber = mPortNumber;
        }
        clientServer = new ClientServer(context, portNumber);
        clientServer.start();
        addressLog = NetworkUtils.getAddressLog(context, portNumber);
        Log.d(TAG, addressLog);
    }

    public static String getAddressLog() {
        Log.d(TAG, addressLog);
        return addressLog;
    }

    public static void shutDown() {
        if (clientServer != null) {
            clientServer.stop();
            clientServer = null;
        }
    }

    public static void setCustomDatabaseFiles(HashMap<String, File> customDatabaseFiles) {
        if (clientServer != null) {
            clientServer.setCustomDatabaseFiles(customDatabaseFiles);
        }
    }

    public static boolean isServerRunning() {
        return clientServer != null && clientServer.isRunning();
    }

}
