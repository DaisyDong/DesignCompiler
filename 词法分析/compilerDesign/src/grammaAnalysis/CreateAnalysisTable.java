//�����Լ���Ƶ��ķ�������Ӧ�ķ�����
package grammaAnalysis;

import java.util.*;

public class CreateAnalysisTable {
	public static	List<String> var = ReadGramm.Var;
	public static	List<String> end = ReadGramm.End;  
	public static 	ArrayList<String[]> pro = ReadGramm.Pro;//�ı����ʽ��ʽ
	public static String[][] go = new String[400][400];	
	//��¼ÿһ��֮����Ҫ������Լ�Ĳ���ʽ,����֮����Ҳ���ʽ
	public static String[][] express = new String[400][400]; 
	public static String[][] go1 = new String[400][400];//���ڼ�¼go������ת����� 
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
//		express[n][k++] = "#";
		str[3] = "#";				//��������������
		it.add(str);	//�õ���һ����չ�Ĳ���ʽ 
		creatItem(str,it);//������һ����Ŀ��  
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
				String pr = new String();
				String s1 = go[j][i];	
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

						//go1[n][j] = spl[0];
						pr = spl[0];	//�ɸ���Ŀ���Ƶ���������Ŀ���ķ��ţ����ڻ�û��ȷ���Ƿ��ܹ��ɹ��������Ի�����ֱ�Ӹ�ֵ��ֹ��֮ǰ�ĸ���
						System.out.println(n+" 1go "+j+" go "+go1[n][j]);
						if(spl.length > 1) {
							StringBuilder st = new StringBuilder();
							for(int a = 1;a < spl.length - 1;a++){
								st.append(spl[a]);
								st.append(" "); 
							} 
							if(spl.length >= 2)
								st.append(spl[spl.length - 1]);
							temp[2] = st.toString(); 
//							temp[3] = express[j][i];	//first��������һ�������Ƴ�����ʽ�Ӳ�����first��
							it1.add(temp); 
							go[n][k++] = spl[1];
							if(var.contains(spl[1]))	//ֻ�в������Ƿ��ս���Ż�����бհ�����
								creatItem(temp,it1);	//first��ֻ�������������Ҫ�㣬��Ϊ����ͬһ���հ��е�
//							for(int a = 0;a < k;a++)
//								System.out.println(go[n][a]);
						}
						else { 
							go[n][k] = null;
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
					go1[n][j] = pr;
					System.out.println(n);
					++n; 
					}
					else{
						for(int d = 0;d < k;d++) 
							go[n][d] = null;
						if(defEqual(it, it1)) {//����֮ǰ�Ѿ���������Ŀ����Ӧ��ת�ƺ���Ҫ������
							go1[j][j] = pr;	//��ȣ����Լ����Բ����Լ�
							System.out.println(j + "go" + j +"go" + go1[j][j]); 
						}
						else {						//���ȣ����������Բ���֮ǰ��ĳ����Ŀ��
							int v = 0;
							for(ArrayList<String[]> it2 : item) {
								if(defEqual(it2,it1))
									break;
								++v;
							}
							if(v < item.size()){
								go1[v][j] = pr;
								System.out.println(v + "go" + j +"go" + go1[v][j]); 
							}
						}
						//go[n][j] = null; 
					}
						
				}
				++i;
			} 
		} while(++j < n);
		for(int c1 = 0;c1 < n;c1++) {
			int c2 = 0;
			for(c2 = 0;c2 < 40;c2++) {
				if(go1[c1][c2] != null)
					System.out.println(c2+" go "+c1+" go "+go1[c1][c2]);
			}
		}
		for(ArrayList<String[]> ss : item) {
			int x = 0;
			while(ss.size() - x > 0) {
				String[] p = ss.get(x++);
				System.out.println(p[0]+"->"+p[1]+"."+p[2]+","+p[3]);
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
		int l = -1;
		for(ArrayList<String[]> it1 : item) {
			l++;
			System.out.println("��" + l + "����Ŀ����"); 
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
						for(y = 0;y < n;y++)
							if(st.equals(go1[y][l]))
								break;
						if(y != n) {
							table[l+1][x] = "S "+y;
//							System.out.println(y + "   "+ x + "S "+l);
						}
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
									table[l+1][x] = "r "+ y; 
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
	public static List<String> findFirst(String str) { 
			String[] s; 
			List<String> first = new ArrayList<String>();	//���ڴ洢һ�������������ķ����ܲ��������е�first����
			String st = new String();
			st = str;
			while(var.contains(st)) {	//�Ƿ��ս������������
				int m = 0;
				while(pro.size() - m > 0) { 
					if(pro.get(m)[0].equals(st)) {	//���ɸñ����������Ĳ���ʽ
						s = pro.get(m)[2].split(" "); 
						System.out.println(s[0]);
						if(end.contains(s[0])) {	//first��ǰ�᣺���ս��
							 first.add(s[0]);
							 ++m;
						 }
						else{ 
							st = s[0]; 
							break;
						}
					} 
				}
			}
			first.add(st);	//���ս��
		return first;  
} 
public static boolean isContains(String[][] go,int n,int k,String str) {
	for(int i = 0;i < k;i++) 
		if(go[n][i].equals(str))
			return true;
	return false;
} 

//ͬһ���հ��У���Ҫ����first��
//��һ���������ʽ(��������һ��������������ȷ��first��)����һ��Ŀ���Լ�һ���������ɵ���Ŀ��
public static ArrayList<String[]> creatItem(String[] str,ArrayList<String[]> it) {
	String[] s = str[2].split(" ");
	ArrayList<String[]> next = new ArrayList<String[]>();	//ȷ���հ�������
	next.add(str);
	int t = 1;
	int e = 1;
	do{						//�õ�I0��Ŀ��
	if(s != null && var.contains(s[0])) {	//��һ�������Ǳ�����������Ҫô�Ǵ�ԭʼ���ű��г���Ҫô����һ���н����ƽ������
		int m = 0; 
		String te = s[0]; 
		while(pro.size() - m > 0) {
			boolean tag = false; 
			String[] s1 = new String[4];  
			String[] s2 = new String[4];
			String tt = pro.get(m)[0];
			if(te.equals(tt)) {
				s1 = pro.get(m);
				tag = true;
			}  
			if(tag) {	//���ɸñ����������Ĳ���ʽ 
				List<String> first = new ArrayList<String>();
				s2[0] = s1[0];
				s2[1] = s1[1];
				s2[2] = s1[2];
				//��������  
				if(s.length > 1)	
					first = findFirst(s[1]);
				else first.add(str[3]);	
				for(String st : first){
					//�����ж�������������ÿ����������Ҫ������Ӧһ������ʽ����Ŀ��
					s2[3] = st;
				if(s1[2] != null) { //�������ɵ���Ŀ�Ƿ��������µ�����ʽ
					String s3 = s1[2].split(" ")[0];
					if(s3 != null && !isContains(go,n,k,s3)) { 
						String[] temp = s1[2].split(" ");
						go[n][k++] = temp[0];	//�������е���һ��Ҫ�Ƴ��ı��ʽ�ķ���
					}
					if(var.contains(s3) && !nextCont(next,s2)) {//����Ƿ��ս�����������Ŀ���л���������������Ĳ���ʽ
						next.add(s2);
						t++; 
					} 
					System.out.println(n+" "+k+" "+s1[2].split(" ")[0]);
					//s = s1[2].split(" ");	//�������������µĲ���ʽ
				}
				//else
					//s = null; 
				if(!nextCont(it,s2)) {
					it.add(s2);
					System.out.println(s2[0]+","+s2[1]+","+s2[2]+","+s2[3]);
				}
				}
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

public static boolean nextCont(ArrayList<String[]> next,String[] s) {
	for(String[] temp : next) {
		int c = 0;
		for(String st : temp) {
			if(st == null) {
				if(s[c] != null) break;
			}
			else{
				if(!st.equals(s[c])) break; 
			}
			++c;
		}
		if(c == 4) return true;
	}
	return false;
} 
}