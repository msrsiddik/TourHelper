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

    public void clear(){
        editor.clear();
        editor.commit();
    }

}