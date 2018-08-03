package com.codvision.step.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codvision.step.ISportStepInterface;
import com.codvision.step.R;
import com.codvision.step.TodayStepManager;
import com.codvision.step.main.StepInterface;
import com.codvision.step.service.TodayStepService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private static final int REFRESH_STEP_WHAT = 0;

    //循环取当前时刻的步数中间的间隔时间
    private long TIME_INTERVAL_REFRESH = 500;

    private Handler mDelayHandler = new Handler(new TodayStepCounterCall());
    private int mStepSum;

    private ISportStepInterface iSportStepInterface;

    private TextView mStepArrayTextView;


    public String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //初始化计步模块,设置0点分隔，启动服务,调用服务里的onCreate()和onStartCommand(),新建数据库实例，sensorManager实例，初始化通知,更新通知，注册传感器
        TodayStepManager.init(getApplication());

        mStepArrayTextView = findViewById(R.id.stepArrayTextView);

        //开启计步Service，同时绑定Activity进行aidl通信
        Intent intent = new Intent(this, TodayStepService.class);
        startService(intent);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //Activity和Service通过aidl进行通信
                iSportStepInterface = ISportStepInterface.Stub.asInterface(service);
                try {
                    mStepSum = iSportStepInterface.getCurrentTimeSportStep();//获取当前步数
                    updateStepCount();//更新步数UI
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);//每隔500毫秒获取一次计步数据刷新UI

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);

    }

    class TodayStepCounterCall implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_STEP_WHAT: {
                    //每隔500毫秒获取一次计步数据刷新UI
                    if (null != iSportStepInterface) {
                        int step = 0;
                        try {
                            step = iSportStepInterface.getCurrentTimeSportStep();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        if (mStepSum != step) {
                            mStepSum = step;
                            updateStepCount();
                        }
                    }
                    mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);

                    break;
                }
            }
            return false;
        }
    }

    private void updateStepCount() {
        Log.e(TAG, "updateStepCount : " + mStepSum);
        TextView stepTextView = findViewById(R.id.stepTextView);
        stepTextView.setText(mStepSum + "步");

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stepArrayButton: {

                List<String> list = StepInterface.getAllStep(getApplicationContext(),getApplication());
                Toast.makeText(getApplicationContext(),"" + list.size(),Toast.LENGTH_SHORT).show();
                String step = "";
                for(int i = 0; i < list.size(); i++){
                    step = step + " " + list.get(i);
                }
                mStepArrayTextView.setText(step);
                /**
                //获取所有步数列表
                if (null != iSportStepInterface) {
                    try {
                        String stepArray = iSportStepInterface.getTodaySportStepArray();
                        mStepArrayTextView.setText(stepArray);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }*/
                break;
            }
            case R.id.stepArrayButton1:{
                String step = StepInterface.getStepByDate(getApplicationContext(),getApplication(),"2018-08-02");
                mStepArrayTextView.setText(step);
               /**
                //根据时间来获取步数列表
                if (null != iSportStepInterface) {
                    try {
                        String stepArray = iSportStepInterface.getTodaySportStepArrayByDate(getTodayDate());
                        mStepArrayTextView.setText(stepArray);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }*/
                break;
            }
            case R.id.stepArrayButton2:{
                String step = StepInterface.getStepByDateAndDays(getApplicationContext(),getApplication(),"2018-08-02",1);
                mStepArrayTextView.setText(step);
                /**
                //获取多天步数列表
                if (null != iSportStepInterface) {
                    try {
                        String stepArray = iSportStepInterface.getTodaySportStepArrayByStartDateAndDays("2018-07-17", 6);
                        try {
                            JSONArray jsonArray = new JSONArray(stepArray);
                            for (int i = jsonArray.length() - 1; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String step = jsonObject.getString("stepNum");
                                Log.d("1111",step);
                                mStepArrayTextView.setText(step);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }*/
                break;
            }
            case R.id.return0:
                if (null != iSportStepInterface){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("确定要清除数据吗？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        iSportStepInterface.cleanStep(getTodayDate());
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.show();
                }
            case R.id.getWeekStep:{
                if(null != iSportStepInterface){
                    long time=System.currentTimeMillis();
                    Date date=new Date(time);
                    SimpleDateFormat format=new SimpleDateFormat("EEEE");
                    String weekDay = format.format(date);
                    try{
                        switch (weekDay){
                            case "星期一":{
                                String stepArray = iSportStepInterface.getTodaySportStepArrayByStartDateAndDays(getTodayDate(), 0);
                                try {
                                    JSONArray jsonArray = new JSONArray(stepArray);
                                    for (int i = jsonArray.length() - 1; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String step = jsonObject.getString("stepNum");
                                        mStepArrayTextView.setText(step);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            case "星期二":{
                                String stepArray = iSportStepInterface.getTodaySportStepArrayByStartDateAndDays(getTodayDate(), 1);
                                try {
                                    JSONArray jsonArray = new JSONArray(stepArray);
                                    for (int i = jsonArray.length() - 1; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String step = jsonObject.getString("stepNum");
                                        mStepArrayTextView.setText(step);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            case "星期三":{
                                String stepArray = iSportStepInterface.getTodaySportStepArrayByStartDateAndDays(getTodayDate(), 2);
                                try {
                                    JSONArray jsonArray = new JSONArray(stepArray);
                                    for (int i = jsonArray.length() - 1; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String step = jsonObject.getString("stepNum");
                                        mStepArrayTextView.setText(step);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            case "星期四":{
                                String stepArray = iSportStepInterface.getTodaySportStepArrayByStartDateAndDays(getTodayDate(), 3);
                                try {
                                    JSONArray jsonArray = new JSONArray(stepArray);
                                    for (int i = jsonArray.length() - 1; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String step = jsonObject.getString("stepNum");
                                        mStepArrayTextView.setText(step);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            case "星期五":{
                                String stepArray = iSportStepInterface.getTodaySportStepArrayByStartDateAndDays(getTodayDate(), 4);
                                try {
                                    JSONArray jsonArray = new JSONArray(stepArray);
                                    for (int i = jsonArray.length() - 1; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String step = jsonObject.getString("stepNum");
                                        mStepArrayTextView.setText(step);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            case "星期六":{
                                String stepArray = iSportStepInterface.getTodaySportStepArrayByStartDateAndDays(getTodayDate(), 5);
                                try {
                                    JSONArray jsonArray = new JSONArray(stepArray);
                                    for (int i = jsonArray.length() - 1; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String step = jsonObject.getString("stepNum");
                                        mStepArrayTextView.setText(step);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            case "星期日":{
                                String stepArray = iSportStepInterface.getTodaySportStepArrayByStartDateAndDays(getTodayDate(), 6);
                                try {
                                    JSONArray jsonArray = new JSONArray(stepArray);
                                    for (int i = jsonArray.length() - 1; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String step = jsonObject.getString("stepNum");
                                        mStepArrayTextView.setText(step);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }

                }
            }
            default:
                break;
        }
    }
    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}

