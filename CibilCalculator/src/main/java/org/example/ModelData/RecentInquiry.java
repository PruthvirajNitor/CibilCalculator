package org.example.ModelData;

public class RecentInquiry implements IAttribute{

    private Double recentInquiryScore;

    public RecentInquiry(){
        this.recentInquiryScore = 0.0;
    };

    public RecentInquiry(int recentInquiryIndex )
    {
        this.recentInquiryScore = this.calculateScore(recentInquiryIndex);
    }

    @Override
    public Double calculateScore( int recentInquiryIndex) {
        //recentInquiryIndex = Credit Score Inquiry raised in last x months;
        return switch (recentInquiryIndex){
            case 0 -> 100.00;
            case  1-> 80.00;
            case 2 -> 60.00;
            case 3 -> 40.00;
            case 4 -> 30.00;
            default -> 0.00;
        };
    }

    @Override
    public Double getScore() {
        return this.recentInquiryScore;
    }

    @Override
    public String getName() {
        return "RecentInquiry";
    }

    @Override
    public String toString() {
        return "RecentInquiryScore= " + recentInquiryScore;
    }
}
