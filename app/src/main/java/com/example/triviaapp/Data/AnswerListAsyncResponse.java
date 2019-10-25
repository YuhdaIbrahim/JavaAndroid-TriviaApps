package com.example.triviaapp.Data;

import com.example.triviaapp.Model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
  void processFinished(ArrayList<Question> questionArrayList);
}
