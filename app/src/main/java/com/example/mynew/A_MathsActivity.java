package com.example.mynew;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;



public class A_MathsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView question,qCount,timer;
    private Button optionA, optionB, optionC, optionD;
    // private List<QuestionModel> questionList;
    ArrayList<A_AQuestionModel> questionList = new ArrayList<>();
    DatabaseReference databaseReference;
    private int queNum ;
    private CountDownTimer countDown;
    private int score;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        question = findViewById(R.id.question);
        qCount = findViewById(R.id.quest_num);
        timer = findViewById(R.id.countdown);

        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);


        optionA.setOnClickListener(this);
        optionB.setOnClickListener(this);
        optionC.setOnClickListener(this);
        optionD.setOnClickListener(this);

        getQuestionsList();

        score = 0;
    }

    private void getQuestionsList() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Biology");

        questionList.add(new A_AQuestionModel("The average of first 50 natural number is","25.30","25.5","25.00","12.25","2"));
        questionList.add(new A_AQuestionModel("The number of 3 digit number divisible by 6, is","149","166","150","151","3"));
        questionList.add(new A_AQuestionModel("Evaluation of 8^3*8^2*8^-5 is ","1","0","8","None of these","1"));
        questionList.add(new A_AQuestionModel("Factors of 9 are ","1,2,3","1,2,3,9","1,6,9","None of these","2"));
        questionList.add(new A_AQuestionModel("What is 999 times 100.0","199.0","999.0","9990","99900","4"));

        setQuestion();

    }
    private void setQuestion(){
        timer.setText(String.valueOf(10));

        question.setText(questionList.get(0).getQuestion());
        optionA.setText(questionList.get(0).getOptionA());
        optionB.setText(questionList.get(0).getOptionB());
        optionC.setText(questionList.get(0).getOptionC());
        optionD.setText(questionList.get(0).getOptionD());



        qCount.setText(String.valueOf(1)+"/"+String.valueOf(questionList.size()));
        statTimer();

        queNum = 1;

    }


    private void statTimer(){
        countDown = new CountDownTimer(12000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished < 10000)
                    timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };
        countDown.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        String selectedOption = "0";
        switch (v.getId()){
            case R.id.optionA:
                selectedOption = "1";
                break;

            case R.id.optionB:
                selectedOption ="2";
                break;

            case R.id.optionC:
                selectedOption="3";
                break;

            case R.id.optionD:
                selectedOption="4";
                break;

            default:

        }
        countDown.cancel();

        checkAnswer(selectedOption, v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(String selectedOption, View view) {
        if(selectedOption == questionList.get(queNum).getCorrectAns()){
            //Right Answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }
        else {
            //Wrong Answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));


            switch (questionList.get(queNum).getCorrectAns()){
                case "1":
                    optionA.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case "2":
                    optionB.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case "3":
                    optionC.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case "4":
                    optionD.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        }, 1500);

    }
    private void changeQuestion(){
        if(queNum < questionList.size() - 1){
            queNum++;

            playAnim(question, 0,0);
            playAnim(optionA, 0,1);
            playAnim(optionB, 0,2);
            playAnim(optionC, 0,3);
            playAnim(optionD, 0,4);


            qCount.setText(String.valueOf(queNum+1)+"/"+String.valueOf(questionList.size()));

            timer.setText(String.valueOf(10));
            statTimer();

        }
        else {
            //go to scores
            Intent intent = new Intent(A_MathsActivity.this, A_ScoreActivity.class);
            intent.putExtra("SCORE", String.valueOf(score) + "/"+String.valueOf(questionList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //Questions.this.finish();
        }
    }
    private void playAnim(View view, final int value, int viewNum){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value == 0){
                            switch (viewNum)
                            {
                                case 0:
                                    ((TextView)view).setText(questionList.get(queNum).getQuestion());
                                    break;
                                case 1:
                                    ((Button)view).setText(questionList.get(queNum).getOptionA());
                                    break;
                                case 2:
                                    ((Button)view).setText(questionList.get(queNum).getOptionB());
                                    break;
                                case 3:
                                    ((Button)view).setText(questionList.get(queNum).getOptionC());
                                    break;
                                case 4:
                                    ((Button)view).setText(questionList.get(queNum).getOptionD());
                                    break;
                            }

                            if (viewNum != 0){
                                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000B76")));
                            }

                            playAnim(view,1,viewNum);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        countDown.cancel();
    }
}