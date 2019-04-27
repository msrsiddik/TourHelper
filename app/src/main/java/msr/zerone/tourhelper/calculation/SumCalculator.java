package msr.zerone.tourhelper.calculation;

import java.util.List;

import msr.zerone.tourhelper.eventfragment.model.Budget;
import msr.zerone.tourhelper.eventfragment.model.Cost;

public class SumCalculator {

    public SumCalculator() {
    }

    public double getBudgetSum(List<Budget> costList){
        double sum = 0.0;
        for (int i = 0; i < costList.size(); i++) {
            sum += Double.parseDouble(costList.get(i).getBudget());
        }
        return sum;
    }

    public double getCostSum(List<Cost> costList){
        double sum = 0.0;
        for (int i = 0; i < costList.size(); i++) {
            sum += Double.parseDouble(costList.get(i).getAmount());
        }
        return sum;
    }


}
