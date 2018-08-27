package me.xujichang.step.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import io.reactivex.Flowable;
import me.xujichang.step.room.entities.DayCount;
import me.xujichang.step.room.entities.TempCount;

/**
 * Des:
 * 对计步的操作 都在此
 *
 * @author xujichang
 * <p>
 * created by 2018/8/20-下午3:54
 */
@Dao()
public interface StepDao {
    //==========================单次计步==========================

    /**
     * 开始单次计步
     * 向数据库添加一条记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void startTempStep(TempCount count);

    /**
     * 停止单次计步
     * 获取此次单次计步的记步数
     */
    @Query("SELECT * FROM tbl_temp_step WHERE flag=:flag")
    Flowable<TempCount> stopTempStep(String flag);

    @Update
    void updateTempStep(TempCount count);

    /**
     * 获取当天系统计步的记步数
     *
     * @param dateMs
     * @return
     */
    @Query("SELECT currentSc FROM tbl_day_step WHERE dayMs =:dateMs")
    int getCurrentSysTempCount(long dateMs);

    @Query("SELECT currentSc FROM tbl_temp_step WHERE flag=:flag")
    int getTempStepCount(long flag);

    //============================按天计步=========================

    /**
     * 获取数据库中的总记步数
     *
     * @param dateMs
     * @return
     */
    @Query("SELECT currentDaySc FROM tbl_day_step WHERE dayMs=:dateMs")
    int getStepCount(long dateMs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addDayCount(DayCount dayCount);

    @Query("SELECT * FROM tbl_day_step WHERE dayMs=:dateMs")
    DayCount getDayCount(long dateMs);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDayCount(DayCount dayCount);
}
