package com.bignerdranch.android.geomain

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )

    private val answeredQuestions = HashSet<Question>()

    private var rightAnswers = 0
    var currentIndex = 0


    val answeredQuestionCountInPercent: Int
        get() = rightAnswers * 100 / questionBank.size

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val isCurrentQuestionAlreadyAnswered: Boolean
        get() = questionBank[currentIndex] in answeredQuestions

    val areAllQuestionsAnswered: Boolean
        get() = questionBank.size == answeredQuestions.size

    fun markCurrentQuestionAsAnswered(isRightAnswer: Boolean) {
        answeredQuestions.add(questionBank[currentIndex])
        if (isRightAnswer) {
            rightAnswers++
        }
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToBack() {
        currentIndex = if (currentIndex == 0) questionBank.lastIndex else currentIndex - 1
    }


}