package com.example.eventplanner.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.eventplanner.R;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    @BindView(R.id.title_input)
    EditText titleInput;
    @BindView(R.id.subtitle_input)
    EditText subtitleInput;
    @BindView(R.id.time_input)
    EditText timeInput;
    @BindView(R.id.date_input)
    EditText dateInput;
    @BindView(R.id.street_input)
    EditText streetInput;
    @BindView(R.id.city_input)
    EditText cityInput;
    @BindView(R.id.state_input)
    EditText stateInput;
    @BindView(R.id.zipcode_input)
    EditText zipcodeInput;
    @BindView(R.id.info_input)
    EditText infoInput;
    @BindView(R.id.add_event_button)
    Button addEventButton;
    @BindView(R.id.public_check)
    MaterialCheckBox publicCheckbox;

    private TimePickerDialog timePickerDialog;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);

        myCalendar = Calendar.getInstance();

        // Open time picker
        timeInput.setOnClickListener(view -> {
            timePickerDialog = new TimePickerDialog(AddEventActivity.this, (timePicker, hourOfDay, minutes) -> {
                boolean amPm;
                if (hourOfDay >= 12) {
                    amPm = true;
                } else {
                    amPm = false;
                }
                timeInput.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minutes, amPm ? "PM" : "AM"));
            }, 0, 0, false);
            timePickerDialog.show();
        });

        // Open date picker when user taps on date field
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                dateInput.setText(sdf.format(myCalendar.getTime()));
            }

        };

        // Set date text
        dateInput.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(AddEventActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

}
