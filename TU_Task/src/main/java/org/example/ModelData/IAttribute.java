package org.example.ModelData;

import java.io.Serializable;

public interface IAttribute extends Serializable {

    Double score = Double.MIN_NORMAL;

    Double calculateScore(int score);

    Double getScore();

    String getName();
}
