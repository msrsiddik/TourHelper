
package msr.zerone.tourhelper.weather.todayforecast;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Rain {

    @SerializedName("3h")
    private Double mH;

    public Double getH() {
        return mH;
    }

    public void setH(Double h) {
        mH = h;
    }

}
