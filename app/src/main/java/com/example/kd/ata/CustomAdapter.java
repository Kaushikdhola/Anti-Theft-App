package com.example.kd.ata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kd on 28-03-2018.
 */

public class CustomAdapter extends BaseAdapter {
    Context c;
    String name,number;
    List l,l1;
    int length;
    CustomAdapter(Context c, List l,List l1, int length){
        this.c=c;
        this.name=name;
        this.number=number;
        this.length=length;
        this.l=l;
        this.l1=l1;
    }
    @Override
    public int getCount() {
        return length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        LayoutInflater in;
        in = LayoutInflater.from(c);

        v = in.inflate(R.layout.mydesign,null);

        TextView tx1 = (TextView)v.findViewById(R.id.tv1);
        tx1.setText(l.get(i).toString());

        TextView tx2 = (TextView)v.findViewById(R.id.tv2);
        tx2.setText(l1.get(i).toString());
        return v;
    }
}
