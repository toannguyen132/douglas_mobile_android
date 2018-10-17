package com.project.groupproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.project.groupproject.R;
import com.project.groupproject.models.Event;
import com.project.groupproject.SingleEventActivity;

public class ListEventsAdapter extends BaseAdapter{

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<Event> modellist;
    ArrayList<Event> arrayList;

    //constructor
    public ListEventsAdapter(Context context, List<Event> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Event>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView mTitleTv, mDescTv;
        ImageView mIconIv;
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int i) {
        return modellist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int postition, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view==null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.layout_list_event, null);

            //locate the views in row.xml
            holder.mTitleTv = view.findViewById(R.id.mainTitle);
            holder.mDescTv = view.findViewById(R.id.mainDesc);
            holder.mIconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);

        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        //set the results into textviews
        holder.mTitleTv.setText(modellist.get(postition).getTitle());
        holder.mDescTv.setText(modellist.get(postition).getDesc());
        //set the result in imageview
        holder.mIconIv.setImageResource(modellist.get(postition).getIcon());

        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                if (modellist.get(postition).getTitle().equals("Neighbor Community Potluck")){
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SingleEventActivity.class);
                    intent.putExtra("actionBarTitle", "Neighbor Community Potluck");
                    intent.putExtra("contentTv", "Event 1 detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(postition).getTitle().equals("Andre Nickatina")){
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SingleEventActivity.class);
                    intent.putExtra("actionBarTitle", "Andre Nickatina");
                    intent.putExtra("contentTv", "Event 2 detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(postition).getTitle().equals("DON DIABLO")){
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SingleEventActivity.class);
                    intent.putExtra("actionBarTitle", "DON DIABLO");
                    intent.putExtra("contentTv", "Event 3 detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(postition).getTitle().equals("DIM SUM Making")){
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SingleEventActivity.class);
                    intent.putExtra("actionBarTitle", "DIM SUM Making");
                    intent.putExtra("contentTv", "Event 4 detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(postition).getTitle().equals("My BizDay Vancouver")){
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SingleEventActivity.class);
                    intent.putExtra("actionBarTitle", "My BizDay Vancouver");
                    intent.putExtra("contentTv", "Event 5 detail...");
                    mContext.startActivity(intent);
                }
            }
        });


        return view;
    }

    //filter
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if (charText.length()==0){
            modellist.addAll(arrayList);
        }
        else {
            for (Event model : arrayList){
                if (model.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    modellist.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }

}