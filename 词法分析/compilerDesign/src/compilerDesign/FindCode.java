package compilerDesign;

import java.util.*; 

public class FindCode {
	public static void findCode(String word) { 
		List<List<String>> encodes = LexicalAnalyzer.encode;
		ListIterator<List<String>> it = encodes.listIterator(); 
		while(it.hasNext()) { 
			List<String> sigalCode = it.next();  
			if(sigalCode.contains(word)) {
				List<String> tokens = new ArrayList<String>(); 
				tokens.add(sigalCode.get(2));
				tokens.add(null);  
				LexicalAnalyzer.token.add(tokens);
				System.out.println(tokens);
				return;
			}	 
		}   
		List<String> tokens = new ArrayList<String>();
		LexicalAnalyzer.SymbolTable.add(word);
		tokens.add("ID");
		tokens.add(word);
		LexicalAnalyzer.token.add(tokens); 
		System.out.println(tokens);
	 } 
	} 
