package com.example.administrator.myapplication.adapter;

/**
 * Created by admin on 2018/6/19.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.dao.Article;

import java.util.List;

/**
 * Created by smyhvae on 2015/5/4.
 */
public class MyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Article> mDatas;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public MyAdapter(Context context, List<Article> datas) {

        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    //返回数据集的长度
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

    //这个方法才是重点，我们要为它编写一个ViewHolder
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        System.out.println("执行"+position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_listview,null); //加载布局
            holder = new ViewHolder();

            holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
            holder.descTv = (TextView) convertView.findViewById(R.id.descTv);
            holder.phoneTv = (TextView) convertView.findViewById(R.id.phoneTv);

            convertView.setTag(holder);
        } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
            holder = (ViewHolder) convertView.getTag();
        }

        Article bean = mDatas.get(position);
        holder.titleTv.setText(bean.getTitle());
        //holder.descTv.setText(String.valueOf(bean.getId()));
        Drawable drawable= convertView.getResources().getDrawable(R.drawable.heart_25);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        holder.phoneTv.setCompoundDrawables(drawable,null,null,null);
        String read =  bean.getNodes();
        if("1".equals(read)){
            drawable= convertView.getResources().getDrawable(R.drawable.heart_24);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.phoneTv.setCompoundDrawables(drawable,null,null,null);
        }

        //holder.phoneTv.setText(bean.getDetail());

        return convertView;
    }

    //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
    private class ViewHolder {
        TextView titleTv;
        TextView descTv;
        TextView phoneTv;
    }


    public void refresh(List<Article> newList) {
        //刷新数据
        mDatas.removeAll(mDatas);
        mDatas.addAll(newList);
        notifyDataSetChanged();
    }
}