package com.example.admin.godzillaandroid.comm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.example.admin.godzillaandroid.R;
import com.example.admin.godzillaandroid.dao.Task;

import java.util.List;

/**
 * Created by admin on 2019/4/3.
 */
public class FruitAdapter extends ArrayAdapter{
    private final int resourceId;

    public FruitAdapter(Context context, int textViewResourceId, List<Task> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task fruit = (Task) getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView fruitImage = (ImageView) view.findViewById(R.id.fruit_image);//获取该布局内的图片视图
        BootstrapLabel fruitName = (BootstrapLabel) view.findViewById(R.id.fruit_name);//获取该布局内的文本视图
        TextView fruitDate = (TextView) view.findViewById(R.id.fruit_date);//获取该布局内的文本视图

        fruitDate.setText(fruit.getDate());
        fruitImage.setImageResource(fruit.getImageId());//为图片视图设置图片资源
        fruitName.setText(fruit.getName());//为文本视图设置文本内容
        return view;
    }
}