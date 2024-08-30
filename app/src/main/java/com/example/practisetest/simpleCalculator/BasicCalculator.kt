package com.example.practisetest.simpleCalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practisetest.R

class BasicCalculator : AppCompatActivity() {
    private lateinit var display: TextView
    private var currentExpression: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_calculator)

        display = findViewById(R.id.tvDisplay)
    }

    fun onDigit(view: View) {
        val button = view as Button
        currentExpression += button.text // Append digit to expression
        display.text = currentExpression
    }

    fun onOperator(view: View) {
        val button = view as Button
        // Only append operator if the last character is not an operator
        if (currentExpression.isNotEmpty() && !isOperator(currentExpression.last())) {
            currentExpression += button.text
            display.text = currentExpression
        }
    }

    fun onClear(view: View) {
        currentExpression = "" // Clear the expression
        display.text = "0"
    }

    fun onEqual(view: View) {
        if (currentExpression.isNotEmpty()) {
            try {
                // Evaluate the expression and display the result
                val result = evaluateSimpleExpression(currentExpression)
                display.text = result.toString()
                currentExpression = result.toString()
            } catch (e: Exception) {
                display.text = "Error"
                currentExpression = ""
            }
        }
    }

    private fun isOperator(char: Char): Boolean {
        return char == '+' || char == '-' || char == '*' || char == '/'
    }

    private fun evaluateSimpleExpression(expression: String): Double {
        val tokens = expression.split("(?<=[-+*/])|(?=[-+*/])".toRegex())
        var result = tokens[0].toDouble()
        var i = 1
        while (i < tokens.size) {
            val operator = tokens[i]
            val nextValue = tokens[i + 1].toDouble()
            when (operator) {
                "+" -> result += nextValue
                "-" -> result -= nextValue
                "*" -> result *= nextValue
                "/" -> result /= nextValue
            }
            i += 2
        }
        return result
    }
}