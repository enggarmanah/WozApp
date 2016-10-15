package id.urbanwash.wozapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.urbanwash.wozapp.Config;
import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.OrderProductBean;

@SuppressLint("SimpleDateFormat")
public class CommonUtil {
	
	public static Boolean LOCK = true;
	
	private static final AtomicLong LAST_TIME_MS = new AtomicLong();
	
	public static GoogleAnalytics analytics;
	public static Tracker tracker;
	
	private static Context mContext;
	private static String mCertDN;
	
	private static boolean isDemo = false;
	
	public static void setDemo(boolean status) {
		
		isDemo = status;
	}
	
	public static boolean isDemo() {
		
		return isDemo;
	}

    public static void setContext(Context context) {
        mContext = context;
    }
	
	public static void initTracker(Context context) {
		
		mContext = context;
		
		if (Config.isDevelopment()) {
			return;
		}

		analytics = GoogleAnalytics.getInstance(context);
	    analytics.setLocalDispatchPeriod(1800);

	    tracker = analytics.newTracker("UA-64012601-1"); 
	    tracker.enableExceptionReporting(true);
	    tracker.enableAdvertisingIdCollection(false);
	    tracker.enableAutoActivityTracking(true);
	}
	
	public static Tracker getTracker() {
		
		return tracker;
	}
	
	public static void sendEvent(final String category, final String action) {
		
		if (tracker == null) {
			return;
		}
		
		getTracker().send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).build());
		analytics.dispatchLocalHits();
	}
		
	public static void sendEvent(final String category, final String action, final String label) {
		
		if (tracker == null) {
			return;
		}
		
		getTracker().send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
		analytics.dispatchLocalHits();
	}
	
	public static String generateRefId() {
	    
		long now = System.currentTimeMillis();

		while (true) {

			long lastTime = LAST_TIME_MS.get();

			if (lastTime >= now) {
				now = lastTime + 1;
			}

			if (LAST_TIME_MS.compareAndSet(lastTime, now)) {
				break;
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Date date = new Date(now);
		
		return formatDateTimeMiliSeconds(date); 
	}
	
	public static int convertDpToPix(int dp) {
		
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
	}
	
	public static boolean compareString(String str1, String str2) {
		
		if (str1 != null && str2 != null && str1.equals(str2)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isEmpty(String str) {
		
		if (str != null && str.length() != 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public static int getMaxIndex(int recordCount, int limit) {
		
		System.out.println("record count : " + recordCount + " limit : " + limit);
		
		return recordCount > limit ? limit : recordCount;
	}
	
	public static Integer getNvlInt(Integer value) {
		
		if (value == null) {
			return 0;
		} else {
			return value;
		}
	}
	
	public static Float getNvlFloat(Float value) {
		
		if (value == null) {
			return Float.valueOf(0);
		} else {
			return value;
		}
	}
	
	public static Long getNvlLong(Long value) {
		
		if (value == null) {
			return Long.valueOf(0);
		} else {
			return value;
		}
	}
	
	public static String getNvlString(String value) {
		
		if (value == null) {
			return Constant.EMPTY_STRING;
		} else {
			return value;
		}
	}
	
	public static String getSqlLikeString(String value) {
		
		if (isEmpty(value)) {
			return "%";
		} else {
			
			String[] queries = value.split(" ");
			String likeStr = "";
			
			for (String str : queries) {
				
				if (isEmpty(likeStr)) {
					likeStr += "%" + str + "%";
				} else {
					likeStr += " %" + str + "%";
				}
			}
			
			return likeStr;
		}
	}
	
	public static Integer strToInt(String s) {
		
		Integer number = null;
		
		try {
			number = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			// do nothing
		}
		
		return number;
	}
	
	public static Float strToFloat(String s) {
		
		Float number = null;
		
		try {
			number = Float.parseFloat(s);
		} catch (NumberFormatException e) {
			// do nothing
		}
		
		return number;
	}
	
	public static String intToStr(Integer i) {
		
		String number = null;
		
		try {
			if (i != null) {
				number = String.valueOf(i);
			}
		} catch (NumberFormatException e) {
			// do nothing
		}
		
		return number;
	}
	
	public static String floatToStr(Float i) {
		
		String number = null;
		
		try {
			if (i != null) {
				number = String.valueOf(i);
			}
		} catch (NumberFormatException e) {
			// do nothing
		}
		
		return number;
	}
	
	public static String trim(String str) {
		
		if (!isEmpty(str)) {
			return str.trim(); 
		} else {
			return Constant.EMPTY_STRING;
		}
	}
	
	static Locale locale;
	
	public static Locale getLocale() {
		
		if (locale == null) {
			locale = Locale.getDefault();
            //locale = new Locale("in", "ID");
		}
		
		return locale;
	}

	public static Locale getLocaleCurrency() {

		return new Locale("in", "ID");
	}

    public static Locale getLocaleNumber() {

        return new Locale("in", "ID");
    }
	
	public static void setLocale(Locale newLocale) {
		
		locale = newLocale;
	}
	
	public static Locale parseLocale(String localeStr) {
		
		String[] localeArray = !CommonUtil.isEmpty(localeStr) ? localeStr.split(",") : null;
		
		Locale locale = null;
		
		if (!CommonUtil.isEmpty(localeStr)) {
			locale = new Locale(localeArray[0], localeArray[1]);
		} else {
			locale = Locale.getDefault();
		}
		
		return locale;
	}
	
	public static DateFormat getDateFormat() {
		
		return SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, getLocale());
	}
	
	public static DateFormat getDateFormat(String format) {
		
		return new SimpleDateFormat(format, getLocale());
	}
	
	public static DateFormat getDateTimeFormat() {
		
		return new SimpleDateFormat("dd MMM yyyy, HH:mm", getLocale());
	}

	public static DateFormat getInputDateFormat() {

		return new SimpleDateFormat("dd/MM/yyyy", getLocale());
	}
				
	public static DateFormat getTimeFormat() {
		
		return new SimpleDateFormat("HH:mm", getLocale());
	}

    public static DateFormat get12HrTimeFormat() {

        return new SimpleDateFormat("hh:mm", getLocale());
    }

	public static DateFormat get12HrAmPmTimeFormat() {

		return new SimpleDateFormat("hh:mm a", getLocale());
	}
	
	public static DateFormat getDateMonthTimeFormat() {
		
		return new SimpleDateFormat("dd MMM, HH:mm", getLocale());
	}
		
	public static DateFormat getDateTimeMiliSecondsFormat() {
		
		return new SimpleDateFormat("yyyyMMddHHmmssSSS", getLocale());
	}
	
	public static DateFormat getDayDateTimeFormat() {
		
		return new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm", getLocale());
	}

	public static String getTimeDesc(Long time) {

		Long startHour = time / (60 * 60 * 1000);
        Long endHour = startHour + Constant.OPERATING_TIME_WINDOW;

        String startHourStr = startHour < 12 ? String.format("%02d", startHour) + ".00 AM " : String.format("%02d", startHour == 12 ? 12 : startHour % 12) + ".00 PM";
        String endHourStr = endHour < 12 ? String.format("%02d", endHour) + ".00 AM " : String.format("%02d", endHour == 12 ? 12 : endHour % 12) + ".00 PM";

		return startHourStr + " - " + endHourStr;
	}

    public static Long getTime(Date date) {

        return date.getTime() - getDate(date);
    }

    public static Long getDate(Date date) {

		Calendar cal = CommonUtil.getCalendar();
		cal.setTime(date);
		removeTime(cal);

		date = cal.getTime();

		return date.getTime();
    }
	
	private static void removeTime(Calendar cal) {
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
	}
	
	public static Date getFirstDayOfYear(Date date) {
		
		Calendar cal = CommonUtil.getCalendar();
		
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, 0);
		
		return cal.getTime();
	}
	
	public static Date getLastDayOfYear(Date date) {
		
		Calendar cal = CommonUtil.getCalendar();
		
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, 0);
		cal.add(Calendar.YEAR, 1);
		cal.add(Calendar.MILLISECOND, -1);
		
		return cal.getTime();
	}
	
	public static Date getFirstDayOfMonth(Date date) {
		
		Calendar cal = CommonUtil.getCalendar();
		
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		
		return cal.getTime();
	}
	
	public static Date getEndDay(Date date) {
		
		Calendar cal = CommonUtil.getCalendar();
		
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.MILLISECOND, -1);
		
		return cal.getTime();
	}
	
	public static Date getLastDayOfMonth(Date date) {
		
		Calendar cal = CommonUtil.getCalendar();
		
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.MILLISECOND, -1);
		
		return cal.getTime();
	}
	
	public static Date getCurrentYear() {
		
		Calendar cal = CommonUtil.getCalendar();
		
		removeTime(cal);
		
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, 0);
		
		return cal.getTime();
	}
	
	public static Date getCurrentMonth() {
		
		Calendar cal = CommonUtil.getCalendar();
		
		removeTime(cal);
		
		cal.set(Calendar.DATE, 1);
		
		return cal.getTime();
	}

	public static Calendar getCalendar() {

        return Calendar.getInstance(TimeZone.getTimeZone(Constant.TIMEZONE));
	}

	public static Date getCurrentDate() {

		Calendar cal = getCalendar();

		removeTime(cal);

		return cal.getTime();
	}

	public static Date getDateWithoutTime(Date date) {

		Calendar cal = CommonUtil.getCalendar();
		cal.setTime(date);

		removeTime(cal);

		return cal.getTime();
	}
	
	public static Date parseDate(String dateStr) {
		
		Date date = null;
		
		try {
			date = getDateFormat().parse(dateStr);
		} catch (ParseException e) {
			// do nothing
		}
		
		return date;
	}
	
	public static Date parseDate(String dateStr, String format) {
		
		Date date = null;
		
		try {
			date = getDateFormat(format).parse(dateStr);
		} catch (ParseException e) {
			// do nothing
		}
		
		return date;
	}
	
	public static String formatReservationNo(String str) {
		
		return str != null && str.length() == 1 ? "0" + str : str;
	}

	public static String formatInputDate(Date inputDate) {

		String dateStr = Constant.EMPTY_STRING;

		try {
			dateStr = getInputDateFormat().format(inputDate);
		} catch (Exception e) {
			// do nothing
		}

		return dateStr;
	}

	public static String formatDate(Date inputDate) {
		
		String dateStr = Constant.EMPTY_STRING;
		
		try {
			dateStr = getDateFormat().format(inputDate);
		} catch (Exception e) {
			// do nothing
		}
		
		return dateStr;
	}
	
	public static String formatDayDate(Date inputDate) {
		
		String dateStr = Constant.EMPTY_STRING;
		
		try {
			dateStr = getDateFormat("EEEE, dd MMM yyyy").format(inputDate);
		} catch (Exception e) {
			// do nothing
		}
		
		return dateStr;
	}

    public static String formatDayShortDate(Date inputDate) {

        String dateStr = Constant.EMPTY_STRING;

        try {
            dateStr = getDateFormat("EEEE, dd MMM").format(inputDate);
        } catch (Exception e) {
            // do nothing
        }

        return dateStr;
    }

    public static String formatDateMonth(Date inputDate) {

        String dateStr = Constant.EMPTY_STRING;

        try {
            dateStr = getDateFormat("dd MMM").format(inputDate);
        } catch (Exception e) {
            // do nothing
        }

        return dateStr;
    }

	public static String formatDateOfMonth(Date inputDate) {

		String dateStr = Constant.EMPTY_STRING;

		try {
			dateStr = getDateFormat("dd").format(inputDate);
		} catch (Exception e) {
			// do nothing
		}

		return dateStr;
	}

    public static String dateToStr(Date inputDate) {

        String dateStr = Constant.EMPTY_STRING;

        try {
            dateStr = getDateFormat("dd/mm/yyyy hh:mi").format(inputDate);
        } catch (Exception e) {
            // do nothing
        }

        return dateStr;
    }

    public static Date strToDate(String input, String format) {

        Date date = null;

        try {
			DateFormat df = getDateFormat(format);
			df.setLenient(false);
            date = df.parse(input);
        } catch (Exception e) {
            // do nothing
        }

        return date;
    }

    public static String formatDay(Date inputDate) {

        String dateStr = Constant.EMPTY_STRING;

        try {
            dateStr = getDateFormat("EEEE").format(inputDate);
        } catch (Exception e) {
            // do nothing
        }

        return dateStr;
    }

	public static String formatDayMonth(Date inputDate) {

		String dateStr = Constant.EMPTY_STRING;

		try {
			dateStr = getDateFormat("EEEE, dd MMM").format(inputDate);
		} catch (Exception e) {
			// do nothing
		}

		return dateStr;
	}
	
	public static String formatYear(Date inputDate) {
		
		String dateStr = Constant.EMPTY_STRING;
		
		try {
			dateStr = getDateFormat("yyyy").format(inputDate);
		} catch (Exception e) {
			// do nothing
		}
		
		return dateStr;
	}
	
	public static String formatMonthYear(Date inputDate) {
		
		String dateStr = Constant.EMPTY_STRING;
		
		try {
			dateStr = getDateFormat("MMMM yyyy").format(inputDate);
		} catch (Exception e) {
			// do nothing
		}
		
		return dateStr;
	}

	public static String formatMonth(Date inputDate) {

		String dateStr = Constant.EMPTY_STRING;

		try {
			dateStr = getDateFormat("MMM").format(inputDate);
		} catch (Exception e) {
			// do nothing
		}

		return dateStr;
	}

    public static String formatDate(Date inputDate, String dateFormat) {

        String dateStr = Constant.EMPTY_STRING;

        try {
            dateStr = getDateFormat(dateFormat).format(inputDate);
        } catch (Exception e) {
            // do nothing
        }

        return dateStr;
    }
	
	public static String formatDateTime(Date inputDate) {
		
		String dateStr = Constant.EMPTY_STRING;
		
		try {
			dateStr = getDateTimeFormat().format(inputDate);
		} catch (Exception e) {
			// do nothing
		}
		
		return dateStr;
	}
	
	public static String formatTime(Date inputDate) {
		
		String dateStr = Constant.EMPTY_STRING;
		
		try {
			dateStr = getTimeFormat().format(inputDate);
		} catch (Exception e) {
			// do nothing
		}
		
		return dateStr;
	}

	public static String format12HrTime(Date inputDate) {

		String dateStr = Constant.EMPTY_STRING;

		try {
			dateStr = get12HrTimeFormat().format(inputDate);
		} catch (Exception e) {
			// do nothing
		}

		return dateStr;
	}

	public static String format12HrAmPmTime(Date inputDate) {

        String dateStr = Constant.EMPTY_STRING;

        try {
            dateStr = get12HrAmPmTimeFormat().format(inputDate);
        } catch (Exception e) {
            // do nothing
        }

        return dateStr;
    }

    public static String formatOperatingHrTimePeriod(Date inputDate) {

        Date startTime = inputDate;
        Date endTime = new Date(startTime.getTime() + Constant.OPERATING_TIME_WINDOW * Constant.TIME_HOUR);

        String collectionStartTime = CommonUtil.format12HrTime(startTime);
        String collectionEndTime = CommonUtil.format12HrAmPmTime(endTime);

        return collectionStartTime + " - " + collectionEndTime;
    }

	public static String formatDateMonthTime(Date inputDate) {
		
		String dateStr = Constant.EMPTY_STRING;
		
		try {
			dateStr = getDateMonthTimeFormat().format(inputDate);
		} catch (Exception e) {
			// do nothing
		}
		
		return dateStr;
	}
	
	public static String formatDayDateTime(Date inputDate) {
		
		String dateStr = Constant.EMPTY_STRING;
		
		try {
			dateStr = getDayDateTimeFormat().format(inputDate);
		} catch (Exception e) {
			// do nothing
		}
		
		return dateStr;
	}
	
	public static String formatDateTimeMiliSeconds(Date inputDate) {
		
		String dateStr = Constant.EMPTY_STRING;
		
		try {
			dateStr = getDateTimeMiliSecondsFormat().format(inputDate);
		} catch (Exception e) {
			// do nothing
		}
		
		return dateStr;
	}
	
	public static String getTransactionNo() {
		
		Date inputDate = new Date();
		
		String transactionNo = Constant.EMPTY_STRING;
		
		try {
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			transactionNo = df.format(inputDate);
		} catch (Exception e) {
			// do nothing
		}
		
		return transactionNo;
	}

	public static String formatString(Object object) {

		if (object != null && object instanceof String) {
			return (String) object;
		} else {
			return Constant.EMPTY_STRING;
		}
	}

	public static String formatString(String inputStr) {
		
		if (inputStr != null) {
			return inputStr;
		} else {
			return Constant.EMPTY_STRING;
		}
	}
	
	public static String formatString(Integer inputInt) {
		
		if (inputInt != null) {
			return String.valueOf(inputInt);
		} else {
			return Constant.EMPTY_STRING;
		}
	}
	
	public static String formatString(Long inputInt) {
		
		if (inputInt != null) {
			return String.valueOf(inputInt);
		} else {
			return Constant.EMPTY_STRING;
		}
	}
	
	public static String formatString(Float inputInt) {
		
		if (inputInt != null) {
			return String.valueOf(inputInt);
		} else {
			return Constant.EMPTY_STRING;
		}
	}
	
	public static int getFirstNumberIndex(String input) {
		
		Matcher matcher = Pattern.compile("\\d+").matcher(input);
		matcher.find();
		
		return matcher.start();
	}
	
	public static String formatCurrency(String inputStr) {
		
		String currency = inputStr;
		String formatted = inputStr;
		
		NumberFormat nf = NumberFormat.getCurrencyInstance(getLocaleCurrency());
		
        try{
            formatted = nf.format(Double.parseDouble(inputStr));
            
            int firstNumberIndex = getFirstNumberIndex(formatted);
            
            String symbol = Constant.EMPTY_STRING;
            String value = formatted;
            
            if (firstNumberIndex != -1) {
            	
            	symbol = formatted.substring(0, firstNumberIndex);
            	value = formatted.substring(firstNumberIndex, formatted.length());
            }
            
            currency = symbol + " " + value;
            
        } catch (NumberFormatException nfe) {
        	// do nothing
        }
        
        return currency;
	}
	
	/*public static String formatNumber(String inputStr) {
		
		String currency = inputStr;
		String formatted = inputStr;
		
		NumberFormat nf = NumberFormat.getCurrencyInstance(getLocale());
		
        try{
            formatted = nf.format(Double.parseDouble(inputStr));
            
            int firstNumberIndex = getFirstNumberIndex(formatted);
            
            if (firstNumberIndex != -1) {
            	currency = formatted.substring(firstNumberIndex, formatted.length());
            }
            
        } catch (NumberFormatException nfe) {
        	// do nothing
        }
        
        return currency;
	}*/
	
	public static boolean isRound(Float value) {
		
		return Math.round(value) == value;
	}
	
	public static boolean isDecimalCurrency() {
		
		String separator = getCurrencyDecimalSeparator();
		
		return separator != null && (separator.contains(".") || separator.contains(","));
	}
	
	public static String getCurrencyDecimalSeparator() {
		
		String separator = formatCurrency(Float.valueOf(0));
		separator = separator.replaceAll("[^.,]", "");
		
		separator = "".equals(separator) ? null : separator;
		
		return separator;
	}
	
	public static String getNumberDecimalSeparator() {
		
		String separator = formatNumber(Float.valueOf(1)/2);
		separator = separator.replaceAll("[^.,]", "");
		
		separator = "".equals(separator) ? null : separator;
		
		return separator;
	}
	
	public static String formatNumber(String inputStr) {
		
		String number = inputStr;
		
		NumberFormat nf = NumberFormat.getNumberInstance(getLocaleNumber());
		
        try{
            number = nf.format(Double.parseDouble(inputStr));
        } catch (NumberFormatException nfe) {
        	// do nothing
        }
        
        return number;
	}
	
	public static String formatPercentage(Float inputFloat) {
		
		return formatPercentage(formatString(inputFloat));
	}
	
	public static String formatPercentage(String inputStr) {
		
		String number = inputStr;
		
		NumberFormat nf = NumberFormat.getPercentInstance(getLocale());
		
        try{
            number = nf.format(Float.parseFloat(inputStr) / 100);
        } catch (NumberFormatException nfe) {
        	// do nothing
        }
        
        return number;
	}
	
	public static String formatCurrency(Integer inputInt) {
		
		return formatCurrency(formatString(inputInt));
	}
	
	public static String formatCurrency(Long inputInt) {
		
		return formatCurrency(formatString(inputInt));
	}
	
	public static String formatCurrency(Float inputInt) {
		
		return formatCurrency(formatString(inputInt));
	}
		
	public static String formatCurrencyWithoutSymbol(Float inputInt) {
		
		return formatCurrency(formatString(inputInt)).replace(getCurrencySymbol() + " ", "");
	}
	
	public static String formatNumber(Integer inputInt) {
		
		return formatNumber(formatString(inputInt));
	}
	
	public static String formatNumber(Long inputInt) {
		
		return formatNumber(formatString(inputInt));
	}
	
	public static String formatNumber(Float inputInt) {
		
		return formatNumber(formatString(inputInt));
	}
		
	public static String formatPlainNumber(Float inputInt) {
		
		String number = formatNumber(inputInt);
		
		if (".".equals(getNumberDecimalSeparator())) {
			number = number.replaceAll(",", "");
		} else {
			number = number.replaceAll("\\.", "");
		}
		
		return number; 
	}
	
	public static Integer parseIntNumber(String inputStr) {
		
		String unformatted = inputStr;
		Integer number = null;
		
		if (!isEmpty(inputStr)) {
			unformatted = inputStr.replaceAll("[^1234567890.,]", "");
		}
        
		try {
			NumberFormat nf = NumberFormat.getNumberInstance(getLocaleNumber());
			number = (nf.parse(unformatted)).intValue();
		} catch (Exception e) {
			// do nothing
		}
		
        return number; 
	}
	
	public static Float parseFloatNumber(String inputStr) {
		
		String unformatted = inputStr;
		Float number = null;
		
		if (!isEmpty(inputStr)) {
			unformatted = inputStr.replaceAll("[^1234567890.,]", "");
		}
        
		try {
			NumberFormat nf = NumberFormat.getNumberInstance(getLocaleNumber());
			number = (nf.parse(unformatted)).floatValue();
		} catch (Exception e) {
			// do nothing
		}
		
        return number; 
	}
	
	public static Integer parseIntCurrency(String inputStr) {
		
		String unformatted = inputStr;
		Integer number = null;
		
		if (!isEmpty(inputStr)) {
			unformatted = inputStr.replaceAll("[^1234567890.,]", "");
		}
        
		try {
			number = Integer.valueOf(unformatted);
		} catch (Exception e) {
			// do nothing
		}
		
        return number; 
	}
	
	public static Float parseFloatCurrency(String inputStr) {
		
		String unformatted = inputStr;
		Float number = null;
		
		if (!isEmpty(inputStr)) {
			unformatted = inputStr.replaceAll("[^1234567890.,]", "");
		}
        
		try {
			NumberFormat nf = NumberFormat.getNumberInstance(getLocaleCurrency());
			number = nf.parse(unformatted).floatValue();
		} catch (Exception e) {
			// do nothing
		}
		
        return number; 
	}
	
	public static String parseCurrencyAsString(String inputStr) {
		
		String unformatted = inputStr;
		
		if (!isEmpty(inputStr)) {
			unformatted = inputStr.replaceAll("[^1234567890.,]", "");
		}
        
		return unformatted; 
	}
	
	public static Integer parseInteger(String inputStr) {
		
		Integer number = null;
		
		try {
			if (!isEmpty(inputStr)) {
				number = Integer.valueOf(inputStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return number; 
	}
	
	public static Float parseFloat(String inputStr) {
		
		Float number = null;
		
		try {
			if (!isEmpty(inputStr)) {
				number = Float.valueOf(inputStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return number; 
	}

	public static String getCurrencySymbol() {
		
		String symbol = Constant.EMPTY_STRING;
		NumberFormat nf = NumberFormat.getCurrencyInstance(getLocaleCurrency());
		
        try{
            String formatted = nf.format(Double.valueOf(0));
            
            int firstNumberIndex = getFirstNumberIndex(formatted);
            
            if (firstNumberIndex != -1) {
            	symbol = formatted.substring(0, firstNumberIndex);
            }
            
        } catch (NumberFormatException nfe) {
        	// do nothing
        }
        
        return symbol;
	}
	
	public static String getCertDN(Context ctx) {
		
		if (!CommonUtil.isEmpty(mCertDN)) {
			return mCertDN;
		}
		
		try {
			PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature signatures[] = pinfo.signatures;

			CertificateFactory cf = CertificateFactory.getInstance("X.509");

			for (int i = 0; i < signatures.length; i++) {
				ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
				X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);

				mCertDN = cert.getSubjectDN().getName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mCertDN;
	}

	public static String getMajorNumber(int value) {

		return String.valueOf(value / 1000);
	}

	public static String getMinorNumber(int value) {

		return String.format(".%03d", value % 1000);
	}

    public static int getHour(Date date) {

        Calendar cal = CommonUtil.getCalendar();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static String format2Digits(int value) {

        return String.format("%02d", value);
    }

    public static String format1Decimal(float value) {

        return String.format("%.1f", value);
    }

	public static Bitmap createBitmap(Context context, int drawable, int dimWidth, int dimHeight) {

		int width = context.getResources().getDimensionPixelSize(dimWidth);
		int height = context.getResources().getDimensionPixelSize(dimHeight);

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Drawable shape = ContextCompat.getDrawable(context, drawable);
		shape.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
		shape.draw(canvas);

		return bitmap;
	}

	public static String getOrderProductInfo(OrderBean orderBean) {

		String info = Constant.EMPTY_STRING;

        List<OrderProductBean> orderProducts = orderBean.getOrderProducts();

        if (orderProducts != null) {

            for (OrderProductBean orderProductBean : orderProducts) {

                String productInfo = "";

                if (Constant.PRODUCT_KG_WASH_IRON.equals(orderProductBean.getProduct().getCode())) {
                    productInfo = CodeUtil.getProductLabel(orderProductBean.getProduct().getCode()) + " (" + CommonUtil.formatNumber(orderProductBean.getCount()) + " Kg)";

                } else if (Constant.PRODUCT_ITEM_WASH_IRON.equals(orderProductBean.getProduct().getCode())) {
                    productInfo = CodeUtil.getProductLabel(orderProductBean.getProduct().getCode()) + " (" + CommonUtil.formatNumber(orderProductBean.getCount()) + " Pc)";

                } else if (Constant.PRODUCT_DRY_CLEAN.equals(orderProductBean.getProduct().getCode())) {
                    productInfo = CodeUtil.getProductLabel(orderProductBean.getProduct().getCode()) + " (" + CommonUtil.formatNumber(orderProductBean.getCount()) + " Pc)";
                }

                if (CommonUtil.isEmpty(info)) {
                    info = productInfo;
                } else {
                    info += ", " + productInfo;
                }
            }
        }

        if (CommonUtil.isEmpty(info)) {
            info = mContext.getString(R.string.empty_order);
        }

		return info;
	}

    public static float getProgressByStatus(String status) {

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(status) ||
			Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(status)) {

			return 0f;

        } else if (Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(status) ||
            Constant.ORDER_STATUS_COLLECTED.equals(status)) {
            return 0.2f;

        } else if (Constant.ORDER_STATUS_CLEANING_IN_PROGRESS.equals(status)) {
            return 0.4f;

        } else if (Constant.ORDER_STATUS_CLEANED.equals(status) ||
			Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY.equals(status)) {

			return 0.6f;

        } else if (Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS.equals(status)) {
            return 0.8f;

        } else if (Constant.ORDER_STATUS_COMPLETED.equals(status)) {
            return 1.0f;

        } else {
            return 1.0f;
        }
    }

    public static String getProcessOrderTitle(String status) {

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(status)) {
            return mContext.getString(R.string.title_order_new_order);

        } else if (Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(status)) {
            return mContext.getString(R.string.title_order_assigned_for_collection);

        } else if (Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(status)) {
			return mContext.getString(R.string.title_order_collection_in_progress);

		} else if (Constant.ORDER_STATUS_COLLECTED.equals(status)) {
            return mContext.getString(R.string.title_order_collected);

        } else if (Constant.ORDER_STATUS_CLEANING_IN_PROGRESS.equals(status)) {
            return mContext.getString(R.string.title_order_cleaning);

        } else if (Constant.ORDER_STATUS_CLEANED.equals(status)) {
            return mContext.getString(R.string.title_order_cleaned);

        } else if (Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY.equals(status)) {
            return mContext.getString(R.string.title_order_assigned_for_delivery);

        } else if (Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS.equals(status)) {
            return mContext.getString(R.string.title_order_delivery_in_progress);

        } else if (Constant.ORDER_STATUS_COMPLETED.equals(status)) {
            return mContext.getString(R.string.title_order_completed);

		} else if (Constant.ORDER_STATUS_WARNING.equals(status)) {
			return mContext.getString(R.string.title_order_warning);

		} else if (Constant.ORDER_STATUS_CRITICAL.equals(status)) {
			return mContext.getString(R.string.title_order_critical);
		}

        return Constant.EMPTY_STRING;
    }

    public static String getCircleOrderStatusLabel(String status) {

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(status)) {
            return mContext.getString(R.string.order_status_circle_new_order);

        } else if (Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(status)) {
			return mContext.getString(R.string.order_status_circle_assigned_for_collection);

		} else if (Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(status)) {
			return mContext.getString(R.string.order_status_circle_collection_in_progress);

		} else if (Constant.ORDER_STATUS_COLLECTED.equals(status)) {
            return mContext.getString(R.string.order_status_circle_collected);

        } else if (Constant.ORDER_STATUS_CLEANING_IN_PROGRESS.equals(status)) {
            return mContext.getString(R.string.order_status_circle_cleaning_in_progress);

        } else if (Constant.ORDER_STATUS_CLEANED.equals(status)) {
            return mContext.getString(R.string.order_status_circle_cleaned);

        } else if (Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY.equals(status)) {
			return mContext.getString(R.string.order_status_circle_assigned_for_collection);

		} else if (Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS.equals(status)) {
            return mContext.getString(R.string.order_status_circle_delivery_in_progress);

        } else if (Constant.ORDER_STATUS_COMPLETED.equals(status)) {
            return mContext.getString(R.string.order_status_circle_completed);

        } else {
            return mContext.getString(R.string.order_status_circle_new_order);
        }
    }

    public static Drawable getCircleOrderStatusImage(String status) {

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(status) ||
			Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(status)) {

			return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_1);

        } else if (Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(status) ||
			Constant.ORDER_STATUS_COLLECTED.equals(status)) {

            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_2);

        } else if (Constant.ORDER_STATUS_CLEANING_IN_PROGRESS.equals(status)) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_3);

        } else if (Constant.ORDER_STATUS_CLEANED.equals(status) ||
			Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY.equals(status)) {

            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_4);

        } else if (Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS.equals(status)) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_5);

        } else if (Constant.ORDER_STATUS_COMPLETED.equals(status)) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_6);

		} else {
			return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_1);
		}
    }

	public static Drawable getCircleOrderProgressImage(int percentage) {

        if (percentage == 0) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_progress_1);

		} else if (percentage == 20) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_progress_2);

		} else if (percentage == 40) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_progress_3);

		} else if (percentage == 60) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_progress_4);

		} else if (percentage == 80) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_progress_5);

		} else if (percentage == 100) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_progress_1);

		} else {
            return null;
		}
	}

    public static Drawable getCircleOrderStatusImage(int percentage) {

        if (percentage == 0) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_1);

        } else if (percentage == 20) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_2);

        } else if (percentage == 40) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_3);

        } else if (percentage == 60) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_4);

        } else if (percentage == 80) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_5);

        } else if (percentage == 100) {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_6);

        } else {
            return ContextCompat.getDrawable(mContext, R.drawable.icon_order_status_1);
        }
    }

	public static Bitmap getBitmap(byte[] bytes) {

		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	public static int getProcessingDays(String speedType) {

		int processingDays = Constant.PROCESS_TIME_REGULAR;

		if (Constant.SPEED_TYPE_EXPRESS.equals(speedType)) {
			processingDays = Constant.PROCESS_TIME_EXPRESS;

		} else if (Constant.SPEED_TYPE_DELUXE.equals(speedType)) {
			processingDays = Constant.PROCESS_TIME_DELUXE;
		}

		return processingDays;
	}

	public static Date getDeliveryDate(Date minProcessingTime, Date idealDeliveryDate) {

		Date date = CommonUtil.getDateWithoutTime(idealDeliveryDate);

		Date deliveryDate = null;
		Date minDeliveryDate = null;

		Map<Date, List<Long>> deliveryDateTimes = Session.getDeliveryDateTimes();

		int maxDateSearchCount = 14;
		int dateSearchCount = 0;

		while (deliveryDate == null && dateSearchCount < maxDateSearchCount) {

			List<Long> availableTimes = deliveryDateTimes.get(date);

			for (Long time : availableTimes) {

				if (date.getTime() + time >= idealDeliveryDate.getTime()) {

					deliveryDate = new Date(date.getTime() + time);
					break;
				}

				if (date.getTime() + time >= minProcessingTime.getTime()) {

					minDeliveryDate = new Date(date.getTime() + time);
				}
			}

			if (deliveryDate == null && minDeliveryDate != null) {
				deliveryDate = minDeliveryDate;
			}

			date.setTime(date.getTime() + Constant.TIME_DAY);

			dateSearchCount++;
		}

		return deliveryDate;
	}

	public static String getAddress(LatLng latLng) {

		List<Address> addresses = null;
		String errorMessage = null;

		String addrStr = null;

		try {

			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

			addresses = geocoder.getFromLocation(
					latLng.latitude,
					latLng.longitude,
					// In this sample, get just a single address.
					1);

		} catch (IOException ioException) {
			// Catch network or other I/O problems.
			errorMessage = "service_not_available";

		} catch (IllegalArgumentException illegalArgumentException) {
			// Catch invalid latitude or longitude values.
			errorMessage = "invalid_lat_long_used";
		}

		// Handle case where no address was found.
		if (addresses == null || addresses.size() == 0) {
			if (CommonUtil.isEmpty(errorMessage)) {
				errorMessage = "no_address_found";
			}
		} else {

			Address address = addresses.get(0);
			ArrayList<String> addressFragments = new ArrayList<String>();

			addrStr = Constant.EMPTY_STRING;

			// Fetch the address lines using getAddressLine,
			// join them, and send them to the thread.

			int addrLine = 2 < address.getMaxAddressLineIndex() ? 2 : address.getMaxAddressLineIndex();

			for (int i = 0; i <= addrLine; i++) {

				addressFragments.add(address.getAddressLine(i));

				if (i == 0) {
					addrStr = address.getAddressLine(i);

					int index = addrStr.indexOf("No.");

					if (index != -1) {
						addrStr = addrStr.substring(0,index);
						addrStr = addrStr.trim();
					}

				} else {
					addrStr += ", " + address.getAddressLine(i);
				}
			}
		}

		return addrStr;
	}

    public static double calculationByDistance(double lat1, double lng1, double lat2, double lng2) {

        int Radius = 6371;// radius of earth in Km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        double km = Radius * c;

        return km * 1000;
    }
}
