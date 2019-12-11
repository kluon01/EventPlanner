package com.example.eventplanner.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.eventplanner.R;
import com.google.android.material.checkbox.MaterialCheckBox;

public class AddEventActivity extends AppCompatActivity {

    @BindView(R.id.title_input)
    EditText titleInput;
    @BindView(R.id.subtitle_input)
    EditText subtitleInput;
    @BindView(R.id.address_input)
    EditText addressInput;
    @BindView(R.id.info_input)
    EditText infoInput;
    @BindView(R.id.add_event_button)
    Button addEventButton;
    @BindView(R.id.public_check)
    MaterialCheckBox publicCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);
    }

}
