package com.example.ai.robot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.ai.robot.R;
import com.example.ai.robot.bean.ChatMessage;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {

    private Context context;
    private List<ChatMessage> mDatas;
    private LayoutInflater mInflater;


    public ChatMessageAdapter(Context context, List<ChatMessage> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMessage chatMessage = mDatas.get(position);
        int type = getItemViewType(position);

        if (convertView == null){
            if (type == 0){
                convertView = mInflater.inflate(R.layout.item_from_msg,parent,false);
            }else{
                convertView = mInflater.inflate(R.layout.item_to_msg,parent,false);
            }
        }
        /**
         * 转化时间
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(chatMessage.getDate());

        if (type == 0){
            TextView date = ViewHolder.getView(convertView,R.id.id_from_msg_date);
            TextView msg = ViewHolder.getView(convertView,R.id.id_from_msg_info);

            date.setText(dateStr);
            msg.setText(chatMessage.getMsg());
        }else{
            TextView date = ViewHolder.getView(convertView,R.id.id_to_msg_date);
            TextView msg = ViewHolder.getView(convertView,R.id.id_to_msg_info);

            date.setText(dateStr);
            msg.setText(chatMessage.getMsg());
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mDatas.get(position);
        if (chatMessage.getType() == ChatMessage.Type.INCOMING){
            return 0;
        }

        return 1;
    }

}
