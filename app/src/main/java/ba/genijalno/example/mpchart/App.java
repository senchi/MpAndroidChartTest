package ba.genijalno.example.mpchart;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Handles the initialization of the App, and keeps references to important instances like
 * Gson, Volley Request Queue, Image Loader and Handler
 */
public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    // public Gson instance
    public static final Gson GSON = new GsonBuilder().create();

    // gui date/time display format
    public static DateFormat getDateTimeFormat(){
        return  DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
    }
    public static DateFormat getDateFormat(){
        return  DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    }
    public static String getDateTimeDisplayString(Date date){
        return String.format("%s %s", getDateTimeFormat().format(date), TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
    }
    public static SimpleDateFormat getChartTimeFormat(){
        return new SimpleDateFormat("HH'h'", Locale.getDefault());
    }
    public static SimpleDateFormat getChartDateFormat(){
        return new SimpleDateFormat("MMM dd", Locale.getDefault());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
