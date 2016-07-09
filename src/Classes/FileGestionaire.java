package Classes;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by bidau on 06/07/2016.
 */
public class FileGestionaire {

    public static void splitFile(File f) throws IOException {
        int partCounter = 1;
        File partList = new File(f.getAbsolutePath()+".txt");
        FileWriter fileWriter = new FileWriter(partList);
        int sizeOfFiles = 1024 * 1024;// 1MB
        byte[] buffer = new byte[sizeOfFiles];

        try  {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(f));
            String name = f.getName();
            int tmp = 0;
            String nameFile = "";
            while ((tmp = bis.read(buffer)) > 0) {

                nameFile = name + "." + String.format("%03d", partCounter++);
                File newFile = new File(f.getParent(), nameFile);
                fileWriter.write(nameFile+"\n");
                FileOutputStream out = new FileOutputStream(newFile);
                out.write(buffer, 0, tmp);
                out.close();

            }
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
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
