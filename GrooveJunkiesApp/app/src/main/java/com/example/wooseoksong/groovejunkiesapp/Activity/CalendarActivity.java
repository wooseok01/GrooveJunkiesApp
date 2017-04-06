package com.example.wooseoksong.groovejunkiesapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wooseoksong.groovejunkiesapp.Adapter.CalendarAdapter;
import com.example.wooseoksong.groovejunkiesapp.Model.DayInfo;
import com.example.wooseoksong.groovejunkiesapp.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wooseoksong on 2017. 3. 30..
 */

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{

    public static int SUNDAY = 0;
    public static int MONDAY = 1;
    public static int TUESDAY = 2;
    public static int WENDSDAY = 3;
    public static int THURSDAY = 4;
    public static int FRIDAY = 5;
    public static int SATURDAY = 6;

    private Calendar prevMonth;
    private Calendar thisMonth;
    private Calendar nextMonth;

    private Calendar nowMonth;

    private ImageButton calendarPreBtn;
    private ImageButton calendarNextBtn;

    private TextView calendarInfo;
    private GridView calendarGrid;

    ArrayList<DayInfo> dayList;
    CalendarAdapter calendarAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.calendar_layout);

        calendarPreBtn = (ImageButton)findViewById(R.id.calendarPrevBtn);
        calendarNextBtn = (ImageButton)findViewById(R.id.calendarNextBtn);

        calendarInfo = (TextView)findViewById(R.id.calendarInfo);
        calendarGrid = (GridView)findViewById(R.id.calendarGrid);

        calendarPreBtn.setOnClickListener(this);
        calendarNextBtn.setOnClickListener(this);

        dayList = new ArrayList<DayInfo>();
        nowMonth = Calendar.getInstance();


    }

    @Override
    protected void onResume() {
        super.onResume();

        thisMonth = Calendar.getInstance();
        thisMonth.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(thisMonth);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getCalendar(thisMonth);
    }

    private void getCalendar(Calendar calendar){
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        dayList.clear();

        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, 1);


        if(dayOfMonth == SUNDAY){
            dayOfMonth+=7;
        }

        lastMonthStartDay -= (dayOfMonth-1)-1;

        calendarInfo.setText(
                thisMonth.get(Calendar.YEAR) + "년 " +
                (thisMonth.get(Calendar.MONTH)+1)+"월");

        DayInfo day;


        int nowMonthInteger = nowMonth.get(Calendar.DAY_OF_MONTH);


        for(int i=0; i<dayOfMonth-1; i++){
            //달력에서 현재 달 이전의 달력의 일수를 표시하는 for문
            int date = lastMonthStartDay + i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);

            dayList.add(day);
        }

        for(int i=1; i<=thisMonthLastDay; i++){
            //달력에서 현재 달의 일수를 표시하는 for문
            day = new DayInfo();
            if(thisMonth.get(Calendar.MONTH) == nowMonth.get(Calendar.MONTH) &&
               i == nowMonthInteger){
                day.setToday(true);
            }
            day.setDay(Integer.toString(i));
            day.setInMonth(true);

            dayList.add(day);
        }

        for(int i=1; i<42-(thisMonthLastDay + dayOfMonth-1)+1; i++){
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);

            dayList.add(day);
        }

        initCalendarAdapter();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.calendarPrevBtn :
                thisMonth = getLastMonth(thisMonth);
                getCalendar(thisMonth);
                break;

            case R.id.calendarNextBtn :
                thisMonth = getNextMonth(thisMonth);
                getCalendar(thisMonth);
                break;
        }
    }

    private void initCalendarAdapter(){
        calendarAdapter = new CalendarAdapter(this, R.layout.calendar_day, dayList, this);
        calendarGrid.setAdapter(calendarAdapter);
    }

    private Calendar getLastMonth(Calendar calendar){
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);

        calendar.add(Calendar.MONTH, -1);
        calendarInfo.setText(
                thisMonth.get(Calendar.YEAR) + "년 " +
                (thisMonth.get(Calendar.MONTH)+1) + "월"
        );

        return calendar;
    }

    private Calendar getNextMonth(Calendar calendar){
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(calendar.MONTH), 1
        );

        calendar.add(Calendar.MONTH, +1);
        calendarInfo.setText(
                thisMonth.get(Calendar.YEAR) + "년" +
                        (thisMonth.get(Calendar.MONTH)+1) + "월"
        );

        return calendar;
    }
}
