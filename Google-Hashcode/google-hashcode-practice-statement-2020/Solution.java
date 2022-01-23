import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solution {

	public static List<Integer> getMaxPizzas (List<Integer> pizzas, int max) {
		int result = 0;

        //pizzas.sort(Integer::compareTo);
		List<Integer> listOfChosenPizzas = new ArrayList<>();
		
		int prevSum = 0;
		int sum1 = 0;
		int futureSum1 = 0;
		int sum2 = 0;
		int futureSum2 = 0;
		for (int i = 0; i < pizzas.size(); i++) {
			sum1 += pizzas.get(i);
			for (int j = i + 1; j < pizzas.size(); j++) {
				futureSum1 = sum1;
				futureSum2 = sum2;
				futureSum1 += pizzas.get(j);
				futureSum2 += pizzas.get(j);
				if (futureSum1 <= max) {
					sum1 = futureSum1;
				}
				if (futureSum2 <= max) {
					sum2 = futureSum2;
				}
				if (futureSum1 > max && 
						futureSum2 > max) {
					break;
				}
			}
			if (sum1 <= max) {
				if (sum1 > sum2) {
					if (prevSum == 0) {
						prevSum = pizzas.get(i);
					}
					else {
						prevSum += pizzas.get(i);
					}
					listOfChosenPizzas.add(i);
				}
				sum1 = prevSum;
				sum2 = prevSum;
				result = Math.max(sum1, sum2);
			}
			else {
				result = sum2;
			}
		}
		
		System.out.println("Max=" + max + ", result=" + result);
		
		return listOfChosenPizzas;
	}
	
	public static void solve (String filePath) throws IOException {
		File fileIn = new File(filePath); 
		  
		BufferedReader br = new BufferedReader(new FileReader(fileIn)); 
		  
		int max = 0;
		int pizzasSize = 0;
		List<Integer> pizzas = new ArrayList<>();
		String st; 
		while ((st = br.readLine()) != null) {
		    String [] init = st.split("\\s");
		    if (pizzasSize == 0) {
		    	max = Integer.parseInt(init[0]);
		    	pizzasSize = Integer.parseInt(init[1]);
		    }
		    else {
		    	for (int i = 0; i < pizzasSize; i++) {
		    		pizzas.add(Integer.parseInt(init[i]));
		    	}
		    }
		} 
		
		br.close();
		
		List<Integer> result = getMaxPizzas(pizzas, max);
		File fileOut = new File("src//result.out");
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
		bw.append(String.valueOf(result.size()));
		bw.newLine();
		for (Integer pizza: result) {
			bw.append(pizza + " ");
		}

		bw.close();
	}
	
	public static void test1 () throws IOException {
		solve("src//a_example.in"); 
	}
	
	public static void test2 () throws IOException {
		solve("src//b_small.in"); 
	}
	
	public static void test3 () throws IOException {
		solve("src//c_medium.in"); 
	}
	
	public static void test4 () throws IOException {
		solve("src//d_quite_big.in"); 
	}
	
	public static void test5 () throws IOException {
		solve("src//e_also_big.in"); 
	}
	
	public static void main (String ... args) throws IOException {
		test5();
	}
	
}
