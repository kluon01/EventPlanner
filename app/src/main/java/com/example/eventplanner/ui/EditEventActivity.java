package com.example.eventplanner.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.eventplanner.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditEventActivity extends AppCompatActivity {

    @BindView(R.id.edit_title)
    EditText titleInput;
    @BindView(R.id.edit_subtitle)
    EditText subtitleInput;
    @BindView(R.id.edit_time)
    EditText timeInput;
    @BindView(R.id.edit_date)
    EditText dateInput;
    @BindView(R.id.edit_street)
    EditText streetInput;
    @BindView(R.id.edit_city)
    EditText cityInput;
    @BindView(R.id.edit_state)
    EditText stateInput;
    @BindView(R.id.edit_zipcode)
    EditText zipcodeInput;
    @BindView(R.id.edit_info)
    EditText infoInput;
    @BindView(R.id.edit_event_button)
    Button addEventButton;
    @BindView(R.id.public_check_edit)
    MaterialCheckBox publicCheckbox;

    private Calendar myCalendar;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ButterKnife.bind(this);

        myCalendar = Calendar.getInstance();

        // Open time picker
        timeInput.setOnClickListener(view -> {
            timePickerDialog = new TimePickerDialog(EditEventActivity.this, (timePicker, hourOfDay, minutes) -> {
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
            new DatePickerDialog(EditEventActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

}
