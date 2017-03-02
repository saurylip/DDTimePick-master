package com.kejiang.yuandl.mytimepick.timepick;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.kejiang.yuandl.mytimepick.widget.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * 地址选择器（包括省级、地级、县级）。
 * 地址数据见示例项目的“city.json”，来源于国家统计局官网（http://www.stats.gov.cn/tjsj/tjbz/xzqhdm）
 *
 * @author 李玉江[QQ :1032694760]
 * @version 2015 /12/15
 */
public class MyCostPicker extends WheelPicker {
    private ArrayList<String> provinceList = new ArrayList<String>();
    private OnAddressPickListener onAddressPickListener;
    private String selectedProvince = "";
    private int selectedProvinceIndex = 0;
//
//    /**
//     * Instantiates a new Address picker.
//     *
//     * @param activity the activity
//     * @param data     the data
//     */
//    public MyTimePicker(Activity activity,  ArrayList<String> provinceList,ArrayList<String> cityList ,ArrayList<String> countyList) {
//        super(activity);
//
////        int provinceSize = data.size();
////        //添加省
////        for (int x = 0; x < provinceSize; x++) {
////            Province pro = data.get(x);
////            provinceList.add(pro.getAreaName());
////            ArrayList<City> cities = pro.getCities();
////            ArrayList<String> xCities = new ArrayList<String>();
////            ArrayList<ArrayList<String>> xCounties = new ArrayList<ArrayList<String>>();
////            int citySize = cities.size();
////            //添加地市
////            for (int y = 0; y < citySize; y++) {
////                City cit = cities.get(y);
////                xCities.add(cit.getAreaName());
////                ArrayList<County> counties = cit.getCounties();
////                ArrayList<String> yCounties = new ArrayList<String>();
////                int countySize = counties.size();
////                //添加区县
////                if (countySize == 0) {
////                    yCounties.add(cit.getAreaName());
////                } else {
////                    for (int z = 0; z < countySize; z++) {
////                        yCounties.add(counties.get(z).getAreaName());
////                    }
////                }
////                xCounties.add(yCounties);
////            }
////            cityList.add(xCities);
////            countyList.add(xCounties);
////        }
//    }

    public MyCostPicker(Activity activity, ArrayList<String> provinceList) {
        super(activity);
        this.provinceList = provinceList;
    }

    /**
     * Sets selected item.
     *
     * @param selectedProvinceIndex the province
     */
    public void setSelectedItem(int selectedProvinceIndex) {
        this.selectedProvinceIndex = selectedProvinceIndex;
//        LogUtils.debug(String.format("init select index: %s-%s-%s", selectedProvinceIndex, selectedCityIndex, selectedCountyIndex));
    }


    /**
     * Sets on address pick listener.
     *
     * @param listener the listener
     */
    public void setOnAddressPickListener(OnAddressPickListener listener) {
        this.onAddressPickListener = listener;
    }

    @Override
    @NonNull
    protected View makeCenterView() {
        if (provinceList.size() == 0) {
            throw new IllegalArgumentException("please initial options at first, can't be empty");
        }
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        final WheelView provinceView = new WheelView(activity);
        provinceView.setLayoutParams(new LinearLayout.LayoutParams(screenWidthPixels, 500));
        provinceView.setTextSize(textSize);
        provinceView.setTextColor(textColorNormal, textColorFocus);
        provinceView.setLineVisible(lineVisible);
        provinceView.setLineColor(lineColor);
        provinceView.setOffset(offset);
        layout.addView(provinceView);

        provinceView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedProvince = item;
                selectedProvinceIndex = selectedIndex;
            }
        });
//
        return layout;
    }

    @Override
    protected void setContentViewBefore() {
        super.setContentViewBefore();
        getSelectedDays();
    }


    private String getSelectHoursValue() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH");

        Calendar c = Calendar.getInstance();
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        int ch = Integer.parseInt(preMonday);
        if (monday.getMinutes() > 55) {
            ch = ch + 1;
        }
        preMonday = String.valueOf(ch);
        if (ch < 10) {
            preMonday = "0" + ch;
        }
        if (ch > 23) {
            preMonday = "00";
        }
//        Log.d("getSelectHours", "getSelectHours=" + preMonday + "时");
        return preMonday + "时";


    }

    /**
     * 获取从今天后的7天
     *
     * @return
     */
    private ArrayList<String> get7Date() {
        ArrayList<String> dates = new ArrayList<String>();
        if (provinceList.get(0).equals("现在")) {
            dates.add("现在");
        }
        for (int i = 1; i < 8; i++) {
            dates.add(getStatetime(i));
        }
        this.provinceList = dates;
        return dates;
    }

    private String getStatetime(int i) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, i);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    private String getSelectedDays() {
        Calendar c = Calendar.getInstance();
        Date monday = c.getTime();
        if (monday.getHours() == 23 && monday.getMinutes() > 55) {
//            get7Date();
//            provinceView.setItems(get7Date(), selectedProvinceIndex);
            get7Date();
            return getStatetime(1);
        } else {
            return getStatetime(0);
        }


    }


    @Override
    public void onSubmit() {
        if (onAddressPickListener != null) {
                onAddressPickListener.onAddressPicked(selectedProvince);
        }
    }

    /**
     * The interface On address pick listener.
     */
    public interface OnAddressPickListener {

        /**
         * On address picked.
         *
         * @param province the province
         */
        void onAddressPicked(String province);

    }

    /**
     * The type Area.
     */
    public abstract static class Area {
        /**
         * The Area id.
         */
        String areaId;
        /**
         * The Area name.
         */
        String areaName;

        /**
         * Gets area id.
         *
         * @return the area id
         */
        public String getAreaId() {
            return areaId;
        }

        /**
         * Sets area id.
         *
         * @param areaId the area id
         */
        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        /**
         * Gets area name.
         *
         * @return the area name
         */
        public String getAreaName() {
            return areaName;
        }

        /**
         * Sets area name.
         *
         * @param areaName the area name
         */
        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        @Override
        public String toString() {
            return "areaId=" + areaId + ",areaName=" + areaName;
        }

    }

    /**
     * The type Province.
     */
    public static class Province extends Area {
        /**
         * The Cities.
         */
        ArrayList<City> cities = new ArrayList<City>();

        /**
         * Gets cities.
         *
         * @return the cities
         */
        public ArrayList<City> getCities() {
            return cities;
        }

        /**
         * Sets cities.
         *
         * @param cities the cities
         */
        public void setCities(ArrayList<City> cities) {
            this.cities = cities;
        }

    }

    /**
     * The type City.
     */
    public static class City extends Area {
        private ArrayList<County> counties = new ArrayList<County>();

        /**
         * Gets counties.
         *
         * @return the counties
         */
        public ArrayList<County> getCounties() {
            return counties;
        }

        /**
         * Sets counties.
         *
         * @param counties the counties
         */
        public void setCounties(ArrayList<County> counties) {
            this.counties = counties;
        }

    }

    /**
     * The type County.
     */
    public static class County extends Area {
    }

}
