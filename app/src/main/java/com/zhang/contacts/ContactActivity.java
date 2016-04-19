package com.zhang.contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;

import com.zhang.contactmanager.QuickAlphabeticBar;
import com.zhang.contactmanager.R;

@SuppressLint({ "UseSparseArrays", "HandlerLeak" })
public class ContactActivity extends Activity {
	private ContactListAdapter adapter;
	private ListView contactList;
	private List<ContactBean> list;
	// 异步查询数据库类对象
	private AsyncQueryHandler asyncQueryHandler;
	// 快速索引条
	private QuickAlphabeticBar alphabeticBar;

	private Map<Integer, ContactBean> contactIdMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		contactList = (ListView) findViewById(R.id.contact_list);

		alphabeticBar = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);

		// 实例化
		asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());

		init();

	}

	private String contactId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
	private String name = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
	private String number = ContactsContract.CommonDataKinds.Phone.NUMBER;
	private String photoId = ContactsContract.CommonDataKinds.Phone.PHOTO_ID;
	private String lookUpKey = ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY;
	private String sortKey;

	private void init() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

		/**
		 * android 4.4以上修改sort_key为汉字，拼音存放在phonebook_label
		 */
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			sortKey = "phonebook_label";
		} else {
			sortKey = ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY;
		}

		// 查询的字段
		String[] projection = { contactId, name, number, sortKey, photoId,
				lookUpKey };

		// 根据本地语言进行排序
		asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");

	}

	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				contactIdMap = new HashMap<Integer, ContactBean>();
				list = new ArrayList<ContactBean>();

				cursor.moveToFirst();

				for (int i = 0; i < cursor.getCount(); i++) {

					cursor.moveToPosition(i);

					String resultName = cursor.getString(cursor
							.getColumnIndex(name));
					String resultNumber = cursor.getString(cursor
							.getColumnIndex(number));
					String resultSortKey = cursor.getString(cursor
							.getColumnIndex(sortKey));
					int resultContactId = cursor.getInt(cursor
							.getColumnIndex(contactId));
					Long resultPhotoId = cursor.getLong(cursor
							.getColumnIndex(photoId));
					String resultLookUpKey = cursor.getString(cursor
							.getColumnIndex(lookUpKey));

					if (contactIdMap.containsKey(contactId)) {
						continue;
					}

					ContactBean contact = new ContactBean();

					contact.setContactId(resultContactId);
					contact.setDesplayName(resultName);
					contact.setPhoneNum(resultNumber);
					contact.setSortKey(resultSortKey);
					contact.setPhotoId(resultPhotoId);
					contact.setLookUpKey(resultLookUpKey);

					list.add(contact);
					contactIdMap.put(resultContactId, contact);

				}
				if (list.size() > 0) {
					setAdapter(list);
				}
			}
			super.onQueryComplete(token, cookie, cursor);
		}

	}

	private void setAdapter(List<ContactBean> list) {
		adapter = new ContactListAdapter(this, list, alphabeticBar);
		contactList.setAdapter(adapter);

		alphabeticBar.init(ContactActivity.this);
		alphabeticBar.setListView(contactList);
		alphabeticBar.setHight(alphabeticBar.getHeight());
		alphabeticBar.setVisibility(View.VISIBLE);
	}
}