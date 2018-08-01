package com.codvision.background.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.codvision.background.room.entity.Record;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/26 - 2:14 PM
 */
@Dao
public interface RecordDao {
    @Query("SELECT * FROM tbl_job_record")
    Flowable<List<Record>> getAllRecords();

    @Query("SELECT COUNT(*) FROM tbl_job_record")
    int getRecordsCount();

    @Query("SELECT * FROM tbl_job_record WHERE startTime > :start AND endTime < :end")
    Flowable<List<Record>> getRecordsBetween(String start, String end);

    @Insert
    List<Long> insertRecord(List<Record> records);

    @Insert
    long insertRecords(Record record);
}
