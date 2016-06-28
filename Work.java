/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depthfirstsearch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author iliaaa
 */
public class Work {
    public static ArrayList<String> ReadFile(String filename) throws IOException
    {
        BufferedReader br = new BufferedReader (new InputStreamReader(
        new FileInputStream(filename)));
        ArrayList<String> fileContent = new ArrayList<String>();
        String str;
        while((str=br.readLine())!= null)
        {
            fileContent.add(str);
            //System.out.print(str);
        }
        //System.out.println(fileContent);
        br.close();
        return fileContent;
    }
    public static void WriteFile(ArrayList<String> InPut, String filename) throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename,false));
        
        for(int i=0; i<InPut.size(); i++)
        {
            bw.write(InPut.get(i));
            bw.write("\r\n");
        }
        bw.close();
    }
}