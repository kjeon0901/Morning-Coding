package com.example.bottomup2020.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bottomup2020.ScreenService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){//시스템이 부팅되면서 ios에서 얘를 broadcast함. 이걸 받아서 서비스 실행
            Intent i = new Intent(context, ScreenService.class);
            context.startService(i);
        }
    }
}
