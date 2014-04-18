package com.powerall.plugin.video;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

//import com.phonegap.api.Plugin;
//import com.phonegap.api.PluginResult;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

//import org.apache.cordova.api.Plugin;
//import org.apache.cordova.api.PluginResult;

public class VideoPlayPlugin extends CordovaPlugin {

	private static final String YOU_TUBE = "youtube.com";
	private static final String ASSETS = "file:///android_asset/";

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
	        android.util.Log.d("VideoPlay","action=>"+action);

			if (action.equals("playVideo")) {
				callbackContext.success();
				playVideo(args.getString(0));
				return true;
	    }
	        return false;
	}

	private void playVideo(String url){
		Uri uri = Uri.parse(url);
		Intent intent = null;
		// Check to see if someone is trying to play a YouTube page.
		if (url.contains(YOU_TUBE)) {
			// If we don't do it this way you don't have the option for youtube
			uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));
			intent = new Intent(Intent.ACTION_VIEW, uri);
		} else if (url.contains(ASSETS)) {
			// get file path in assets folder
			try{
				String filepath = url.replace(ASSETS, "");
				// get actual filename from path as command to write to internal
				// storage doesn't like folders
				String filename = filepath.substring(filepath.lastIndexOf("/") + 1,
						filepath.length());
				// Don't copy the file if it already exists
				File fp = new File(cordova.getActivity().getFilesDir() + "/"
						+ filename);
				if (!fp.exists()) {
					this.copy(filepath, filename);
				}
				// change uri to be to the new file in internal storage
				uri = Uri
						.parse("file://" + cordova.getActivity().getFilesDir()
								+ "/" + filename);
				// Display video player
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(uri, "video/*");
			}catch(IOException e){
				Log.d("VideoPlay", e.toString());
			}			
		} else {
			// Display video player
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(uri, "video/*");
		}
		cordova.getActivity().startActivity(intent);

	}

	private void copy(String fileFrom, String fileTo) throws IOException {																			
		InputStream in = cordova.getActivity().getAssets().open(fileFrom);
		FileOutputStream out = cordova.getActivity().openFileOutput(
				fileTo, Context.MODE_WORLD_READABLE);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0)
			out.write(buf, 0, len);
		in.close();
		out.close();
	}
}