
package msr.zerone.tourhelper.weather.todaypojo;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Main {

    @SerializedName("humidity")
    private Long mHumidity;
    @SerializedName("pressure")
    private Long mPressure;
    @SerializedName("temp")
    private int mTemp;
    @SerializedName("temp_max")
    private int mTempMax;
    @SerializedName("temp_min")
    private int mTempMin;

    public Long getHumidity() {
        return mHumidity;
    }

    public void setHumidity(Long humidity) {
        mHumidity = humidity;
    }

    public Long getPressure() {
        return mPressure;
    }

    public void setPressure(Long pressure) {
        mPressure = pressure;
    }

    public int getTemp() {
        return mTemp;
    }

    public void setTemp(int temp) {
        mTemp = temp;
    }

    public int getTempMax() {
        return mTempMax;
    }

    public void setTempMax(int tempMax) {
        mTempMax = tempMax;
    }

    public int getTempMin() {
        return mTempMin;
    }

    public void setTempMin(int tempMin) {
        mTempMin = tempMin;
    }

}
