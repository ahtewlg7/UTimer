package ahtewlg7.utimer.db.media;

import android.database.Cursor;
import android.provider.MediaStore;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.material.ImageMediaInfo;
import ahtewlg7.utimer.entity.material.MusicInfo;

public class MediaCursorParser {
    public MusicInfo getImageInfo(Cursor cursor){
        if(cursor == null)
            return null;
        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
//        int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//        String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        String url = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
        DateTime modifyTime = new DateTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)));
        DateTime addTime = new DateTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)));
        DateTime dateTaken = new DateTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)));

        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setSize(size);
//        musicInfo.setDuration(duration);
        musicInfo.setTitle(title);
//        musicInfo.setSinger(singer);
//        musicInfo.setAlbum(album);
        musicInfo.setUrl(url);
        musicInfo.setDisplayName(displayName);
        musicInfo.setMimeType(mimeType);
        return musicInfo;
    }

    public ImageMediaInfo getImageMediaInfo(Cursor cursor){
//        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE)).trim();
        String url = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)).trim();
        String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
        int mimeType = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
//        DateTime modifyTime = new DateTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)));
//        DateTime addTime = new DateTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)));
        DateTime dateTaken = new DateTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)));

        ImageMediaInfo imageMediaInfo = new ImageMediaInfo();
//        musicInfo.setSize(size);
        imageMediaInfo.setTitle(title);
        imageMediaInfo.setUrl(url);
        imageMediaInfo.setDisplayName(displayName);
        imageMediaInfo.setMimeType(mimeType);
        imageMediaInfo.setCreateDate(dateTaken);
        return imageMediaInfo;
    }
}
