/*
Name: Josh Grainge
Student Number: A00129117
Description: Assigns calculator buttons to listener function. Updates calculator screen text based
             on user input. Does calculations based on users input and outputs product to
             calculators screen.
 */

package com.example.calculatorappjoshgrainge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    String oldEquationString = " ";
    String equationString = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Update initial text view
        UpdateScreenText();

        // Assign buttons onclick
        // Numbers
        Button zero = findViewById(R.id.num0);
        Button one = findViewById(R.id.num1);
        Button two = findViewById(R.id.num2);
        Button three = findViewById(R.id.num3);
        Button four = findViewById(R.id.num4);
        Button five = findViewById(R.id.num5);
        Button six = findViewById(R.id.num6);
        Button seven = findViewById(R.id.num7);
        Button eight = findViewById(R.id.num8);
        Button nine = findViewById(R.id.num9);
        // Symbols
        Button plus = findViewById(R.id.plus);
        Button minus = findViewById(R.id.minus);
        Button multiply = findViewById(R.id.multiply);
        Button divide = findViewById(R.id.divide);
        Button absolute = findViewById(R.id.absolute);
        Button factorial = findViewById(R.id.factorial);
        Button modulo = findViewById(R.id.modulo);
        Button exponent = findViewById(R.id.exponent);

        Button decimal = findViewById(R.id.decimal);
        // Clear
        Button clear = findViewById(R.id.clear);
        // Equals
        Button equals = findViewById(R.id.equals);

        // Assign button listeners
        zero.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);

        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        multiply.setOnClickListener(this);
        divide.setOnClickListener(this);
        absolute.setOnClickListener(this);
        factorial.setOnClickListener(this);
        modulo.setOnClickListener(this);
        exponent.setOnClickListener(this);
        decimal.setOnClickListener(this);

        clear.setOnClickListener(this);

        equals.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        /* This was originally a switch statement, but was giving errors so I switched to
           this else if monstrosity */
        int id = v.getId();
        // Add math symbol
        if(id == R.id.plus){
            AddToEquationString(" + ");
        }
        else if(id == R.id.minus){
            AddToEquationString(" - ");
        }
        else if(id == R.id.multiply){
            AddToEquationString(" * ");
        }
        else if(id == R.id.divide){
            AddToEquationString(" / ");
        }
        else if(id == R.id.absolute){
            AddToEquationString(" |x| ");
        }
        else if(id == R.id.factorial) {
            AddToEquationString(" ! ");
        }
        else if(id == R.id.modulo){
            AddToEquationString(" % ");
        }
        else if(id == R.id.exponent){
            AddToEquationString(" ^2 ");
        }
        // Add decimal
        else if(id == R.id.decimal){
            AddToEquationString(".");
        }
        // Clear screen text
        else if(id == R.id.clear){
            ClearScreenStrings();
        }
        // Computer equation
        else if(id == R.id.equals){
            Compute(equationString);
        }
        // Add number value
        else
        {
            Button numButton = (Button)v;
            AddToEquationString(numButton.getText().toString());
        }

        // After button press update screens text
        UpdateScreenText();
    }

    // Adds equations string value to current equation string
    void AddToEquationString(String num){
        // Clear string when only zero is in the screen text
        if(equationString.length() == 1 && equationString.contains("0") && num != ".")
            equationString = "";

        equationString += num;
    }

    // Clear screen text strings
    void ClearScreenStrings() {
        oldEquationString = "";
        equationString = "0";
    }

    // Update string variables associated with "screen" text
    void UpdateEquationStrings(String outputText){
        oldEquationString = equationString + " =";
        equationString = outputText;
        UpdateScreenText();
    }

    // Update calculator "screen" text views
    void UpdateScreenText() {
        // Old equation screen
        TextView oldEquationTextView = findViewById(R.id.old_equation);
        oldEquationTextView.setText(String.valueOf(oldEquationString));
        // Output screen
        TextView screenText = findViewById(R.id.screen_text);
        screenText.setText(String.valueOf(equationString));
    }


    void Compute(String equation) {

        String[] strings = equationString.split("\\s+");

        boolean validOneSymbolEquation = false;
        // This is for when strings[1] doesn't exist
        try {
            validOneSymbolEquation = strings[1].equals("^2") || strings[1].equals("!") || strings[1].equals("|x|");
        }catch (ArrayIndexOutOfBoundsException e){

        }

        // Throw error when equation doesn't have valid components
        if(strings.length <= 2 && !validOneSymbolEquation) {
                UpdateEquationStrings("Invalid equation");
                return;
        }


        double output = 0.0;
        switch(strings[1])
        {
            case "+":
                output = Double.parseDouble(strings[0]) + Double.parseDouble(strings[2]);
                break;
            case "-":
                output = Double.parseDouble(strings[0]) - Double.parseDouble(strings[2]);
                break;
            case "*":
                output = Double.parseDouble(strings[0]) * Double.parseDouble(strings[2]);
                break;
            case "/":
                output = Double.parseDouble(strings[0]) / Double.parseDouble(strings[2]);
                break;
            case "%":
                output = Double.parseDouble(strings[0]) % Double.parseDouble(strings[2]);
                break;
            case "!":
                // Only do factorial calculation when number is an integer, else throw error
                try {
                    output = Factorial(Integer.parseInt(strings[0]));
                    break;
                }
                catch (NumberFormatException e) {
                    UpdateEquationStrings("Factorial calculations must be done on a single integer value");
                }
                break;
            case "^2":
                output = Double.parseDouble(strings[0]) * Double.parseDouble(strings[0]);
                break;
            case "|x|":
                output = Math.abs(Double.parseDouble(strings[0]));
                break;
        }

        // Make output integer or double
        if(output % 1 == 0) {
            UpdateEquationStrings(String.valueOf((int)output));
        }
        else
            UpdateEquationStrings(String.valueOf(output));
    }

    int Factorial(double value){
        int sum = (int)value;
        for(int i = (int)value - 1; i > 0; i--){
            sum *= i;
        }

        return sum;
    }

    boolean IsOperator(String s){
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(s);
        return m.find();
    }
}