package msr.zerone.tourhelper.userfragment;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserPreference(Context context){
        sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setHomeuId(String stutus){
        editor.putString("uid", stutus);
        editor.commit();
    }

    public String getHomeuId(){
        return sharedPreferences.getString("uid","default");
    }

    public void setHomeEventId(String stutus){
            editor.putString("eventid", stutus);
            editor.commit();
    }

    public String getHomeEventId(){
        return sharedPreferences.getString("eventid","default");
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

}