package org.example.ModelData;

public class CreditUtilisation implements IAttribute{

    private Double creditUtlisationScore;

    public CreditUtilisation(){
        this.creditUtlisationScore = 0.0;
    };

    public CreditUtilisation(int creditUtilisationIndex)
    {
        this.creditUtlisationScore = this.calculateScore(creditUtilisationIndex);
    }

    @Override
    public Double getScore() {
        return this.creditUtlisationScore;
    }

    @Override
    public String getName() {
        return "CreditUtilisation";
    }

    @Override
    public String toString(){
        return "CreditUtilisation score = "+this.creditUtlisationScore;
    }

    @Override
    public Double calculateScore(int creditUtilisationIndex) {
        return switch(creditUtilisationIndex){
            case 0 -> 0.0;
            case 1,2,3 -> 100.00;
            case 4,5 -> 80.00;
            case 6,7 -> 60.00;
            default -> 40.00;
        };
    }
}
