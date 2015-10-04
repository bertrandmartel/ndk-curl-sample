package fr.bmartel.android.curlndk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CurlActivity extends Activity{

    private String TAG = CurlActivity.this.getClass().getName();

    private ThreadPoolExecutor executor = null;

    // the java declaration for your wrapper test function
    public native String  getCurlResponse();

    // tell java which library to load
    static {
        System.loadLibrary("curl-ndk");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executor=new ThreadPoolExecutor(1,1,1, TimeUnit.MINUTES,new ArrayBlockingQueue<Runnable>(1,true),new ThreadPoolExecutor.CallerRunsPolicy());

        Log.i(TAG,"Create CurlActivity");

        Button button = (Button) findViewById(R.id.curl_request_btn);

        final TextView text =(TextView) findViewById(R.id.curl_response_txt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //run that in separate thread to avoid ui blocking
                executor.execute(new Runnable() {

                    @Override
                    public void run() {

                        final String response = getCurlResponse();
                        Log.i(TAG, "http request from ndk");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(response);
                            }
                        });
                    };
                });
            }
        });
    }

}
