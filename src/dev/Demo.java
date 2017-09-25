package dev;

import java.math.BigDecimal;
import java.util.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import dev.CLibrary;

import java.io.*;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

import file.*;

public class Demo {
 
    public static void main(String[] args) throws Exception {
        //��ʼ��
        CLibrary instance = (CLibrary)Native.loadLibrary("E:\\ICTCLAS2016\\20140928\\lib\\win64\\NLPIR", CLibrary.class);
        int init_flag = instance.NLPIR_Init("", 1, "0");
        String resultString = null;
        if (0 == init_flag) {
            resultString = instance.NLPIR_GetLastErrorMsg();
            System.err.println("��ʼ��ʧ�ܣ�\n"+resultString);
            return;
        }
         
        String sInput = FileReadWrite.readFileToString(".//text//inn.txt");
        String[] strInput = sInput.split("\r\n");
        int numFile = strInput.length;
        String stopFile[] = new String[numFile];	
        System.out.println(numFile);
        int index_file = 0;
        String stopWord = FileReadWrite.readFileToString(".//text//stopword.txt");
        String[] strStopWord = stopWord.split("\r\n");		
        System.out.println(strInput[0]);
        File afterStopFileS =  new File(".\\text\\afterstop.txt");
        if(afterStopFileS.exists()){
        	afterStopFileS.delete();
        }
        File featFileS =  new File(".\\text\\feat.txt");
        if(featFileS.exists()){
      	  featFileS.delete();
        }
        File outFile =  new File(".//text//out.txt");
        if(outFile.exists()){
        	outFile.delete();
        }
        try {
        	/**
        	 * forѭ���з�ÿ������
        	 */
       
        	for (int i = 0; i < numFile; i++) {
        		resultString = instance.NLPIR_ParagraphProcess(strInput[i], 1);
        		FileReadWrite.writeFile(resultString+"\r\n", ".//text//out.txt",true);
        		/**
        		 * �������еĵ��ʼ����ָһ��ͣ��
        		 */
        		String[] strOutSplit = resultString.split(" ");
                for(int j=0;j<strOutSplit.length;j++){
             	   strOutSplit[j] = strOutSplit[j].split("/")[0];
                }
                List outList = Demo.arrayToList(strOutSplit);
                List stopList = Demo.arrayToList(strStopWord);
                List resultStop = new ArrayList();
                resultStop = StopWord.stopWord(outList, stopList); //去除停用词
                String strAfterStop = resultStop.toString();
                strAfterStop = strAfterStop.replace('[', ' ');
                strAfterStop = strAfterStop.replace(']', ' ');
                strAfterStop = strAfterStop.replace('\n', ' ');
                strAfterStop = strAfterStop.replace(',', ' ');
                System.out.println(strAfterStop); 
                stopFile[index_file] = strAfterStop;
//                strAfterStop = strAfterStop+"\r\n";
                FileReadWrite.writeFile(strAfterStop, ".//text//afterstop.txt", true);
                index_file++;
			}
        	
        	String afterStopFile = FileReadWrite.readFileToString(".//text//afterstop.txt");
        	Set<String> feature = Demo.featureSet(afterStopFile);
        	int numFeature = feature.size();
        	int text_feat[][] = new int[numFile][numFeature];		//词频矩阵
        	String feat_arr[] = new String[numFeature];		//特征词
        	Iterator iterator = feature.iterator();
        	int indexFeat = 0;
        	/*
        	 * �õ���������һ��
        	 */
        	while (iterator.hasNext()) {
        		feat_arr[indexFeat] = (String)iterator.next();
        		System.out.println(feat_arr[indexFeat]);
				indexFeat++;
			}
        	for(int i = 0 ;i <numFeature ; i++){
        		FileReadWrite.writeFile(feat_arr[i]+"  ", ".\\text\\feat.txt", true);
        	}
        	FileReadWrite.writeFile("\r\n", ".\\text\\feat.txt", true);
        	/**
        	 * 
        	 */
        	for (int i = 0; i < numFile; i++) {
        		text_feat[i] = Demo.getFature(feature, stopFile[i], numFeature);
        		
        		for(int j = 0 ;j <numFeature ; j++){
            		FileReadWrite.writeFile(text_feat[i][j]+"  ", ".\\text\\feat.txt", true);
            		System.out.println("每"+(i+1)+"个文本的特征词"+text_feat[i][j]);
            	}
        		FileReadWrite.writeFile("\r\n", ".\\text\\feat.txt", true);
			}
        	
        	
            instance.NLPIR_Exit();
            System.out.println("完毕");       

        } catch (Exception e) {
            System.out.println("������Ϣ��");
            e.printStackTrace();
        }
    }
    
    public static List  arrayToList(String[] source) {
    	List result = new ArrayList();
    	for (int i = 0; i < source.length; i++) {
			result.add(source[i]);
		}
		return result;
	}  
    
    public static Set<String> featureSet(String source){
    	Set<String> result = new HashSet<String>();
    	String[] str = source.split("  ");
    	for (int i = 0; i < str.length; i++) {
			if(str[i]!="\r\n"){
				result.add(str[i]);
				System.out.println("++++++++++"+str[i]);
			}
    	
		}
    	return result;
    }
    
    public static int[] getFature(Set feature,String file,int length){
    	int result[] = new int[length];
    	
    	String source[] = file.split("  ");
    	
    	Iterator iterator = feature.iterator();
    	String temp = null;
    	int numOfFeat ;
    	int test = feature.size();
    	int index = 0;
    	while(iterator.hasNext()){
    		temp = iterator.next().toString();
    		numOfFeat = 0;
    		for (int i = 0; i <source.length; i++) {			
				if(temp==source[i] || source[i].equals(temp)){
					numOfFeat += 1;
					System.out.println(temp+"========"+source[i]);
				}
			}
    		result[index] = numOfFeat;
    		index ++;
    	}
    	return result;
    }
}