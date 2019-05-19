package file;



import uk.ac.ebi.pride.tools.braf.BufferedRandomAccessFile;

import java.io.*;
import java.util.Date;

public class ReadFile {
    public static void main(String[] args) throws Exception {
        File file = new File("/Users/thirteen/git/doCtrl.txt");
        long time = new Date().getTime();

        //readAccess(file);
//         readByByte(file);
       readByChar(file);
//       readByLine(file);
        long time1 = new Date().getTime();
        //System.out.println("allChars"+splitFileReader.getCharSize());
        System.out.println("time:   " + (time1 - time));
    }


    public static void readByByte(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        int l = 0;
        byte[] bs = new byte[(int) file.length()];
        while ((l = fileInputStream.read(bs)) != -1) {
            System.out.println(new String(bs, "GB2312"));

        }
        fileInputStream.close();
    }

    public static void readByChar(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        BufferedRandomAccessFile raf = new BufferedRandomAccessFile("/Users/thirteen/git/temp.txt", "rw",8192);
        long length = file.length();
        int l = 0;
        char[] chars = new char[(int) length];
        while ((l = reader.read(chars) )!= -1) {
//            System.out.print("sdasdas\t"+new String(chars));
//            raf.writeBytes(new String(chars));

        }
        reader.close();
        raf.close();
    }

    public static void readByLine(File file) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String temp = null;
        int l = 0;

        while (true) {
            temp = bufferedReader.readLine();
            if (temp == null)
                break;
//            String[] split = temp.split("[|]",-1);
            System.out.println(l + "  " + temp);
            l++;

        }
//        Stream<String> lines = bufferedReader.lines();
//       // System.out.println(lines.count());
//        Object[] objects = lines.toArray();
//        for (Object s : objects){
//            System.out.println(l+"  "+ s);
//            l++;
//        }
        bufferedReader.close();

    }

    public static void readAccess(File file) throws IOException {
        SplitFileReader splitFileReader = new SplitFileReader(file,100000);
        splitFileReader.startReadThreads();
        byte[][] allContent = splitFileReader.getAllContent();
        StringBuilder stringBuilder = new StringBuilder();
        RandomAccessFile raf = new RandomAccessFile("/Users/thirteen/git/temp.txt", "rw");
        for (int a = 0; a < allContent.length; a++) {
            stringBuilder.append(new String(allContent[a]));
          raf.write(allContent[a]);
        }
        splitFileReader.close();
        System.out.println(stringBuilder.toString());
        raf.close();
    }
}
