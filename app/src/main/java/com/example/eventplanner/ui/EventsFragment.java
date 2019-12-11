package com.example.eventplanner.ui;

import android.os.Bundle;
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

    // TODO: App launches and fetches local database first then will run firestore query to see if list is out of date
   /* private void checkLocalDatabase(List<String> eventnames) {
        // Check if event names match those already in the RoomDB
        Observable<List<String>> checkNewEventsObservable = Observable.create(emitter -> efPresenter.checkNewEvents(emitter, eventnames));

        mycompositeDisposable.add(
                checkNewEventsObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> updateEventList(result)));
    }*/

    private void findUserEvents() {
        Observable<List<Event>> findUserEventsSubGroupObservable = Observable.create(emitter -> efPresenter.getUserEventsRealtime(emitter));

        mycompositeDisposable.add(
                findUserEventsSubGroupObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(events -> mEventAdapter.setData(events)));
    }

    // Lifecycle Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.setIndex(TAG);
        efPresenter = new EventsFragmentPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        efPresenter = new EventsFragmentPresenter();
        //findUserEvents();
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