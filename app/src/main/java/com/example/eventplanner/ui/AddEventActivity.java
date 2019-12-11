package com.example.eventplanner.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.presenter.firebase.CreateEventPresenter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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
    private CompositeDisposable mycompositeDisposable = new CompositeDisposable();
    private CreateEventPresenter createEventPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);

        createEventPresenter = new CreateEventPresenter();

        myCalendar = Calendar.getInstance();

        // Open time picker
        timeInput.setOnClickListener(view -> {
            timePickerDialog = new TimePickerDialog(AddEventActivity.this, (timePicker, hourOfDay, minutes) -> {
                boolean amPm;
                amPm = hourOfDay >= 12;
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

        addEventButton.setOnClickListener(view -> createEvent());
    }

    public void createEvent() {

        String title = titleInput.getText().toString();
        String subtitle = subtitleInput.getText().toString();
        String time = timeInput.getText().toString();
        String date = dateInput.getText().toString();
        String street = streetInput.getText().toString();
        String city = cityInput.getText().toString();
        String state = stateInput.getText().toString();
        String zipcode = zipcodeInput.getText().toString();
        String info = infoInput.getText().toString();

        if (title.trim().isEmpty() ||
                subtitle.trim().isEmpty() ||
                time.trim().isEmpty() ||
                date.trim().isEmpty() ||
                street.trim().isEmpty() ||
                city.trim().isEmpty() ||
                state.trim().isEmpty() ||
                zipcode.trim().isEmpty() ||
                info.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please make sure all information is entered", Toast.LENGTH_LONG).show();
        } else {
            String address = street + ", " + city + ", " + state;

            Observable<LatLng> getLatLngForEventObservable = Observable.create(emitter -> createEventPresenter.getLocationFromAddress(address, getApplicationContext(), emitter));
            mycompositeDisposable.add(
                    getLatLngForEventObservable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> {
                                if(result != null) {
                                    Event newEvent = new Event(title, subtitle, info, (float) result.latitude, (float) result.longitude, 0);
                                    addEvent(newEvent);
                                }
                            })
            );
        }
    }

    private void addEvent(Event event){
        Observable<Boolean> addEventObservable = Observable.create(emitter -> createEventPresenter.addEventToFirebase(emitter, event));
        mycompositeDisposable.add(
                addEventObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if(result) {
                                Toast.makeText(getApplicationContext(), "Add new event", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Error, could not add new event", Toast.LENGTH_LONG).show();
                            }
                        })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mycompositeDisposable.clear();
    }
}
