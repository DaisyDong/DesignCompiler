package grammaAnalysis;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ReadGramm {
	public static List<String> Var = new ArrayList<String>();	//文法变量
	public static List<String> End = new ArrayList<String>();	//终结符
	public static ArrayList<String[]> Pro = new ArrayList<String[]>(); //产生式 
	public static String[][] table;			//根据文法生成的转移表
	
	public static void main(String args[]) {
		Var = new ArrayList<String>();
		End = new ArrayList<String>();
		Pro = new ArrayList<String[]>(); 
		readGramm();
		System.out.println(AnalysisGramm.analysisGramm());
	}
	public static void readGramm() { 
	try{
		FileReader readGramm = new FileReader("gramm1.txt");
		
		char[] v = new char[40];
		char[] e = new char[40];
		boolean tag = false;
		int i = 0;
		int j = 0;
		int n = 0;
		int m = 0;
		int r = readGramm.read();
		StringBuilder temp = new StringBuilder();
	while(r != -1 ) { 
		tag = false;
		if(n == 0) {
			while(r != '\n') { 
			temp.append((char)r);  
			r = readGramm.read(); 
			if((char)r == ' ' || (char)r == '\r') {  
				Var.add(temp.toString());
				System.out.println(Var); 
				temp = new StringBuilder(); 
				if(r == '\r') break;
				r = readGramm.read();
			}
			}
			r = readGramm.read();
			++n;
		}  
		else if(n == 1) {
			while(r != '\r') {
			System.out.println((char)r);
			temp.append((char)r);
			r = readGramm.read();
			if(r == ' ' || r == '\r') {
				End.add(temp.toString()); 
				System.out.println(End);
				temp = new StringBuilder(); 
				if(r == '\r') break;
				r = readGramm.read(); 
			} 
			}
			r = readGramm.read();	//windows中换行符为\r\n两个
//			temp = new StringBuilder(); 
//			while(r != '\r') {
//				temp.append(r);
//				System.out.println(r);
//				r = readGramm.read();
//			}
//			for(int a = 0;a < temp.length();a++) 
//				m += (temp.charAt(a) - '0') * (temp.length() - a - 1) * 10;  
			System.out.println((char)r); //\r
			tag = true;
			++n;
		} 
		else if(n == 2) {  
			System.out.println(m);
			r = readGramm.read(); 
			System.out.println((char)r);
			while(m > 0) {
				int a = 0;
				r  = readGramm.read();
				while(r != '\r') {
					temp.append((char)r);
					r = readGramm.read(); 
				}
				System.out.println(temp.toString() + " "+ m);
				String[] p1 = temp.toString().split("->"); 
				System.out.println(p1[0]); 
				String[] p2 = {p1[0],null,p1[1],null};
				Pro.add(p2);
				temp = new StringBuilder();
				m--; 
				r = readGramm.read();
			}  
			table =  CreateAnalysisTable.CreatTable();
			try(PrintStream ps = new PrintStream(new FileOutputStream("AnalysisTable.txt"))){
				System.setOut(ps);  
				int a = table[0].length;
				int b = table.length;
				System.out.println(a+" "+b);
				for(int x = 0;x < b;x++) {
					for(int y = 0;y < a;y++){ 
						System.out.print(table[x][y]+"\t");
					}
					System.out.println();
				}
				ps.close(); 
				System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
			}catch(IOException ex){
				ex.printStackTrace();
			}
//			Var = new ArrayList<String>();
//			End = new ArrayList<String>();
//			Pro = new ArrayList<String[]>();
//			n = 0;
		} 
		r = readGramm.read();	//重新读取
		if(tag) {
		StringBuilder stb = new StringBuilder();
		while(r != '\r') {
			stb.append((char)r);
			System.out.println((char)r);
			r = readGramm.read();
		}
		for(int c = 0;c < stb.length();c++) {
			int b = 1;
			for(int d = 0; d < stb.length() - c - 1;d++)
				b *= 10; 
			m += (stb.charAt(c) - 48)*b;
		}
		}
		System.out.println(m);
	} 
	} catch(Exception e) {
		e.printStackTrace();
	}
	}
}
