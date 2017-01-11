package com.arsy.maps_library;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import android.view.animation.CycleInterpolator;
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
    private Bitmap backgroundImage, backgroundImageSweep;                     //ripple image.
    private float transparency = 0.5f;                    //transparency of image.
    private volatile int distance = 2000;                       //distance to which ripple should be shown in metres
    private int numberOfRipples = 1;                      //number of ripples to show, max = 4
    private int fillColor = Color.TRANSPARENT;           //fillcolor of circle
    private int strokeColor = Color.BLACK;               //border color of circle
    private int strokewidth = 10;                          //border width of circle
    private long durationBetweenTwoRipples = 4000;      //in microseconds.
    private long rippleDuration = 12000;                //in microseconds
    private ValueAnimator vAnimators[];
    private GroundOverlay gOverlays[];
    private GradientDrawable drawable;
    private boolean isAnimationRunning = false;
    private MapAnimationType mapAnimationType = MapAnimationType.RIPPLE;


    //Sweep animation params
    private Handler handlers[], sweepHandler;
    private int rotationAngle = 0;
    private boolean isthreesixty = false;
    private GroundOverlay gOverlaySweep;
    private ValueAnimator vAnimatorSweep;
    private float sweeptransparency = 0.5f;                    //transparency of image.
    private boolean sweepAnimationClockWiseAnticlockwise = false;
    private int sweepAnimationClockwiseAnticlockwiseDuration = 1;
    private int sweepSpeed = 3;                     //increase this to increase speed
    private int angle = 0;
    private int colors[] = {Color.parseColor("#0038728f"), Color.parseColor("#ff38728f")};    //sweep animation colors

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

    public void withSweepTransparency(float transparency) {
        this.sweeptransparency = transparency;
    }

    public boolean isSweepAnimationClockWiseAnticlockwise() {
        return sweepAnimationClockWiseAnticlockwise;
    }

    public void withSweepAnimationClockWiseAnticlockwise(boolean sweepAnimationClockWiseAnticlockwise) {
        this.sweepAnimationClockWiseAnticlockwise = sweepAnimationClockWiseAnticlockwise;
    }

    public int getSweepAnimationClockwiseAnticlockwiseDuration() {
        return sweepAnimationClockwiseAnticlockwiseDuration;
    }

    public void withSweepAnimationClockwiseAnticlockwiseDuration(int sweepAnimationClockwiseAnticlockwiseDuration) {
        this.sweepAnimationClockwiseAnticlockwiseDuration = sweepAnimationClockwiseAnticlockwiseDuration;
    }

    private int getSweepSpeed() {
        return sweepSpeed;
    }

    public void withSweepSpeed(int sweepSpeed) {
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

    public void withNumberOfRipples(int numberOfRipples) {
        if (mapAnimationType == MapAnimationType.RIPPLE) {
            if (numberOfRipples > 4 || numberOfRipples < 1)
                numberOfRipples = 4;
            this.numberOfRipples = numberOfRipples;
        }
    }


    public void withFillColor(int fillColor) {
        if (mapAnimationType != MapAnimationType.SWEEP) {
            this.fillColor = fillColor;
            setDrawableAndBitmap();
        }
    }


    public void withStrokeColor(int strokeColor) {
        if (mapAnimationType != MapAnimationType.SWEEP) {
            this.strokeColor = strokeColor;
            setDrawableAndBitmap();
        }
    }

    public void withStrokewidth(int strokewidth) {
        if (mapAnimationType != MapAnimationType.SWEEP) {
            this.strokewidth = strokewidth;
            setDrawableAndBitmap();
        }
    }

    public void withDurationBetweenTwoRipples(long durationBetweenTwoRipples) {
        if (mapAnimationType == MapAnimationType.RIPPLE) {
            this.durationBetweenTwoRipples = durationBetweenTwoRipples;
        }
    }

    public boolean isAnimationRunning() {
        return isAnimationRunning;
    }


    public void withRippleDuration(long rippleDuration) {
        this.rippleDuration = rippleDuration;
    }

    public void setMapAnimationType(MapAnimationType type) {
        if (!isAnimationRunning())
            this.mapAnimationType = type;
    }

    public MapAnimationType getMapAnimationType() {
        return mapAnimationType;
    }

    final Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            gOverlays[0] = googleMap.addGroundOverlay(new
                    GroundOverlayOptions()
                    .position(latLng, distance)
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
                    .position(latLng, distance)
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
                    .position(latLng, distance)
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
                    .position(latLng, distance)
                    .transparency(transparency)
                    .image(BitmapDescriptorFactory.fromBitmap(backgroundImage)));

            OverLay(3);

        }
    };

    private void OverLay(final int i) {
        vAnimators[i] = ValueAnimator.ofInt(0, distance);
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
            gOverlays[0] = googleMap.addGroundOverlay(new
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
        if (mapAnimationType == MapAnimationType.RIPPLE) {
            drawable.setColor(fillColor);
            float d = Resources.getSystem().getDisplayMetrics().density;
            int width = (int) (strokewidth * d); // margin in pixels
            drawable.setStroke(width, strokeColor);
        }
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

    public void startMapAnimation() {
        if (mapAnimationType == MapAnimationType.SWEEP) {
            startSweepRadarAnimation();
        } else if (mapAnimationType == MapAnimationType.RIPPLE)
            startRippleMapAnimation();
    }

    private void startSweepRadarAnimation() {

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        gradientDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setSize(1500, 1500);

        //drawableSweep.setColors(colors);
        //drawableSweep.setColor(Color.parseColor("#38728f"));
        backgroundImageSweep = drawableToBitmap(gradientDrawable);
        sweepHandler = new Handler();
        sweepHandler.post(sweepRunnable);

        drawable.setColor(Color.parseColor("#00000000"));
        float d = Resources.getSystem().getDisplayMetrics().density;
        int width = (int) (strokewidth * d); // margin in pixels
        drawable.setStroke(width, strokeColor);
        backgroundImage = drawableToBitmap(drawable);
        handlers[0] = new Handler();
        handlers[0].post(runnable);
        isAnimationRunning = true;
    }

    private void startRippleMapAnimation() {
        drawable.setColor(fillColor);
        float d = Resources.getSystem().getDisplayMetrics().density;
        int width = (int) (strokewidth * d); // margin in pixels
        drawable.setStroke(width, strokeColor);
        backgroundImage = drawableToBitmap(drawable);
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
        isAnimationRunning = true;
    }

    public void stopMapAnimation() {
        if (mapAnimationType == MapAnimationType.RIPPLE)
            stopRippleMapAnimation();
        else if (mapAnimationType == MapAnimationType.SWEEP)
            stopSweepAnimation();
    }

    private void stopSweepAnimation() {
        sweepHandler.removeCallbacks(sweepRunnable);
        vAnimatorSweep.cancel();
        gOverlaySweep.remove();
        handlers[0].removeCallbacks(runnable);
        gOverlays[0].remove();
        isAnimationRunning = false;
    }

    private void stopRippleMapAnimation() {
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
        isAnimationRunning = false;
    }

    //not needed for now
    public Bitmap drawToBitmap() {

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
