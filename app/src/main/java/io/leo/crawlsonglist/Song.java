package io.leo.crawlsonglist;

import java.util.Arrays;

/**
 * Song object.
 *
 */
public class Song {

    private String id;
    private String name;
    private String coverUrl;
    //A song may product by multiple singers.
    private String[] artists;

    public Song() {
        super();
    }

    public Song(String id, String name, String coverUrl, String[] artists, String songUrl) {
        this.id = id;
        this.name = name;
        this.coverUrl = coverUrl;
        this.artists = artists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String[] getArtists() {
        return artists;
    }

    public void setArtists(String[] artists) {
        this.artists = artists;
    }


    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", artists=" + Arrays.toString(artists) +
                '}';
    }
}
