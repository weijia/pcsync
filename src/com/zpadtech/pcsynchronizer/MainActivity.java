package com.zpadtech.pcsynchronizer;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.http.conn.util.InetAddressUtils;

import it.sauronsoftware.ftp4j.FTPClient;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {


	private Future working_future;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //String localIp = getIPAddress(true);
        //Log.v("PC Synchronizer, IP is:", localIp);
        
		//Thread thread = new Thread(new FtpSync());
		//thread.start();
        try{
        	mThreadPool = Executors.newFixedThreadPool(1);
        	working_future = mThreadPool.submit(new FtpSync());
        }
        catch(Exception e){
        	e.printStackTrace();
        }
	}

    
	private ExecutorService mThreadPool;
	
	@Override
	protected void onDestroy() {
		Log.v("PC Sync", "shutting down");
		working_future.cancel(true);
		mThreadPool.shutdownNow();
		try {
			mThreadPool.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("PCSync", "term timeout");
		}
		Log.v("PCSync", "term OK");
		super.onDestroy();
	}
    
}

