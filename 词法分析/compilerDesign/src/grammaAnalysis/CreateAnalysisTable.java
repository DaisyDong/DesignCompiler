//根据自己设计的文法生成相应的分析表
package grammaAnalysis;

import java.util.*;

public class CreateAnalysisTable {
	public static	List<String> var = ReadGramm.Var;
	public static	List<String> end = ReadGramm.End;  
	public static 	ArrayList<String[]> pro = ReadGramm.Pro;//改变产生式形式
	public static String[][] go = new String[400][400];	
	//记录每一个之后还需要继续规约的产生式,用于之后查找产生式
	public static String[][] express = new String[400][400]; 
	public static String[][] go1 = new String[400][400];//用于记录go函数的转移情况 
	public static int k = 0;	//代表每个项目集中的项目个数
	public static int n = 0;	//代表项目集的序号
	public static String[][] CreatTable() {	//返回生成的表 
		ArrayList<ArrayList<String[]>> item = new ArrayList<ArrayList<String[]>>(); 
		ArrayList<String[]> it = new ArrayList<String[]>(); 
		String[] str = new String[4]; 
		str[0] = pro.get(0)[0]; //代表产生式的产生符
		str[1] = null;				//代表已经被推出的符号
		str[2] = pro.get(0)[2];	//代表还未被推出的符号  
		go[n][k++] = str[2].split(" ")[0];
//		express[n][k++] = "#";
		str[3] = "#";				//代表后面的搜索符
		it.add(str);	//得到第一个扩展的产生式 
		creatItem(str,it);//产生第一个项目集  
		item.add(it);
		it = item.get(0);
		int c = 0;
		while(it.size() - c > 0)
			System.out.println(it.get(c++)[3]);
		++n;	//下一个项目集
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
							temp[1] = s2[1]+" "+s1;		//移进已经匹配的符号
						else
							temp[1] = s1;

						//go1[n][j] = spl[0];
						pr = spl[0];	//由父项目集推导产生子项目集的符号，由于还没有确定是否能够成功产生所以还不能直接赋值防止将之前的覆盖
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
//							temp[3] = express[j][i];	//first集等于上一个产生推出它的式子产生的first集
							it1.add(temp); 
							go[n][k++] = spl[1];
							if(var.contains(spl[1]))	//只有产生的是非终结符才会继续有闭包产生
								creatItem(temp,it1);	//first集只有在这里面才需要算，因为是在同一个闭包中的
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
					if(!defContain(item,it1)) {	//可能有循环产生或者之前有产生一样的
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
						if(defEqual(it, it1)) {//对于之前已经产生的项目集相应的转移函数要做处理
							go1[j][j] = pr;	//相等，则自己可以产生自己
							System.out.println(j + "go" + j +"go" + go1[j][j]); 
						}
						else {						//不等，则由它可以产生之前的某个项目集
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
		//由go函数构造分析表
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
			System.out.println("第" + l + "个项目集："); 
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
								String[] st = p[3].split(" ");	//分离超前搜索符
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
	//用于判定两个项目集是否相等
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
	//用于判定总项目集中是否含有一个项目
	public static boolean defContain(ArrayList<ArrayList<String[]>> item,ArrayList<String[]> it1) {
		if(item == null || it1 == null)
			return false;
		for(ArrayList<String[]> it : item) {
			if(defEqual(it,it1)) return true;
		} 
		return false;
	}
	//用于找到first集
	public static List<String> findFirst(String str) { 
			String[] s; 
			List<String> first = new ArrayList<String>();	//用于存储一个符号在扩广文法中能产生的所有的first符号
			String st = new String();
			st = str;
			while(var.contains(st)) {	//是非终结符，继续查找
				int m = 0;
				while(pro.size() - m > 0) { 
					if(pro.get(m)[0].equals(st)) {	//有由该变量符产生的产生式
						s = pro.get(m)[2].split(" "); 
						System.out.println(s[0]);
						if(end.contains(s[0])) {	//first的前提：是终结符
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
			first.add(st);	//是终结符
		return first;  
} 
public static boolean isContains(String[][] go,int n,int k,String str) {
	for(int i = 0;i < k;i++) 
		if(go[n][i].equals(str))
			return true;
	return false;
} 

//同一个闭包中，需要考虑first集
//由一个先起产生式(即：是哪一个来产生，用于确定first集)、上一项目集以及一个正在生成的项目集
public static ArrayList<String[]> creatItem(String[] str,ArrayList<String[]> it) {
	String[] s = str[2].split(" ");
	ArrayList<String[]> next = new ArrayList<String[]>();	//确定闭包的生成
	next.add(str);
	int t = 1;
	int e = 1;
	do{						//得到I0项目集
	if(s != null && var.contains(s[0])) {	//第一个符号是变量符，所以要么是从原始符号表中出现要么从上一级中进行移进后出现
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
			if(tag) {	//有由该变量符产生的产生式 
				List<String> first = new ArrayList<String>();
				s2[0] = s1[0];
				s2[1] = s1[1];
				s2[2] = s1[2];
				//求搜索符  
				if(s.length > 1)	
					first = findFirst(s[1]);
				else first.add(str[3]);	
				for(String st : first){
					//对于有多个搜索符，则对每个搜索符都要生成相应一个产生式（项目）
					s2[3] = st;
				if(s1[2] != null) { //即新生成的项目是否还能生成新的生成式
					String s3 = s1[2].split(" ")[0];
					if(s3 != null && !isContains(go,n,k,s3)) { 
						String[] temp = s1[2].split(" ");
						go[n][k++] = temp[0];	//保存所有的下一个要推出的表达式的符号
					}
					if(var.contains(s3) && !nextCont(next,s2)) {//如果是非终结符则在这个项目集中还能依靠其生产别的产生式
						next.add(s2);
						t++; 
					} 
					System.out.println(n+" "+k+" "+s1[2].split(" ")[0]);
					//s = s1[2].split(" ");	//还能由它产生新的产生式
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