package file;

import uk.ac.ebi.pride.tools.braf.BufferedRandomAccessFile;

import java.io.File;
import java.io.IOException;

public class SplitFileReader extends BufferedRandomAccessFile {
    private static volatile int endCounter = 0;
    private final static int BUF_SIZE = 8192;
    private final static int SINGLE_SIZE = 1000000;
    private static final String MODE = "r";
    private long allSize;
    private int singleSize;
    private int  blockCount;
    private long leftover;
    private byte[][] allContent;
    public SplitFileReader(String filename, String mode,int singleSize) throws IOException {
        super(filename, mode, BUF_SIZE);
        this.singleSize = singleSize;
        this.allSize = this.getChannel().size();
        setPosition();
    }

    public SplitFileReader(File file, String mode,int singleSize) throws IOException {
        this(file.getAbsolutePath(), mode,singleSize);
    }

    public SplitFileReader(File file,int singleSize) throws IOException {
        this(file, MODE,singleSize);
    }

    public SplitFileReader(File file) throws IOException {
        this(file, MODE,SINGLE_SIZE);
    }


    public long getCharSize() {
        return this.allSize;
    }

    private void setPosition() {
        this.blockCount = (int) (this.allSize / this.singleSize);
        this.leftover = this.allSize % this.singleSize;
        if (this.leftover != 0) {
            this.blockCount += 1;
        }
        System.out.println("剩余： "+leftover + "\t单个线程读取字符："+this.singleSize+"\t分割块： "+this.blockCount);


    }

    private synchronized byte[]  readByPosition(int i) throws IOException {

        long start = i* this.singleSize ;
        this.seek(start);
        byte[] bytes = new byte[this.singleSize];
        if(i == this.blockCount-1 && this.leftover > 0){
            bytes = new byte[(int)this.leftover];
        }
        this.read(bytes,0,this.singleSize);
        return bytes;
    }

    public ReadThread[] startReadThreads(){
        int length = this.blockCount;
        ReadThread[] readThreads = new ReadThread[length];
        this.allContent = new byte[length][this.singleSize];
        for (int i=0;i<length;i++){
            readThreads[i] = new ReadThread(i,this.allContent);
            new Thread(readThreads[i]).start();
        }
        while(endCounter!=this.blockCount){

        }
        return readThreads;
    }
    public byte[][] getAllContent(){
        return this.allContent;
    }

    class ReadThread implements Runnable{
        private  int index;
        private byte[][] contents;

        public ReadThread(int i,byte[][] contents){
            this.index = i;
            this.contents = contents;
        }
        @Override
        public synchronized   void run() {

            try {

             this.contents[this.index] = readByPosition(this.index);
             endCounter++;
            } catch (Exception e) {
                System.out.println(index);
                e.printStackTrace();
            }

        }
    }

}
