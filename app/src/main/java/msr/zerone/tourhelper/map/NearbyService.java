package msr.zerone.tourhelper.map;

import msr.zerone.tourhelper.map.nearby.NearbyResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NearbyService {
    @GET
    Call<NearbyResponse>getNearbyPlaces(@Url String endUrl);
}
