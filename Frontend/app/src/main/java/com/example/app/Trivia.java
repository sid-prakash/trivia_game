package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


public class Trivia extends Activity {
    Button btnLobbyESC;
    RadioButton rbOne, rbTwo, rbThree, rbFour;
    TextView txtQuestion, txtTimer, txtScore;
    Bundle extras;
    Question questions[];
    int number;
    int points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        extras = getIntent().getExtras();
        questions = (Question[])extras.getSerializable("triviaQ");
        number = extras.getInt("currentQ");
        points = extras.getInt("score");

        btnLobbyESC = findViewById(R.id.trivia_leaveTrivia);
        rbOne = findViewById(R.id.radioButton);
        rbTwo = findViewById(R.id.radioButton2);
        rbThree = findViewById(R.id.radioButton3);
        rbFour = findViewById(R.id.radioButton4);
        txtQuestion = findViewById(R.id.triviaQuestionText);
        txtTimer = findViewById(R.id.timer);
        txtScore = findViewById(R.id.score);

        txtQuestion.setText("Question "+ (number + 1) + ":" + ((MCQuestion) questions[number]).questionText);
        rbOne.setText(((MCQuestion) questions[number]).optA);
        rbTwo.setText(((MCQuestion) questions[number]).optB);
        rbThree.setText(((MCQuestion) questions[number]).optC);
        rbFour.setText(((MCQuestion) questions[number]).optD);
        txtScore.setText(""+ points);

        //JOIN GAME (using new format)
        btnLobbyESC.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
        });

        new CountDownTimer(10000, 1000){
            @Override
            public void onTick(long remaining) {
                txtTimer.setText("" + remaining / 1000);
            }

            @Override
            public void onFinish() {
                switch(((MCQuestion) questions[number]).correct){
                    case 1:
                        if(rbOne.isChecked()) points++;
                        break;
                    case 2:
                        if(rbTwo.isChecked()) points++;
                        break;
                    case 3:
                        if(rbThree.isChecked()) points++;
                        break;
                    case 4:
                        if(rbFour.isChecked()) points++;
                }
                Intent intent;
                if(number + 1 < questions.length){
                    intent = questions[number + 1].prepareActivity(getApplicationContext());
                    intent.putExtra("triviaQ", questions);
                    intent.putExtra("currentQ", number + 1);
                    intent.putExtra("score", points);
                }
                else{
                    intent = new Intent(getApplicationContext(), Dashboard.class);
                }
                startActivity(intent);
            }
        }.start();

    }
}