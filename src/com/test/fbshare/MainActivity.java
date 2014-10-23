package com.test.fbshare;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.PendingCall;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class MainActivity extends Activity implements OnClickListener {

	private UiLifecycleHelper uiHelper;
	Session session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);
		Button fb = (Button) findViewById(R.id.btnFbShare);
		fb.setOnClickListener(this);
		Button login = (Button) findViewById(R.id.btnLogin);
		login.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Session.getActiveSession().onActivityResult(this, requestCode,
		// resultCode, data);
		Log.e(">>>>>", "in onactivity result");

		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {

			public void onError(PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("Activity", String.format("Error: %s", error.toString()));

			}

			public void onComplete(PendingCall pendingCall, Bundle data) {
				Log.e("Activity", "Success!");

			}
		});

		// if (ParseFacebookUtils.getSession() != null) {
		if (requestCode == com.facebook.Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE) {
			ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		uiHelper.onDestroy();
	}

	public void onClickFBLogin() {
		Log.e(">>>>>>>>>>", "On fb login click");
		List<String> permissions = Arrays.asList("public_profile", "email");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {

			@Override
			public void done(ParseUser user, ParseException e) {
				if (user == null) {
					 Log.e("FAILURE", "User denied for facebook login" +
					 e.getMessage());
					 e.printStackTrace();

				} else if (user.isNew()) {
					// call facebook parse session here and get me object data
					// and put to parse user
					Log.e("SUCEESS", "New Facebook logged in user");
					NewPermissionsRequest nRequest=new NewPermissionsRequest(MainActivity.this,"publish_actions");
					ParseFacebookUtils.getSession().requestNewPublishPermissions(nRequest);
					ParseFacebookUtils.saveLatestSessionData(user);

				} else {
					// 1.call facebook parse session here and get me object data
					// and put to parse user

					Log.e("SUCESS", "Already existing user for facebook login");

				}

			}
		});

	}

	protected void shareToFb() {
		session = ParseFacebookUtils.getSession();
		if (session != null) {
			Log.e(">>>>>", "session not null"+session.isOpened());
			if (session.isOpened()) {
				Log.e(">>>>>", "session is opened");
				/*FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this).setName("Title:Nexus9 price")
						.setDescription("Nexus 9 price would be starting from $399")
						.setPicture("http://i58.tinypic.com/2iqkg04.png")
						.setLink("https://www.androidauthority.com/nexus-9-specs-features-price-availability-538604/")
						.setApplicationName("Devotify").build();
				uiHelper.trackPendingDialogCall(shareDialog.present());*/
				postPic(session);
			}
		}

	}
	private void postPic(Session session)
	{
		Bundle params = new Bundle();
		params.putString("url","http://i58.tinypic.com/2iqkg04.png");
		params.putString("message","this is a test photo for posting an image and text");
		if(session==null)
			Log.e("response","session null");
		else
			Log.e("ac",session.getAccessToken().toString());
		Request request=new Request(Session.getActiveSession(),"/me/photos",params,HttpMethod.POST,new Request.Callback() {
			
			@Override
			public void onCompleted(Response response) {
				Log.e("response",response.toString());
				Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
				
			}
		});
		request.executeAsync();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.btnFbShare) {
			try {
				PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
				for (Signature signature : info.signatures) {
					MessageDigest md = MessageDigest.getInstance("SHA");
					md.update(signature.toByteArray());
					Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();

			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			// onClickFBLogin();
			shareToFb();
		} else if (v.getId() == R.id.btnLogin) {
			onClickFBLogin();
		}

	}
}
