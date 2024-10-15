package org.example.ModelData;



public class DebtToIncome implements IAttribute{

    private Double debtToIncomeScore;

    public DebtToIncome(){
        this.debtToIncomeScore = 0.0;
    };

    public DebtToIncome(int debtToIncomeIndex) {
        this.debtToIncomeScore = this.calculateScore(debtToIncomeIndex);
    }

    @Override
    public Double calculateScore(int debtToIncomeIndex) {
        //debtToIncomeIndex = total monthly debt wrt total monthly income

       return switch (debtToIncomeIndex/10){
            case 0,1,2,3 -> 100.00;
            case 4,5 -> 80.00;
            case 6,7 -> 60.00;
            default -> 40.00;
        };

    }

    @Override
    public Double getScore() {
        return this.debtToIncomeScore;
    }

    @Override
    public String getName() {
        return "DebtToIncome";
    }

    @Override
    public String toString() {
        return this.getName() + debtToIncomeScore;
    }
}
