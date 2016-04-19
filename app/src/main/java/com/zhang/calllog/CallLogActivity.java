package com.zhang.calllog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.ListView;

import com.zhang.contactmanager.R;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class CallLogActivity extends Activity {
	private ListView callLogListView;
	private AsyncQueryHandler asyncQuery;
	private DialAdapter adapter;
	private List<CallLogBean> callLogs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_log);

		callLogListView = (ListView) findViewById(R.id.call_log_list);
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		init();
	}

	private void init() {
		Uri uri = android.provider.CallLog.Calls.CONTENT_URI;

		String[] projection = {
				CallLog.Calls.DATE, 
				CallLog.Calls.NUMBER,
				CallLog.Calls.TYPE, 
				CallLog.Calls.CACHED_NAME,
				CallLog.Calls._ID, };
		
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);
	}

	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				callLogs = new ArrayList<CallLogBean>();

				SimpleDateFormat sfd = new SimpleDateFormat("MM-dd hh:mm");

				Date date;

				cursor.moveToFirst();

				for (int i = 0; i < cursor.getCount(); i++) {

					cursor.moveToPosition(i);

					date = new Date(cursor.getLong(cursor
							.getColumnIndex(CallLog.Calls.DATE)));
					String number = cursor.getString(cursor
							.getColumnIndex(CallLog.Calls.NUMBER));
					int type = cursor.getInt(cursor
							.getColumnIndex(CallLog.Calls.TYPE));
					String cachedName = cursor.getString(cursor
							.getColumnIndex(CallLog.Calls.CACHED_NAME));
					int id = cursor.getInt(cursor
							.getColumnIndex(CallLog.Calls._ID));

					CallLogBean callLogBean = new CallLogBean();

					callLogBean.setId(id);
					callLogBean.setNumber(number);
					callLogBean.setName(cachedName);
					if (null == cachedName || "".equals(cachedName)) {
						callLogBean.setName(number);
					}
					callLogBean.setType(type);
					callLogBean.setDate(sfd.format(date));

					callLogs.add(callLogBean);
				}
				if (callLogs.size() > 0) {
					setAdapter(callLogs);
				}
			}
			super.onQueryComplete(token, cookie, cursor);
		}
	}

	private void setAdapter(List<CallLogBean> callLogs) {
		adapter = new DialAdapter(this, callLogs);
		callLogListView.setAdapter(adapter);
	}

}
