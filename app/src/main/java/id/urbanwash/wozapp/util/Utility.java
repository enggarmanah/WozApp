package id.urbanwash.wozapp.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.model.SearchAddress;

/**
 * Created by apridosandyasa on 3/9/16.
 */
public class Utility {

    public static class DateUtility {

        public static String getCalendarMonthName(int monthOfYear) {
            return new DateFormatSymbols().getShortMonths()[monthOfYear];
        }

        public static String getCalendarDayOfWeek(int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = getCalendarInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            return new DateFormatSymbols().getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)];
        }

        public static String getTodayCalendarDayOfWeek() {
            return new DateFormatSymbols().getWeekdays()[getCalendarInstance().get(Calendar.DAY_OF_WEEK)];
        }

        public static String getTodayCalendarMonthName() {
            return new DateFormatSymbols().getShortMonths()[getCalendarInstance().get(Calendar.MONTH)];
        }

        public static int getTodayCalendarDayOfMonth() {
            return getCalendarInstance().get(Calendar.DAY_OF_MONTH);
        }

        public static Calendar getCalendarInstance() {
            return CommonUtil.getCalendar();
        }

    }

    public static class TimeUtitlity {

        public static String getFormatTime(int hourOfDay, int minute, int second) {
            Calendar calendar = DateUtility.getCalendarInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
            return (calendar.get(Calendar.AM_PM) == Calendar.AM) ? "am" : "pm";
        }

        public static int getTodayCalendarTime() {
            return DateUtility.getCalendarInstance().get(Calendar.HOUR);
        }

        public static String getTodayCalendarTimeFormat() {
            return (DateUtility.getCalendarInstance().get(Calendar.AM_PM) == Calendar.AM) ? "am" : "pm";
        }

    }

    public static class JSONUtility {

        public static String getAddressTextFromJSON(String json) throws JSONException {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            String addressText = jsonArray.getJSONObject(0).getString("formatted_address");
            return addressText;
        }

        public static List<SearchAddress> getListAddressTextFromJSON(String json) throws JSONException {
            List<SearchAddress> objects = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    objects.add(new SearchAddress(jsonArray.getJSONObject(i).getString("formatted_address"),
                            jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                            jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng")));
                }
            }else {
                objects.add(new SearchAddress("No address is found", 0, 0));
            }
            return objects;
        }

    }

    public static class DisplayUtilty {

        public static DisplayMetrics getDisplayMetricFromWindow(AppCompatActivity appCompatActivity) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            appCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics;
        }
    }

    public static class AnimationUtility {

        public static Animation setAnimationOnView(Context context) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_anim);
            animation.reset();
            return animation;
        }

    }
}
