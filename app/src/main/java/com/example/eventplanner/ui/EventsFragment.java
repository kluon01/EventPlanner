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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EventsFragment extends Fragment {

    private static final String TAG = "EVENTS";
    //@BindView(R.id.section_label) TextView textView;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private PageViewModel pageViewModel;
    private EventAdapter mEventAdapter;
    private LinearLayoutManager mLayoutManager;
    private CompositeDisposable mycompositeDisposable = new CompositeDisposable();


    private EventsFragmentPresenter efPresenter;

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

    // TODO: This apporach works, but firebase supports queries across subcollections, might get rid of event attendees and instead have events contain a attendees subcollection with the list of users
    private void findUserEvents() {
        Log.d(TAG, "Getting names of events user is in");
        Observable<List<String>> findUserEventsObservable = Observable.create(emitter -> efPresenter.getUsersEventNames(emitter));

        mycompositeDisposable.add(
                findUserEventsObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(eventnames -> checkLocalDatabase(eventnames)));
    }

    private void checkLocalDatabase(List<String> eventnames) {
        // Check if event names match those already in the RoomDB
        Observable<List<String>> checkNewEventsObservable = Observable.create(emitter -> efPresenter.checkNewEvents(emitter, eventnames));

        mycompositeDisposable.add(
                checkNewEventsObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> updateEventList(result)));
    }

    // TODO: Right now all events are quired from firebase, add local RoomDB and check for changes only to reduce network calls
    private void updateEventList(List<String> eventnames){
        for(String name : eventnames) {
            if (!eventnames.isEmpty()) {
                Observable<Event> updateEventList = Observable.create(emitter -> efPresenter.getNewEvents(emitter, name));

                mycompositeDisposable.add(
                        updateEventList.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(event -> {
                                    mEventAdapter.addData(event);
                                }));
            }
        }
    }

    // Lifecycle Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.setIndex(TAG);
        efPresenter = new EventsFragmentPresenter();
        //efPresenter.addEventattendee(); // Added example data to event attendee collection
        //getTestEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        efPresenter = new EventsFragmentPresenter();
        findUserEvents();
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