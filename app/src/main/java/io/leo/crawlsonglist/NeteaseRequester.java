package io.leo.crawlsonglist;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Requester of netease music.
 *
 * Problem :
 *  - Can't get the artist of song
 *  - Can't get the song cover
 */
public class NeteaseRequester extends Requester{

    public NeteaseRequester(Callback callback) {
        super(callback);
    }

    @Override
    public void getSongList(String id) {
        Log.i(this.getClass().getSimpleName(),"id=>"+id);
        //
        get("https://music.163.com/playlist/"+id+"/",create("User-Agent","Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BLA-AL00 Build/HUAWEIBLA-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/8.9 Mobile Safari/537.36"));
    }

    @Override
    public SongList convertToSongs(String resp) {
        SongList songList = new SongList();
        List<Song> songs = new ArrayList<>();
        String jsonStr = MainActivity.getString(resp,"window\\.REDUX_STATE = (\\{.*?\\});[ \\f\\n\\r\\t\\v]+<\\/script>");
        try {
            JSONObject playList = new JSONObject(jsonStr).getJSONObject("Playlist");
            JSONArray datas = playList.getJSONArray("data");
            for (int i=0;i<datas.length();i++){
                JSONObject songJO = datas.getJSONObject(i);
                Song song = new Song();
                song.setId(String.valueOf(songJO.getInt("id")));
                song.setName(songJO.getString("songName"));
                song.setArtists(new String[]{songJO.getString("singerName")});
                songs.add(song);
            }
            JSONObject info = playList.getJSONObject("info");
            songList.setName(info.getString("name"));
            songList.setSongs(songs);
            songList.setCreateUser(info.getJSONObject("creator").getString("nickname"));
            songList.setCoverUrl(info.getString("coverImgUrl"));
            return songList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
