package com.example.calculatorappjoshgrainge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    String oldEquationString = " ";
    String equationString = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        UpdateScreenText();
    }

    // Adds equations string value to current equation string
    void AddToEquationString(String num){
        // Clear string when only zero is in the screen text
        if(equationString.length() == 1 && equationString.contains("0") && num != ".")
            equationString = "";

        equationString += num;
    }

    void ClearScreenStrings() {
        oldEquationString = "";
        equationString = "0";
    }

    void OutputResults(String outputText){
        oldEquationString = equationString + " =";
        equationString = outputText;
        UpdateScreenText();
    }

    void UpdateScreenText() {
        // Old equation screen
        TextView oldEquationTextView = findViewById(R.id.old_equation);
        oldEquationTextView.setText(String.valueOf(oldEquationString));
        // Output screen
        TextView screenText = findViewById(R.id.screen_text);
        screenText.setText(String.valueOf(equationString));
    }

    void SeparateParentheses() {

        boolean writingParenetheses = false;
        List<String> statements = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < equationString.length(); i++){

            if(equationString.charAt(i) == '('){

                if(writingParenetheses)
                    index++;
                else if(!writingParenetheses && index > 0)
                    index = statements.size();

                writingParenetheses = true;
                statements.add("");
            }
            if(writingParenetheses)
            {
                String s = statements.get(index);
                s += equationString.charAt(i);
                statements.set(index, s);

                if(equationString.charAt(i) == ')') {
                    Log.d("Replace", statements.get(index));
                    equationString = equationString.replace(statements.get(index), Compute(statements.get(index)));
                    index--;
                    if(index < 0){
                        writingParenetheses = false;
                        index = 0;
                    }
                }

            }
        }

        Log.d("After parentheses:", equationString);

        // This is to test parentheses strings
        /*
        String outputTest = "";
        for (String s: matchList) {
            outputTest += s + ", ";
        }

        Log.d("Strings: ", outputTest);
         */

        //List<String> newValues = new ArrayList<>();
        //for (int i = matchList.size()-1; i >= 0; i--){
        //    Compute(matchList.get(i));
        //}
    }

    String Compute(String equation) {
        String[] strings = equationString.split("\\s+");
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
                    OutputResults("Factorial calculations must be done on a single integer value");
                }
                break;
            case "^2":
                output = Double.parseDouble(strings[0]) * Double.parseDouble(strings[0]);
                break;
            case "|x|":
                output = Math.abs(Double.parseDouble(strings[0]));
                break;
        }

        OutputResults(String.valueOf(output));
        return "";
    }

    int Factorial(double value){
        int sum = (int)value;
        for(int i = (int)value - 1; i > 0; i--){
            sum *= i;
        }

        return sum;
    }

    boolean IsNumber(String s){

//        try{
//            Double.parseDouble(s);
//            return true;
//        }catch (NumberFormatException){
//            return false;
//        }

        return false;
    }

    boolean IsOperator(String s){
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(s);
        return m.find();
    }

    String infixToPostfix(String infix) {
        /* To find out the precedence, we take the index of the
           token in the ops string and divide by 2 (rounding down).
           This will give us: 0, 0, 1, 1, 2 */
        final String ops = "-+/*^";

        StringBuilder sb = new StringBuilder();
        Stack<Integer> s = new Stack<>();

        for (String token : infix.split("\\s")) {
            if (token.isEmpty())
                continue;
            char c = token.charAt(0);
            int idx = ops.indexOf(c);

            // check for operator
            if (idx != -1) {
                if (s.isEmpty())
                    s.push(idx);

                else {
                    while (!s.isEmpty()) {
                        int prec2 = s.peek() / 2;
                        int prec1 = idx / 2;
                        if (prec2 > prec1 || (prec2 == prec1 && c != '^'))
                            sb.append(ops.charAt(s.pop())).append(' ');
                        else break;
                    }
                    s.push(idx);
                }
            }
            else if (c == '(') {
                s.push(-2); // -2 stands for '('
            }
            else if (c == ')') {
                // until '(' on stack, pop operators.
                while (s.peek() != -2)
                    sb.append(ops.charAt(s.pop())).append(' ');
                s.pop();
            }
            else {
                sb.append(token).append(' ');
            }
        }
        while (!s.isEmpty())
            sb.append(ops.charAt(s.pop())).append(' ');
        return sb.toString();
    }

    String ComputeInfixEquation(String equation) {
        String[] sArr = equation.split("\\s");

        Stack<String> strings = new Stack<>();

        for (int i = sArr.length-1; i >= 0; i--) {
            strings.push(sArr[i]);
        }

        double output = 0.0;

        while(!strings.isEmpty()){
            double x = Double.parseDouble(strings.pop());
            if(strings.isEmpty())
                return String.valueOf(x);

            double y = 0;
            // Only update when the next value is a number (Not when its an exponent pretty much)
            if(!IsOperator(strings.peek())) {
                y = Double.parseDouble(strings.pop());
            }

            switch (strings.pop()) {
                case "+":
                    strings.add(String.valueOf(x + y));
//                    output += x + Double.parseDouble(sArr[i-1]);
                    break;
                case "-":
                    strings.add(String.valueOf(x - y));
//                    output += x - Double.parseDouble(sArr[i-1]);
                    break;
                case "*":
                    strings.add(String.valueOf(x * y));
//                    strings.add(String.valueOf(x * Double.parseDouble(strings.pop())));
//                    output += x * Double.parseDouble(sArr[i-1]);
                    break;
                case "/":
                    strings.add(String.valueOf(x / y));
//                    strings.add(String.valueOf(x / Double.parseDouble(strings.pop())));
//                    output += x / Double.parseDouble(sArr[i-1]);
                    break;
                case "^":
                    strings.add(String.valueOf(x*x));
                    //output += x * x;
                    break;
            }

            Log.d("Loop", strings.peek());

        }

        for (int i =0; i < sArr.length; i++) {



        }

        return "";
    }
}