/*
 * Name: Jayden
 * Date: Sept 25, 2024
 * Skills: working with swing, string manipulation, debugging skills
 * Description: Creates the frame for the calculator
 */

/* 
 * 	NOTE:
 * 
 * This calculator cannot divide multiple times in a row, due to
 * inaccuracies with scientific notation
 * it also assumes division by zero is Infinity
 * 
 * This is a very incredibly inefficient program
 * supports operations with bedmas
 * 
 */

// import all the packages
import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.*;

// create the class which extends (uses) JFrame
public class CalculatorFrame extends JFrame{

	// create a delimeter to seperate the numbers
	static char delimeter = '#';
	static char end_of_line = ',';
	
	// create some variables that we
	// will use throughout the program
	String expression = "";
	// output button
	// since I prefer the rounded courners and the
	// consistent look of a button over a label
	JButton TextJLabel = new JButton("0");

	// if you want to see some debugging stuff
	// such as the true value of "expression"
	// then set to true
	static boolean debug = true;

	// create a method that takes in a string, then 
	// gets the next two numbers and returns the calculated
	// answer for them
	public static double next_two_nums(String expression, int start_index) {

		String expr;
		expression = expression.substring(start_index);
		int end_index = expression.indexOf(delimeter);
		// set the substring of the original expression
		// to the number
		expr = expression.substring(0, end_index - 1);
		double first_num = Double.valueOf(expr);
		// check what operation to perform
		char operation = expression.charAt(end_index - 1);
		
		// Same thing twice
		expr = expression.substring(end_index + 1);
		int end_index2 = end_index + expr.indexOf(delimeter);
		// keep track of the indicies for later, and note the second_num
		expr = expression.substring(end_index + 1, end_index2);
		double second_num = Double.valueOf(expr);

		// check which operation is to be performed, then return the result
		if ((char)operation == '^'){return Math.pow(first_num, (double)second_num);}
		if ((char)operation == '÷'){return first_num / (double)second_num;}
		if ((char)operation == '*'){return first_num * second_num;}
		if ((char)operation == '+'){return first_num + second_num;}
		if ((char)operation == '-'){return first_num - second_num;}
		// theoretically should never return -1
		return -1;
	}
	
	
	// method that parses a string and returns an answer
	public static String calculate(String expression) {

		if (debug) System.out.println(expression);
		// create some variables to use later
		int i = 0;
		int prev_number_end = 0;
		int next_num_start = 0;
		// loop over the expression, then preform operations in the order bedmas
		// (exponents first, then division and multiplication, finally addition and subtraction)
		while(i < expression.length()){
			// check if the operation is exponent
			if (expression.charAt(i) == '^'){
				// create a new string that is result of the next two numbers
				String new_expression = String.valueOf(next_two_nums(expression, prev_number_end));
				// find where the next number ends
				next_num_start = i+3+expression.substring(i+2).indexOf(delimeter);
				// if there is no number after this one, it means it is the last one.
				// this means we add an "end_of_line" character that just indicates that
				// it is the last character
				if (next_num_start >= expression.length()){
					expression = expression.substring(0, prev_number_end) + new_expression + end_of_line + delimeter;
				} else {
					// otherwise it is not the last operation, therefore we dont add any characters
					// the delimeter is automatically added as the next number has one
					expression = expression.substring(0, prev_number_end) + new_expression + expression.substring(next_num_start-2);
					// we have to reset the i value because
					// if the expression is 2.000000000000 + 5 + 7
					// it simplifies to 7.0 + 7 which means
					// that the i value (index of the operator)
					// is now out of bounds
					i = 0;
				}
			// otherwise, there is no operator, however we do check
			// to see if the character is a delimeter. this basically 
			// keeps track of the index of the first number, so later
			// we can replace the expression with one with our result
			// slotted in place
			// (simplified) example:
			// 5.6 * 3.3 - 6.1
			//   ^         ^
			//   |         |
			// prev end    |
			//      	next start
			} else if (expression.charAt(i) == delimeter){
				prev_number_end = i + 1;
			}
			// increment the counter
			i++;
		}
		
		// basically the same thing but 2 more times
		// for division and multiplication
		// and addition and subtraction
		i = 0; prev_number_end = 0; next_num_start = 0;
		while(i < expression.length()){
			if (expression.charAt(i) == '÷' || expression.charAt(i) == '*'){
				String new_expression = String.valueOf(next_two_nums(expression, prev_number_end));
				next_num_start = i+3+expression.substring(i+2).indexOf(delimeter);
				if (next_num_start >= expression.length()){
					expression = expression.substring(0, prev_number_end) + new_expression + end_of_line + delimeter;
				} else {
					expression = expression.substring(0, prev_number_end) + new_expression + expression.substring(next_num_start-2);
					i = 0;
				}				
			} else if (expression.charAt(i) == delimeter){
				prev_number_end = i + 1;
			}
			i++;
		}

		i = 0; prev_number_end = 0; next_num_start = 0;
		while(i < expression.length()){
			if (expression.charAt(i) == '+' || expression.charAt(i) == '-'){
				String new_expression = String.valueOf(next_two_nums(expression, prev_number_end));
				next_num_start = i+3+expression.substring(i+2).indexOf(delimeter);
				if (next_num_start >= expression.length()){
					expression = expression.substring(0, prev_number_end) + new_expression + end_of_line + delimeter;
				} else {
					expression = expression.substring(0, prev_number_end) + new_expression + expression.substring(next_num_start-2);
					i = 0;
				}		
			} else if (expression.charAt(i) == delimeter){
				prev_number_end = i + 1;
			}
			i++;
		}	
		// finally we return the simplified expression
		return expression;
	}	
	
	public CalculatorFrame() {
		
		// set the title
		setTitle("Calculator");
		
		// make sure to setLayout = null
		setLayout(null);

		// set the size of the frame
		setSize(600, 600);

		// create some fonts for late use
		Font outputFont = new Font("Times New Roman", Font.ITALIC, 48);
		Font buttonFont= new Font("Times New Roman", Font.PLAIN, 30);
		
		// set the color of the background
		Color textColor = new Color(0, 0, 0);
		Color bgColor = new Color(94, 125, 145);
		getContentPane().setBackground(bgColor);
		
		// create the label that shows the expression
		// add some color, and change some label properties
		TextJLabel.setForeground(textColor);
		TextJLabel.setBounds(50, 50, 500, 100);
		TextJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		// set the font of the output text field (which is a button for aesthetics)
		TextJLabel.setFont(outputFont);
		
		// create the buttons
		JButton JButton0 = new JButton("0");
		JButton JButton1 = new JButton("1");
		JButton JButton2 = new JButton("2");
		JButton JButton3 = new JButton("3");
		JButton JButton4 = new JButton("4");
		JButton JButton5 = new JButton("5");
		JButton JButton6 = new JButton("6");
		JButton JButton7 = new JButton("7");
		JButton JButton8 = new JButton("8");
		JButton JButton9 = new JButton("9");
		JButton JButtonP = new JButton("+");
		JButton JButtonS = new JButton("-");
		JButton JButtonM = new JButton("*");
		JButton JButtonD = new JButton("÷");
		JButton JButtonE = new JButton("^");
		JButton JButtonC = new JButton("CE");
		JButton JButtonBCS = new JButton("⌫");
		JButton JButtonDot = new JButton(".");
		JButton JButtonEnter = new JButton("=");
		
		// add some fonts to make the text larger
		JButton0.setFont(buttonFont);
		JButton1.setFont(buttonFont);
		JButton2.setFont(buttonFont);
		JButton3.setFont(buttonFont);
		JButton4.setFont(buttonFont);
		JButton5.setFont(buttonFont);
		JButton6.setFont(buttonFont);
		JButton7.setFont(buttonFont);
		JButton8.setFont(buttonFont);
		JButton9.setFont(buttonFont);
		JButtonP.setFont(buttonFont);
		JButtonS.setFont(buttonFont);
		JButtonM.setFont(buttonFont);
		JButtonD.setFont(buttonFont);
		JButtonE.setFont(buttonFont);
		JButtonC.setFont(buttonFont);
		JButtonBCS.setFont(buttonFont);
		JButtonDot.setFont(buttonFont);
		JButtonEnter.setFont(buttonFont);
		
		// set the boundaries
		JButton1.setBounds(150, 150, 100, 100);
		JButton2.setBounds(250, 150, 100, 100);
		JButton3.setBounds(350, 150, 100, 100);
		JButton4.setBounds(150, 250, 100, 100);
		JButton5.setBounds(250, 250, 100, 100);
		JButton6.setBounds(350, 250, 100, 100);
		JButton7.setBounds(150, 350, 100, 100);
		JButton8.setBounds(250, 350, 100, 100);
		JButton9.setBounds(350, 350, 100, 100);
		JButton0.setBounds(250, 450, 100, 100);
		JButtonP.setBounds(50, 150, 100, 100);
		JButtonS.setBounds(50, 250, 100, 100);
		JButtonM.setBounds(50, 350, 100, 100);
		JButtonD.setBounds(50, 450, 100, 100);
		JButtonE.setBounds(150, 450, 100, 100);
		JButtonC.setBounds(450, 250, 100, 100);
		JButtonDot.setBounds(350, 450, 100, 100);
		JButtonBCS.setBounds(450, 150, 100, 100);
		JButtonEnter.setBounds(450, 350, 100, 200);
		
		// add the buttons
		add(JButton0);
		add(JButton1);
		add(JButton2);
		add(JButton3);
		add(JButton4);
		add(JButton5);
		add(JButton6);
		add(JButton7);
		add(JButton8);
		add(JButton9);
		add(JButtonP);
		add(JButtonS);
		add(JButtonM);
		add(JButtonE);
		add(JButtonD);
		add(JButtonC);
		add(JButtonDot);
		add(JButtonEnter);
		add(JButtonBCS);
		add(TextJLabel);
		
		// add the actionlisteners
		JButton0.addActionListener(e -> buttonPressed(e));
		JButton1.addActionListener(e -> buttonPressed(e));
		JButton2.addActionListener(e -> buttonPressed(e));
		JButton3.addActionListener(e -> buttonPressed(e));
		JButton4.addActionListener(e -> buttonPressed(e));
		JButton5.addActionListener(e -> buttonPressed(e));
		JButton6.addActionListener(e -> buttonPressed(e));
		JButton7.addActionListener(e -> buttonPressed(e));
		JButton8.addActionListener(e -> buttonPressed(e));
		JButton9.addActionListener(e -> buttonPressed(e));
		JButtonP.addActionListener(e -> buttonPressed(e));
		JButtonS.addActionListener(e -> buttonPressed(e));
		JButtonM.addActionListener(e -> buttonPressed(e));
		JButtonE.addActionListener(e -> buttonPressed(e));
		JButtonD.addActionListener(e -> buttonPressed(e));
		JButtonC.addActionListener(e -> buttonPressed(e));
		JButtonDot.addActionListener(e -> buttonPressed(e));
		JButtonBCS.addActionListener(e -> buttonPressed(e));
		JButtonEnter.addActionListener(e -> buttonPressed(e));
		
		// show the window
		setVisible(true);
	}

	// helper method that parses the string for humans
	public String parse (String expr){
		// replace the unneccessary characters
		expr = expr.replace(String.valueOf(end_of_line), "");
		return expr.replace(String.valueOf(delimeter), "");
	}

	// method that handles the button presses and the logic behind it
	public void buttonPressed (ActionEvent event) {
		boolean set_overridable_zero = false;
		boolean set_custom_expression = false;
		String custom_expression = "";
		// update the label (yes technically a button) if the enter button is pressed
		if ((String.valueOf(event).substring(48, 49)).equals("=")) {
			if (expression.length() > 2){
				// if the expression has more than two characters, it means its not empty
				// which means we can check if it already ends in a (EOL + delimeter)
				// if it is we know is was the result of a previous calculation and we can
				// prevent adding two sets of (EOL + delimeter) chars
				if (!expression.substring(expression.length() - 2).equals(String.valueOf(end_of_line) + String.valueOf(delimeter))){
					expression = expression + end_of_line + delimeter;
				}
			}
			// calculate the expression
			// and add custom expression
			custom_expression = expression + "=" + calculate(expression);
			set_custom_expression = true;
			expression = calculate(expression);
			// Example expression - 23.37d#23.37#
		} else if ((String.valueOf(event).substring(48, 49)).equals("^") || (String.valueOf(event).substring(48, 49)).equals("÷") || (String.valueOf(event).substring(48, 49)).equals("*") || (String.valueOf(event).substring(48, 49)).equals("+") || (String.valueOf(event).substring(48, 49)).equals("-")) {
		// otherwise, if the button pressed was a operator button, then we add an operator character
		// to signify that we want to perform xyz
			if (expression.length() > 2){
				// again if it is greater than 2 then we can
				// refrain from adding a delimeter
				if (expression.substring(expression.length() - 2).equals(String.valueOf(end_of_line) + String.valueOf(delimeter))){
					expression = expression.substring(0, expression.length() - 2);
				}
			}
			// add on the operation
			expression += String.valueOf(event).substring(48, 49) + delimeter;
		} else if ((String.valueOf(event).substring(48, 49)).equals("C")) {
			// if the user pressed the CE (Clear) button then we set the 
			// expression to an empty string while also setting [set_overridable_zero]
			// to true. This allows the program to later set the text of the output
			// to be zero without changing the expression.
			// This allows us to have a zero when the expression is blank
			// but as soon as we type something then the zero is removed
			set_overridable_zero = true;
			expression = "";
		} else if ((String.valueOf(event).substring(48, 49)).equals("⌫")) {
			// yeah i know that when you backspace right after outputting an answer it doesnt work very
			// well, you have to press it twice but at least its like a confirmation, do you really want to backspace?
			if (expression.length() > 0){
				// if the character is an operator (+, -, *, /)
				// then there are two characters that we have to delete
				if (expression.charAt(expression.length() - 1) == delimeter){
					expression = expression.substring(0, expression.length() - 2);
				} else {
				// otherwise we just remove one
					expression = expression.substring(0, expression.length() - 1);
				}	
			}
		} else {
			// otherwise it is a number that we pressed
			if (expression.length() > 2){
				if (expression.substring(expression.length() - 2).equals(String.valueOf(end_of_line) + String.valueOf(delimeter))){
					expression = expression.substring(0, expression.length() - 2);
				}
			}
			expression += String.valueOf(event).substring(48, 49);
		}
		if (set_overridable_zero){
			// this creates an overridable zero
			// which means as soon as we press a button the zero disappears
			TextJLabel.setText("0");
		} else if (set_custom_expression) {
			// custom expression with equal sign when 
			// the user presses the calculate button
			TextJLabel.setText(parse(custom_expression));
		} else {
			// otherwise we output the correct information
			TextJLabel.setText(parse(expression));
		}
	}
}
