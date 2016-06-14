package utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by shituocheng on 16/6/9.
 */

public class NetworkState {
    public static boolean networkConneted(Context context) {

        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null)
                return info.isAvailable();
        }

        return false;
    }
}
