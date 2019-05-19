package test;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.apache.commons.io.FileUtils.copyDirectory;
import static org.apache.commons.io.FileUtils.copyDirectoryToDirectory;

import static test.ZipUtil.deleteDirectory;


public class Pack {

    private final static String NAME_SPACE = "Shared Archive.sar,Process Archive.par,lib.zip";
    private String fileName;
    private File root;

    public Pack(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入解压文件名：");

        if (scanner.hasNextLine()) {
            String fileName = scanner.nextLine();
            //解压主文件
            Pack pack = new Pack(fileName);
            pack.init();
            scanner.close();
        }
    }

    //筛选出需要的文件
    private List selectFile(String[] fs, List list) {
        for (String f : fs
        ) {
            File file = new File(f);
            if (NAME_SPACE.contains(file.getName())) {
                list.add(file);
            }
        }
        return list;
    }

    private void init() throws IOException {
        List<File> fileNames = getFileNames();
        boolean b = packFile(fileNames);
        System.out.println("执行完成：" + b);
    }

    //使用Scanner手动输入获取需要处理的文件名
    private List<File> getFileNames() {
        root = ZipUtil.unzipToCur(fileName);
        ArrayList<File> list = new ArrayList<File>();
        List<File> files = null;
        //解压成功
        if (root != null) {
            String path = root.getPath();
            String[] list1 = root.list();
            for (int i = 0; i < list1.length; i++) {
                list1[i] = path + File.separator + list1[i];
            }
            files = selectFile(list1, list);
        }
        return files;

    }

    //打包文件
    private boolean packFile(List<File> list) throws IOException {
        ArrayList<File> files = new ArrayList<File>();
        for (File f : list
        ) {
            //修改文件名
            String s = renameFile(f);
            //解压文件
            File file = ZipUtil.unzipToCur(s);
            files.add(file);
        }
        // 合并文件
        String s = mergeFiles(files);
        deleteDirectory(root.getPath());
        System.out.println("删除" + root.getPath());
        return true;
    }

    /**
     * 将压缩文件名都改为“zip”
     *
     * @param file
     * @return
     */
    private String renameFile(File file) {
        boolean isZip = file.getPath().endsWith(".zip");
        if (isZip || file.isDirectory()) {
            return file.getPath();
        }
        String substring = file.getPath().substring(0, file.getPath().lastIndexOf("."));
        String newName = substring + ".zip";
        boolean b = file.renameTo(new File(newName));
        return newName;
    }


    //合并文件
    private String mergeFiles(List<File> files) throws IOException {

        String fileName1 = root.getPath()+".0";
        //创建一个合并的目录
        File fileAll = new File(fileName1);

        System.out.println("创建合并文件：" + fileAll.getPath());
        //遍历文件列表，将文件copy至合并目录中
        for (File file : files
        ) {
            System.out.println("复制：" + file.getPath() + "到" + fileAll.getPath());
            if (file.getName().equals("lib")) {
                copyDirectoryToDirectory(new File(file.getPath() + File.separator + "WEB-INF" + File.separator + "lib"), fileAll);
            } else {
                copyDirectory(file, fileAll);
            }
        }
        //返回合并目录名
        return "success";
    }
}
