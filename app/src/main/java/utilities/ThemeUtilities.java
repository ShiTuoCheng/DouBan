package utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.shituocheng.calcalculateapplication.com.douban.R;

/**
 * Created by shituocheng on 16/6/5.
 */

public class ThemeUtilities {

    public static int getThemeState(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_setting",Context.MODE_PRIVATE);
        return sharedPreferences.getInt("theme",0);
    }

    public static void setThemeState(Context context, int themeValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_setting",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme",themeValue);
        editor.apply();
    }

    public static void setTheme(Activity activity){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (ThemeUtilities.getThemeState(activity)==0){
                window.setStatusBarColor(activity.getResources().getColor(R.color.colorStatusBarLight));
            }else {
                window.setStatusBarColor(activity.getResources().getColor(R.color.colorStatusBarDark));
            }
        }
    }
}
