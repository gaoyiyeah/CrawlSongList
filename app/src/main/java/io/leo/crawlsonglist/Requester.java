package io.leo.crawlsonglist;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * All the requester's parent class.
 *
 * It provide base http get and post function.
 */
public abstract class Requester {

    private final Callback callback;

    interface Callback{
        void done(Exception err,SongList songList);
    }


    private static final int WHAT_SUCCESSED = 666,
                                WHAT_FAILD = 444;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT_SUCCESSED){
                callback.done(null, (SongList) msg.obj);
            }else{
                callback.done((Exception) msg.obj,null);
            }

        }
    };


    public Requester(Callback callback){
        this.callback = callback;
    }
    /**
     * Get all the song in the song list.
     * @param id song list id
     */
    public abstract void getSongList(String id);

    /**
     * Convert the response from server to the List<Song>
     * @param resp
     * @return
     */
    public abstract SongList convertToSongs(String resp);


    void get(String url,Map<String,String> header){
        request(url,"GET",header,null);
    }

    void post(String url,Map<String,String> header,Map<String,String> data){
        request(url,"POST",header,data);
    }

    void request(String url, String method, Map<String,String> header, Map<String,String> data){
        // To make sure header and data are not null.
        if (header==null)header=new HashMap<>();
        if (data==null)data=new HashMap<>();
        // Add User-Agent
        if (!header.containsKey("User-Agent"))
            header.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");


        OkHttpClient client = new OkHttpClient();
        final Request.Builder reqBuilder = new Request.Builder();
        for (String key:header.keySet())
            reqBuilder.addHeader(key,header.get(key));

        FormBody.Builder bodyBuilder = null;
        if (method!="GET"){
            bodyBuilder = new FormBody.Builder();
            for (String key : data.keySet())
                bodyBuilder.add(key,data.get(key));
        }


        reqBuilder.method(method,bodyBuilder!=null?bodyBuilder.build():null);
        reqBuilder.url(url);

        client.newCall(reqBuilder.build()).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendMessage(false,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    SongList songList = convertToSongs(response.body().string());
                    if (songList!=null)
                        sendMessage(true,songList);
                    else
                        sendMessage(false,new RuntimeException("Can not get the information from the response."));
                }else{
                    sendMessage(false,new RuntimeException("Request faild , code => "+response.code()));
                }
            }
        });
    }

    public void sendMessage(boolean isSuccessed,Object obj){
        Message message = handler.obtainMessage();
        message.what = isSuccessed?WHAT_SUCCESSED:WHAT_FAILD;
        message.obj = obj;
        handler.sendMessage(message);
    }


    public static Map<String,String> create(String...kv){
        if (kv.length%2!=0)throw new RuntimeException("Wrong length");
        Map<String,String> map = new HashMap<>();
        for (int i=0;i<kv.length;i+=2){
            map.put(kv[i],kv[i+1]);
        }
        return map;
    }
}
