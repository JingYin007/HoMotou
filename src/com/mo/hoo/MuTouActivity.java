package com.mo.hoo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mo.adapter.ChatMessageAdapter;
import com.mo.bean.ChatMessage;
import com.mo.bean.ChatMessage.Type;
import com.mo.util.HttpUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MuTouActivity extends Activity {

	public static String toMsg="";
	private ListView mMsgs;
	private ChatMessageAdapter mAdapter;
	private List<ChatMessage> mDatas;
	private TextView tv_back;
	public static EditText mInputMsg;
	private Button mSendMsg;
	private Button iv_voice;
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			// 等待接收，子线程完成数据的返回
			ChatMessage fromMessge = (ChatMessage) msg.obj;
			mDatas.add(fromMessge);
			mAdapter.notifyDataSetChanged();
			mMsgs.setSelection(mDatas.size()-1);
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.motou_view);

		initView();
		initDatas();
		// 初始化事件
		initListener();
	}

	private void initListener()
	{
		mSendMsg.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				toMsg = mInputMsg.getText().toString();
				if (TextUtils.isEmpty(toMsg))
				{
					Toast.makeText(MuTouActivity.this, "发送消息不能为空！",
							Toast.LENGTH_SHORT).show();
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
		
		tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.hold,R.anim.push_right_out);
			}
		});
	}

	private void initDatas()
	{
		mDatas = new ArrayList<ChatMessage>();
		mDatas.add(new ChatMessage("笨蛋，找我干嘛啊？", Type.INCOMING, new Date()));
		mAdapter = new ChatMessageAdapter(this, mDatas);
		mMsgs.setAdapter(mAdapter);
		iv_voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 初始化，再加载
				//MyRecognizerDialogLister.text="";
				VoiceToWord voice = new VoiceToWord(MuTouActivity.this,"534e3fe2");
				voice.GetWordFromVoice();
			}
		});
	}

	private void initView()
	{
		mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
		mInputMsg = (EditText) findViewById(R.id.id_input_msg);
		mSendMsg = (Button) findViewById(R.id.id_send_msg);
		tv_back=(TextView) findViewById(R.id.tv_back);
		iv_voice=(Button) findViewById(R.id.iv_voice);
	}

}
