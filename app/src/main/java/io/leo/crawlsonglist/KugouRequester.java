package io.leo.crawlsonglist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KugouRequester extends Requester{

    public KugouRequester(Callback callback) {
        super(callback);
    }

    @Override
    public void getSongList(String id) {
        get(id,null);
    }

    @Override
    public SongList convertToSongs(String resp) {
        List<Song> songs = new ArrayList<>();
        String jsonStr = MainActivity.getString(resp,"var data=(.*?]),[ \\f\\n\\r\\t\\v]+sp");

        if (jsonStr!=null){
            try {
                JSONArray arr = new JSONArray(jsonStr);
                for (int i=0;i<arr.length();i++){
                    JSONObject songJO = arr.getJSONObject(i);
                    Song song = new Song();
                    song.setName(songJO.getString("audio_name"));
                    song.setId(songJO.getString("audio_id"));
                    JSONArray authorsJO = songJO.getJSONArray("authors");
                    String authors[] = new String[authorsJO.length()];
                    for (int j = 0 ;j<authorsJO.length();j++){
                        authors[j] = authorsJO.getJSONObject(j).getString("author_name");
                    }
                    song.setArtists(authors);
                    songs.add(song);
                }
                SongList songList = new SongList();
                songList.setName(MainActivity.getString(resp,"specialName :\"(.*?)\""));
                songList.setCreateUser(MainActivity.getString(resp,"specialCreat :\"(.*?)\""));
                songList.setCoverUrl(MainActivity.getString(resp,"specialImg :\"(.*?)\""));
                songList.setSongs(songs);
                return songList;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}