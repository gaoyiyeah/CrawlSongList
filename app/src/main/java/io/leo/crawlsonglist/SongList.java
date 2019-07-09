package io.leo.crawlsonglist;

import java.util.List;

public class SongList {
    private String name;
    private String coverUrl;
    private List<Song> songs;
    private String createUser;

    public SongList() {
        super();
    }

    public SongList(String name, String coverUrl, List<Song> songs, String createUser) {
        this.name = name;
        this.coverUrl = coverUrl;
        this.songs = songs;
        this.createUser = createUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Override
    public String toString() {
        return "SongList{" +
                "name='" + name + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", songs=" + songs +
                ", createUser='" + createUser + '\'' +
                '}';
    }
}
