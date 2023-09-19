package com.example.assignment5_q5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var operand1: EditText = findViewById(R.id.operand1EditText)
        var operand2: EditText = findViewById(R.id.operand2EditText)

        operand1.setText("0")
        operand2.setText("0")

        operand1.addTextChangedListener(decimalLookout)
        operand1.filters = arrayOf(decimalFilter)
        operand2.addTextChangedListener(decimalLookout)
        operand2.filters = arrayOf(decimalFilter)

        var spinner: Spinner = findViewById(R.id.operationSpinner)
        val operations = resources.getStringArray(R.array.operations)
        val adapter = ArrayAdapter(this, R.layout.activity_main, operations)
        lateinit var selectedOperation: String
        spinner.adapter = adapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                selectedOperation = operations[position]

                }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }



        var calculateButton: Button = findViewById(R.id.calculateButton)

        calculateButton.setOnClickListener {
            try{
                calculate(selectedOperation, operand1, operand2)
            } catch (e: ArithmeticException) {
                Toast.makeText(getApplicationContext(), "integer overflow", Toast.LENGTH_SHORT).show()
            }

            operand1.setText("0")
            operand2.setText("0")
        }





    }

    private fun calculate(s:String, operand1: EditText, operand2: EditText){

        if (s == "+"){
            Toast.makeText(getApplicationContext(), (operand1.toString().toFloat() + operand2.toString().toFloat()).toString(), Toast.LENGTH_SHORT).show()
        }

        if (s == "-"){
            Toast.makeText(getApplicationContext(), (operand1.toString().toFloat() - operand2.toString().toFloat()).toString(), Toast.LENGTH_SHORT).show()
        }

        if (s == "*"){
            Toast.makeText(getApplicationContext(), (operand1.toString().toFloat() * operand2.toString().toFloat()).toString(), Toast.LENGTH_SHORT).show()
        }

        if (s == "/"){
            if (operand2.toString().toFloat() == (0).toFloat()){
                Toast.makeText(getApplicationContext(), "division by zero error", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(getApplicationContext(), (operand1.toString().toFloat() + operand2.toString().toFloat()).toString(), Toast.LENGTH_SHORT).show()
            }
        }

        if (s == "%"){
            Toast.makeText(getApplicationContext(), (operand1.toString().toFloat() % operand2.toString().toFloat()).toString(), Toast.LENGTH_SHORT).show()
        }

    }


    //sourced from Java code found at https://stackoverflow.com/questions/3349121/how-do-i-use-inputfilter-to-limit-characters-in-an-edittext-in-android
    private var decimalFilter: InputFilter =
        InputFilter { input, start, end, dest, dstart, dend ->
            if (!canAddDecimal and input.contains('.')){
                ""
            } else {
                input
            }
        }

    private var canAddDecimal = true

    //using text watcher to be 'smart' and avoid multiple decimals when inputting a number
    //from keyboard
    //source: https://www.geeksforgeeks.org/how-to-implement-textwatcher-in-android/

    private fun dropFirstChar(e: Editable){
        e.delete(0,1)
    }

    private val decimalLookout: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            canAddDecimal = !s.contains('.')

            if (s.isEmpty()){
                s.append("0")
            } else {
                if (s.first() == '0' && s.length == 2 && s.last() != '.'){
                    dropFirstChar(s)
                }
            }


        }
    }
}