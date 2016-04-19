package com.zhang.contacts;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.zhang.contactmanager.QuickAlphabeticBar;
import com.zhang.contactmanager.R;

@SuppressLint({ "DefaultLocale", "InflateParams" })
public class ContactListAdapter extends BaseAdapter {
	private Context ctx;
	private LayoutInflater inflater;
	private List<ContactBean> list;

	// 存储每个章节
	private String[] sections;
	// 字母索引
	private HashMap<String, Integer> alphaIndexer;

	public ContactListAdapter(Context context, List<ContactBean> list,
			QuickAlphabeticBar alpha) {
		this.ctx = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);

		this.alphaIndexer = new HashMap<String, Integer>();

		for (int i = 0; i < list.size(); i++) {
			String key = getFristAlpha(list.get(i).getSortKey());
			if (!alphaIndexer.containsKey(key)) {
				alphaIndexer.put(key, i);
			}
		}

		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);

		alpha.setAlphaIndexer(alphaIndexer);
	}

	private String getFristAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		String key = str.trim().substring(0, 1).toUpperCase();

		if (key.matches("[A-Z]")) {
			return key;
		} else {
			return "#";
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(int position) {
		list.remove(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contact_list_item, null);
			holder = new ViewHolder();
			holder.quickContactBadge = (QuickContactBadge) convertView
					.findViewById(R.id.qcb);
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ContactBean contact = list.get(position);
		String name = contact.getDesplayName();
		String number = contact.getPhoneNum();

		holder.name.setText(name);
		holder.number.setText(number);

		holder.quickContactBadge.assignContactUri(Contacts.getLookupUri(
				contact.getContactId(), contact.getLookUpKey()));

		if (0 == contact.getPhotoId()) {
			holder.quickContactBadge.setImageResource(R.drawable.ic_launcher);
		} else {
			Uri uri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI,
					contact.getContactId());
			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(ctx.getContentResolver(), uri);
			Bitmap contactPhoto = BitmapFactory.decodeStream(input);
			holder.quickContactBadge.setImageBitmap(contactPhoto);
		}

		// 当前字母
		String currentStr = getFristAlpha(contact.getSortKey());
		// 前面的字母
		String previewStr = (position - 1) >= 0 ? getFristAlpha(list.get(
				position - 1).getSortKey()) : " ";

		if (!previewStr.equals(currentStr)) {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		return convertView;
	}

	private static class ViewHolder {
		QuickContactBadge quickContactBadge;
		TextView alpha;
		TextView name;
		TextView number;
	}

}
