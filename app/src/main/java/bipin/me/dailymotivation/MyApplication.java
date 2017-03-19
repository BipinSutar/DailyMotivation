package bipin.me.dailymotivation;

import android.app.Application;
import android.widget.Toast;


/**
 * Created by BipinSutar on 15/03/17.
 */

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "MyApplication.java", Toast.LENGTH_SHORT).show();
    }

}
