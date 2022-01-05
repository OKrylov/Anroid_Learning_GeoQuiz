package com.bignerdranch.android.geomain

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )

    private val answeredQuestions = HashSet<Question>()

    private var currentIndex = 0
    private var rightAnswers = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        trueButton.setOnClickListener { checkAnswer(true) }
        falseButton.setOnClickListener { checkAnswer(false) }

        nextButton.setOnClickListener { goToNextQuestion() }
        backButton.setOnClickListener { goToPreviousQuestion() }
        questionTextView.setOnClickListener { goToNextQuestion() }

        updateQuestion()
    }

    private fun goToPreviousQuestion() {
        currentIndex = if (currentIndex == 0) questionBank.lastIndex else currentIndex - 1
        updateQuestion()
    }

    private fun goToNextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
        updateQuestion()
    }

    private fun initViews() {
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        backButton = findViewById(R.id.back_button)
        questionTextView = findViewById(R.id.question_text_view)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        answeredQuestions.add(questionBank[currentIndex])
        updateAnswerButtons()
        val currentAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == currentAnswer) {
            rightAnswers++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        showToast(messageResId)
    }

    private fun updateQuestion() {
        if (answeredQuestions.size == questionBank.size) {
            val finishPhrase = getString(R.string.finish_phrase, (rightAnswers * 100 / questionBank.size))
            showToast(finishPhrase)
        } else {
            val questionTextResId = questionBank[currentIndex].textResId
            questionTextView.setText(questionTextResId)
            updateAnswerButtons()
        }
    }

    private fun updateAnswerButtons() {
        val isEnabled = (questionBank[currentIndex] !in answeredQuestions)
        falseButton.isEnabled = isEnabled
        trueButton.isEnabled = isEnabled
    }

    private fun showToast(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(this, textId, duration)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 240)
        toast.show()
    }

    private fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(this, text, duration)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 240)
        toast.show()
    }
}