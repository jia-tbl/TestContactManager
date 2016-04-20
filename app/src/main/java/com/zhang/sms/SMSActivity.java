package com.zhang.sms;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zhang.contactmanager.R;
import com.zhang.messagebox.MessageBoxListActivity;

public class SMSActivity extends Activity {
    private ListView smsListView;
    private SMSAdpter smsAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        smsListView = (ListView) findViewById(R.id.sms_list);

        smsAdpter = new SMSAdpter(SMSActivity.this);
        RexseeSMS rsms = new RexseeSMS(SMSActivity.this);
        List<SMSBean> list_mmt = rsms.getThreadsNum(rsms.getThreads(0));
        smsAdpter.assignment(list_mmt);

        smsListView.setAdapter(smsAdpter);
        smsListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (smsAdpter.getItem(position) == null) {
                    Log.e("AA", "---------------TXT");
                    return;
                }
                SMSBean sb = (SMSBean) smsAdpter.getItem(position);

                Intent intent = new Intent(SMSActivity.this,
                        MessageBoxListActivity.class);
                intent.putExtra("threadId", sb.getThread_id());
                startActivityForResult(intent, 0);
            }
        });
        TextView tv = new TextView(this);
        tv.setText("hahhhahaha");
        tv.setTextColor(Color.WHITE);
        smsListView.addFooterView(tv);
    }
}
