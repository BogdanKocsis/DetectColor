package com.example.DAMproject.colorblindness.activity;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.DAMproject.colorblindness.CameraPreview;
import com.example.DAMproject.colorblindness.ColorN;
import com.example.DAMproject.colorblindness.Overlay;
import com.example.DAMproject.colorblindness.R;


public class MainActivity extends AppCompatActivity {

    ConstraintLayout cam_prev;
    Camera myCamera = null;
    FloatingActionButton buttonFlash, buttonDetect, buttonSwitch;
    Boolean flashEnabled = false;
    int red, blue, green, r_clo, b_clo, g_clo, closest_int, color_int = 0;
    TextView cname, color_closest;
    String[] color_name;
    CameraPreview preview;
    ColorN colorName;
    boolean flash = true;
    int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private static final int cam_per = 100;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        cname = findViewById(R.id.C_name);
        color_closest = findViewById(R.id.color_closest);
        cam_prev = findViewById(R.id.cam_prev);
        buttonFlash = findViewById(R.id.flash);
        buttonDetect = findViewById(R.id.detect);
        buttonSwitch = findViewById(R.id.switch_camera);

        startCamera();

        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myCamera != null) {
                    myCamera.stopPreview();
                    myCamera.setPreviewCallback(null);
                }


                myCamera.release();

                if (camId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    camId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    flash = false;
                    buttonSwitch.setImageResource(R.drawable.ic_camera_front);
                } else {
                    camId = Camera.CameraInfo.CAMERA_FACING_BACK;
                    flash = true;
                    buttonSwitch.setImageResource(R.drawable.ic_camera_rear);
                }

                myCamera = Camera.open(camId);

                try {
                    preview = new CameraPreview(MainActivity.this, myCamera);
                    colorName = new ColorN();
                    cam_prev.addView(preview);
                    myCamera.setDisplayOrientation(90);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bitmap = preview.getBitmap();
                    int Pixel = bitmap.getPixel(0, 0);
                    red = Color.red(Pixel);
                    green = Color.green(Pixel);
                    blue = Color.blue(Pixel);
                    color_int = red * 65536 + green * 256 + blue - 16777216;
                } catch (Exception e) {
                }
                try {
                    color_name = colorName.getColorName(red, green, blue);//String[] object return with length 3
                    r_clo = Integer.parseInt(color_name[1]);//here string return
                    g_clo = Integer.parseInt(color_name[2]);
                    b_clo = Integer.parseInt(color_name[3]);
                    closest_int = r_clo * 65536 + g_clo * 256 + b_clo - 16777216;//rgb to int //here second value for color with alpha value
                } catch (Exception e) {
                }

                if (color_name != null) {
                    cname.setText("COLOR :  " + color_name[0].toUpperCase());
                    color_closest.setBackgroundColor(closest_int);
                }
            }
        });

        buttonFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flash)
                    if (myCamera != null) {
                        if (!flashEnabled) {
                            Camera.Parameters parameters = myCamera.getParameters();
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            myCamera.setParameters(parameters);
                            flashEnabled = true;
                            buttonFlash.setImageResource(R.drawable.ic_flash_on);
                        } else {
                            Camera.Parameters parameters = myCamera.getParameters();
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            myCamera.setParameters(parameters);
                            flashEnabled = false;
                            buttonFlash.setImageResource(R.drawable.ic_flash_off);
                        }
                    }
            }
        });
    }

    public void onStart() {
        super.onStart();
        Overlay ov2 = new Overlay(this);
        addContentView(ov2, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, cam_per);
        }
    }

    @Override
    protected void onStop() {
        myCamera = null;
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startCamera();
    }

    public void onDestroy() {
        if (myCamera != null) {
            myCamera.release();
            myCamera = null;
        }
        super.onDestroy();
    }

    public void startCamera() {
        try {
            myCamera = Camera.open();
            preview = new CameraPreview(this, myCamera);
            colorName = new ColorN();
            cam_prev.addView(preview);
            myCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startTeste(View v) {
        startActivity(new Intent(this, TestActivity.class));
    }
}
