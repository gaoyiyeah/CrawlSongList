package io.leo.crawlsonglist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextView headerView;
    private Requester.Callback callback = new Requester.Callback() {
        @Override
        public void done(Exception err, SongList songList) {
            if (err!=null) Toast.makeText(MainActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
            else {
                List<Song> songs = songList.getSongs();
                String songsArr[] = new String[songs.size()];
                for (int i=0;i<songs.size();i++){
                    songsArr[i] = songs.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,songsArr);
                headerView.setText(headerView.getText().toString()+" "+songList.getName()+" , create by "+songList.getCreateUser());
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(MainActivity.this, songs.get(i).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private ListView mListView;
    private EditText mEdit;
    private Button mImport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.am_listview);
        mEdit = findViewById(R.id.am_edit);
        headerView = new TextView(this);
        mListView.addHeaderView(headerView);
    }


    public void importIt(View v){
        String url = mEdit.getText().toString();
        if (!url.equals("")){
            Requester requester = null;
            String songId = null;
            if (url.indexOf("y.qq.com")!=-1) {requester = new QQMusicRequester(callback);songId=getString(url,"playsquare/(\\d+)\\.html");headerView.setText("来自QQ音乐");}
            else if(url.indexOf("www.kugou.com")!=-1){requester = new KugouRequester(callback);songId=url;headerView.setText("来自酷狗音乐");}
            if (songId!=null)
                requester.getSongList(songId);
            else
                Toast.makeText(this, "Wrong URL", Toast.LENGTH_SHORT).show();
        }

    }

    public static String getString(String source,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()){
            return matcher.group(1);
        }
        return null;
    }
}
