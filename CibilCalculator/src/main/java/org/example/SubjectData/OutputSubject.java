package org.example.SubjectData;

import org.example.ModelData.IAttribute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OutputSubject  {

    String userId;
    int cibilScore;
    String attributeScore;

    public OutputSubject(String userId, List<IAttribute> attributeSet) {
        this.userId = userId;
        this.cibilScore = (int) calCibilScore(attributeSet);
        this.attributeScore = getAttibuteNames(attributeSet);
    }

    private String getAttibuteNames(List<IAttribute> attributeSet) {
        StringBuilder attributeScores =  new StringBuilder();
        for(IAttribute attribute : attributeSet) {
            attributeScores.append(attribute.getName()).append(" score : ").append(attribute.getScore()).append(" , ");
        }
        return attributeScores.substring(0, attributeScores.length()-2);
    }

    private int calCibilScore(List<IAttribute> attributeSet) {

        // Get the count of attributes to be considered for cibil score as per model registry
        double cibilFactor = 9.0 /attributeSet.size();
        double cibilScore =0  ;
        for(IAttribute attribute : attributeSet) {
               cibilScore += attribute.getScore();
        }

        //e.g. if 3 attributes used for calc, cibil score will be out of 300 : convert it to out of 900;
        cibilScore *= cibilFactor;
        return (int) Math.max(cibilScore,300);
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCibilScore() {
        return cibilScore;
    }

    public void setCibilScore(int cibilScore) {
        this.cibilScore = cibilScore;
    }


    public String getAttributeScore() {
        return attributeScore;
    }

    public void setAttributeScore(String attributeScore) {
        this.attributeScore = attributeScore;
    }

    @Override
    public String toString() {
        return "OutputSubject{" +
                "userId='" + userId + '\'' +
                ", cibilScore=" + cibilScore +
                ", attributeScore='" + attributeScore + '\'' +
                '}';
    }
}
