package com.example.DAMproject.colorblindness.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.DAMproject.colorblindness.R;
import com.example.DAMproject.colorblindness.adapter.CustomViewPager;
import com.example.DAMproject.colorblindness.adapter.ImageAdapter;

public class TestActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private int[] images = {R.drawable.image_3, R.drawable.image_4, R.drawable.image_5, R.drawable.image_6, R.drawable.image_1, R.drawable.image_2};
    private String[]  correct = {"16", "42", "2", "5", "10", "29"};
    private String[] wrong = {"19", "72", "7", "6", "11", "70"};
    private ImageAdapter imageAdapter;
    private int c = 1, count = 1;
    private Button bt_right, bt_wrong;
    CustomViewPager viewPager;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolbar_main);
        viewPager = findViewById(R.id.viwpager);
        bt_right = findViewById(R.id.bt_right);
        bt_wrong = findViewById(R.id.bt_wrong);

        bt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextImage(view);
                c++;
            }
        });

        toolbar.setTitle("Test Rapid");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageAdapter = new ImageAdapter(this, images);
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(imageAdapter);
    }

    public void nextImage(View v) {
        viewPager.setCurrentItem(getItem(+1), true);
        bt_wrong.setText(wrong[viewPager.getCurrentItem()]);
        bt_right.setText(correct[viewPager.getCurrentItem()]);
        count++;
        if (count == 7) {
            if (c == 6) {

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.alert1, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Result")
                        .setView(view)
                        .setPositiveButton("Retake The Test", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(TestActivity.this, TestActivity.class));
                            }
                        }).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setCancelable(false);
                alert = builder.create();
                alert.show();
            } else {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.alert2, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Result")
                        .setView(view)
                        .setPositiveButton("Retake The Test", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(TestActivity.this, TestActivity.class));
                            }
                        }).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setCancelable(false);
                alert = builder.create();
                alert.show();
            }
        }

    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
}
