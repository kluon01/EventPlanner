package com.example.eventplanner.model.localDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.eventplanner.model.Event;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface EventDao {

    @Query("SELECT * FROM event")
    Flowable<List<Event>> getUserEvents();

    @Query("SELECT * FROM event where documentId LIKE :documentID")
    Event findByDocumentId(String documentID);

    @Query("SELECT COUNT(*) from event")
    int countEvents();

    @Query("DELETE FROM event")
    void clearTable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEvent(Event event);

    @Update
    int updateList(Event event);

    @Delete
    void delete(Event event);
}