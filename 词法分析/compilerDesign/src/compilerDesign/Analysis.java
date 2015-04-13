package compilerDesign;

import java.util.*;

public class Analysis {
	int curNum;
	int preNum; 
	char[] buffer; 
	Analysis(int curNum,char[] buffer) {
		this.curNum = curNum; 
		this.buffer = buffer;
	}
	//���ڵ��ʣ�Ҫ������Ӧ���Զ���ʶ��
	public int analysisWord() {
		System.out.println("��������");
		StringBuilder word = new StringBuilder();
		char ch = buffer[curNum];
		preNum = curNum;
		while((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_' || (ch >='1' && ch <= '9') || ch == '$') {
			word.append(ch);
			preNum++;
			if(buffer[preNum] == '��'){
				if(!LexicalAnalyzer.tag){
					if(!LexicalAnalyzer.full){	//����һ�벻�Ǹ��µ������ݻ�δ��ȡ��ϵ�ʱ�򣬷��ض�ȡ�µ�����
						if(preNum >= buffer.length-1) 
							return -1;
						else return -2;
					} 
				}
				else{						//����һ�����µ�ʱ�򣬵�����һ���ͷ����ʼʶ��
					if(preNum >= buffer.length-1)
						preNum = -1;
					else 
						preNum = 1023;
				}
			}
			ch = buffer[preNum];
		} 	//��ʾ��ǰ�Ѿ�ʶ���˻�������ĩβ��δ��������Ҫ��ǰ��ε�������Ҫ���¶�ȡ
		FindCode.findCode(word.toString());
		return preNum - 1;	//����Ļ��ӱ�������ҵ��ô������ڵ���𣬷�����token�� 
	}
	//�������֣�ʶ����Ϊ�������Ǹ�������������Ӧ�Զ���
	public int analysisDigit() {
		System.out.println("����������");
		StringBuilder Digit = new StringBuilder();
		char num = buffer[curNum];
		boolean flag = false;
		preNum = curNum;
		while((num >= '0' && num <= '9') || num == '.') {
			Digit.append(num);
			preNum++;
			if(num == '.') flag = true;  
			if(buffer[preNum] == '��'){
				if(!LexicalAnalyzer.tag){
					if(!LexicalAnalyzer.full){	//����һ�벻�Ǹ��µ������ݻ�δ��ȡ��ϵ�ʱ�򣬷��ض�ȡ�µ�����
						if(preNum >= buffer.length-1) 
							return -1;
						else return -2;
					} 
				}
				else{						//����һ�����µ�ʱ�򣬵�����һ���ͷ����ʼʶ��
					if(preNum >= buffer.length-1)
						preNum = -1;
					else 
						preNum = 1023;
				}
			}
			num = buffer[preNum];
		}
		List<String> tokens = new ArrayList<String>();
		if(flag)
			tokens.add("REAL");
		else 
			tokens.add("INT_NUMBER");
		tokens.add(Digit.toString());
		LexicalAnalyzer.token.add(tokens); 
		System.out.println(tokens);
		return preNum - 1;
	}
	//�������ţ���Ҫʶ�������Ƿ����
	public int analysisBrackets(){
		System.out.println("��������");
		char ch = buffer[curNum]; 
		preNum = curNum; 
		List<String> tokens = new ArrayList<String>();
		switch(ch)
		{
		case '(':
			tokens.add("LR_BRAC");
			++LexicalAnalyzer.BrackTag[0]; 
			break;
		case ')':
			tokens.add("RR_BRAC");
			--LexicalAnalyzer.BrackTag[0];
			break;
		case '[':
			tokens.add("LS_BRAC");
			++LexicalAnalyzer.BrackTag[1]; 
			break;
		case ']':
			tokens.add("RS_BRAC");
			--LexicalAnalyzer.BrackTag[1];
			break;
		case '{':
			tokens.add("BIG_LR_BRAC");
			++LexicalAnalyzer.BrackTag[2]; 
			break;
		case '}':
			tokens.add("BIG_RR_BRAC");
			--LexicalAnalyzer.BrackTag[2];
			break;
		case '\'':
			tokens.add("COMMA");
			if(LexicalAnalyzer.BrackTag[3] == 0)
				++LexicalAnalyzer.BrackTag[3];
			else
				--LexicalAnalyzer.BrackTag[3];
			break;
		case '\"':
			tokens.add("DOUBLE_COMMA");
			if(LexicalAnalyzer.BrackTag[4] == 0)
				++LexicalAnalyzer.BrackTag[4];
			else
				--LexicalAnalyzer.BrackTag[4];
			break; 
		default:
		}
		tokens.add(null);
		LexicalAnalyzer.token.add(tokens);
		System.out.println(tokens);
		return preNum; 
			
	} 
	
	public int analysisAnnotation()
	{
		System.out.println("ע��ƥ��");
		char ch = buffer[curNum];
		preNum = curNum;
		if(buffer[++preNum] == '��'){
			if(preNum >= buffer.length-1) 
				return -1;
			else return -2;
		}
		else if(buffer[preNum] == ch){
			while(buffer[++preNum] != '\n'){
			if(buffer[preNum] == '��'){
				if(!LexicalAnalyzer.tag){
					if(!LexicalAnalyzer.full){	//����һ�벻�Ǹ��µ������ݻ�δ��ȡ��ϵ�ʱ�򣬷��ض�ȡ�µ�����
						if(preNum >= buffer.length-1) 
							return -1;
						else return -2;
					}
					else 
						return 2048;
				}
				else{						//����һ�����µ�ʱ�򣬵�����һ���ͷ����ʼʶ��
					if(preNum >= buffer.length-1)
						preNum = 0;
					else 
						preNum = 1024;
				}
			}
		}
		return preNum;
		}
		else if(buffer[preNum] == '*') {
			while(buffer[++preNum] != '*' || (preNum < buffer.length-1 && buffer[++preNum] != '/')){
				if(buffer[preNum] == '��'){
					if(!LexicalAnalyzer.tag){
						if(!LexicalAnalyzer.full){	//����һ�벻�Ǹ��µ������ݻ�δ��ȡ��ϵ�ʱ�򣬷��ض�ȡ�µ�����
							if(preNum >= buffer.length-1) 
								return -1;
							else return -2;
						}
						else 
							return 2048;
					}
					else{						//����һ�����µ�ʱ�򣬵�����һ���ͷ����ʼʶ��
						if(preNum >= buffer.length-1)
							preNum = 0;
						else 
							preNum = 1024;
					}
				}
			} 
			return preNum;
		}
		else{
			List<String> tokens = new ArrayList<String>();
			if(buffer[++preNum] == '=')
				tokens.add("RDIV_EQ");
			else tokens.add("RDIV");
		}
		return preNum;
	}
	public int analysisOperator() {
		System.out.println("���ַ���ƥ��");
		char ch = buffer[curNum];
		preNum = curNum;  
		for(int i = 0;i < 3 && LexicalAnalyzer.tag;i++)
			if(buffer[preNum+i] == '��'){ 
					if(!LexicalAnalyzer.tag){
						if(!LexicalAnalyzer.full){	//����һ�벻�Ǹ��µ������ݻ�δ��ȡ��ϵ�ʱ�򣬷��ض�ȡ�µ�����
							if(preNum+i >= buffer.length-1) 
								return -1;
							else return -2;
						}
						else 
							return 2048;
					}
					else{						//����һ�����µ�ʱ�򣬵�����һ���ͷ����ʼʶ��
						if(preNum >= buffer.length-1)
							preNum = 0;
						else 
							preNum = 1024;
					}
				} 
		List<String> tokens = new ArrayList<String> ();
		switch(ch)
		{ 
		case '<':
			if(buffer[preNum + 1] == '>'){
				tokens.add("NE"); 
				preNum += 1;
			}
			else if(buffer[preNum + 1] == '='){
				tokens.add("LE"); 
				preNum += 1;
			}
			else if(buffer[preNum + 1] == '<'){
				if(buffer[preNum + 2] == '='){
					tokens.add("LR_EQ"); 
					preNum += 2;
				}
				tokens.add("LR");
				preNum +=1;
			}
			break;
		case '>':
			if(buffer[preNum + 1] == '='){
				tokens.add("BE");
				preNum += 1;
			}
			else if(buffer[preNum] == '>'){
				if(buffer[preNum + 1] == '>'){
					if(buffer[preNum + 2] == '='){
						tokens.add("NRR_EQ");
						preNum += 2;
					}
					tokens.add("NRR");
					preNum += 1;
				}
				else if(buffer[preNum] == '=')
					tokens.add("RR_EQ");
				else tokens.add("RR");
			}
			break; 
		case '+':
			if(buffer[preNum + 1] == '+'){
				tokens.add("DOUBLE_PLUS");
				preNum += 1;
			}
			else if(buffer[preNum + 1] == '='){ 
				tokens.add("PLUS_EQ");
				preNum += 1;
			}
			else
				tokens.add("PLUS");
			break;
		case '-':
			if(buffer[preNum + 1] == '-'){
				tokens.add("DOUBLE_SUB");
				preNum += 1;
			}
			else if(buffer[preNum + 1] == '='){
				tokens.add("SUB_EQ");
				preNum += 1;
			}
			else tokens.add("SUB");
			break;
		case '*':
			if(buffer[preNum + 1] == '='){
				tokens.add("MULTI_EQ");
				preNum += 1;
			}
			else
				tokens.add("MULTI");
			break;
		case '%' :
			if(buffer[preNum + 1] == '='){
				tokens.add("REMAIN_EQ");
				preNum += 1;
			}
			else
				tokens.add("REMAIN");
			break;
		case '&' :
			if(buffer[preNum + 1] == '&'){ 
				tokens.add("DOUBLE_ADD");
				preNum += 1;
			}
			else if(buffer[preNum + 1] == '='){
					tokens.add("ADD_EQ");
					preNum += 1;
			}
			break;
		case '|':
			if(buffer[preNum + 1] == '|'){
				tokens.add("DOUBLE_OR");
				preNum += 1;
			}
			else if(buffer[preNum + 1] == '='){
				tokens.add("OR_EQ");
				preNum += 1;
			}
			else tokens.add("SIGAL_OR");
			break;
		case '^':
			if(buffer[preNum + 1] == '='){
				tokens.add("CAP_EQ");
				preNum += 1;
			}
			else
				tokens.add("CAP");
			break;
		default:	//������ŵ���������Լ���ѯ
			if(ch != '\b' && ch != '\t' && ch != '\n' && ch != '\r' && ch != '\f' && ch != ' '){
				StringBuilder word = new StringBuilder();
				word.append(ch); 
				FindCode.findCode(word.toString());
				return preNum;
			}
			else return preNum;
		}
		tokens.add(null);
		LexicalAnalyzer.token.add(tokens);
		System.out.println(tokens);
		return preNum;
		} 	
	} 
