package com.example.mypotplant.person.login;

import android.view.animation.LinearInterpolator;

/**
 * Created by MXL on 2019/12/12
 * <br>类描述：自定义插值器用于登录<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class JellyInterpolator extends LinearInterpolator {
    private float factor;

    public JellyInterpolator() {
        this.factor = 0.15f;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (Math.pow(2, -10 * input)
                * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
    }
}

