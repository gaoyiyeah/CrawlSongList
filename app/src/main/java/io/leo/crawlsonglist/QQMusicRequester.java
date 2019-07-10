package io.leo.crawlsonglist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Requester of qq music.
 *
 * Problem :
 *  - Can't get the song cover
 */
public class QQMusicRequester extends Requester{

    public QQMusicRequester(Callback callback) {
        super(callback);
    }

    @Override
    public void getSongList(String id) {
        String reqUrl = "https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&onlysong=0&new_format=1&disstid="+id+"&g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0";
        get(reqUrl,
                create("Origin","https://y.qq.com",
                        "Referer","https://y.qq.com/n/yqq/playsquare/"+id+".html"));

    }


    @Override
    public SongList convertToSongs(String resp) {
        try {
            SongList list = new SongList();
            List<Song> songs = new ArrayList<>();
            JSONObject cdlist = new JSONObject(resp).getJSONArray("cdlist").getJSONObject(0);

            list.setName(cdlist.getString("dissname"));
            list.setCreateUser(cdlist.getString("nick"));
            list.setCoverUrl(cdlist.getString("logo"));

            JSONArray songsArray = cdlist.getJSONArray("songlist");
            for (int i=0;i<songsArray.length();i++){
                JSONObject songJO = songsArray.getJSONObject(i);
                Song song = new Song();
                song.setId(songJO.getString("id"));
                song.setName(songJO.getString("name"));
//                song.setCoverUrl();
                JSONArray singersArray = songJO.getJSONArray("singer");
                String singers[] = new String[singersArray.length()];
                for (int j=0;j<singersArray.length();j++){
                    singers[j] = singersArray.getJSONObject(j).getString("name");
                }
                song.setArtists(singers);
                songs.add(song);
            }
            list.setSongs(songs);
            return list;
        } catch (JSONException e) {
            return null;
        }
    }
}
