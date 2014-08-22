/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io.binary;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gpetkos
 */
public class BinaryRW {
    public static byte[] read(String aInputFileName){
        System.out.println("Reading in binary file named : " + aInputFileName);
        File file = new File(aInputFileName);
        System.out.println("File size: " + file.length());
        byte[] result = new byte[(int)file.length()];
        try {
        InputStream input = null;
        try {
            int totalBytesRead = 0;
            input = new BufferedInputStream(new FileInputStream(file));
            while(totalBytesRead < result.length){
            int bytesRemaining = result.length - totalBytesRead;
            //input.read() returns -1, 0, or more :
            int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
            if (bytesRead > 0){
                totalBytesRead = totalBytesRead + bytesRead;
            }
            }
            /*
            the above style is a bit tricky: it places bytes into the 'result' array; 
            'result' is an output parameter;
            the while loop usually has a single iteration only.
            */
            System.out.println("Num bytes read: " + totalBytesRead);
        }
        finally {
            System.out.println("Closing input stream.");
            input.close();
        }
        }
        catch (FileNotFoundException ex) {
        System.out.println("File not found.");
        }
        catch (IOException ex) {
        System.out.println(ex.toString());
        }
        return result;
    }
    
  void write(byte[] aInput, String aOutputFileName){
    System.out.println("Writing binary file...");
    try {
      OutputStream output = null;
      try {
        output = new BufferedOutputStream(new FileOutputStream(aOutputFileName));
        output.write(aInput);
      }
      finally {
        output.close();
      }
    }
    catch(FileNotFoundException ex){
      System.out.println("File not found.");
    }
    catch(IOException ex){
      System.out.println(ex);
    }
  }    
    
  public static int readLittleEndianInt(RandomAccessFile in){
    int nextInt=0;
    try {
        byte byte0 = in.readByte();
        byte byte1=in.readByte();
        byte byte2=in.readByte();
        byte byte3=in.readByte();
        nextInt = (byte0 & 0xFF) | (byte1 & 0xFF) << 8 | (byte2 & 0xFF) << 16 | (byte3 & 0xFF) << 24;
        return nextInt;
    } catch (IOException ex) {
        Logger.getLogger(BinaryRW.class.getName()).log(Level.SEVERE, null, ex);
    }
    return nextInt;
  }
  
    
  
  public static float readLittleEndianFloat(RandomAccessFile in){
    float nextFloat=0;
    try {
        int accum = 0;
        for ( int shiftBy=0; shiftBy<32; shiftBy+=8 )
        {
           accum |= ( in.readByte () & 0xff ) << shiftBy;
        }
        return Float.intBitsToFloat( accum );        
    } catch (IOException ex) {
        Logger.getLogger(BinaryRW.class.getName()).log(Level.SEVERE, null, ex);
    }
    return nextFloat;
  }

  
  
}
