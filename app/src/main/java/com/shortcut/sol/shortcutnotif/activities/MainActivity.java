package com.shortcut.sol.shortcutnotif.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.shortcut.sol.shortcutnotif.ConstEnum;
import com.shortcut.sol.shortcutnotif.R;
import com.shortcut.sol.shortcutnotif.activities.notification_btn.HomeInvisibleActivity;
import com.shortcut.sol.shortcutnotif.activities.notification_btn.LockInvisibleActivity;
import com.shortcut.sol.shortcutnotif.activities.notification_btn.TaskManagerInvisibleActivity;
import com.shortcut.sol.shortcutnotif.activities.notification_btn.VolumeInvisibleActivity;
import com.shortcut.sol.shortcutnotif.fragments.HelpFragment;
import com.shortcut.sol.shortcutnotif.fragments.SettingFragment;
import com.shortcut.sol.shortcutnotif.fragments.UninstallTipsFragment;

/**
 * Created by safeki on 01/06/2015.
 */
public class MainActivity extends AppCompatActivity {

    private static final String FIRST_TIME_LAUNCH = "firsTimeLaunch";
    private NotificationManager notificationManager;
    private NotificationReceiver notificationReceiver;
    private NotificationCompat.Builder builderShortcutNotif;
    private SharedPreferences settings;
    private Button btnSwitch;
    private static boolean isEnabled;
    private static final int NOTIFICATION_ID = 11;
    //ic_menu_setting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.shortcut.sol.shortcutnotif.R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        constructNotification();

        btnSwitch = (Button) findViewById(R.id.btnEnableDisable);
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                enableDisableShortcutNotif();
                changeIsEnabled();
            }
        });

        initIsEnabled();
        initReceiver();
        verifyFirstTimeLaunch();
    }

    private void verifyFirstTimeLaunch() {
        settings = getPreferences(MODE_PRIVATE);
        if (settings.getBoolean(FIRST_TIME_LAUNCH, true)) {
            settings.edit().putBoolean(FIRST_TIME_LAUNCH, false).apply();
            Intent intent = new Intent(this, FirstTimeLaunchActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstEnum.GET_NOTIFICATION_RESPONSE.getValue());
        notificationReceiver = new NotificationReceiver();
        registerReceiver(notificationReceiver, filter);
    }

    public void constructNotification() {
        Intent intentLock = new Intent(this, LockInvisibleActivity.class);
        Intent intentVolume = new Intent(this, VolumeInvisibleActivity.class);
        Intent intentHome = new Intent(this, HomeInvisibleActivity.class);
        Intent intentTaskManager = new Intent(this, TaskManagerInvisibleActivity.class);

        PendingIntent pIntentLock = PendingIntent.getActivity(this, 0, intentLock, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pIntentHome = PendingIntent.getActivity(this, 0, intentHome, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pIntentVolume = PendingIntent.getActivity(this, 0, intentVolume, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pIntentTaskManager = PendingIntent.getActivity(this, 0, intentTaskManager, PendingIntent.FLAG_UPDATE_CURRENT);

        int theme_choice = getPreferences(Context.MODE_PRIVATE).getInt("pref_theme", 0);
        TypedArray themes_available = getResources().obtainTypedArray(R.array.pref_themes_entries);
        
        RemoteViews notificationView = new RemoteViews(getPackageName(), themes_available.getResourceId(theme_choice, 0));
        notificationView.setOnClickPendingIntent(R.id.lockBtn, pIntentLock);
        notificationView.setOnClickPendingIntent(R.id.homeBtn, pIntentHome);
        notificationView.setOnClickPendingIntent(R.id.volumeBtn, pIntentVolume);
        notificationView.setOnClickPendingIntent(R.id.taskManagerBtn, pIntentTaskManager);

        builderShortcutNotif = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_app)
                .setTicker(getString(R.string.shortcut_notification_enabled))
                .setContent(notificationView)
                .setAutoCancel(false)
                .setOngoing(true);
    }

    private void enableDisableShortcutNotif() {
        if (isEnabled) {
            // Do Disable
            doDisable();
        } else {
            // Do Enable
            doEnable();
        }
    }

    public void doEnable() {
        notificationManager.notify(NOTIFICATION_ID, builderShortcutNotif.build());
        btnSwitch.setText(getString(R.string.btn_disable_shortcut_notification));
    }

    public void doDisable() {
        notificationManager.cancel(NOTIFICATION_ID);
        btnSwitch.setText(getString(R.string.btn_enable_shortcut_notification));
    }

    private void initIsEnabled() {
        Intent i = new Intent(ConstEnum.GET_NOTIFICATION.getValue());
        i.putExtra(ConstEnum.NOTIFICATION_ID.getValue(), NOTIFICATION_ID);
        sendBroadcast(i);
    }

    private void changeIsEnabled() {
        isEnabled = !isEnabled;
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(notificationReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                SettingFragment themeChoiceDialogFragment = new SettingFragment();
                themeChoiceDialogFragment.show(getFragmentManager(), "SettingFragmentTag");
                return true;
            case R.id.action_help:
                HelpFragment helpFragment = new HelpFragment();
                helpFragment.show(getFragmentManager(), "HelpFragmentTag");
                return true;
            case R.id.action_uninstall:
                UninstallTipsFragment uninstallTipsFragment = new UninstallTipsFragment();
                uninstallTipsFragment.show(getFragmentManager(), "UninstallTipsFragmentTag");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("NotificationReceiver", "notification id exist= " + String.valueOf(intent.getBooleanExtra(ConstEnum.NOTIFICATION_ID_EXIST.getValue(), false)));
            isEnabled = intent.getBooleanExtra(ConstEnum.NOTIFICATION_ID_EXIST.getValue(), false);
            if (isEnabled) {
                btnSwitch.setText(getString(R.string.btn_disable_shortcut_notification));
            } else {
                btnSwitch.setText(getString(R.string.btn_enable_shortcut_notification));
            }
        }
    }
}
