package com.example.triviaapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviaapp.Data.AnswerListAsyncResponse;
import com.example.triviaapp.Data.QuestionBank;
import com.example.triviaapp.Model.Question;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static android.provider.Telephony.BaseMmsColumns.MESSAGE_ID;

public class MainActivity extends AppCompatActivity {

  private TextView HighScore;
  private TextView questionText;
  private TextView questionCounter;
  private Button buttonTrue;
  private Button buttonFalse;
  private int currentIndex = 0;
  private List<Question> questionList;
  CardView cardView2;
  private TextView scoreText;
  private int score ;
  private int highScoreVar;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    questionText = findViewById(R.id.questionText);
    questionCounter = findViewById(R.id.counter);
    buttonFalse = findViewById(R.id.btnFalse);
    buttonTrue = findViewById(R.id.btnTrue);
    cardView2 = findViewById(R.id.cardView);
    scoreText  = findViewById(R.id.Scoretext);
    HighScore = findViewById(R.id.HighScoreText);

    Animation fadeButton = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadein);
    buttonTrue.setAnimation(fadeButton);
    buttonFalse.setAnimation(fadeButton);
    cardView2.setAnimation(fadeButton);

    SharedPreferences getScoreData = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
    String value = getScoreData.getString("score","0");
    HighScore.setText(value);
    highScoreVar = (Integer.parseInt(value));



     questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
      @Override
      public void processFinished(ArrayList<Question> questionArrayList) {
        questionText.setText(questionArrayList.get(currentIndex).getAnswer());
        questionCounter.setText(MessageFormat.format("{0} out of {1}", currentIndex, questionArrayList.size()));
        Log.d("inside", "processFinished: " + questionArrayList);

      }
    });
//    Log.d("Main", "onCreate: " + questionList);
  }
  public void TrueButton(View v){
    Animation bounceButton = AnimationUtils.loadAnimation(MainActivity.this,R.anim.blink_anim);
    buttonTrue = findViewById(R.id.btnTrue);
    checkAnswer(true);
    buttonTrue.setAnimation(bounceButton);
    updateQuestion();
  }
  public void FalseButton(View v){
    Animation bounceButton = AnimationUtils.loadAnimation(MainActivity.this,R.anim.blink_anim);
    buttonFalse = findViewById(R.id.btnFalse);
    checkAnswer(false);
    buttonFalse.setAnimation(bounceButton);
    updateQuestion();
  }
  private void checkAnswer(boolean userChose){
    boolean answerTrue = questionList.get(currentIndex).isAnswerTrue();
    int toastMessage = 0;

    if(userChose == answerTrue){
      score = (score + 1);
      FadeIn();
      toastMessage = R.string.answer_true;
      Toast.makeText(MainActivity.this,toastMessage,Toast.LENGTH_SHORT).show();

      nextQuestion();
    }else{
      shakeAnimation();
      toastMessage = R.string.answer_false;
      Toast.makeText(MainActivity.this,toastMessage,Toast.LENGTH_SHORT).show();

    }


  }

  public void nextQuestion(){
    currentIndex = (currentIndex + 1) % questionList.size();
    updateQuestion();

  }

  private void updateQuestion() {
    String question = questionList.get(currentIndex).getAnswer();
    questionText.setText(question);
    scoreText.setText( String.valueOf(score));
    if (score > highScoreVar){
      saveLocal();
    }

    questionCounter.setText(currentIndex + " out of " + questionList.size());
  }
  private void shakeAnimation(){
    Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
    final CardView cardView = findViewById(R.id.cardView);
    cardView.setAnimation(shake);
    shake.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        cardView.setCardBackgroundColor(Color.RED);
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        cardView.setCardBackgroundColor(Color.WHITE);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });
  }

  private void FadeIn(){
    final CardView cardView = findViewById(R.id.cardView);
    Animation fade= AnimationUtils.loadAnimation(MainActivity.this,R.anim.blink_anim);
    cardView.setAnimation(fade);
    fade.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        cardView.setCardBackgroundColor(Color.GREEN);
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        cardView.setCardBackgroundColor(Color.WHITE);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });
  }

  private void saveLocal(){
    String saveScore = scoreText.getText().toString().trim();
    SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("score",saveScore);
    editor.apply();
  }

}

