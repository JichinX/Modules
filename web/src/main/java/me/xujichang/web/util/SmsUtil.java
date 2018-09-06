package me.xujichang.web.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.xujichang.web.SystemOperate;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/31-下午5:33
 */
public class SmsUtil {
    private static SmsUtil instance;
    public static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
    public static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
    private SMSReceiver mDeliveredSMSReceiver;
    private SMSReceiver mSendSMSReceiver;

    private static class Holder {
        private static SmsUtil instance = new SmsUtil();
    }

    private SmsUtil() {
    }

    public static SmsUtil getInstance() {
        return new SmsUtil();
    }

    public static ArrayList<String> getMessages(String url) {
        int startIndex = url.indexOf("body=");
        if (startIndex == -1) {
            return null;
        }
        String message = url.substring(startIndex);
        SmsManager smsManager = SmsManager.getDefault();
        return
                smsManager.divideMessage(message);
    }

    public static List<String> getNumbers(String url) {
        if (null == url || !url.startsWith(SystemOperate.SMS.getScheme())) {
            return null;
        }
        int startIndex = url.indexOf(":");
        int endIndex = url.indexOf("?");
        if (startIndex == -1) {
            return null;
        }
//        if(endIndex==-1){
//
//        }
        String Numstr = url.substring(startIndex, endIndex);
        String[] numbers = Numstr.split(",");

        return Arrays.asList(numbers);
    }

    public void sendMessages(Context context, String url) {
        List<String> numbers = getNumbers(url);
        ArrayList<String> messages = getMessages(url);
        if (null == messages) {
            return;
        }
        SmsManager smsManager = SmsManager.getDefault();
        /* 建立自定义Action常数的Intent(给PendingIntent参数之用) */
        Intent itSend = new Intent(SMS_SEND_ACTIOIN);
        /* sentIntent参数为传送后接受的广播信息PendingIntent */
        Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);
        /* deliveryIntent参数为送达后接受的广播信息PendingIntent */
        PendingIntent mSendPI = PendingIntent.getBroadcast(context, 0, itSend, 0);
        PendingIntent mDeliverPI = PendingIntent.getBroadcast(
                context, 0, itDeliver, 0);
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            sentPendingIntents.add(i, mSendPI);
            deliveredPendingIntents.add(i, mDeliverPI);
        } /* 发送SMS短信，注意倒数的两个PendingIntent参数 */
        for (String number : numbers) {
            smsManager.sendMultipartTextMessage(number, null, messages, sentPendingIntents, deliveredPendingIntents);
        }
    }

    /**
     * 注册
     */
    public void registerReceiver(Context context) {
        /* 自定义IntentFilter为SENT_SMS_ACTIOIN Receiver */
        IntentFilter mFilter01;
        mFilter01 = new IntentFilter(SMS_SEND_ACTIOIN);
        mSendSMSReceiver = new SMSReceiver();
        /* 自定义IntentFilter为DELIVERED_SMS_ACTION Receiver */
        context.registerReceiver(mSendSMSReceiver, mFilter01);
        mFilter01 = new IntentFilter(SMS_DELIVERED_ACTION);
        mDeliveredSMSReceiver = new SMSReceiver();
        context.registerReceiver(mDeliveredSMSReceiver, mFilter01);
    }

    /**
     * 取消注册自定义Receiver
     */
    public void unregisterReceiver(Context context) {
        if (mSendSMSReceiver != null) {
            context.unregisterReceiver(mSendSMSReceiver);
        }
        if (mDeliveredSMSReceiver != null) {
            context.unregisterReceiver(mDeliveredSMSReceiver);
        }
    }


    private class SMSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SMS_SEND_ACTIOIN.equals(intent.getAction())) {
                try { /* android.content.BroadcastReceiver.getResultCode()方法 */ //Retrieve the current result code, as set by the previous receiver.
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(context, "短信发送失败", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (SMS_DELIVERED_ACTION.equals(intent.getAction())) {
                /* android.content.BroadcastReceiver.getResultCode()方法 */
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "短信已送达", Toast.LENGTH_SHORT).show();
                        break;
                    /* 短信未送达 */
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "短信未送达", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                    default:
                        break;
                }
            }

        }
    }
}
