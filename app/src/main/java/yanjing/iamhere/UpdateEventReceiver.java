package yanjing.iamhere;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/**
 * Created by Yanjing on 9/18/14.
 */
public class UpdateEventReceiver extends BroadcastReceiver {


        private static final String APP_TAG = "yanjing.iamhere";

        @Override
        public void onReceive(final Context ctx, final Intent intent) {
            Log.d(APP_TAG, "SchedulerEventReceiver.onReceive() called");
            Intent eventService = new Intent(ctx, UpdateEventService.class);
            ctx.startService(eventService);
        }



}
