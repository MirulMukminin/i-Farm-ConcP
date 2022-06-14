package ifarm.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class UpdateLog {
    private List<String> delete = new ArrayList<String>();

    public UpdateLog(List<String> delete) {
        this.delete = delete;
    }
    
    //rename txt file
    public void rename(String file1, String file2){
        File oldName = new File("ActivityLog.txt");
        File newName = new File("ActivityLogOld.txt");
        
        oldName.renameTo(newName);
    }
    
    //update txt file
    public void update() throws FileNotFoundException, IOException{
        String file1 = "ActivityLog.txt";
        String file2 = "ActivityLogOld.txt";
        
        //rename the previous activity log
        rename(file1,file2);
        
        // PrintWriter object for new activity log
        PrintWriter pw = new PrintWriter(file1);
                  
        // BufferedReader object for old activity log
        BufferedReader br1 = new BufferedReader(new FileReader(new File(file2)));
          
        String line1 = br1.readLine();
          
        // loop for each line of old activity log
        while(line1 != null)
        {
            boolean flag = false;
             
            for(int i=0;i<delete.size();i++){
                if(line1.contains(delete.get(i)))
                flag = true;
            }
                                      
            // if flag = false
            // write line of old activity log to new activity log
            if(!flag)
                pw.println(line1);
              
            line1 = br1.readLine();
              
        }
          
        pw.flush();
          
        // closing resources
        br1.close();
        pw.close();
          
        System.out.println("File operation [delete] performed successfully");
        
    }

}

