package com.example.share.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.share.R;
import com.example.share.model.Comment;

import java.util.List;

public class CommentAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Comment> data;

    public CommentAdapter(Context context, List<Comment> data) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewItem listViewItem;
        if (convertView == null) {
            listViewItem = new ListViewItem();
            convertView = layoutInflater.inflate(R.layout.list_item2, null);
            listViewItem.tv1 = convertView.findViewById(R.id.tv1);
            listViewItem.tv2 = convertView.findViewById(R.id.tv2);
            convertView.setTag(listViewItem);
        } else {
            listViewItem = (ListViewItem) convertView.getTag();
        }
        listViewItem.tv1.setText("author:" + data.get(position).getUser().getName() + "  time:" + data.get(position).getCreateTime());
        listViewItem.tv2.setText("content:" + data.get(position).getContent());
        return convertView;
    }

    public final class ListViewItem {
        TextView tv1;
        TextView tv2;
    }
}
