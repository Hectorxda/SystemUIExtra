package com.android.systemui.statusbar.powerwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.IPowerManager;
import android.os.ServiceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

public class BrightnessSeekBar extends SeekBar implements
        SeekBar.OnSeekBarChangeListener {

    private int mOldBrightness;
    private int mOldAutomatic;

    private boolean mAutomaticAvailable;

    // Backlight range is from 0 - 255. Need to make sure that user
    // doesn't set the backlight to 0 and get stuck
    private static final int MINIMUM_BACKLIGHT = android.os.Power.BRIGHTNESS_DIM + 10;
    private static final int MAXIMUM_BACKLIGHT = android.os.Power.BRIGHTNESS_ON;

    public BrightnessSeekBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public BrightnessSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

        setMax(MAXIMUM_BACKLIGHT - MINIMUM_BACKLIGHT);
        try {
            mOldBrightness = Settings.System.getInt(getContext()
                    .getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException snfe) {
            mOldBrightness = MAXIMUM_BACKLIGHT;
        }
        setProgress(mOldBrightness - MINIMUM_BACKLIGHT);
        setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        // TODO Auto-generated method stub
        setBrightness(progress + MINIMUM_BACKLIGHT);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        setValeToSystem();
    }

    private void setBrightness(int brightness) {
        try {
            IPowerManager power = IPowerManager.Stub.asInterface(ServiceManager
                    .getService("power"));
            if (power != null) {
                power.setBacklightBrightness(brightness);
            }
        } catch (RemoteException doe) {

        }
    }

    private void setValeToSystem() {

        Settings.System.putInt(getContext().getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, getProgress()
                        + MINIMUM_BACKLIGHT);
    }
}
