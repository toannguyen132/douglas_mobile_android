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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import com.project.groupproject.R;
import com.project.groupproject.lib.Helper;
import com.project.groupproject.models.Event;
import com.project.groupproject.SingleEventActivity;
import com.squareup.picasso.Picasso;

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
        TextView mTitleTv, mDescTv, mMonthTv, mDayTv, mLike;
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
            holder.mMonthTv = view.findViewById(R.id.mainMonth);
            holder.mDayTv = view.findViewById(R.id.mainDate);
            holder.mLike = view.findViewById(R.id.mainLike);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }

        Event currentEvent = modellist.get(postition);

        //set the results into textviews
        holder.mTitleTv.setText(currentEvent.name);
        holder.mDescTv.setText(currentEvent.location);
        holder.mLike.setText(currentEvent.num_like + " " + Helper.pluralize(currentEvent.num_like, "Like", "Likes"));

        Date date = new Date(currentEvent.start_date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String month = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());

        holder.mMonthTv.setText(month);
        holder.mDayTv.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

//        holder.mMonthTv.setText(cal.get(Calendar.MONTH));
//        holder.mDayTv.setText(cal.get(Calendar.DAY_OF_MONTH));

        if (currentEvent.image != null) {
//            holder.mIconIv.setImageURI(currentEvent.imageUri);
            Picasso.get().load(currentEvent.image).into(holder.mIconIv);
        } else {
            //set the result in imageview
            holder.mIconIv.setImageResource(R.drawable.event1);
        }

        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                Event selectedEvent = modellist.get(postition);

                Intent intent = new Intent(mContext, SingleEventActivity.class);
                intent.putExtra("event_id", selectedEvent.id);
                mContext.startActivity(intent);
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