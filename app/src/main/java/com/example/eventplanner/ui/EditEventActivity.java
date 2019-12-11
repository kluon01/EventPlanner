package com.example.eventplanner.ui;

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
import android.widget.EditText;

import com.example.eventplanner.R;

public class EditEventActivity extends AppCompatActivity {

    @BindView(R.id.edit_title)
    EditText titleInput;
    @BindView(R.id.edit_subtitle)
    EditText subtitleInput;
    @BindView(R.id.edit_address)
    EditText addressInput;
    @BindView(R.id.edit_info)
    EditText infoInput;
    @BindView(R.id.edit_event_button)
    Button addEventButton;
    @BindView(R.id.public_check_edit)
    MaterialCheckBox publicCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ButterKnife.bind(this);
    }

}
