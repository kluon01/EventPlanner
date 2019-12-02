package com.example.eventplanner.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
        efPresenter = new EventsFragmentPresenter();
        efPresenter.addTestDataEvent();
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

        //prepareDummyEvents();
        getTestEvents();
    }

    private void prepareDummyEvents() {
        ArrayList<Event> mEvents = new ArrayList<>();
        String[] eventsList = getResources().getStringArray(R.array.event_titles);
        String[] eventsSubtitles = getResources().getStringArray(R.array.event_subtitles);
        String[] eventsInfo = getResources().getStringArray(R.array.event_info);

        for (int i = 0; i < eventsList.length; i++) {
            mEvents.add(new Event(eventsList[i], eventsSubtitles[i], eventsInfo[i]));
        }
        mEventAdapter.addItems(mEvents);
        mRecyclerView.setAdapter(mEventAdapter);
    }

    private void getTestEvents() {
        Log.d(TAG, "Getting test events");
        Observable<List<Event>> eventObservable = Observable.create(emitter -> {
            try {
                efPresenter.getEvents(emitter);
            } catch (Exception e) {
                emitter.onError(e);
                Log.d(TAG, "Rxjava Error");
            }
        });

        mycompositeDisposable.add(
                eventObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getObserver()));

    }

    public DisposableObserver<List<Event>> getObserver() {
        return mydisposableObserver = new DisposableObserver<List<Event>>() {
            @Override
            public void onNext(List<Event> mEvents) {
                mEventAdapter.addItems(mEvents);
                mRecyclerView.setAdapter(mEventAdapter);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.setIndex(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.card_fragment_layout, container, false);
        ButterKnife.bind(this, root);
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                setUp();
            }
        });
        return root;
    }
}