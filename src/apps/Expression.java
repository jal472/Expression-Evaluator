package apps;

import java.io.*;
import java.util.*;

import structures.Stack;

public class Expression {

	/**
	 * Expression to be evaluated
	 */
	String expr;                
    
	/**
	 * Scalar symbols in the expression 
	 */
	ArrayList<ScalarSymbol> scalars;   
	
	/**
	 * Array symbols in the expression
	 */
	ArrayList<ArraySymbol> arrays;
    
    /**
     * String containing all delimiters (characters other than variables and constants), 
     * to be used with StringTokenizer
     */
    public static final String delims = " \t*+-/()[]";
    
    /**
     * Initializes this Expression object with an input expression. Sets all other
     * fields to null.
     * 
     * @param expr Expression
     */
    public Expression(String expr) {
        this.expr = expr;
    }
    
    private boolean checkDuplicates(String ch){    //helper method to check for duplicates
    	for(int i=0; i<arrays.size(); i++){
    		if(arrays.get(i).name.equals(ch)){
    			return true;
    		}
    	}
    	for(int j=0; j<scalars.size();j++){
    		if(scalars.get(j).name.equals(ch)){
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * Populates the scalars and arrays lists with symbols for scalar and array
     * variables in the expression. For every variable, a SINGLE symbol is created and stored,
     * even if it appears more than once in the expression.
     * At this time, values for all variables are set to
     * zero - they will be loaded from a file in the loadSymbolValues method.
     */
    public void buildSymbols() {
        arrays = new ArrayList<ArraySymbol>();
        scalars = new ArrayList<ScalarSymbol>();

        int i=0;
        String ch = "";								
        while(i<expr.length()){
        	ch=ch+expr.charAt(i);
        	if(expr.charAt(i)== '['){
        		ch= ch+ "-";
        	}
        	i++;
        }
        
        StringTokenizer str = new StringTokenizer(ch, " \t*+-/()]~");
        while (str.hasMoreElements()){
            String token = str.nextToken();
            if (token.charAt(token.length()-1) == '['){
            	if(checkDuplicates(token)==false){
            	arrays.add(new ArraySymbol(token.substring(0, token.length()-1)));
            	}
            }else{
                if (!Character.isLetter(token.charAt(0))){
                    continue;
                }else{
                	if(checkDuplicates(token)==false){
                    scalars.add(new ScalarSymbol(token));
                	}
            }

            }


    }
    }
    
    /**
     * Loads values for symbols in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     */
    public void loadSymbolValues(Scanner sc) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String sym = st.nextToken();
            ScalarSymbol ssymbol = new ScalarSymbol(sym);
            ArraySymbol asymbol = new ArraySymbol(sym);
            int ssi = scalars.indexOf(ssymbol);
            int asi = arrays.indexOf(asymbol);
            if (ssi == -1 && asi == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                scalars.get(ssi).value = num;
            } else { // array symbol
            	asymbol = arrays.get(asi);
            	asymbol.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    asymbol.values[index] = val;              
                }
            }
        }
    }
    
    private float reverseStacks(Stack<Float> vars, Stack<String> ops){
 	   Stack<Float> vars2 = new Stack<>();
 	   Stack<String> ops2 = new Stack<>();

 		   while(!ops.isEmpty()){									//reverse the ops stack
 			   String ch = ops.pop();
 			   ops2.push(ch); 
 		   }
 		   while(!vars.isEmpty()){									//reverse the var stack
 			   vars2.push(vars.pop()); 
 		   }
 		   return doMath(vars2, ops2);	// return the solved stack from do math
 	   
    }
    
    
    private float doMath(Stack<Float> vars, Stack<String> ops){
    Stack<Float> vars2=new Stack<Float>();
    Stack<String> ops2=new Stack<String>();
    
   while(!ops.isEmpty()){
	   if(ops.peek().equals("*")){
		   float a=vars.pop();
		   float b=vars.pop();
		   vars.push(a*b);
		   ops.pop();
	   }else if(ops.peek().equals("/")){
		   float a=vars.pop();
		   float b=vars.pop();
		   vars.push(a/b);  
		   ops.pop();
	   }else if(ops.peek().equals("+")||ops.peek().equals("-")){
		vars2.push(vars.pop());
		ops2.push(ops.pop());
	   }
   }
   // must move last number in vars to vars 2
	  if(vars2.isEmpty()){
		  return vars.pop();
	  }
	  vars2.push(vars.pop());
   
   while(!ops2.isEmpty()){
	   if(ops2.peek().equals("+")){
		   float a=vars2.pop();
		   float b=vars2.pop();
		   vars2.push(a+b);
		   ops2.pop();
	   }else{
		   float a=vars2.pop();
		   float b=vars2.pop();
		   vars2.push(b-a);
		   ops2.pop();
	   }
   }
   return vars2.pop();
    }
 	
    
    /**
     * Evaluates the expression, using RECURSION to evaluate subexpressions and to evaluate array 
     * subscript expressions.
     * 
     * @return Result of evaluation
     */
    public float evaluate() {
  	   Stack<Float> var = new Stack<Float>();
  	   Stack<String> ops = new Stack<String>();
 	   Stack<String> arr = new Stack<String>();
 	   StringTokenizer str = new StringTokenizer(expr, "-+()[]*/",true);
 	   String token = null;
 	   String temp = expr;
 	   
 	   while(str.hasMoreTokens()){
 		   token = str.nextToken();
 		   token = token.trim();
 		   int length = token.length();
 		   if(Character.isLetter(token.charAt(0))){								//if its a letter push on to the current stack
 			   for(int i =0;i<scalars.size();i++){
 				   if(scalars.get(i).name.equals(token)){
 					   int val = scalars.get(i).value;
 					   float val2 = (float)val;
 					   var.push(val2);
 					   temp = temp.substring(length);
 				   }
 			   }
 			   for(int i = 0;i<arrays.size();i++){					//if its an array pop name to the array stack
 				   if(arrays.get(i).name.equals(token)){
 					   arr.push(token);
 					   temp = temp.substring(length);
 				   }
 			   }
 			   										// increment the temp expression
 		   }else if(Character.isDigit(token.charAt(0))){						   //if its a number push on to the current stack
 			   var.push(Float.parseFloat(token));
 			   temp = temp.substring(length);
 		   }else if(token.charAt(0) == '*' || token.charAt(0)=='/'|| token.charAt(0)=='+'|| token.charAt(0)=='-'){		//if its an operations store into the operation stack
 			   ops.push(token);
 			   temp = temp.substring(length);
 		   }else if(token.charAt(0) == '('){											//opening bracket need to recursive call
 			   temp = temp.substring(length);
 			   /*this new tokenizer is to loop through the temp string
 			    * it then adds each token to a new string
 			    * the loop ends when there are an equal amount of open and close brackets
 			    * it then calls evaluate on the new string without the first and last ().
 			    */
 			   StringTokenizer str2 = new StringTokenizer(temp, "+-[]()*/",true);
 			   String token2 = str2.nextToken();
 			   int open = 1;
 			   int close = 0;
 			   String build = "(";
 			   while(open != close){
 				   if(token2.charAt(0)=='('){
 					   open++;
 					   build+="(";
 					   token2 = str2.nextToken();
 				   }else if(token2.charAt(0)==')'){
 					   close++;
 					   build+=")";
 					   if(open == close){
 						   break;
 					   }
 					   token2 = str2.nextToken();
 				   }else{
 					   build+=token2;
 					   token2 = str2.nextToken();
 				   }
 			   }			   
 			   int front = build.lastIndexOf(")");
 			   length = build.length();
 			   temp = temp.substring(length-1);
 			   build = build.substring(1, build.length()-1);			   
 			   Expression exp = new Expression(build);
 			   exp.arrays = arrays;
 			   exp.scalars = scalars;
 			   float next = exp.evaluate();
 			   var.push(next);
 			   while(front>0){	
 	   			   token = str.nextToken();
 				   front--;
 			   }
 		   }else if(token.charAt(0) == '['){
 			   temp = temp.substring(length);
 			   StringTokenizer str2 = new StringTokenizer(temp, "+-[]()*/",true);
 			   String token2 = str2.nextToken();
 			   int open = 1;
 			   int close = 0;
 			   String build = "[";
 			   while(open != close){
 				   if(token2.charAt(0)=='['){
 					   open++;
 					   build+="[";
 					   token2 = str2.nextToken();
 				   }else if(token2.charAt(0)==']'){
 					   close++;
 					   build+="]";
 					   if(open == close){
 						   break;
 					   }
 					   token2=str2.nextToken();
 				   }else{
 					   build+=token2;
 					   token2 = str2.nextToken();
 				   }
 			   }
 			   int front = build.lastIndexOf(']');
 			   length = build.length()-1;
 			   temp = temp.substring(length);
 			   build = build.substring(1, build.length()-1);
 			   Expression exp = new Expression(build);
 			   exp.arrays = arrays;
 			   exp.scalars = scalars;
 			   float next = exp.evaluate();
 			   String name = arr.pop();
 			   for(int i = 0;i<arrays.size();i++){					//if its an array pop name to the array stack
 				   if(arrays.get(i).name.equals(name)){
 					   float val = (float)arrays.get(i).values[(int)next];
 					   var.push(val);
 					   break;
 				   }
 			   }
 			   
 			   while(front>0){	
 	   			   token = str.nextToken();
 				   front--;
 			   } 	
 			   															//if its not a var or op then it must be "[,],(,)"
 		   }
 		   //evaluate remaining stack
 		   
 	   }
 	   return reverseStacks(var, ops);
 	   
     }

    /**
     * Utility method, prints the symbols in the scalars list
     */
    public void printScalars() {
        for (ScalarSymbol ss: scalars) {
            System.out.println(ss);
        }
    }
    
    /**
     * Utility method, prints the symbols in the arrays list
     */
    public void printArrays() {
    		for (ArraySymbol as: arrays) {
    			System.out.println(as);
    		}
    }

}
