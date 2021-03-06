package net.bwei.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.bwei.okhttpdemo.okhttp.CommonOkHttpClient;
import net.bwei.okhttpdemo.okhttp.listener.DisposeDataHandle;
import net.bwei.okhttpdemo.okhttp.listener.DisposeDataListener;
import net.bwei.okhttpdemo.okhttp.request.CommonRequest;
import net.bwei.okhttpdemo.okhttp.request.RequestParams;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button getButton;
    Button postButton;
    TextView infoTextView;
    final String url = "https://www.baidu.com";
    final String posturl = "";//收藏某一本书。

    final String getUrl = "https://api.douban.com/v2/book/17604305";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getButton = (Button) findViewById(R.id.get_button);
        postButton = (Button) findViewById(R.id.post_button);
        infoTextView = (TextView) findViewById(R.id.info_textview);
        getButton.setOnClickListener(this);
        postButton.setOnClickListener(this);
        OkHttpClient client = new OkHttpClient();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_button:
                getRequest();
                break;
            case R.id.post_button:
                postRequest();
                break;
        }


    }

    void get() {


        Request request = CommonRequest.createGetRequest
                (getUrl, new RequestParams("fields", "title"));
        DisposeDataHandle handle = new DisposeDataHandle
                (new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        // Book book = (Book) responseObj;
                        infoTextView.setText(responseObj.toString());
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        infoTextView.setText(reasonObj.toString());

                    }
                });


        CommonOkHttpClient.get(request, handle);

    }


    void postRequest() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new LoggingInterceptors().run();
                } catch (Exception e) {
                    e.printStackTrace();
                    LoggingInterceptors.logger.info("e" + e.toString());
                }
            }
        });

        return;
//        OkHttpClient client = new OkHttpClient();
//
//        FormBody.Builder formBuilder = new FormBody.Builder();
//        formBuilder.add("status", "reading");
//        FormBody body = formBuilder.build();
//
//
//        Request request = new Request.Builder().url(posturl).post(body).build();
//
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, final IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        infoTextView.setText(e.toString());
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String res = response.body().string();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        infoTextView.setText(res);
//                    }
//                });
//            }
//        });

    }

    void getRequest() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://www.tngou.net/api/top/classify").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String res = e.toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        infoTextView.setText(res);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        infoTextView.setText(res);
                    }
                });
            }
        });


    }

    public static class LoggingInterceptors {
        private static final Logger logger = Logger.getLogger(LoggingInterceptors.class.getName());
        private final OkHttpClient client = new OkHttpClient();

        public LoggingInterceptors() {
            client.newBuilder().addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    long t1 = System.nanoTime();
                    Request request = chain.request();
                    logger.info("aaaa" + t1);
                    Response response = chain.proceed(request);
                    long t2 = System.nanoTime();
                    logger.info("aaa" + t2);
                    return response;
                }
            }).build();

        }

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("https://publicobject.com/helloworld.txt")
                    .build();
            Response response = client.newCall(request).execute();
            response.body().close();
        }

    }


//
//    void get() {
//        CommonOkHttpClient.get(CommonRequest.createGetRequest(getUrl,
//                new RequestParams().params("fields", "title")),new DisposeDataHandle(new DisposeDataListener() {
//            @Override
//            public void onSuccess(Object responseObj) {
//                infoTextView.setText(responseObj.toString());
//            }
//
//            @Override
//            public void onFailure(Object reasonObj) {
//                infoTextView.setText(reasonObj.toString());
//
//            }
//        }));
//    }

}
