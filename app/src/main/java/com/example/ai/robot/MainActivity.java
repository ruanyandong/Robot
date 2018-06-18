package com.example.ai.robot;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ai.robot.adapter.ChatMessageAdapter;
import com.example.ai.robot.bean.ChatMessage;
import com.example.ai.robot.utils.HttpUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private static ChatMessageAdapter mAdapter;
    private static List<ChatMessage> mDatas;

    private EditText mInputMsg;

    private Button mSendMsg;

    private static ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    /**
     *  /**
     * 声明静态内部类不会持有外部类的隐式引用
     */

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                ChatMessage fromMessage =(ChatMessage) msg.obj;
                mDatas.add(fromMessage);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private final MyHandler mHandler = new MyHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDatas();

        initListener();

    }

    private void initListener() {
        mSendMsg.setOnClickListener(view->{
            String toMsg = mInputMsg.getText().toString();

            if (TextUtils.isEmpty(toMsg)){
                Toast.makeText(MainActivity.this,"发送消息不能为空",Toast.LENGTH_LONG).show();
                return;
            }

            ChatMessage toMessage = new ChatMessage(toMsg, ChatMessage.Type.OUTCOMING,new Date());
            mDatas.add(toMessage);
            mAdapter.notifyDataSetChanged();
            mInputMsg.setText("");

            Thread thread =new Thread(){
                @Override
                public void run() {
                    ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
                    Message message = Message.obtain();
                    message.obj = fromMessage;
                    mHandler.sendMessage(message);
                }
            };
            mExecutorService.execute(thread);

        });
    }

    private void initDatas() {
        mDatas = new ArrayList<ChatMessage>();
        mDatas.add(new ChatMessage("你好，小幕为你服务",ChatMessage.Type.INCOMING,new Date()));
        mAdapter = new ChatMessageAdapter(MainActivity.this,mDatas);
        mListView.setAdapter(mAdapter);

    }

    private void initView() {

        mListView = findViewById(R.id.id_listView_msg);
        mInputMsg = findViewById(R.id.id_input_msg);
        mSendMsg = findViewById(R.id.id_send_msg);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除消息
        mHandler.removeCallbacksAndMessages(null);

    }
}
