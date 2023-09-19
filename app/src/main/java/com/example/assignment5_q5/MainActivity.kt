package com.example.assignment5_q5

import android.app.Activity
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


private var selectedOperation = ""
class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        selectedOperation = parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}


}

class MainActivity : AppCompatActivity(){

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



        //I used https://developer.android.com/develop/ui/views/components/spinner as a source


        val spinner: Spinner = findViewById(R.id.operationSpinner)
        spinner.onItemSelectedListener = SpinnerActivity()

        ArrayAdapter.createFromResource(this, R.array.operations, android.R.layout.simple_spinner_item).also{
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

        }




        var calculateButton: Button = findViewById(R.id.calculateButton)

        calculateButton.setOnClickListener {
            //Toast.makeText(getApplicationContext(), selectedOperation, Toast.LENGTH_SHORT).show()
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

        when (s) {
            "+" -> Toast.makeText(getApplicationContext(), (operand1.toString().toFloat() + operand2.toString().toFloat()).toString(), Toast.LENGTH_SHORT).show()
            "-" -> Toast.makeText(getApplicationContext(), (operand1.toString().toFloat() - operand2.toString().toFloat()).toString(), Toast.LENGTH_SHORT).show()
            "*" -> Toast.makeText(getApplicationContext(), (operand1.toString().toFloat() * operand2.toString().toFloat()).toString(), Toast.LENGTH_SHORT).show()
            "/" -> {
                if (operand2.toString().toFloat() == (0).toFloat()){
                    Toast.makeText(getApplicationContext(), "division by zero error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getApplicationContext(), (operand1.toString().toFloat() + operand2.toString().toFloat()).toString(), Toast.LENGTH_SHORT).show()
                }
            }
            "mod" -> Toast.makeText(getApplicationContext(), (operand1.toString().toFloat() % operand2.toString().toFloat()).toString(), Toast.LENGTH_SHORT).show()
        }

        return
    }

    private var canAddDecimal = true

    //sourced from Java code found at https://stackoverflow.com/questions/3349121/how-do-i-use-inputfilter-to-limit-characters-in-an-edittext-in-android
    private var decimalFilter: InputFilter =
        InputFilter { input, start, end, dest, dstart, dend ->
            if (!canAddDecimal and input.contains('.')){
                ""
            } else {
                input
            }
        }



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