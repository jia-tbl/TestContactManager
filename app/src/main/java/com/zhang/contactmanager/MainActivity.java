package com.zhang.contactmanager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhang.calllog.CallLogActivity;
import com.zhang.contacts.ContactActivity;
import com.zhang.progressbar.ProgressAct;
import com.zhang.sms.SMSActivity;

@SuppressLint({ "HandlerLeak", "UseSparseArrays" })
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void openContact(View view) {
		startActivityForResult(new Intent(this, ContactActivity.class), 0);
	}

	public void openCallLog(View view) {
		startActivityForResult(new Intent(this, CallLogActivity.class), 0);
	}

	public void openSMSActivity(View view) {
		startActivityForResult(new Intent(this, SMSActivity.class), 0);
	}
	public void openSeekBarActivity(View view) {
		startActivityForResult(new Intent(this, ProgressAct.class), 0);
	}
}
