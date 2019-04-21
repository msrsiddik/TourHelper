
package msr.zerone.tourhelper.weather.todaypojo;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Wind {

    @SerializedName("deg")
    private float mDeg;
    @SerializedName("speed")
    private Double mSpeed;

    public float getDeg() {
        return mDeg;
    }

    public void setDeg(float deg) {
        mDeg = deg;
    }

    public Double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Double speed) {
        mSpeed = speed;
    }

}
