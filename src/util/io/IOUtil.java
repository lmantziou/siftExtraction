/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author gpetkos
 */
public class IOUtil {

    public static String readFileAsString(String filename) {
        StringBuffer fileData = new StringBuffer(1000);
        try{
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
            reader.close();
        }
        catch(IOException e){
        }
        return fileData.toString();
    }
    
    
    public static List<String> readFileToStringList(String textFile){
		List<String> stringList = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(textFile), StringConstants.UTF8));
			String line = null;
			while ( (line = reader.readLine()) != null){
				stringList.add(line);
			}
			reader.close();
		} catch (IOException e){
			e.printStackTrace();
			if (reader != null){
				try {
					reader.close();
				} catch (IOException ex){
					ex.printStackTrace();
				}
			}
		}
		return stringList;
	}
	
	
	public static void writeStringCollectionToFile(Collection<String> stringCollection, String textFile){
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(textFile), StringConstants.UTF8));
			for (String line : stringCollection){
				writer.append(line);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e){
			if (writer != null){
				e.printStackTrace();
				try {
					writer.close();
				} catch (IOException ex){
					ex.printStackTrace();
				}
			}
		}
	}
	
}
