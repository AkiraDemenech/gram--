package br.uel.hu.lab;






import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Bacteria {
	
	public Bacteria () {
		setProbabilities(null);
		
	}
	
	public int compareTo (Bacteria b) {
		return compare(b,this);
	}
	
	

	public double[] getResult () {
		return result;
	}
	public int getZeros () {
		return zeros;
	}
	 
	private int zeros = 0;
	private double[] result = null;
	private String name = null;
	private Map<String,double[]> probabilities;
	
	public Map<String, double[]> getProbabilities () {
		return this.probabilities;
	}
	public double[] getProbabilities (String test) {
		return this.probabilities.get(test);
	}
	public boolean setProbabilities (String test, double[] probability) {
		if(this.probabilities == null)
			return false;
		this.probabilities.put(test,probability);
		return true;
	}
	public void setProbabilities (Map<String,double[]> probabilities) {
		this.probabilities = probabilities;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	public String getName () {
		return this.name;
	}

	public Bacteria (String name) {
		this(name,new HashMap<>());		
		
	}
	public Bacteria (String name, Map<String,double[]> probabilities) {
		setName(name);
		setProbabilities(probabilities);		
	}
	
	public Bacteria test (Map<String, Boolean> tests) {
		return test(tests,new HashMap<>());
	}
	public Bacteria test (Map<String, Boolean> tests, Map<String, double[]> results) {				
		Bacteria b = new Bacteria(this.name,results);
		double[] temp;
		for(Map.Entry<String, Boolean> t: tests.entrySet()) {			
			temp = getProbabilities(t.getKey());
			if(t.getValue() == null || temp == null)
				continue;			
			if(!t.getValue()) 
				temp = complement(temp);														
			b.result = multiply(b.result, temp);
			results.put(t.getKey(), temp);
			for(int c = 0; c < temp.length; c++)
				if(temp[c] == 0) 
					b.zeros++;
		}				
		return b;
	}
	

	@Override
	public String toString() {
		return probability(this.result,3) + '\t' + this.name;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = zeros;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((probabilities == null) ? 0 : probabilities.hashCode());		
		return prime * result + Arrays.hashCode(this.result);
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		Bacteria other = (Bacteria) obj;
		if (this.name == null) 
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (this.probabilities == null) 
		{
			if (other.probabilities != null)
				return false;
		} else if (!probabilities.equals(other.probabilities))
			return false;		
		return zeros == other.zeros && Arrays.equals(result, other.result);
	}
	
	
	
	

	public static double MAX_PERCENTAGE = 100;
	public static double[] complement (double[] event) {
		return complement(event,MAX_PERCENTAGE);
	}
	public static double[] complement (double[] event, double max) {
		if(event == null)
			return null;
		
		double[] not = new double[event.length];
		for(int c = 0; c < not.length; c++)
			not[c] = max - event[event.length - 1 - c];
		
		return not;
	}
	
	public static double[] multiply (double[] a, double[] b) {
		return multiply(a,b,true); 
	}
	public static double[] multiply (double[] a, double[] b, boolean positivo) {
		return multiply(a,b,positivo,MAX_PERCENTAGE);
	}
	public static double[] multiply (double[] a, double[] b, boolean positivo, double max) {
		if(a == null)
			return b;
		if(b == null)
			return a;
		double[] c = b;
		double[] r = new double[a.length * b.length];		
		if(!positivo) 
			c = complement(b,max);
		for(int i = 0; i < a.length; i++)
			for(int j = 0; j < c.length; j++)
				r[j + (i * c.length)] = a[i] * c[j] / max;
		Arrays.sort(r);
		return r;
	}	
	public static double[] multiply (double[] a, double[] b, double max) {
		return multiply(a,b,true,max);
		
	}
	public static String probability (double[] p, int frac0, int fracn, String open, String close, String sep, String varies) {
		if(p == null || p.length <= 0)
			return varies;
		StringBuffer s = new StringBuffer();
		if(p.length > 1)
			s.append(open);
		s.append(String.format(Locale.US, "%." + frac0 + "f", p[0]));
		for(int c = 1; c < p.length; c++)
			s.append(sep).append(String.format(Locale.US,"%."+fracn+"f",p[c]));
		if(p.length > 1)
			s.append(close);				
		return s.toString();
	}

	public static final int CONSIDERAR_A_PARTIR_DE = 1;
	public static final int NEGATIVO = 1;
	public static final int POSITIVO = 2;
	public static final int IMPRECISO = 3;
	public static final int IGNORAR = 0;

	public static final String[] RESPOSTAS = {"ignorar", "NEGATIVO (-)", "POSITIVO (+)", "Incerto (+/-)"};
	public static final String SINAIS = " -+/";

	public static final String OPEN_RANGE = "[";
	public static final String CLOSE_RANGE = "]";
	public static final String SEP_ITEM = ", ";
	public static final String NULL_ITEM = "v";
	public static String probability (double[] p, int frac) {
		return probability(p,frac,frac-1);
	}
	public static String probability (double[] p, int first_frac, int other_frac) {
		return probability(p,first_frac,other_frac,OPEN_RANGE,CLOSE_RANGE,SEP_ITEM,NULL_ITEM);
	}
	
	public static int compare (Bacteria u, Bacteria v) {
		if(u != v) { // se forem instâncias diferentes
			if(u == null) // v é maior 
				return 1;
			if(v == null) // u é maior
				return -1;
			
			if(v.result != u.result) {				
				if(v.result == null)
					return -1; // u é maior
				if(u.result == null)
					return 1; // v é maior
				
				if(u.result.length > 0 || v.result.length > 0) {
					if(u.result.length <= 0)
						return 1;	// v é maior
					if(v.result.length <= 0)
						return -1;	// u é maior
				
					
				
					// sua maior possibilidade
					if(v.result[v.result.length - 1] < u.result[u.result.length - 1])
						return -1; // v é menor
					if(u.result[u.result.length - 1] < v.result[v.result.length - 1])
						return 1; // u é menor
					
					// sua menor possibilidade
					if(v.result[0] > u.result[0])
						return 1; // v é maior
					if(u.result[0] > v.result[0])
						return -1; // u é maior
				}
				
				// quantos zeros multiplicaram seu resultado
				if(v.zeros > u.zeros)
					return -1; // u é maior
				if(v.zeros < u.zeros)
					return 1; // v é maior
				
			}
			
			if(u.probabilities != v.probabilities){
				if(u.probabilities == null)
					return 1;	// v é maior
				if(v.probabilities == null)
					return -1;	// u é maior
				
				if(v.probabilities.size() < u.probabilities.size())
					return -1; //	 u é maior
				if(v.probabilities.size() > u.probabilities.size())
					return 1; //	v é maior			
			}
			
			
			if(u.name != null || v.name != null) {

				if(v.name == null)
					return -1;
				if(u.name == null)
					return 1;
				
				return v.name.compareTo(u.name);
			}
			
		}
		
		return 0;
	}
	
	public static class Sort implements Comparator<Bacteria> {

		@Override
		public int compare(Bacteria x, Bacteria y) {
						
			return Bacteria.compare(x, y);
		}
		
	}
	
	

	public static List<Bacteria> results (List<Bacteria> probabilities, Map<String,Boolean> tests) {
		ArrayList<Bacteria> possibilities = new ArrayList<>();
		for(Bacteria b : probabilities)
			possibilities.add(b.test(tests));
		Collections.sort(possibilities,new Bacteria.Sort());
		return possibilities;
	}
}
