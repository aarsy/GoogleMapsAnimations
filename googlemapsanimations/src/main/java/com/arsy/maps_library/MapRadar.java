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
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by abhay yadav on 12-Jan-17.
 */
public class MapRadar {
    private GoogleMap mGoogleMap;
    private LatLng mLatLng, mPrevLatLng;
    private BitmapDescriptor mBackgroundImageDescriptor, mBackgroundImageSweepDescriptor;             //ripple image.
    private float mTransparency = 0.5f;                                 //transparency of image.
    private volatile int mDistance = 2000;                              //distance to which ripple should be shown in metres
    private int mFillColor = Color.TRANSPARENT;                         //fill color of circle
    private int mStrokeColor = 0x38728f;                                //border color of circle
    private int mStrokeWidth = 4;                                       //border width of circle
    private GradientDrawable mOuterDrawable;
    private boolean isAnimationRunning = false;
    private Handler mSweepHandler, mOuterHandler;
    private int mRotationAngle = 0;
    private boolean isThreeSixty = false;
    private GroundOverlay mGroundOverlaySweep, mGroundOverlay;
    private ValueAnimator mAnimatorSweep;
    private float mSweepTransparency = 0.5f;                            //transparency of image.
    private boolean mSweepAnimationClockwiseAnticlockwise = false;
    private int mSweepAnimationClockwiseAnticlockwiseDuration = 1;
    private int mSweepSpeed = 2;                                        //increase this to increase speed
    private int mCurrentAngle = 0;
    private int mColors[] = {0x0038728f, 0xff38728f};                   //sweep animation colors

    public MapRadar(GoogleMap googleMap, LatLng latLng, Context context) {
        mGoogleMap = googleMap;
        mLatLng = latLng;
        mPrevLatLng = latLng;
        mOuterDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.background);
    }

    /**
     * @return current duration of animation
     */
    public int getClockwiseAnticlockwiseDuration() {
        return mSweepAnimationClockwiseAnticlockwiseDuration;
    }

    /**
     * @return current radar rotation speed
     */
    public int getRadarSpeed() {
        return mSweepSpeed;
    }

    /**
     * @return current state of both side rotation
     */
    public boolean isRadarAnimationClockWiseAnticlockwise() {
        return mSweepAnimationClockwiseAnticlockwise;
    }

    /**
     * @return current state of animation
     */
    public boolean isAnimationRunning() {
        return isAnimationRunning;
    }

    /**
     * @param transparency sets end transparency for circle
     */
    public MapRadar withOuterCircleTransparency(float transparency) {
        mTransparency = transparency;
        return this;
    }

    /**
     * @param transparency sets transparency for background of circle
     */
    public MapRadar withRadarTransparency(float transparency) {
        mSweepTransparency = transparency;
        return this;
    }

    /**
     * @param sweepAnimationClockWiseAnticlockwise enable both side rotation
     */
    public MapRadar withClockwiseAnticlockwise(boolean sweepAnimationClockWiseAnticlockwise) {
        mSweepAnimationClockwiseAnticlockwise = sweepAnimationClockWiseAnticlockwise;
        return this;
    }

    /**
     * @deprecated use {@link #withClockwiseAnticlockwise(boolean)} instead
     */
    @Deprecated
    public void withClockWiseAnticlockwise(boolean sweepAnimationClockWiseAnticlockwise) {
        withClockwiseAnticlockwise(sweepAnimationClockWiseAnticlockwise);
    }

    /**
     * @param sweepAnimationClockWiseAnticlockwiseDuration duration of animation
     */
    public MapRadar withClockwiseAnticlockwiseDuration(int sweepAnimationClockWiseAnticlockwiseDuration) {
        mSweepAnimationClockwiseAnticlockwiseDuration = sweepAnimationClockWiseAnticlockwiseDuration;
        return this;
    }

    /**
     * @param startColor start color of radar gradient
     * @param tailColor  end color of radar gradient
     */
    public MapRadar withRadarColors(int startColor, int tailColor) {
        mColors[0] = startColor;
        mColors[1] = tailColor;
        return this;
    }

    /**
     * @param sweepSpeed radar rotation speed
     */
    public MapRadar withRadarSpeed(int sweepSpeed) {
        mSweepSpeed = sweepSpeed;
        return this;
    }

    /**
     * @param distance sets radius distance for circle
     */
    public MapRadar withDistance(int distance) {
        if (distance < 200) {
            distance = 200;
        }
        mDistance = distance;
        return this;
    }

    /**
     * @param latLng sets position for center of circle
     */
    public MapRadar withLatLng(LatLng latLng) {
        mPrevLatLng = mLatLng;
        mLatLng = latLng;
        return this;
    }

    /**
     * @param fillColor inner color of circle
     */
    public MapRadar withOuterCircleFillColor(int fillColor) {
        mFillColor = fillColor;
        return this;
    }

    /**
     * @param strokeColor color of circle stroke
     */
    public MapRadar withOuterCircleStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        return this;
    }

    @Deprecated
    public void withOuterCircleStrokewidth(int strokeWidth) {
        withOuterCircleStrokeWidth(strokeWidth);
    }

    /**
     * @param strokeWidth width of circle stroke
     */
    public MapRadar withOuterCircleStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        return this;
    }

    private final Runnable mSweepRunnable = new Runnable() {
        @Override
        public void run() {
            mGroundOverlaySweep = mGoogleMap.addGroundOverlay(new GroundOverlayOptions()
                    .position(mLatLng, mDistance)
                    .transparency(mSweepTransparency)
                    .image(mBackgroundImageSweepDescriptor));
            AnimateSweep();
        }
    };

    private final Runnable mRadarRunnable = new Runnable() {
        @Override
        public void run() {
            mGroundOverlay = mGoogleMap.addGroundOverlay(new GroundOverlayOptions()
                    .position(mLatLng, mDistance + mStrokeWidth)
                    .transparency(mTransparency)
                    .image(mBackgroundImageDescriptor));
        }
    };

    private void AnimateSweep() {
        mAnimatorSweep = ValueAnimator.ofInt(0, 360);
        mAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
        mAnimatorSweep.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (mSweepAnimationClockwiseAnticlockwise) {
                    if ((mCurrentAngle) >= (360 * 2 * mSweepAnimationClockwiseAnticlockwiseDuration)) {
                        isThreeSixty = true;
                    }
                    if (mCurrentAngle <= 0) {
                        isThreeSixty = false;
                    }
                    if (isThreeSixty) {
                        mRotationAngle = (mRotationAngle - mSweepSpeed) % 360;
                        mCurrentAngle = mCurrentAngle - mSweepSpeed;
                    } else {
                        mRotationAngle = (mRotationAngle + mSweepSpeed) % 360;
                        mCurrentAngle = mCurrentAngle + mSweepSpeed;
                    }
                } else {
                    mRotationAngle = (mRotationAngle + mSweepSpeed) % 360;
                }
                mGroundOverlaySweep.setBearing(mRotationAngle);
                if (mLatLng != mPrevLatLng) {
                    mGroundOverlaySweep.setPosition(mLatLng);
                }
            }
        });
        mAnimatorSweep.start();
    }

    private void setDrawableAndBitmap() {
        mOuterDrawable.setColor(mFillColor);
        mOuterDrawable.setStroke(UiUtil.dpToPx(mStrokeWidth), mStrokeColor);
        mBackgroundImageDescriptor = UiUtil.drawableToBitmapDescriptor(mOuterDrawable);
        GradientDrawable radarDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, mColors);
        radarDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        radarDrawable.setShape(GradientDrawable.OVAL);
        radarDrawable.setSize(1200, 1200);
        mBackgroundImageSweepDescriptor = UiUtil.drawableToBitmapDescriptor(radarDrawable);
    }

    /**
     * Starts animation
     */
    public void startRadarAnimation() {
        setDrawableAndBitmap();
        if (!isAnimationRunning) {
            mSweepHandler = new Handler();
            mSweepHandler.post(mSweepRunnable);
            mOuterHandler = new Handler();
            mOuterHandler.post(mRadarRunnable);
            isAnimationRunning = true;
        }
    }

    /**
     * Stops current animation if it's running
     */
    public void stopRadarAnimation() {
        if (isAnimationRunning) {
            mSweepHandler.removeCallbacks(mSweepRunnable);
            mAnimatorSweep.cancel();
            mGroundOverlaySweep.remove();
            mOuterHandler.removeCallbacks(mRadarRunnable);
            mGroundOverlay.remove();
            isAnimationRunning = false;
        }
    }
}
