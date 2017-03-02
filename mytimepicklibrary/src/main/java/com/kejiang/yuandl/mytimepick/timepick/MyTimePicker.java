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
import java.util.logging.Logger;
/**
 * 地址选择器（包括省级、地级、县级）。
 * 地址数据见示例项目的“city.json”，来源于国家统计局官网（http://www.stats.gov.cn/tjsj/tjbz/xzqhdm）
 *
 * @author 李玉江[QQ :1032694760]
 * @version 2015 /12/15
 */
public class MyTimePicker extends WheelPicker {
    //天时分的集合
    private ArrayList<String> provinceList = new ArrayList<String>();
    private ArrayList<String> cityList = new ArrayList<String>();
    private ArrayList<String> countyList = new ArrayList<String>();
    private OnAddressPickListener onAddressPickListener;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private int selectedProvinceIndex = 0, selectedCityIndex = 0, selectedCountyIndex = 0;
    private boolean hideProvince = false;
    private boolean hideCounty = false;

    public MyTimePicker(Activity activity, ArrayList<String> provinceList, ArrayList<String> cityList, ArrayList<String> countyList) {
        super(activity);
        this.provinceList = provinceList;
        this.cityList = cityList;
        this.countyList = countyList;
    }

    /**
     * Sets selected item.
     *
     * @param selectedProvinceIndex the province
     * @param selectedCityIndex     the city
     * @param selectedCountyIndex   the county
     */
    public void setSelectedItem(int selectedProvinceIndex, int selectedCityIndex, int selectedCountyIndex) {
        this.selectedProvinceIndex = selectedProvinceIndex;
        this.selectedCityIndex = selectedCityIndex;
        this.selectedCountyIndex = selectedCountyIndex;
    }

    /**
     * Sets on address pick listener.
     *
     * @param listener the listener
     */
    public void setOnAddressPickListener(OnAddressPickListener listener) {
        this.onAddressPickListener = listener;
    }
    //设置布局管理器
    @Override
    @NonNull
    protected View makeCenterView() {
        if (hideCounty) {
            hideProvince = false;
        }
        if (provinceList.size() == 0) {
            throw new IllegalArgumentException("please initial options at first, can't be empty");
        }
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        final WheelView provinceView = new WheelView(activity);
        provinceView.setLayoutParams(new LinearLayout.LayoutParams(screenWidthPixels / 3, WRAP_CONTENT));
        provinceView.setTextSize(textSize);
        provinceView.setTextColor(textColorNormal, textColorFocus);
        provinceView.setLineVisible(lineVisible);
        provinceView.setLineColor(lineColor);
        provinceView.setOffset(offset);
        layout.addView(provinceView);
        if (hideProvince) {
            provinceView.setVisibility(View.GONE);
        }
        final WheelView cityView = new WheelView(activity);
        cityView.setLayoutParams(new LinearLayout.LayoutParams(screenWidthPixels / 3, WRAP_CONTENT));
        cityView.setTextSize(textSize);
        cityView.setTextColor(textColorNormal, textColorFocus);
        cityView.setLineVisible(lineVisible);
        cityView.setLineColor(lineColor);
        cityView.setOffset(offset);
        layout.addView(cityView);
        final WheelView countyView = new WheelView(activity);
        countyView.setLayoutParams(new LinearLayout.LayoutParams(screenWidthPixels / 3, WRAP_CONTENT));
        countyView.setTextSize(textSize);
        countyView.setTextColor(textColorNormal, textColorFocus);
        countyView.setLineVisible(lineVisible);
        countyView.setLineColor(lineColor);
        countyView.setOffset(offset);
        layout.addView(countyView);
        if (hideCounty) {
            countyView.setVisibility(View.GONE);
        }
        //设置天
        provinceView.setItems(provinceList, selectedProvinceIndex);
        provinceView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedProvince = item;
                selectedProvinceIndex = selectedIndex;
                Log.d("timepick", "selectedProvince=" + selectedProvince);
                if (selectedProvince.equals("现在")) {
                    cityView.setItems(cityList.subList(0, 1), 0);
//                    countyView.setItems(countyList.subList(0, 1), 0);
                } else if (selectedProvince.equals(getSelectedDays())) {
                    cityView.setItems(cityList.subList(getSelectHours(), cityList.size()), 0);
//                    countyView.setItems(countyList.subList(getSelectMinutes(), countyList.size()), 0);
                } else {
//                    Log.d("timepick", "cityList=" + cityList);
                    cityView.setItems(cityList.subList(cityList.get(0).equals(" ") ? 1 : 0, cityList.size()), selectedCityIndex);
//                    Log.d("timepick", "cityList.subList(1, countyList.size())=" + cityList.subList(1, countyList.size()));
                }
            }
        });
        //设置时
        cityView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedCity = item;
                selectedCityIndex = selectedIndex;
                Log.d("timepick", "selectedCity=" + selectedCity);
                if (selectedCity.equals(" ")) {
                    countyView.setItems(countyList.subList(0, 1), 0);
                } else if (selectedProvince.equals(getSelectedDays()) && selectedCity.equals(getSelectHoursValue())) {
//                    Log.d("timepick", "=========selectedProvince="+selectedProvince+"getSelectedDays()="+getSelectedDays());
                    countyView.setItems(countyList.subList(getSelectMinutes(), countyList.size()), 0);
                } else {
                    countyView.setItems(countyList.subList(countyList.get(0).equals(" ") ? 1 : 0, countyList.size()), selectedCountyIndex);
                }
            }
        });
        //设置分
        countyView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedCounty = item;
                selectedCountyIndex = selectedIndex;
                System.out.println("selectedCountyIndex=" + selectedCountyIndex);
            }
        });
        return layout;
    }

    @Override
    protected void setContentViewBefore() {
        super.setContentViewBefore();
        getSelectedDays();
    }

    private int getSelectHours() {

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
        return cityList.indexOf(preMonday + "时");


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
        for (int i = 1; i < 3; i++) {
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

    private int getSelectMinutes() {

        SimpleDateFormat sdf = new SimpleDateFormat("mm");

        Calendar c = Calendar.getInstance();
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        int curentMiniutis = Integer.parseInt(preMonday);
        if (curentMiniutis % 10 != 0) {
            curentMiniutis = curentMiniutis + 10 - curentMiniutis % 10;
        }
        String s = String.valueOf(curentMiniutis);
        if (curentMiniutis < 10) {
            s = "0" + s;
        }
        if (curentMiniutis > 59) {
            s = "00";
        }
//        Log.d("getSelectMinutes", "getSelectMinutes=" + s + "分");
        return countyList.indexOf(s + "分");
    }

    @Override
    public void onSubmit() {
        if (onAddressPickListener != null) {
            if (hideCounty) {
                onAddressPickListener.onAddressPicked(selectedProvince, selectedCity, null);
            } else {
                onAddressPickListener.onAddressPicked(selectedProvince, selectedCity, selectedCounty);
            }
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
         * @param city     the city
         * @param county   the county ，if {@hideCounty} is true，this is null
         */
        void onAddressPicked(String province, String city, String county);

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
