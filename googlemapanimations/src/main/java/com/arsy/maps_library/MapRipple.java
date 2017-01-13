/*
 * Copyright (C) 2016 Abhay Raj Singh Yadav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package com.arsy.maps_library;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by abhay yadav on 09-Aug-16.
 */
public class MapRipple {
    private GoogleMap googleMap;


    private LatLng latLng, prevlatlng;
    private Bitmap backgroundImage;                     //ripple image.
    private float transparency = 0.5f;                    //transparency of image.
    private volatile double distance = 2000;                       //distance to which ripple should be shown in metres
    private int numberOfRipples = 1;                      //number of ripples to show, max = 4
    private int fillColor = Color.TRANSPARENT;           //fillcolor of circle
    private int strokeColor = Color.BLACK;               //border color of circle
    private int strokewidth = 10;                          //border width of circle
    private long durationBetweenTwoRipples = 4000;      //in microseconds.
    private long rippleDuration = 12000;                //in microseconds
    private ValueAnimator vAnimators[];
    private Handler handlers[];
    private GroundOverlay gOverlays[];
    private GradientDrawable drawable;
    private boolean isAnimationRunning = false;

    public MapRipple(GoogleMap googleMap, LatLng latLng, Context context) {
        this.googleMap = googleMap;
        this.latLng = latLng;
        this.prevlatlng = latLng;
        drawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.background);
        vAnimators = new ValueAnimator[4];
        handlers = new Handler[4];
        gOverlays = new GroundOverlay[4];
    }

    public void withTransparency(float transparency) {
        this.transparency = transparency;
    }


    public void withDistance(double distance) {
        if (distance < 200)
            distance = 200;
        this.distance = distance;
    }

    public void withLatLng(LatLng latLng) {
        prevlatlng = this.latLng;
        this.latLng = latLng;

    }


    public void withNumberOfRipples(int numberOfRipples) {
        if (numberOfRipples > 4 || numberOfRipples < 1)
            numberOfRipples = 4;
        this.numberOfRipples = numberOfRipples;
    }


    public void withFillColor(int fillColor) {
        this.fillColor = fillColor;
    }


    public void withStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }


    public void withStrokewidth(int strokewidth) {
        this.strokewidth = strokewidth;
    }


    public void withDurationBetweenTwoRipples(long durationBetweenTwoRipples) {
        this.durationBetweenTwoRipples = durationBetweenTwoRipples;
    }

    public boolean isAnimationRunning() {
        return isAnimationRunning;
    }


    public void withRippleDuration(long rippleDuration) {
        this.rippleDuration = rippleDuration;
    }

    final Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            gOverlays[0] = googleMap.addGroundOverlay(new
                    GroundOverlayOptions()
                    .position(latLng, (int) distance)
                    .transparency(transparency)
                    .image(BitmapDescriptorFactory.fromBitmap(backgroundImage)));

            OverLay(0);

        }
    };
    final Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            gOverlays[1] = googleMap.addGroundOverlay(new
                    GroundOverlayOptions()
                    .position(latLng, (int) distance)
                    .transparency(transparency)
                    .image(BitmapDescriptorFactory.fromBitmap(backgroundImage)));

            OverLay(1);

        }
    };
    final Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            gOverlays[2] = googleMap.addGroundOverlay(new
                    GroundOverlayOptions()
                    .position(latLng, (int) distance)
                    .transparency(transparency)
                    .image(BitmapDescriptorFactory.fromBitmap(backgroundImage)));

            OverLay(2);

        }
    };
    final Runnable runnable4 = new Runnable() {
        @Override
        public void run() {
            gOverlays[3] = googleMap.addGroundOverlay(new
                    GroundOverlayOptions()
                    .position(latLng, (int) distance)
                    .transparency(transparency)
                    .image(BitmapDescriptorFactory.fromBitmap(backgroundImage)));

            OverLay(3);

        }
    };

    private void OverLay(final int i) {
        vAnimators[i] = ValueAnimator.ofInt(0, (int) distance);
        vAnimators[i].setRepeatCount(ValueAnimator.INFINITE);
        vAnimators[i].setRepeatMode(ValueAnimator.RESTART);
        vAnimators[i].setDuration(rippleDuration);
        vAnimators[i].setEvaluator(new IntEvaluator());
        vAnimators[i].setInterpolator(new LinearInterpolator());
        vAnimators[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final Integer val = (Integer) valueAnimator.getAnimatedValue();
                gOverlays[i].setDimensions(val);
                if (distance - val <= 10) {
                    if (latLng != prevlatlng) {
                        gOverlays[i].setPosition(latLng);
                    }
                }

            }
        });
        vAnimators[i].start();
    }

    private void setDrawableAndBitmap() {
        drawable.setColor(fillColor);
        float d = Resources.getSystem().getDisplayMetrics().density;
        int width = (int) (strokewidth * d); // margin in pixels
        drawable.setStroke(width, strokeColor);
        backgroundImage = drawableToBitmap(drawable);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void stopRippleMapAnimation() {
        if (isAnimationRunning) {
            try {
                for (int i = 0; i < numberOfRipples; i++) {
                    if (i == 0) {
                        handlers[i].removeCallbacks(runnable1);
                        vAnimators[i].cancel();
                        gOverlays[i].remove();

                    }
                    if (i == 1) {
                        handlers[i].removeCallbacks(runnable2);
                        vAnimators[i].cancel();
                        gOverlays[i].remove();
                    }
                    if (i == 2) {
                        handlers[i].removeCallbacks(runnable3);
                        vAnimators[i].cancel();
                        gOverlays[i].remove();
                    }
                    if (i == 3) {
                        handlers[i].removeCallbacks(runnable4);
                        vAnimators[i].cancel();
                        gOverlays[i].remove();
                    }
                }


            } catch (Exception e) {

            }
        }
        isAnimationRunning = false;
    }

    public void startRippleMapAnimation() {
        if (!isAnimationRunning) {
            setDrawableAndBitmap();
            for (int i = 0; i < numberOfRipples; i++) {
                if (i == 0) {
                    handlers[i] = new Handler();
                    handlers[i].postDelayed(runnable1, durationBetweenTwoRipples * i);
                }
                if (i == 1) {
                    handlers[i] = new Handler();
                    handlers[i].postDelayed(runnable2, durationBetweenTwoRipples * i);
                }
                if (i == 2) {
                    handlers[i] = new Handler();
                    handlers[i].postDelayed(runnable3, durationBetweenTwoRipples * i);
                }
                if (i == 3) {
                    handlers[i] = new Handler();
                    handlers[i].postDelayed(runnable4, durationBetweenTwoRipples * i);
                }
            }
        }
        isAnimationRunning = true;
    }
}
