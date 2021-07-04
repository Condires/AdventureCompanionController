package com.conteus.adventure.companion.controller;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;


import com.conteus.adventure.companion.controller.settings.Settings;
import com.conteus.adventure.companion.controller.settings.SettingsPrefActivity;

import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;

/*
  Der Controller läuft auf demselben Hyndy wie der Player. Der Controller kann den Player starten und stopen
  Der Controller muss immer laufen, er ist die Kontakts tellen von aussen, wen der Player abstürzt.
 */
public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private String TAG = this.getClass().getSimpleName();
    private static MainActivity inst;
    //ArrayList<String> smsMessagesList = new ArrayList<String>();
    //ListView smsListView;
    //ArrayAdapter arrayAdapter;
    private static Context myContext;

    public static Context getContext() {
        return myContext;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public static MainActivity instance() {
        return inst;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (inst == null) inst = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myContext = this;


        //smsListView = (ListView) findViewById(R.id.SMSList);
        //arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        //smsListView.setAdapter(arrayAdapter);
        //smsListView.setOnItemClickListener(this);

        requestPermissions(new String[]{RECEIVE_SMS, SEND_SMS}, 10);
        Context context = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getApplicationContext().getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("package:" + packageName));
                context.startActivity(intent);
            }
        }

        // die Preferenzen des Companions werden in den Settings abgelegt

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Settings settings = Settings.getInstance();
            String keyCaller = (String) getIntent().getStringExtra("keyCaller"); //Obtaining data
            settings.setKeyCaller(keyCaller);
            String keyNotfall = (String) getIntent().getStringExtra("key_notfall_nummer"); //Obtaining data
            settings.setKeyNotfallNummer(keyNotfall);
            String startMe = (String) getIntent().getStringExtra("startMe"); //Obtaining data
            if (startMe != null) {
                try {
                    Thread.sleep(10000);
                } catch(InterruptedException e) {
                    // Process exception
                }
                startAsdventureCompanion();
            }
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button mSettingsButton = (Button) findViewById(R.id.button_setting);

        mSettingsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(new Intent(MainActivity.this, SettingsPrefActivity.class));
                    }
                });

    }

    private void loadAnlageFromAirtable(String name){
        Intent intent = new Intent(Intent.ACTION_SEND);  //Action_Main heißt Hauptklasse
        Toast.makeText(getApplicationContext(), " Load "+name +" from Airtable called", Toast.LENGTH_SHORT).show();
        intent.setComponent(new ComponentName("com.condires.adventure.companion", "com.condires.adventure.companion.bluetooth.RemoteActivity"));
        intent.putExtra("Command", "LoadFromAirtable");
        intent.putExtra("Name", name);
        startActivity(intent);

    }
    private void flipVol(String name){
        Intent intent = new Intent(Intent.ACTION_SEND);  //Action_Main heißt Hauptklasse
        Toast.makeText(getApplicationContext(), " toggle Sound called", Toast.LENGTH_SHORT).show();
        intent.setComponent(new ComponentName("com.condires.adventure.companion", "com.condires.adventure.companion.bluetooth.RemoteActivity"));
        intent.putExtra("Command", "FLIPVOL");
        intent.putExtra("Name", name);
        startActivity(intent);

    }

    private void saveAnlageToAirtable(String name){
        Intent intent = new Intent(Intent.ACTION_SEND);  //Action_Main heißt Hauptklasse
        Toast.makeText(getApplicationContext(), " Save "+name +" from Airtable called", Toast.LENGTH_SHORT).show();
        intent.setComponent(new ComponentName("com.condires.adventure.companion", "com.condires.adventure.companion.bluetooth.RemoteActivity"));
        intent.putExtra("Command", "SaveToAirtable");
        intent.putExtra("Name", name);
        startActivity(intent);

    }

    private void loadAnlageFromDB(String name){
        Intent intent = new Intent(Intent.ACTION_SEND);  //Action_Main heißt Hauptklasse
        Toast.makeText(getApplicationContext(), " Load "+name +" from Database called", Toast.LENGTH_SHORT).show();
        intent.setComponent(new ComponentName("com.condires.adventure.companion", "com.condires.adventure.companion.bluetooth.RemoteActivity"));
        intent.putExtra("Command", "LoadFromDB");
        intent.putExtra("Name", name);
        startActivity(intent);

    }

    private void saveAnlageToDB(String name){
        Intent intent = new Intent(Intent.ACTION_SEND);  //Action_Main heißt Hauptklasse
        Toast.makeText(getApplicationContext(), " Save "+name +" from Database called", Toast.LENGTH_SHORT).show();
        intent.setComponent(new ComponentName("com.condires.adventure.companion", "com.condires.adventure.companion.bluetooth.RemoteActivity"));
        intent.putExtra("Command", "SaveToDB");
        intent.putExtra("Name", name);
        startActivity(intent);

    }

    public void openApp(String appPackageName) {

        PackageManager pm = this.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appPackageName);
        if (intent != null) {
            this.startActivity(intent);
        }else{
            Log.e("<Class name>", "Cannot start app, appPackageName:'" + appPackageName + "'");
        }
    }

    private void startAsdventureCompanion(){
        Intent launchIntent = new Intent(Intent.ACTION_MAIN);//Action_Main heißt Hauptklasse
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        launchIntent.setClassName("com.condires.adventure.companion", "com.condires.adventure.companion.MainActivity");//genau spezifizieren, was aufgerufen werden soll, sonst kommt auswahl
        if (launchIntent != null) {
            /*
            launchIntent.putExtra("TestOn", "0");
            launchIntent.putExtra("DarkScreen", true);
            launchIntent.putExtra("AnlageIndex", 0);
            launchIntent.putExtra("Stop", false);
            */
            startActivity(launchIntent);
        } else {
            Toast.makeText(getApplicationContext(), " launch Intent not available", Toast.LENGTH_SHORT).show();
        }
    }




    public void startBooster() {
        openApp( "com.goodev.volume.booster");
    }

    public void startCompanion() {
        openApp( "com.condires.adventure.companion");
    }


    private void startAdventureCompanion() {
        Intent launchIntent = new Intent();
        launchIntent.setComponent(new ComponentName("com.condires.adventure.companion", "com.condires.adventure.companion.LocationTrackerActivity"));

        //Intent launchIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.condires.adventure.companion");
        if (launchIntent != null) {

            launchIntent.putExtra("TestOn", "0");
            launchIntent.putExtra("DarkScreen", true);
            launchIntent.putExtra("AnlageIndex", 0);
            launchIntent.putExtra("Stop", false);

            startActivity(launchIntent);
            //finish();
        } else {
            Toast.makeText(getApplicationContext(), " launch Intent not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAdventureCompanion() {
        Intent intent = new Intent(Intent.ACTION_SEND);  //Action_Main heißt Hauptklasse
        Toast.makeText(getApplicationContext(), " Stop called", Toast.LENGTH_SHORT).show();
        intent.setComponent(new ComponentName("com.condires.adventure.companion", "com.condires.adventure.companion.MainActivity"));
        intent.putExtra("Stop", true);
        startActivity(intent);
    }



    /*
     * wird im Moment nicht verwendet, sollte eine App stoppen
     */
    private int getPid(String packageName){
        ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> pids = am.getRunningAppProcesses();
        int processid = 0;
        for(int i = 0; i < pids.size(); i++) {
            ActivityManager.RunningAppProcessInfo info = pids.get(i);

            Log.i("PID",pids.get(i) + "");
            Log.i("PID Package",info.processName);

            if(info.processName.equalsIgnoreCase(packageName)){
                processid = info.pid;
                return processid;
            }
        }
        return -1;
    }

    private void remoteControl(String msg) {
        String cmd = msg.substring(0,3);
        switch (cmd) {
            case "AC+":
                startCompanion();
                //startAdventureCompanion();
                break;
            case "AC-":
                // stop
                stopAdventureCompanion();
                break;
            case "BO+":
                // stop
                startBooster();
                break;
            case "FLI":
                // stop
                flipVol("test");
                break;

        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsPrefActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateList(final String smsBody, String smsSender) {
        //arrayAdapter.insert(smsBody, 0);
        //arrayAdapter.notifyDataSetChanged();

        // die Preferenzen werden von Companion beim Starten gesetzt
        //String SenderPhoneNumber = "+41795432109";
        //String SMSStartsWith = "Wangs:";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        String SenderPhoneNumber = prefs.getString("key_caller", "+41795432109");
        String SMSStartsWith = prefs.getString("sms_signature", "Wangs:");
        if (SenderPhoneNumber.contains(smsSender) && smsBody.startsWith(SMSStartsWith)) {
            String cmd = smsBody.substring(SMSStartsWith.length());
            remoteControl(cmd);
        }

    }


    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        /*
        try {
            String[] smsMessages = smsMessagesList.get(pos).split("\n");
            String address = smsMessages[0];
            String smsMessage = "";
            for (int i = 1; i < smsMessages.length; ++i) {
                smsMessage += smsMessages[i];
            }

            String smsMessageStr = address + "\n";
            smsMessageStr += smsMessage;
            Toast.makeText(this, smsMessageStr, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

}
