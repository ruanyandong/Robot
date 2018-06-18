package com.example.ai.robot.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * ViewHolder的超简洁写法
 */
public class ViewHolder {

    private View mConvertView;

    public ViewHolder(View convertView){

        this.mConvertView = convertView;
    }

    /**
     * 实例方法
     * @param ViewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int ViewId){

        SparseArray<View> viewHolder =(SparseArray<View>) mConvertView.getTag();

        if (viewHolder == null){
            viewHolder = new SparseArray<View>();
            mConvertView.setTag(viewHolder);
        }

        View childView = viewHolder.get(ViewId);

        if (childView == null){
            childView = mConvertView.findViewById(ViewId);
            viewHolder.put(ViewId,childView);
        }

        return (T)childView;
    }

    /**
     * 静态方法
     * @param convertView
     * @param ViewId
     * @param <T>
     * @return
     */
    public static <T extends View> T getView(View convertView,int ViewId){
        SparseArray<View> viewHolder =(SparseArray<View>) convertView.getTag();

        if (viewHolder == null){
            viewHolder = new SparseArray<View>();
            convertView.setTag(viewHolder);
        }

        View childView = viewHolder.get(ViewId);

        if (childView == null){
            childView = convertView.findViewById(ViewId);
            viewHolder.put(ViewId,childView);
        }

        return (T)childView;

    }

}
