package grammaAnalysis;

import java.util.ArrayList;
import java.util.Stack;

public class AnalysisGramm {
	public static String[][] table = ReadGramm.table; 
	public static 	ArrayList<String[]> pro = ReadGramm.Pro;//����ʽ
	public static String analysisGramm() {
		ArrayList<String> input = inputWord();
		Stack<Integer> stack1 = new Stack<Integer>();	//�����﷨����������ջ��״̬�ı仯
		Stack<String> stack2 = new Stack<String>();	//�����﷨�����Ĺ����ƽ���Լ�����з��ŵı仯
		stack1.push(1);		//��ʼջ��״̬Ϊ��ʼ״̬
		stack2.push("#");
		int x = 0;
		int y = 0; 
		String p = input.get(y);
		x = findVar(p);
		String state = table[stack1.peek()][x];
		
		while(input.size() - y > 0) {	//�������봮�е�ÿһ������ 
			if(state == null)
				return "error";
			String[] spl = state.split(" ");
			if(spl[0].equals("S")) { 
				char[] ch = spl[1].toCharArray(); 
				 stack1.push(toInt(ch)+1);
				 stack2.push(p);
				 p = input.get(++y);
				 x = findVar(p);
				 state = table[stack1.peek()][x]; 
			}
			else if(spl[0].equals("r")) {
				 char[] ch = spl[1].toCharArray();
				 int a = toInt(ch);
				 String[] pr = pro.get(a - 1);	//�õ���a������ʽ
				 int m = pr[2].split(" ").length;
				 for(int i = 0;i < m;i++) {	//���������������ż���Ӧ״̬
					 stack1.pop();
					 stack2.pop();
				 }
				 int b = findVar(pr[0]);
				 char[] s = table[stack1.peek()][b].toCharArray();
				 stack1.push(toInt(s) + 1);
				 stack2.push(pr[0]);
				 System.out.println(pr[0]+"->"+pr[2]);
				 p = input.get(y);
				 x = findVar(p);
				 state = table[stack1.peek()][x]; 
			}
			else if(spl[0].equals("acc"))
				return "success";
				
		}
		return "notAcc";
	}
	public static int findVar(String var) {
		for(int i = 0;i < table[0].length;i++) {
			if(var.equals(table[0][i]))
				return i; 
		}
		return -1;
	}
	//��ȡ�ʷ������Ľ�� ĩβҪ�ӡ�#��
	public static ArrayList<String> inputWord() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("i");
		list.add("=");
		list.add("*");
		list.add("*");
		list.add("i");
		list.add("#");
		return list;
	}
	public static int toInt(char[] ch) { 
		int a = 0;
		for(int i = 0;i < ch.length;i++) {
			int b = 1;
			for(int j = 0;j < ch.length - i - 1;j++) 
				b *= 10;
			a += ((int)ch[i]-48) * b; 
		}
		return a;
	}
}
