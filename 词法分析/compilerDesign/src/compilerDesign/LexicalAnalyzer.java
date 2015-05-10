/*�ʷ��������֣�����Դ�ļ��е��ַ��������Լ����ʱ����ļ������ɷ��ű��token���ļ�
 * ���ṩ����һ���﷨��������ʹ��
 * 
 */
package compilerDesign;
import java.util.*;
import java.io.*;

public class LexicalAnalyzer { 
	public static char[] buffer = new char[2048];	//���������ô���ǵ�˫������
	public static int preNum = 0;
	public static int curNum = 0;
	public static List<List<String>> encode = new ArrayList<List<String>>();
	public static List<List<String>> token = new ArrayList<List<String>>();
	public static Collection<String> SymbolTable = new HashSet<String>();	//�������ű�
	public static boolean tag;	//���ڱ�ǵ�ǰ�������Ƿ������ģ����ж�Դ�������Ƿ���û��ʶ�����
	public static int[] BrackTag = new int[] {0,0,0,0,0,0};
	public static char[] Brack = new char[] {'(',')','[',']','{','}','\'','\'','\"','\"'};
	public static boolean full = false;
	public static List<List<String>> getToken() throws IOException{
		initEncodeTable();	//��ʼ�������   
		FileReader readSource = new FileReader("SourceCode2.txt");
		int n = readSource.read(buffer,0,2046); 
		if(n != 0){
			buffer[n] = '��';	//�������Ľ�β
			if(n != 2047) full = true;	//û������˵�������Ѿ�ȫ����ȡ���
			tag = false;	//���ڱ���������һ���Ƿ����¸��µģ��ն�ȡʱ����
			for(curNum = 0;curNum < n;curNum++){
				int res = 0;  
				preNum = curNum;	//����ÿһ������ʶ��Ĵʣ���վ��ͬһ�������ٳ�ǰ����
				char ch = buffer[curNum];
				System.out.println(ch);
				Analysis analysis = new Analysis(curNum,buffer);
				//תȥʶ���ʾ����ؼ���
				if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_' || ch == '$')
					res = analysis.analysisWord(); 
				//ʶ������
				else if(ch >= '0' && ch <= '9')
					res = analysis.analysisDigit(); 
				//ʶ�����ţ���Ҫ����Ƿ���
				else if(ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' ||ch == '}' || ch == '\'' || ch == '\"')
					res = analysis.analysisBrackets();
				//ʶ��ע��
				else if(ch == '/')
					res = analysis.analysisAnnotation();
				else 
				//ʶ�������,��Ҫ��ǰ���� 
					res = analysis.analysisOperator();  
				System.out.println("res:"+res); 
				if(res == -1) {	//��ʾ����β����û��ʶ����һ���ʣ���Ҫ��ǰ�벿���ٶ����µ��ַ�
					int m = readSource.read(buffer,0,1022);
					if(m > 0){
						buffer[m] = '��'; 
						if(m < 1023) full = true;
						tag = true;	//��һ���Ѿ��и���
					}
					else{
						curNum--;	//��һ��û�и��£����ļ��Ѿ���ȡ��ϣ���ǰ��Ϊȫ����ʶ���
						full = true;
						tag = false;
					}
				}
				else if(res == -2) {
					int m = readSource.read(buffer,1024,2046);
					if(m > 0){
						buffer[m+1024] = '��';
						if(m < 1023) full = true;
						tag = true;	//��һ���Ѿ��и���
					}
					else{
						curNum--;
						full = true;
						tag = false;
					}
				}
				else
					curNum = res;	//��ǰ�����������Ѿ�ʶ��ķ��� 	 
			}
		} 
		try(PrintStream ps = new PrintStream(new FileOutputStream("token.txt"))){
			System.setOut(ps); 
			System.out.println("token�֣�");
			for(List<String> tok : token)
				System.out.println(tok); 
		}catch(IOException ex){
			ex.printStackTrace();
		}
		try(PrintStream pc = new PrintStream(new FileOutputStream("SysmbolTable.txt"))){
			System.setOut(pc);
			System.out.println("���ű�");
			for(String symbol : SymbolTable)
				System.out.println(symbol);
		}catch(IOException ex){
			ex.printStackTrace();
		}
		//�жϴ�����Ϣ
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
					System.out.println(Brack[2*i+1]+"����գ������"+Brack[2*i]);
				else if(BrackTag[i] < 0)
					System.out.println(Brack[2*i]+"����գ������"+Brack[2*i+1]); 
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
//					return 1;	//�ҵ���Ӧ������ֵ		
//				}	 
//			}  
//			return -1; //û����Ӧ������ֵ������ܴ��ڻ�����ĩβ�����ڶ���һ������
//		 } 
}
