package com.test.fbshare;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class MyApplication extends Application {
	private static final String FB_APP_ID = "1711453775745803";

	@Override
	public void onCreate() {

		super.onCreate();

		// Parse.initialize(this, "8wXXQcqgdlF5IZ4bbQ2x6jVlCP6a59TRo2MKDsVm",
		// "IVmzTcy3qnhi2cWcExtLZ7rI21xGx63xfV7hM90l");
		Parse.initialize(this, "fy6nHyWuCJtnH0w2bPaCY4GO9e5F7UnDm1bz81oA", "uXCrbxPSX4YDEQkPuitrmBB1nF7WFOslT1cNWea1");
		ParseFacebookUtils.initialize(FB_APP_ID);
	}

}
