package org.example.ModelData;



public class AccountAge implements  IAttribute{

    private Double accountAgeScore;

    public AccountAge(){
        this.accountAgeScore = 0.0;
    };

    public AccountAge(int accountAgeIndex){
        this.accountAgeScore = this.calculateScore(accountAgeIndex);
    }

    @Override
    public Double getScore() {
        return this.accountAgeScore;
    }

    @Override
    public String getName() {
        return "AccountAge";
    }

    @Override
    public Double calculateScore(int accountAgeIndex) {
        return switch (accountAgeIndex) {
            case 0 -> 0.00;
            case 1, 2 -> 40.00;
            case 3, 4, 5 -> 60.00;
            case 6, 7, 8, 9, 10 -> 80.00;
            default -> 100.00;
        };
    }

    @Override
    public String toString(){
        return "Account age score = "+this.accountAgeScore;
    }

/*
10+ years: +100 points
6-10 years: +80 points
3-5 years: +60 points
1-2 years: +40 points
< 1 year: 0 points



 */
}
