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
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class EventsFragment extends Fragment {

    private static final String TAG = "EVENTS";
    //@BindView(R.id.section_label) TextView textView;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private PageViewModel pageViewModel;
    private EventAdapter mEventAdapter;
    private LinearLayoutManager mLayoutManager;
    private DisposableObserver<List<Event>> mydisposableObserver;
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

    private void getTestEvents() {
        Log.d(TAG, "Getting test events");
        Observable<List<Event>> eventObservable = Observable.create(emitter -> {
            efPresenter.activateListener(emitter);
        });

        mycompositeDisposable.add(
                eventObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(events -> mEventAdapter.setData(events)));
    }

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
        getTestEvents();
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