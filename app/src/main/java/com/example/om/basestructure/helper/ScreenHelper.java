package com.example.om.basestructure.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by android on 03.03.18.
 */

public class ScreenHelper {

    public static <T extends Activity> void startScreen(Context context, Class<T> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static <T extends Activity> void startScreenForResult(Activity context, Class<T> activity, int requestCode, Bundle bundle) {
        Intent intent = new Intent(context, activity);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
    }

}
