package com.example.bottomup2020;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.bottomup2020.Receiver.PackageReceiver;
import com.example.bottomup2020.Receiver.RestartReceiver;
import com.example.bottomup2020.Receiver.ScreenReceiver;

public class ScreenService extends Service {
    private ScreenReceiver mReceiver = null;
    private PackageReceiver pReceiver;
    boolean isPhoneIdle = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        registerRestartAlarm(true);
        mReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);

        //어플리케이션 프레임워크에서 TelephonyManager 객체를 얻어옴
        TelephonyManager telephonyManager=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //모니터링 할 이벤트를 리스너에 등록
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        //앱 설치, 삭제, 업데이트 시 서비스 실행
        pReceiver = new PackageReceiver();
        IntentFilter pFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        pFilter.addDataScheme("package");
        registerReceiver(pReceiver, pFilter);
    }

    //원래 @Override 하고 매개변수에 Context context는 없었는데, 아래 Builder(context)에서 오류나서 이렇게 해봄
    public int onStartCommand(Intent intent, int flags, int startId, Context context){
        super.onStartCommand(intent, flags, startId);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(null)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Screen Service")
                .setContentText("Run on Foreground")
                .setTicker("Service is running");
        Notification notification = builder.build();
        startForeground(1, notification);//TaskKiller에 거의 죽지 않고 계속 백그라운드에 살아 있음

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
    //@Override
    public void onDestroy(){
        super.onDestroy();
        registerRestartAlarm(false);
        if(mReceiver != null){
            mReceiver.reenableKeyguard();
            unregisterReceiver(mReceiver);
        }
        if(pReceiver != null){
            unregisterReceiver(pReceiver);
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

    public void registerRestartAlarm(boolean isOn){
        Intent intent = new Intent(ScreenService.this, RestartReceiver.class);
        intent.setAction(RestartReceiver.ACTION_RESTART_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        if(isOn){
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+1000, 10000, sender);
        }else{
            am.cancel(sender);
        }
    }

}
