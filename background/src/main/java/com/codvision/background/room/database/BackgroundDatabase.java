package com.codvision.background.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.codvision.background.room.dao.RecordDao;
import com.codvision.background.room.entity.Record;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/26 - 2:21 PM
 */
@Database(entities = {Record.class}, version = 1, exportSchema = false)
public abstract class BackgroundDatabase extends RoomDatabase {
    private static volatile BackgroundDatabase INSTANCE;

    public abstract RecordDao recordDao();


    public static BackgroundDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BackgroundDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BackgroundDatabase.class, "background_job.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
