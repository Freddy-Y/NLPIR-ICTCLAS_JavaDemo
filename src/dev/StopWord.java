package dev;

import java.util.*;

public class StopWord {
		public static List stopWord(List outList,List stopList){
			List result = new ArrayList();
			String temp = null;
			String temp1 = null;
			
			Iterator stopIterator = stopList.iterator();
			int i = 1;
			while(stopIterator.hasNext()){
				temp = (String) stopIterator.next();
				System.out.println("--------------------------------1"+temp);
				Iterator outIterator = outList.iterator();
				while (outIterator.hasNext()) {
					temp1=(String)outIterator.next();
					if(temp.equals(temp1)||temp1==temp){
						outIterator.remove();
						System.out.println(i++);
					}
//					System.out.println(i++);
				}
			}
			result = outList;
			return result;
		}
}
