package com.example.wooseoksong.groovejunkiesapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wooseoksong.groovejunkiesapp.Model.DayInfo;
import com.example.wooseoksong.groovejunkiesapp.R;

import java.util.ArrayList;

/**
 * Created by wooseoksong on 2017. 4. 1..
 */

public class CalendarAdapter extends BaseAdapter implements View.OnClickListener{
    private Context mContext;
    private ArrayList<DayInfo> dayList;
    private int resource;
    private LayoutInflater inflater;
    private Activity activity;

    public CalendarAdapter(
            Context mContext,
            int textResource,
            ArrayList<DayInfo> dayList,
            Activity activity
    ){
        this.mContext = mContext;
        this.resource = textResource;
        this.dayList = dayList;
        this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return dayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DayInfo dayInfo = dayList.get(position);

        DayViewHolder dayViewHolder;

        if(convertView == null){
            convertView = inflater.inflate(resource, null);

            if(position%7 == 6){
                convertView.setLayoutParams(
                        new GridView.LayoutParams(
                                getCellWidthDP()+getRestCellWidthDP(),
                                getCellHeightDP()
                        ));
            }else{

                convertView.setLayoutParams(
                        new GridView.LayoutParams(
                                getCellWidthDP(),
                                getCellHeightDP()
                        ));
            }

            dayViewHolder = new DayViewHolder();
            dayViewHolder.llBackground = (LinearLayout)convertView.findViewById(R.id.day_layout);
            dayViewHolder.tvDay = (TextView)convertView.findViewById(R.id.day_cell_tv);

            convertView.setTag(dayViewHolder);

        }else{
            dayViewHolder = (DayViewHolder)convertView.getTag();
        }

        if(dayInfo != null){
            dayViewHolder.tvDay.setText(dayInfo.getDay());

            if(dayInfo.isInMonth()){
                if(position%7 == 0){
                    dayViewHolder.tvDay.setTextColor(
                            ContextCompat.getColor(mContext, R.color.colorAccent));
                }else if(position%7 == 6){
                    dayViewHolder.tvDay.setTextColor(
                            ContextCompat.getColor(mContext, R.color.saturdayColor));
                }else{
                    dayViewHolder.tvDay.setTextColor(
                            ContextCompat.getColor(mContext, R.color.textColor));
                }
            }else{
                dayViewHolder.tvDay.setTextColor(
                        ContextCompat.getColor(mContext, R.color.notThisMonth));
            }

            if(dayInfo.isToday()){
                dayViewHolder.llBackground.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.todayDateColor));
                //Resources r = activity.getResources();
                //dayViewHolder.tvDay.setBackgroundResource(R.drawable.day_border_radius);
                //dayViewHolder.tvDay.setTextColor(
                  //      ContextCompat.getColor(mContext, R.color.todayDateColor));
            }
        }

        convertView.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        TextView cellTv = (TextView) v.findViewById(R.id.day_cell_tv);
        Log.e("day", cellTv.getText().toString());
    }

    public class DayViewHolder{
        public LinearLayout llBackground;
        public TextView tvDay;
    }

    private int getCellWidthDP(){
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        return dm.widthPixels/7;
    }

    private int getRestCellWidthDP(){
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        return dm.widthPixels%7;
    }

    private int getCellHeightDP(){

        int height = activity.findViewById(R.id.calendarGrid).getHeight();
        return height/6;
    }
}
