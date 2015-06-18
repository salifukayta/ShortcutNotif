package com.shortcut.sol.shortcutnotif.activities;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.shortcut.sol.shortcutnotif.R;
import com.shortcut.sol.shortcutnotif.receivers.AdminReceiver;

public class FirstTimeLaunchActivity extends Activity {

    private static final int ADMIN_INTENT = 15;
    private ComponentName adminComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_launch);

        adminComponent = new ComponentName(this, AdminReceiver.class);
        Button btnConfirmation = (Button) findViewById(R.id.btnConfirm);
        btnConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // activate notification listener
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);

                // get admin to lock
                Intent intent2 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent2.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
                intent2.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Make me admin now!");
                startActivityForResult(intent2, ADMIN_INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_INTENT) {
            // back to main activity
            Intent intent3 = new Intent(FirstTimeLaunchActivity.this, MainActivity.class);
            startActivity(intent3);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_time_launch, menu);
        return true;
    }

    @Override
    public void onBackPressed() { }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
