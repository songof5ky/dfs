/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depthfirstsearch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Work {

    public static ArrayList<String> readFile(String filePath) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

        ArrayList<String> fileContent = new ArrayList<>();

        String str;

        while ((str = br.readLine()) != null) {
            fileContent.add(str);
        }

        br.close();

        return fileContent;
    }

    public static void writeFile(List<String> inputList, String filePath) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false));

        for (int i = 0; i < inputList.size(); i++) {
            bw.write(inputList.get(i));
            bw.write("\r\n");
        }

        bw.close();
    }
}
