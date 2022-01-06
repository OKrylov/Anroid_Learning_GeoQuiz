package com.bignerdranch.android.geomain

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"


class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var questionTextView: TextView

    private val viewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, viewModel.currentIndex)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0

        initViews()

        trueButton.setOnClickListener { checkAnswer(true) }
        falseButton.setOnClickListener { checkAnswer(false) }

        nextButton.setOnClickListener { goToNextQuestion() }
        backButton.setOnClickListener { goToPreviousQuestion() }
        questionTextView.setOnClickListener { goToNextQuestion() }

        updateQuestion()
    }

    private fun goToPreviousQuestion() {
        viewModel.moveToBack()
        updateQuestion()
    }

    private fun goToNextQuestion() {
        viewModel.moveToNext()
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
        val currentAnswer = viewModel.currentQuestionAnswer
        val isAnswerCorrect = currentAnswer == userAnswer
        viewModel.markCurrentQuestionAsAnswered(isAnswerCorrect)
        updateAnswerButtons()

        (if (isAnswerCorrect) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }).apply {
            showToast(this)
        }
    }

    private fun updateQuestion() {
        questionTextView.setText(viewModel.currentQuestionText)
        updateAnswerButtons()
        if (viewModel.areAllQuestionsAnswered) {
            val finishPhrase = getString(R.string.finish_phrase, viewModel.answeredQuestionCountInPercent)
            showToast(finishPhrase)
        }
    }

    private fun updateAnswerButtons() {
        val isEnabled = !viewModel.isCurrentQuestionAlreadyAnswered
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