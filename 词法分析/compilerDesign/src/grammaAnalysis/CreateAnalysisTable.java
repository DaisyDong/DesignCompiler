//�����Լ���Ƶ��ķ�������Ӧ�ķ�����
package grammaAnalysis;

import java.util.*;

public class CreateAnalysisTable {
	public static	List<String> var = ReadGramm.Var;
	public static	List<String> end = ReadGramm.End;  
	public static 	ArrayList<String[]> pro = ReadGramm.Pro;//�ı����ʽ��ʽ
	public static String[][] go = new String[40][40];	
	public static String[][] go1 = new String[40][40];//���ڼ�¼go������ת�����
	public static int k = 0;	//����ÿ����Ŀ���е���Ŀ����
	public static int n = 0;	//������Ŀ�������
	public static String[][] CreatTable() {	//�������ɵı� 
		ArrayList<ArrayList<String[]>> item = new ArrayList<ArrayList<String[]>>(); 
		ArrayList<String[]> it = new ArrayList<String[]>(); 
		String[] str = new String[4]; 
		str[0] = pro.get(0)[0]; //�������ʽ�Ĳ�����
		str[1] = null;				//�����Ѿ����Ƴ��ķ���
		str[2] = pro.get(0)[2];	//����δ���Ƴ��ķ���  
		go[n][k++] = str[2].split(" ")[0]; 
		str[3] = "#";				//��������������
		it.add(str);	//�õ���һ����չ�Ĳ���ʽ 
		creatItem(str,pro,it);//������һ����Ŀ��  
		item.add(it);
		it = item.get(0);
		int c = 0;
		while(it.size() - c > 0)
			System.out.println(it.get(c++)[3]);
		++n;	//��һ����Ŀ��
		int j = 0;
		j = n - 1;  
		do { 
			it = item.get(j);   
			int i = 0;
			while(go[j][i] != null) {
				String s1 = go[j][i++];	
				int m = 0;
				k = 0;
				ArrayList<String[]> it1 = new ArrayList<String[]>();
				while(it.size() - m > 0) { 
					String[] s2 = it.get(m++);
					String[] temp = new String[4]; 
					if(s2[2] != null) {
						String[] spl = s2[2].split(" ");
						if(spl[0].equals(s1)) {
						temp[0] = s2[0];
						temp[3] = s2[3];
						if(s2[1] != null)
							temp[1] = s2[1]+" "+s1;		//�ƽ��Ѿ�ƥ��ķ���
						else
							temp[1] = s1;
						if(spl.length > 1) {
							StringBuilder st = new StringBuilder();
							for(int a = 1;a < spl.length - 1;a++){
								st.append(spl[a]);
								st.append(" "); 
							} 
							if(spl.length >= 2)
								st.append(spl[spl.length - 1]);
							temp[2] = st.toString(); 
							it1.add(temp); 
							go[n][k++] = spl[1];
							go1[n][j] = spl[1];
							System.out.println();
							creatItem(temp,pro,it1);
							for(int a = 0;a < k;a++)
								System.out.println(go[n][a]);
						}
						else {
							go1[n][j] = 
							go[n][k++] = null;
							temp[2] = null; 
							it1.add(temp);
						}
					} 
					}
				}
				if(it1 != null) {
					if(!defContain(item,it1)) {	//������ѭ����������֮ǰ�в���һ����
					item.add(it1);
					ArrayList<String[]> it3 = item.get(n); 
					int mx = 0; 
					while(it3.size() - mx > 0) {
						String[] p = it3.get(mx++);
						System.out.println(p[0]+","+p[1]+","+p[2]+","+p[3]);
					}  
					System.out.println(n);
					++n; 
					}
					else{
						for(int d = 0;d < k;d++) 
							go[n][d] = null; 
						go1[n][j] = null;
					}
						
				}
			} 
		} while(++j < n);
		for(int c1 = 0;c1 < n;c1++) {
			int c2 = 0;
			for(c2 = 0;c2 < 40;c2++) {
				if(go1[c1][c2] != null)
					System.out.println(c1+" "+c2+" "+go1[c1][c2]);
			}
		}
		//��go�������������
		String[][] table = new String[n+1][end.size()+var.size()];
		int i = 0;
		while(end.size() - i > 0)
			table[0][i] = end.get(i++); 
		table[0][i++] = "#";
		int a = 1;
		while(var.size() - a > 0) 
			table[0][i++] = var.get(a++); 
		for(int l = 0;l < n;l++) {
			System.out.println(l);
			ArrayList<String[]> it1 = item.get(l); 
			int m = 0; 
			while(it1.size() - m > 0) {
				String[] p = it1.get(m++);
				System.out.println(p[0]+"->"+p[1]+"."+p[2]+","+p[3]);
				 	if(p[2] != null) { 
						String st = p[2].split(" ")[0];
						if(end.contains(st)) {
						int x;
						int y;
						for(x = 0;x < table[0].length;x++)
							if(table[0][x].equals(st))
								break;
						for(y = 0;y < go1[l].length;y++)
							if(st.equals(go1[l][y]))
								break;
						if(y != go1[l].length)
							table[y+1][x] = "S "+l;
						}
					}
					else if(p[2] == null) {
						int y = 0;
						while(pro.size() - y > 0) {
							String[] st1 = pro.get(y++);
							if(p[0].equals(st1[0]) && p[1].equals(st1[2])) {
								int x;
								String[] st = p[3].split(" ");	//���볬ǰ������
								for(int i1 = 0;i1 < st.length;i1++) {
									for(x = 0;x < table[0].length;x++)
										if(table[0][x].equals(st[i1]))
											break; 
									table[l+1][x] = "r "+(y-1); 
								}
								break;
							} 
						}
					}
					if(p[0].equals(var.get(0)) && p[2]==null) {
						table[l+1][end.size()] = "acc";
					}
				} 
			for(int y = 0;y < go1[l].length;y++) {
				if(var.contains(go1[l][y])) {
					int x;
					for(x = end.size();x < table[0].length;x++)
						if(table[0][x].equals(go1[l][y])) {
							table[y+1][x] = ""+l;
							break;
						} 
				}
			}
	} 
	return table;
}
	//�����ж�������Ŀ���Ƿ����
	public static boolean defEqual(ArrayList<String[]> it,ArrayList<String[]> it1) {
		 if(it.size() != it1.size())
			 return false;
		 else {
			 int m = 0;
			 String[] p1;
			 String[] p2;
			 while(it.size() - m > 0) {
				 p1 = it.get(m);
				 p2 = it1.get(m++);
				 int n = 0;
				 while(n < 4) {
					 if(p1[n] == null && p2[n] == null);
					 else if(!p1[n].equals(p2[n]))
						 return false;
					 ++n;
				 }
			 }
			 return true;
		 } 
	}
	//�����ж�����Ŀ�����Ƿ���һ����Ŀ
	public static boolean defContain(ArrayList<ArrayList<String[]>> item,ArrayList<String[]> it1) {
		if(item == null || it1 == null)
			return false;
		for(ArrayList<String[]> it : item) {
			if(defEqual(it,it1)) return true;
		} 
		return false;
	}
	//�����ҵ�first��
	public static String findFirst(ArrayList<String[]> pro,String str) { 
			String[] s;
			int count = 0;
			String st = new String();
			st = str;
			if(var.contains(str)) {
				int m = 0;
				while(pro.size() - m > 0) { 
					if(pro.get(m)[0].equals(st)) {	//���ɸñ����������Ĳ���ʽ
						s = pro.get(m)[2].split(" "); 
						System.out.println(s[0]);
						if(count == 0) {	//�ж��first
							str = s[0];
						}  
						else
							str += " "+s[0];
						++count;
					}
					++m;
				}
				str = str + " #";
			}
			else str += " #";
			return str;  
} 
public static boolean isContains(String[][] go,int n,int k,String str) {
	for(int i = 0;i < k;i++) 
		if(go[n][i].equals(str))
			return true;
	return false;
} 

//�ɵõ���һ����Ŀ��һ���������ʽ���Լ�һ���������ɵ���Ŀ��
public static ArrayList<String[]> creatItem(String[] str,ArrayList<String[]> its,ArrayList<String[]> it) {
	String[] s = str[2].split(" ");
	ArrayList<String[]> next = new ArrayList<String[]>();
	next.add(str);
	int t = 1;
	int e = 1;
	do{						//�õ�I0��Ŀ��
	if(s != null && var.contains(s[0])) {	//��һ�������Ǳ�����������Ҫô�Ǵ�ԭʼ���ű��г���Ҫô����һ���н����ƽ������
		int m = 0;
		int x = 0;
		String te = s[0]; 
		while(its.size() - m > 0) {
			boolean tag = false; 
			String[] s1 = new String[4];  
			String[] s2 = new String[4];
			String tt = its.get(m)[0];
			if(te.equals(tt)) {
				s1 = its.get(m);
				tag = true; 
			} 
//			else if(its.size() - m == 1 && !pro.equals(its)) { 
//				String tp;
//				while(pro.size() - x > 0) {
//					tp = pro.get(x)[0];
//					if(te.equals(tp)) {
//						s1 = pro.get(x++); 
//						tag = true;
//						flag = true;
//						break;
//					}
//					++x;
//				} 
//			}
//			if(pro.size() == x || !flag)	//ԭʼ����ʽ��û����ƥ���
//				++m;
			if(tag) {	//���ɸñ����������Ĳ���ʽ 
				s2[0] = s1[0];
				s2[1] = s1[1];
				s2[2] = s1[2];
				//�������� 
				if(s.length > 1) {
//					if(flag)
//						s2[3] = findFirst(pro,s[1]);
//					else
						s2[3] = findFirst(its,s[1]);
				}
				else s2[3] = str[3]; 
				if(s1[2] != null) { //�������ɵ���Ŀ�Ƿ��������µ�����ʽ
					String s3 = s1[2].split(" ")[0];
					if(s3 != null && !isContains(go,n,k,s3)) {
						go[n][k++] = s1[2].split(" ")[0];	//�������е���һ��Ҫ�Ƶ�X
						if(var.contains(s3) && !next.contains(s1)) {
							next.add(s2);
							t++;
						}
					}
					System.out.println(n+" "+k+" "+s1[2].split(" ")[0]);
					//s = s1[2].split(" ");	//�������������µĲ���ʽ
				}
				//else
					//s = null; 
				it.add(s2);
				System.out.println(s2[0]+","+s2[1]+","+s2[2]+","+s2[3]); 
			}  
			++m;
		}
		if(e < t) {
			str = next.get(e++);
			s = str[2].split(" ");
		}
		else
			s = null;
	}
	else break; 
	} while(true);
	
	return it;
}
}
