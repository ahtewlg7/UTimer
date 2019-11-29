package ahtewlg7.utimer.factory;

import android.database.Cursor;
import android.provider.MediaStore;

import ahtewlg7.utimer.entity.material.MusicInfo;

public class MediaCursorFactory {
    public MusicInfo getMusicInfo(Cursor cursor){
        if(cursor == null)
            return null;
        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
        int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));

        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setSize(size);
        musicInfo.setDuration(duration);
        musicInfo.setTitle(title);
        musicInfo.setSinger(singer);
        musicInfo.setAlbum(album);
        musicInfo.setUrl(url);
        musicInfo.setDisplayName(displayName);
        musicInfo.setMimeType(mimeType);
        return musicInfo;
    }
}
