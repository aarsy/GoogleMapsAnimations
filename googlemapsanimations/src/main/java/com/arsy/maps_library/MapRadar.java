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

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by abhay yadav on 12-Jan-17.
 */
public class MapRadar {

    private GoogleMap googleMap;
    private LatLng latLng, prevlatlng;
    private Bitmap backgroundImage, backgroundImageSweep;                     //ripple image.
    private float transparency = 0.5f;                    //transparency of image.
    private volatile int distance = 2000;                       //distance to which ripple should be shown in metres
    private int fillColor = Color.TRANSPARENT;           //fillcolor of circle
    private int strokeColor = Color.parseColor("#38728f");               //border color of circle
    private int strokewidth = 4;                          //border width of circle
    private GradientDrawable outerDrawable, radarDrawable;
    private boolean isAnimationRunning = false;
    private Handler sweepHandler, outerHandler;
    private int rotationAngle = 0;
    private boolean isthreesixty = false;
    private GroundOverlay gOverlaySweep, gOverlay;
    private ValueAnimator vAnimatorSweep;
    private float sweeptransparency = 0.5f;                    //transparency of image.
    private boolean sweepAnimationClockWiseAnticlockwise = false;
    private int sweepAnimationClockwiseAnticlockwiseDuration = 1;
    private int sweepSpeed = 2;                     //increase this to increase speed
    private int angle = 0;
    private int colors[] = {Color.parseColor("#0038728f"), Color.parseColor("#ff38728f")};    //sweep animation colors

    public MapRadar(GoogleMap googleMap, LatLng latLng, Context context) {
        this.googleMap = googleMap;
        this.latLng = latLng;
        this.prevlatlng = latLng;
        outerDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.background);
    }

    public void withOuterCircleTransparency(float transparency) {
        this.transparency = transparency;
    }

    public void withRadarTransparency(float transparency) {
        this.sweeptransparency = transparency;
    }

    public boolean isRadarAnimationClockWiseAnticlockwise() {
        return sweepAnimationClockWiseAnticlockwise;
    }

    public void withClockWiseAnticlockwise(boolean sweepAnimationClockWiseAnticlockwise) {
        this.sweepAnimationClockWiseAnticlockwise = sweepAnimationClockWiseAnticlockwise;
    }

    public int getClockwiseAnticlockwiseDuration() {
        return sweepAnimationClockwiseAnticlockwiseDuration;
    }

    public void withClockwiseAnticlockwiseDuration(int sweepAnimationClockwiseAnticlockwiseDuration) {
        this.sweepAnimationClockwiseAnticlockwiseDuration = sweepAnimationClockwiseAnticlockwiseDuration;
    }

    public void withRadarColors(int startColor, int tailColor) {
        colors[0] = startColor;
        colors[1] = tailColor;
    }

    public int getRadarSpeed() {
        return sweepSpeed;
    }

    public void withRadarSpeed(int sweepSpeed) {
        this.sweepSpeed = sweepSpeed;
    }

    public void withDistance(int distance) {
        if (distance < 200)
            distance = 200;
        this.distance = distance;
    }

    public void withLatLng(LatLng latLng) {
        prevlatlng = this.latLng;
        this.latLng = latLng;
    }

    public void withOuterCircleFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void withOuterCircleStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;

    }

    public void withOuterCircleStrokewidth(int strokewidth) {
        this.strokewidth = strokewidth;
    }

    public boolean isAnimationRunning() {
        return isAnimationRunning;
    }


    final Runnable sweepRunnable = new Runnable() {
        @Override
        public void run() {
            gOverlaySweep = googleMap.addGroundOverlay(new
                    GroundOverlayOptions()
                    .position(latLng, distance)
                    .transparency(sweeptransparency)
                    .image(BitmapDescriptorFactory.fromBitmap(backgroundImageSweep)));
            AnimateSweep();

        }
    };

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            gOverlay = googleMap.addGroundOverlay(new
                    GroundOverlayOptions()
                    .position(latLng, distance + strokewidth)
                    .transparency(transparency)
                    .image(BitmapDescriptorFactory.fromBitmap(backgroundImage)));
        }
    };

    private void AnimateSweep() {
        vAnimatorSweep = ValueAnimator.ofInt(0, 360);
        vAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
        vAnimatorSweep.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (sweepAnimationClockWiseAnticlockwise) {
                    if ((angle) >= (360 * 2 * sweepAnimationClockwiseAnticlockwiseDuration))
                        isthreesixty = true;
                    if (angle <= 0)
                        isthreesixty = false;
                    if (isthreesixty) {
                        rotationAngle = (rotationAngle - sweepSpeed) % 360;
                        angle = angle - sweepSpeed;
                    } else {
                        rotationAngle = (rotationAngle + sweepSpeed) % 360;
                        angle = angle + sweepSpeed;
                    }
                } else {
                    rotationAngle = (rotationAngle + sweepSpeed) % 360;
                }
                gOverlaySweep.setBearing(rotationAngle);
                if (latLng != prevlatlng) {
                    gOverlaySweep.setPosition(latLng);
                }
            }
        });
        vAnimatorSweep.start();


    }

    private void setDrawableAndBitmap() {

        outerDrawable.setColor(fillColor);
        float d = Resources.getSystem().getDisplayMetrics().density;
        int width = (int) (strokewidth * d); // margin in pixels
        outerDrawable.setStroke(width, strokeColor);
        backgroundImage = drawableToBitmap(outerDrawable);
        radarDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        radarDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        radarDrawable.setShape(GradientDrawable.OVAL);
        radarDrawable.setSize(1200, 1200);
        backgroundImageSweep = drawableToBitmap(radarDrawable);

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


    public void startRadarAnimation() {
        setDrawableAndBitmap();
        if (!isAnimationRunning) {
            sweepHandler = new Handler();
            sweepHandler.post(sweepRunnable);
            outerHandler = new Handler();
            outerHandler.post(runnable);
        }
        isAnimationRunning = true;
    }


    public void stopRadarAnimation() {
        if (isAnimationRunning) {
            sweepHandler.removeCallbacks(sweepRunnable);
            vAnimatorSweep.cancel();
            gOverlaySweep.remove();
            outerHandler.removeCallbacks(runnable);
            gOverlay.remove();
        }
        isAnimationRunning = false;
    }

    //not needed for now
    private Bitmap drawToBitmap() {

        int radarColor = Color.parseColor("#ffffff");
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(distance, distance, config);
        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas((workingBitmap));
        Paint mPaintRadar = new Paint();
        mPaintRadar.setColor(radarColor);
        mPaintRadar.setAntiAlias(true);
        Shader shader = new SweepGradient(0, 0, Color.parseColor("#00000000"),
                Color.parseColor("#ff000000"));
        mPaintRadar.setShader(shader);
        //canvas.concat(matrix);
        RectF rect = new RectF(0, 0, distance, distance);
        canvas.drawRoundRect(rect, distance / 2, distance / 2, mPaintRadar);
        //canvas.drawCircle(0, 0, w/2, mPaintRadar);
        return workingBitmap;
    }
}
