package edu.bu.metcs673.project.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import edu.bu.metcs673.project.R;

public class ViewProfileActivity extends AppCompatActivity {


    private ImageView img;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        img = findViewById(R.id.img_profile);
        tv = findViewById(R.id.tv_profile);
    }
}