
package smithstimeclock;

import java.io.BufferedWriter;
import java.io.IOException;

/**

 @author R-Mule
 */
public class FileWriter {
    BufferedWriter bw = null;
    java.io.FileWriter fw = null;

   public  FileWriter(){
        
    }

    public boolean writeFile(String fileName, String data){//returns true if wrote successfully
         try
        {
     fw = new java.io.FileWriter(fileName);//Report for Hollie
            bw = new BufferedWriter(fw);
            bw.write(data + "\n");
        }
        catch (IOException e)
        {

            e.printStackTrace();
            return false;

        }
        finally
        {

            try
            {

                if (bw != null)
                {
                    bw.close();
                }

                if (fw != null)
                {
                    fw.close();
                }
                return true;//successful write
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                return false;

            }

        }
    }}
