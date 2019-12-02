package com.example.eventplanner.ui;

import android.os.Bundle;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsFragment extends Fragment {

    private static final String TAG = "Events";

    private PageViewModel pageViewModel;
    //@BindView(R.id.section_label) TextView textView;
    @BindView(R.id.mRecyclerView) RecyclerView mRecyclerView;
    EventAdapter mEventAdapter;
    LinearLayoutManager mLayoutManager;

    public EventsFragment() {
        // Required empty public constructor
    }

    private void setUp() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mEventAdapter = new EventAdapter(new ArrayList<>());

        prepareDummyEvents();
    }

    private void prepareDummyEvents() {
        ArrayList<Event> mEvents = new ArrayList<>();
        String[] eventsList = getResources().getStringArray(R.array.event_titles);
        String[] eventsSubtitles = getResources().getStringArray(R.array.event_subtitles);
        String[] eventsInfo = getResources().getStringArray(R.array.event_info);

        for (int i =0; i < eventsList.length; i++) {
            mEvents.add(new Event(eventsList[i], eventsSubtitles[i], eventsInfo[i]));
        }
        mEventAdapter.addItems(mEvents);
        mRecyclerView.setAdapter(mEventAdapter);
    }

    private void getTestEvents(){

    }

    /**
     * @return A new instance of fragment EventsFragment.
     */
    public static EventsFragment newInstance() {
        return new EventsFragment();
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