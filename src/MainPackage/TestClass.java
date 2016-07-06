
import Classes.FileGestionaire;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bidau on 06/07/2016.
 */
public class TestClass {
    public static void main(String[] args) throws IOException {
        String fileName = "01 Main Title and The Attack on the.m4a";
        File f = new File(fileName);

        List<File> files = getPartFile(fileName+".txt");
        FileGestionaire.mergeFiles(files, f);
        if(f!=null){
            System.out.println("ok");
        }else{
            System.out.println("not ok");
        }
        //FileGestionaire.splitFile(f);
    }

    private static List<File> getPartFile(String fileName){
        ArrayList<File> files = new ArrayList<>();
        File file = new File(fileName);
        if(file.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = "";
                File f;
                while ((line = br.readLine())!=null){
                    f = new File(line);
                    if(f.exists()){
                        files.add(f);
                    }
                }
                br.close();
                return files;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }else{
            return null;
        }

    }

}
