package com.example.practisetest.simpleCalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practisetest.R

class SimpleCalculator : AppCompatActivity() {
    private lateinit var display: TextView
    private var lastNumeric: Boolean = false
    private var stateError: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_calculator)

        display = findViewById(R.id.tvDisplay)
        setNumericOnClickListener()
        setOperatorOnClickListener()
    }

    private fun setNumericOnClickListener() {
        val numericButtons = listOf<Button>(
            findViewById(R.id.btn0), findViewById(R.id.btn1),
            findViewById(R.id.btn2), findViewById(R.id.btn3),
            findViewById(R.id.btn4), findViewById(R.id.btn5),
            findViewById(R.id.btn6), findViewById(R.id.btn7),
            findViewById(R.id.btn8), findViewById(R.id.btn9)
        )

        for (button in numericButtons) {
            button.setOnClickListener {
                if (stateError) {
                    display.text = (button.text)
                    stateError = false
                } else {
                    display.append(button.text)
                }
                lastNumeric = true
            }
        }
    }

    private fun setOperatorOnClickListener() {
        val operatorButtons = listOf<Button>(
            findViewById(R.id.btnAdd), findViewById(R.id.btnSubtract),
            findViewById(R.id.btnMultiply), findViewById(R.id.btnDivide)
        )

        for (button in operatorButtons) {
            button.setOnClickListener {
                if (lastNumeric && !stateError) {
                    display.append(button.text)
                    lastNumeric = false
                    lastDot = false
                }
            }
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            display.text = ""
            lastNumeric = false
            stateError = false
            lastDot = false
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener {
            if (lastNumeric && !stateError && !lastDot) {
                display.append(".")
                lastNumeric = false
                lastDot = true
            }
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            onEqual()
        }
    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val text = display.text.toString()
            try {
                val result = evaluate(text)
                display.text = result.toString()
                lastDot = true
            } catch (e: Exception) {
                display.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

    private fun evaluate(expression: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < expression.length) expression[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expression.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when {
                        eat('+'.code) -> x += parseTerm()
                        eat('-'.code) -> x -= parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when {
                        eat('*'.code) -> x *= parseFactor()
                        eat('/'.code) -> x /= parseFactor()
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()

                var x: Double
                val startPos = pos
                if (eat('('.code)) {
                    x = parseExpression()
                    eat(')'.code)
                } else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) {
                    while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                    x = expression.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }

                return x
            }
        }.parse()
    }
}