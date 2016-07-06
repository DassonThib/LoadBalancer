package Classes;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by bidau on 06/07/2016.
 */
public class FileGestionaire {

    public static void splitFile(File f) throws IOException {
        int partCounter = 1;//I like to name parts from 001, 002, 003, ...
        //you can change it to 0 if you want 000, 001, ...
        File partList = new File(f.getName()+".txt");
        FileWriter fileWriter = new FileWriter(partList);
        int sizeOfFiles = 1024 * 1024;// 1MB
        byte[] buffer = new byte[sizeOfFiles];

        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(f))) {//try-with-resources to ensure closing stream
            String name = f.getName();

            int tmp = 0;
            String nameFile = "";
            while ((tmp = bis.read(buffer)) > 0) {
                //write each chunk of data into separate file with different number in name
                nameFile = name + "." + String.format("%03d", partCounter++);
                File newFile = new File(f.getParent(), nameFile);
                fileWriter.write(nameFile+"\n");
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, tmp);//tmp is chunk size
                }
            }
            fileWriter.close();
        }
    }

    public static void mergeFiles(List<File> files, File into)
            throws IOException {
        try (BufferedOutputStream mergingStream = new BufferedOutputStream(
                new FileOutputStream(into))) {
            for (File f : files) {
                Files.copy(f.toPath(), mergingStream);
            }
        }
    }


}
