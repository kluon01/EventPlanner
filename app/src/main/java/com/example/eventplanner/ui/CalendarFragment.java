package com.example.eventplanner.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.eventplanner.R;

import java.util.Calendar;

public class CalendarFragment extends Fragment {

    private static final String TAG = "Calendar";
    private PageViewModel pageViewModel;
    private Calendar calendar;
    private CalendarView calendarView;
    private FragmentManager fragmentManager;
    //@BindView(R.id.section_label) TextView textView;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment CalendarFragment.
     */
    public static CalendarFragment newInstance() {
        return new CalendarFragment();
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
        View root = inflater.inflate(R.layout.fragment_calendarview, container, false);
        //ButterKnife.bind(this, root);
        fragmentManager = getChildFragmentManager();
        calendarView = root.findViewById(R.id.calendar);
        pageViewModel.getText().observe(this, s -> calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String msg = "Selected date Day: " + dayOfMonth + " Month : " + (month + 1) + " Year " + year;
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }));
        return root;
    }
}