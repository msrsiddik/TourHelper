package msr.zerone.tourhelper.userfragment;

import android.content.Context;
import android.content.SharedPreferences;

public class BudgetAndCostPref {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public BudgetAndCostPref(Context context){
        sharedPreferences = context.getSharedPreferences("budget_cost_pref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setEventName(String eventName){
        editor.putString("eventName", eventName);
        editor.commit();
    }

    public String getEventName(){
        return sharedPreferences.getString("eventName","No tour event set");
    }

    public void setBudget(int budget){
        editor.putInt("totalBudget", budget);
        editor.commit();
    }

    public int getBudget(){
        return sharedPreferences.getInt("totalBudget",0);
    }

    public void setNewBudget(int budget){
        editor.putInt("totalNewBudget", budget);
        editor.commit();
    }

    public int getNewBudget(){
        return sharedPreferences.getInt("totalNewBudget",0);
    }

    public void setRoad(float cost){
        editor.putFloat("totalRoad", cost);
        editor.commit();
    }

    public float getRoad(){
        return sharedPreferences.getFloat("totalRoad",0);
    }

    public void setMeal(float cost){
        editor.putFloat("totalMeal", cost);
        editor.commit();
    }

    public float getMeal(){
        return sharedPreferences.getFloat("totalMeal",0);
    }

    public void setMedical(float cost){
        editor.putFloat("totalMedical", cost);
        editor.commit();
    }

    public float getMedical(){
        return sharedPreferences.getFloat("totalMedical",0);
    }

    public void setShopping(float cost){
        editor.putFloat("totalShopping", cost);
        editor.commit();
    }

    public float getShopping(){
        return sharedPreferences.getFloat("totalShopping",0);
    }

    public void setTickets(float cost){
        editor.putFloat("totalTickets", cost);
        editor.commit();
    }

    public float getTickets(){
        return sharedPreferences.getFloat("totalTickets",0);
    }

    public void setHotel(float cost){
            editor.putFloat("totalHotel", cost);
            editor.commit();
        }

    public float getHotel(){
        return sharedPreferences.getFloat("totalHotel",0);
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

}