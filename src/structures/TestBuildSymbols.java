package structures;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class TestBuildSymbols {
	
	public Stack() {
		items = new ArrayList<T>();
	}
	
	public void push(T item) {
		items.add(item);
	}

	/**
	 * Pops item at top of stack and returns it.
	 * 
	 * @return Popped item.
	 * @throws NoSuchElementException If stack is empty.
	 */
	public T pop() 
	throws NoSuchElementException {
		if (items.isEmpty()) {
			//return null;
			throw new NoSuchElementException("can't pop from an empty stack");
		}
		return items.remove(items.size()-1);
	}

	/**
	 * Returns item on top of stack, without popping it.
	 * 
	 * @return Item at top of stack.
	 * @throws NoSuchElementException If stack is empty.
	 */
	public T peek() 
	throws NoSuchElementException {
		if (items.size() == 0) {
			//return null;
			throw new NoSuchElementException("can't peek on an empty stack");
		}
		return items.get(items.size()-1);
	}

	/**
	 * Tells if stack is empty.
	 * 
	 * @return True if stack is empty, false if not.
	 */
	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	private float calculate(Stack<Float> nums, Stack<Character> ops){
	    	Stack<Float> nums2= new Stack<Float>();
	    	Stack<Character> ops2= new Stack<Character>();
	    	float temp=0;
	    	float temp2=0;
	    	float result=0;
	    	
	    	while(!nums.isEmpty()){
	    		if(ops.isEmpty()){
	    			nums2.push(nums.pop());
	    		}
	    		if(ops.peek()=='*'){
	    			temp=nums.pop();
	    			temp=temp*nums.pop();
	    			nums.push(temp);
	    			ops.pop();
	    		}
	    		if(ops.peek()=='/'){
	    			temp=nums.pop();
	    			temp=(int)temp/nums.pop();
	    			temp=(float)temp;
	    			nums.push(temp);
	    			ops.pop();
	    		}
	    		if(ops.peek()=='+'||ops.peek()=='-'){
	    			nums2.push(nums.pop());
	    			ops2.push(ops.pop());
	    		}
	    	}
	    	
	    	while(!nums2.isEmpty()){
	    		if(ops2.peek()=='-'){
	    			temp2=nums2.pop();
	    			temp=nums2.pop();
	    			result=temp-temp2;
	    			ops2.pop();	
	    		}
	    		if(ops2.peek()=='+'){
	    			temp=nums2.pop();
	    			temp=temp+nums2.pop();
	    			nums2.push(temp);
	    			ops2.pop();
	    		}
	    		if(ops2.isEmpty()){
	    			return nums2.pop();
	    		}
	    	}
	     	return 0;
	    }
	public static void main(String[] args) {
		
		Stack<Float> nums=new Stack<Float>();
		Stack<Character> ops=new Stack<Character>();
		
		nums.push(1);
		
		

	}

}
