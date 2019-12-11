package com.example.eventplanner.presenter.localDB;

import android.content.Context;

import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.localDB.AppDatabase;

import java.util.List;
import java.util.Observable;

import io.reactivex.Flowable;
import io.reactivex.ObservableEmitter;

public class LocalDatabaseHandler {
    private final AppDatabase database;

    public LocalDatabaseHandler(Context context) {
        database = AppDatabase.getAppDatabase(context);
    }

    public Flowable<List<Event>> getEvents(){
        return database.eventDao().getUserEvents();
    }

    public void insertData(Event event, ObservableEmitter<Long> emitter){
        emitter.onNext(database.eventDao().insertEvent(event));
    }

    public void clearData(ObservableEmitter<Boolean> emitter){
        database.eventDao().clearTable();
        emitter.onNext(true);
    }
}
