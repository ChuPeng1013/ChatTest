package com.example.chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.ChatMessage.Type;
import com.example.chat.utils.HttpUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	
	private ListView mMsgs;
	private ChatMessageAdapter mAdapter;
	private List<ChatMessage> mDatas;
	
	private EditText mInputMsg;
	private Button mSendMsg;
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			//等待接收子线程数据的返回
			ChatMessage fromMessage = (ChatMessage) msg.obj;
			mDatas.add(fromMessage);
			mAdapter.notifyDataSetChanged();
			mMsgs.setSelection(mDatas.size()-1);
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		
		initView();
		initDatas();
		
		initListener();
		
	}

	private void initListener() 
	{
		mSendMsg.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				final String toMsg = mInputMsg.getText().toString();
				if(TextUtils.isEmpty(toMsg))
				{
					Toast.makeText(MainActivity.this, "发送消息不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				ChatMessage toMessage = new ChatMessage();
				toMessage.setDate(new Date());
				toMessage.setMsg(toMsg);
				toMessage.setType(Type.OUTCOMING);
				mDatas.add(toMessage);
				mAdapter.notifyDataSetChanged();
				mMsgs.setSelection(mDatas.size()-1);
				
				mInputMsg.setText("");
				new Thread()
				{
					public void run() 
					{
						ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
						Message m = Message.obtain();
						m.obj = fromMessage;
						mHandler.sendMessage(m);
						
					};
				}.start();
				
			}
		});
	}

	private void initDatas() 
	{
		mDatas = new ArrayList<ChatMessage>();
		mDatas.add(new ChatMessage("你好，小鹏为您服务", Type.INCOMING, new Date()));
		//mDatas.add(new ChatMessage("你好", Type.OUTCOMING, new Date()));
		mAdapter = new ChatMessageAdapter(this, mDatas);
		
		mMsgs.setAdapter(mAdapter);
	}

	private void initView() 
	{
		mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
		mInputMsg = (EditText) findViewById(R.id.id_input_msg);
		mSendMsg = (Button) findViewById(R.id.id_send_msg);
	}
}












