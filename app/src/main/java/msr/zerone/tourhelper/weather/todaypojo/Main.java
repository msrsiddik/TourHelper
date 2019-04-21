
package msr.zerone.tourhelper.weather.todaypojo;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Main {

    @SerializedName("humidity")
    private float mHumidity;
    @SerializedName("pressure")
    private float mPressure;
    @SerializedName("temp")
    private float mTemp;
    @SerializedName("temp_max")
    private float mTempMax;
    @SerializedName("temp_min")
    private float mTempMin;

    public float getHumidity() {
        return mHumidity;
    }

    public void setHumidity(float humidity) {
        mHumidity = humidity;
    }

    public float getPressure() {
        return mPressure;
    }

    public void setPressure(float pressure) {
        mPressure = pressure;
    }

    public float getTemp() {
        return mTemp;
    }

    public void setTemp(float temp) {
        mTemp = temp;
    }

    public float getTempMax() {
        return mTempMax;
    }

    public void setTempMax(float tempMax) {
        mTempMax = tempMax;
    }

    public float getTempMin() {
        return mTempMin;
    }

    public void setTempMin(float tempMin) {
        mTempMin = tempMin;
    }

}
