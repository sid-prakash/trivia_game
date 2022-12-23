package com.example.app;

import android.content.Context;
import android.content.Intent;

public class MCQuestion extends Question{

    public String questionText;
    public String optA;
    public String optB;
    public String optC;
    public String optD;

    public int correct;

    public MCQuestion(String text, String a, String b, String c, String d, int right){
        questionText = text;
        optA = a;
        optB = b;
        optC = c;
        optD = d;
        correct = right;
    }

    @Override
    public Intent prepareActivity(Context c) {
        return new Intent(c, Trivia.class);
    }
}
