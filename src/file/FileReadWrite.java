package file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class FileReadWrite {
	
/*
 *读取文件的方法，返回值是String类型 
 *参数是文件路径
 */
	public static String readFileToString(String path) {
		String result = null;
		File inputFile = new File(path);
		try {
			FileInputStream fileInputStream = new FileInputStream(inputFile);
			int fileSize = fileInputStream.available();
			byte[] buffer = new byte[fileSize];
			 fileInputStream.read(buffer);
			 result = new String(buffer, "utf-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/*
	 * 
	 * 将String类型的变量存在文件中，有两种方式：
	 * 		1.追加在文件尾
	 * 		2.覆盖源文件
	 * 参数列表：
	 * file ：存的字符串
	 * path：存的文件路径
	 * append: 是否追加在文件尾
	 * 			false：覆盖源文件
	 * 			true：追加在文件尾
	 * 	 */
	public static void writeFile(String file,String path,boolean append){
		byte[] buffer = file.getBytes();
		try {
			File writeFile = new File(path);
			FileOutputStream fileOutputStream = new FileOutputStream(path,append);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
			fileOutputStream.write(buffer, 0, buffer.length);
			fileOutputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("文件存储失败");
			e.printStackTrace();
		}
	}
	
	
}
