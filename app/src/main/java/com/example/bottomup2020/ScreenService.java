package com.example.bottomup2020;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;

public class ScreenService extends Service {
    private ScreenReceiver mReceiver = null;
    boolean isPhoneIdle = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
        
        //어플리케이션 프레임워크에서 TelephonyManager 객체를 얻어옴
        TelephonyManager telephonyManager=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //모니터링 할 이벤트를 리스너에 등록
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        if(intent != null){
            if(intent.getAction()==null){
                if(mReceiver==null){
                    mReceiver = new ScreenReceiver();
                    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
                    registerReceiver(mReceiver, filter);
                }
            }
        }
        return START_REDELIVER_INTENT;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mReceiver != null){
            mReceiver.reenableKeyguard();
            unregisterReceiver(mReceiver);
        }
    }

    PhoneStateListener phoneListener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber){//통화상태(벨, 끊기) --- 전화 수신 반응
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE://폰이 울리거나 통화중이 아님
                    isPhoneIdle=true;
                    break;
                case TelephonyManager.CALL_STATE_RINGING://폰이 울린다
                    isPhoneIdle=false;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://현재 통화중
                    isPhoneIdle=false;
                    break;
            }
        }
    };

}
