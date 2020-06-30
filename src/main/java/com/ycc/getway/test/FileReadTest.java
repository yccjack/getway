package com.ycc.getway.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileReadTest {
   static File fileRead = new File("E:\\ssk\\file\\file_1.txt");
    static File fileWriter = new File("E:\\ssk\\file\\file_2.txt");
    static String s = "这是需要写入的数据，，，非常可怕的数据，千万别照着念，不然你会累死！！！！";

    static int MB_8 =1024*1024*8*8;
    public static void main(String[] args)throws Exception {
       //fileDispos();
        writeFile();
    }
    private static void writeFile() throws IOException {
        FileOutputStream ops = new FileOutputStream(fileRead);
        while (true){
            ops.write( s.getBytes());
        }
    }

    private static void fileDispos() {
        int count =0;
        try (FileOutputStream ops = new FileOutputStream(fileWriter);
             FileInputStream ios = new FileInputStream(fileRead);
             FileChannel channelRead = ios.getChannel();
             FileChannel channelWriter = ops.getChannel();){
            ByteBuffer byteBuf = ByteBuffer.allocate(MB_8);
            while ((count=channelRead.read(byteBuf))>-1){
                byteBuf.flip();
                while (byteBuf.hasRemaining()) {
                    channelWriter.write(byteBuf);
                }
                byteBuf.compact();
                System.out.println("已写入64MB文件，暂停0.1秒");
                Thread.sleep(100);
            }
        }catch (Exception e){
        e.printStackTrace();
        }
    }
}
