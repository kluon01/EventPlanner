package com.example.eventplanner.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.presenter.EventAdapter;
import com.example.eventplanner.presenter.firebase.EventsFragmentPresenter;
import com.example.eventplanner.presenter.localDB.LocalDatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EventsFragment extends Fragment {

    private static final String TAG = "USEREVENTS";
    //@BindView(R.id.section_label) TextView textView;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private PageViewModel pageViewModel;
    private EventAdapter mEventAdapter;
    private LinearLayoutManager mLayoutManager;
    private CompositeDisposable mycompositeDisposable = new CompositeDisposable();

    private EventsFragmentPresenter efPresenter;
    private LocalDatabaseHandler localDatabaseHandler;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment EventsFragment.
     */
    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    private void setUp() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mEventAdapter = new EventAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mEventAdapter);
    }

    private void getEventListToDisplay() {
        mycompositeDisposable.add(
                localDatabaseHandler.getEvents()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(events -> {
                            Log.d(TAG, "Got events locally");

                            // Sort the events by closest time
                            Collections.sort(events, (o1, o2) -> Long.compare(o1.getDateAndTime(), o2.getDateAndTime()));

                            // Update the ui
                            mEventAdapter.setData(events);
                        }));
    }

    private void getUserEventDocumentIds() {
        Observable<List<String>> userEventDocumentIdObservable = Observable.create(emitter -> efPresenter.getUserEventDocumentIDs(emitter));

        mycompositeDisposable.add(
                userEventDocumentIdObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setUpEventDataUpdates));
    }

    private void setUpEventDataUpdates(List<String> eventDocumentIds) {
        Observable<Event> eventRTUpdateObservable = Observable.create(emitter -> efPresenter.setUpRTEventUpdate(eventDocumentIds, emitter));

        mycompositeDisposable.add(
                eventRTUpdateObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateLocalDatabase));
    }

    private void updateLocalDatabase(Event event) {
        Observable<Long> insertDataObservable = Observable.create(emitter -> localDatabaseHandler.insertData(event, emitter));

        mycompositeDisposable.add(
                insertDataObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            Log.d(TAG, "Inserted " + result + " event(s)");
                            // TODO: Get list of event from local database again
                        }));
    }

    private void insertTestLocalData() {
        Event event = new Event("local database event",
                "did it work?",
                "info",
                14.567f,
                -5.2f,
                1233454345,
                "documentId" + System.currentTimeMillis());

        Observable<Long> addTestDataObservable = Observable.create(emitter -> localDatabaseHandler.insertData(event, emitter));

        mycompositeDisposable.add(
                addTestDataObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(result -> Log.d(TAG, "Inserted " + result + " event(s)"))
        );
    }

    private void clearLocalDatabase() {
        Observable<Boolean> clearTableObservable = Observable.create(emitter -> localDatabaseHandler.clearData(emitter));

        mycompositeDisposable.add(
                clearTableObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(result -> Log.d(TAG, "Cleared Table"))
        );
    }

    // Lifecycle Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.setIndex(TAG);
        efPresenter = new EventsFragmentPresenter();
        localDatabaseHandler = new LocalDatabaseHandler(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        efPresenter = new EventsFragmentPresenter();
        localDatabaseHandler = new LocalDatabaseHandler(getContext());

        // Sets up Firebase queries
        getUserEventDocumentIds();

        // Get event already stored on local database
        getEventListToDisplay();

        // Test functions
        //insertTestLocalData();
        //clearLocalDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.card_fragment_layout, container, false);
        ButterKnife.bind(this, root);
        pageViewModel.getText().observe(this, s -> setUp());
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mycompositeDisposable.clear();
    }
}