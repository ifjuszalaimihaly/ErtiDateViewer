package szalaimihaly.hu.ertidataviewer.dataloader;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by Mihaly on 2016.02.12..
 */
public class ErtiDataViewer extends Application {

    private static DataLoader dataLoader;
    private static Context context;



    @Override
    public void onCreate() {
        super.onCreate();
        ErtiDataViewer.context = getApplicationContext();

    }

    public static DataLoader getDataLoader() {
        return dataLoader;
    }

    public static void setDataLoader(DataLoader dataLoader) {
        ErtiDataViewer.dataLoader = dataLoader;
    }

    public static boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    public static void changeDataLoader() {
        if (!isOnline()) {
            ErtiDataViewer.setDataLoader(null);
        } else {
            if (!(ErtiDataViewer.getDataLoader() instanceof SensorhubDataLoader)) {
                    ErtiDataViewer.setDataLoader(new SensorhubDataLoader(SensorhubDataLoader.OUTER));
            }
        }
    }



}

