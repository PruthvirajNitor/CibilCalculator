package org.example.ModelData;

public class PaymentHistory implements IAttribute{


    private Double paymentHistoryScore;


    public PaymentHistory(){
        this.paymentHistoryScore = 0.0;
    };

    public PaymentHistory(int paymentHistoryIndex) {
        this.paymentHistoryScore = this.calculateScore(paymentHistoryIndex);
    }

    @Override
    public Double calculateScore(int paymentHistoryIndex) {
        //  paymentHistoryIndex = total missed payments count
    return (double) paymentHistoryIndex;
    }

    @Override
    public Double getScore() {
        return this.paymentHistoryScore;
    }

    @Override
    public String getName() {
        return "PaymentHistory";
    }

    @Override
    public String toString() {
        return "PaymentHistoryScore=" + paymentHistoryScore ;
    }
}
