/*词法分析部分：根据源文件中的字符串输入以及单词编码文件，生成符号表和token字文件
 * 以提供给下一个语法分析部分使用
 * 
 */
package compilerDesign;
import java.util.*;
import java.io.*;

public class LexicalAnalyzer { 
	public static char[] buffer = new char[2048];	//缓冲区采用带标记的双缓冲区
	public static int preNum = 0;
	public static int curNum = 0;
	public static List<List<String>> encode = new ArrayList<List<String>>();
	public static List<List<String>> token = new ArrayList<List<String>>();
	public static Collection<String> SymbolTable = new HashSet<String>();	//建立符号表
	public static boolean tag;	//用于标记当前缓冲区是否是满的，以判断源程序中是否还有没有识别完的
	public static int[] BrackTag = new int[] {0,0,0,0,0,0};
	public static char[] Brack = new char[] {'(',')','[',']','{','}','\'','\'','\"','\"'};
	public static boolean full = false;
	public static List<List<String>> getToken() throws IOException{
		initEncodeTable();	//初始化编码表   
		FileReader readSource = new FileReader("SourceCode2.txt");
		int n = readSource.read(buffer,0,2046); 
		if(n != 0){
			buffer[n] = '￥';	//标记数组的结尾
			if(n != 2047) full = true;	//没读满，说明程序已经全部读取完毕
			tag = false;	//用于标记数组的另一半是否是新更新的，刚读取时不是
			for(curNum = 0;curNum < n;curNum++){
				int res = 0;  
				preNum = curNum;	//对于每一个正在识别的词，先站在同一起跑线再超前搜索
				char ch = buffer[curNum];
				System.out.println(ch);
				Analysis analysis = new Analysis(curNum,buffer);
				//转去识别标示符或关键字
				if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_' || ch == '$')
					res = analysis.analysisWord(); 
				//识别数字
				else if(ch >= '0' && ch <= '9')
					res = analysis.analysisDigit(); 
				//识别括号，需要检查是否封闭
				else if(ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' ||ch == '}' || ch == '\'' || ch == '\"')
					res = analysis.analysisBrackets();
				//识别注释
				else if(ch == '/')
					res = analysis.analysisAnnotation();
				else 
				//识别运算符,需要超前搜索 
					res = analysis.analysisOperator();  
				System.out.println("res:"+res); 
				if(res == -1) {	//表示读到尾部还没有识别完一个词，需要再前半部分再读入新的字符
					int m = readSource.read(buffer,0,1022);
					if(m > 0){
						buffer[m] = '￥'; 
						if(m < 1023) full = true;
						tag = true;	//另一半已经有更新
					}
					else{
						curNum--;	//另一半没有更新，即文件已经读取完毕，当前即为全部待识别的
						full = true;
						tag = false;
					}
				}
				else if(res == -2) {
					int m = readSource.read(buffer,1024,2046);
					if(m > 0){
						buffer[m+1024] = '￥';
						if(m < 1023) full = true;
						tag = true;	//另一半已经有更新
					}
					else{
						curNum--;
						full = true;
						tag = false;
					}
				}
				else
					curNum = res;	//超前搜索后跳过已经识别的符号 	 
			}
		} 
		try(PrintStream ps = new PrintStream(new FileOutputStream("token.txt"))){
			System.setOut(ps); 
			System.out.println("token字：");
			for(List<String> tok : token)
				System.out.println(tok); 
		}catch(IOException ex){
			ex.printStackTrace();
		}
		try(PrintStream pc = new PrintStream(new FileOutputStream("SysmbolTable.txt"))){
			System.setOut(pc);
			System.out.println("符号表");
			for(String symbol : SymbolTable)
				System.out.println(symbol);
		}catch(IOException ex){
			ex.printStackTrace();
		}
		//判断错误信息
		defineErroInfo();
		readSource.close();  
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		return token;
		} 
		private static void defineErroInfo() {
			try(PrintStream ps = new PrintStream(new FileOutputStream("erroInfo.txt")))
			{
			System.setOut(ps);
			for(int i = 0;i < 5;i++) {
				System.out.println(i+" "+BrackTag[i]);
				if(BrackTag[i] > 0)
					System.out.println(Brack[2*i+1]+"不封闭，请插入"+Brack[2*i]);
				else if(BrackTag[i] < 0)
					System.out.println(Brack[2*i]+"不封闭，请插入"+Brack[2*i+1]); 
			}
			ps.close();
			}catch(IOException ex)
			{
				ex.printStackTrace();
			} 
		}
		private static void initEncodeTable() throws IOException{ 
			FileReader recode = new FileReader("EncodeTable.txt");
			BufferedReader br = new BufferedReader(recode); 
			
			String line = br.readLine();
			while(line != null) { 
				List<String> oneClass = new ArrayList<String>();
				String[] one = new String[3];
				one = line.split("\t"); 
				for(String str : one)
					oneClass.add(str);
				encode.add(oneClass); 
				//System.out.println(oneClass);
				line = br.readLine();
			} 
			br.close();
	} 
//		private static int findCode(String word) { 
//			List<List<String>> encodes = LexicalAnalyzer.encode;
//			ListIterator<List<String>> it = encodes.listIterator(); 
//			while(it.hasNext()) { 
//				List<String> sigalCode = it.next();  
//				if(sigalCode.contains(word)) {
//					List<String> tokens = new ArrayList<String>(); 
//					tokens.add(sigalCode.get(1));
//					tokens.add(sigalCode.get(2));  
//					token.add(tokens);
//					System.out.println(tokens); 
//					return 1;	//找到相应的属性值		
//				}	 
//			}  
//			return -1; //没有相应的属性值，则可能处于缓冲区末尾，需在读进一半数据
//		 } 
}
