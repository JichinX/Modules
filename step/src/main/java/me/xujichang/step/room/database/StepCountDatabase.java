package me.xujichang.step.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import me.xujichang.step.room.entities.DayCount;
import me.xujichang.step.room.entities.TempCount;
import me.xujichang.step.room.dao.StepDao;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/20-下午3:42
 */
@Database(entities = {TempCount.class, DayCount.class}, version = 3, exportSchema = false)
public abstract class StepCountDatabase extends RoomDatabase {
    private static volatile StepCountDatabase INSTANCE;

    public abstract StepDao stepDao();


    public static StepCountDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (StepCountDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StepCountDatabase.class, "count_step.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
