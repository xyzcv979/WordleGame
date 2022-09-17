package com.example.wordlegame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val guess1 = findViewById<TextView>(R.id.guess1)
        val guess2 = findViewById<TextView>(R.id.guess2)
        val guess3 = findViewById<TextView>(R.id.guess3)

        val guessCheck1 = findViewById<TextView>(R.id.guessCheck1)
        val guessCheck2 = findViewById<TextView>(R.id.guessCheck2)
        val guessCheck3 = findViewById<TextView>(R.id.guessCheck3)

        val input1 = findViewById<TextView>(R.id.input1)
        val input2 = findViewById<TextView>(R.id.input2)
        val input3 = findViewById<TextView>(R.id.input3)

        val check1 = findViewById<TextView>(R.id.check1)
        val check2 = findViewById<TextView>(R.id.check2)
        val check3 = findViewById<TextView>(R.id.check3)

        val matrix : Array<Array<TextView>> = arrayOf(
            arrayOf(guess1, guessCheck1, input1, check1),
            arrayOf(guess2, guessCheck2, input2, check2),
            arrayOf(guess3, guessCheck3, input3, check3)
        )

        val answer = findViewById<TextView>(R.id.answer)
        val enterButton = findViewById<Button>(R.id.button)
        val restartButton = findViewById<Button>(R.id.restartButton)
        val inputText = findViewById<EditText>(R.id.inputText)

        val answerWord = FourLetterWordList.getRandomFourLetterWord()
        val text = String.format("Answer: %s\n", answerWord)
        answer.text = text

        Log.i(answerWord, "answer word")

        var currentGuessCount = 0

        enterButton.setOnClickListener() {
            // Hides keyboard when pressing button
            val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

            val currWord = inputText.text.toString().uppercase()

            if(currWord.length != 4) { // validate user input == 4 letters
                Toast.makeText(this, "Enter a 4-letter word!", Toast.LENGTH_LONG).show()
                inputText.setText("")
                return@setOnClickListener // Like a continue
            }

            val textCorrectness = checkGuess(currWord, answerWord)

            inputText.setText("")

            // index current guess number
            // set guess word and check to visible, delay?
            // fill in input and check textviews
            val currArray = matrix[currentGuessCount]
            currArray[0].visibility = View.VISIBLE // guess

            currArray[1].visibility = View.VISIBLE // guessCheck

            currArray[2].text = currWord           // input
            currArray[2].visibility = View.VISIBLE

            currArray[3].text = textCorrectness    // check
            currArray[3].visibility = View.VISIBLE

            // if guess was correct
            // block text input and button
            // show answer and congrats on getting it
            if(currWord == answerWord) {
                Toast.makeText(this, "Congrats!", Toast.LENGTH_LONG).show()
                enterButton.isClickable = false
                inputText.isEnabled = false
                answer.visibility = View.VISIBLE
                restartButton.visibility = View.VISIBLE
                restartButton.isClickable = true
            }

            currentGuessCount++
            if(currentGuessCount == 3) { // no more guesses
                // block textinput and can't click button
                // show answer
                // reset button?
                enterButton.isClickable = false
                inputText.isEnabled = false
                answer.visibility = View.VISIBLE
                restartButton.visibility = View.VISIBLE
                restartButton.isClickable = true
            }
        }

        // Restarts application upon click
        restartButton.setOnClickListener() {
            val i = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
            finish()
        }
    }

    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String of 'O', '+', and 'X', where:
     *   'O' represents the right letter in the right place
     *   '+' represents the right letter in the wrong place
     *   'X' represents a letter not in the target word
     */
    private fun checkGuess(guess: String, wordToGuess : String) : String {
        var result = ""
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result += "O"
            } else if (guess[i] in wordToGuess) {
                result += "+"
            } else {
                result += "X"
            }
        }
        return result
    }
}