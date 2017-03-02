package com.kejiang.yuandl.mytimepick;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.kejiang.yuandl.mytimepick.timepick.MyTimePicker;
import com.kejiang.yuandl.mytimepick.util.LogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tv_chose;

    private int count = 1;
    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_chose = (TextView) findViewById(R.id.tv_chose);
        tv_show = (TextView) findViewById(R.id.tv_show);
        tv_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoseTimeDialog();
            }
        });
    }


    private void showChoseTimeDialog() {
        final ArrayList<String> minutes = get64minutes();
        final ArrayList<String> hours = get24hours();
        final ArrayList<String> dates = get3Date();

        MyTimePicker myTimePicker = new MyTimePicker(this, dates, hours, minutes);
        myTimePicker.setSelectedItem(0, 0, 0);
        myTimePicker.setTitleText("选择时间");
        myTimePicker.setTitleTextSize(16);

        myTimePicker.setTopBackgroundColor(Color.parseColor("#FFEAEAEB"));
        myTimePicker.setTextSize(21);

        myTimePicker.setCancelText("取消");
        myTimePicker.setSubmitText("完成");
        myTimePicker.setSubmitTextColor(Color.parseColor("#F77B55"));
        myTimePicker.setLineColor(Color.parseColor("#FFEAEAEB"));
        myTimePicker.setTextColor(Color.parseColor("#000000"));
        myTimePicker.show();
        myTimePicker.setOnAddressPickListener(new MyTimePicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(String province, String city, String county) {
                tv_show.setText("选择时间:"+province+":"+city+":"+county);
            }
        });
    }

    /**
     * 获取从今天后的3天
     *
     * @return
     */
    private ArrayList<String> get3Date() {
        ArrayList<String> dates = new ArrayList<String>();
        dates.add("现在");
        for (int i = 0; i < 3; i++) {
            dates.add(getStatetime(i));
        }
        return dates;
    }

    /**
     * 获取24小时
     *
     * @return
     */
    private ArrayList<String> get24hours() {
        ArrayList<String> dates = new ArrayList<String>();
        dates.add(" ");
        for (int i = 0; i < 24; i++) {
            dates.add((i < 10 ? "0" + i : i) + "时");
        }
        return dates;
    }

    /**
     * 获取60分
     *
     * @return
     */
    private ArrayList<String> get64minutes() {
        int curentMiniutis = 0;
        ArrayList<String> dates = new ArrayList<String>();
        dates.add(" ");
        while(curentMiniutis<60){
            dates.add((curentMiniutis<10?("0"+curentMiniutis):curentMiniutis)+"分");
            curentMiniutis+=10;
        }
        return dates;
    }
    //获取当前时间
    private String getStatetime(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, i);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }
}
