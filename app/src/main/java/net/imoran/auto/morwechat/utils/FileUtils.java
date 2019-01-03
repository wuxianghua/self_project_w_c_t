package net.imoran.auto.morwechat.utils;

import android.content.Context;
import android.webkit.MimeTypeMap;

import net.imoran.auto.morwechat.bean.Contact;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by shixinhua on 2018/13/30.
 */

public class FileUtils {
    public File headerPath;
    private File dataFileDir;
    private Context context;
    private File msgCacheDir;
    private static FileUtils instance;

    private FileUtils(Context context) {
        this.context=context;
        this.msgCacheDir=new File(context.getFilesDir(),"msg");
        if (!msgCacheDir.exists()){
            msgCacheDir.mkdir();
        }
        headerPath = new File(context.getFilesDir(), "header");
        if (!headerPath.exists()) {
            headerPath.mkdir();
        }
        dataFileDir = context.getFilesDir();
    }

    public static FileUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (FileUtils.class) {
                if (instance == null) {
                    instance = new FileUtils(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public String headerDir(Contact contact){
        String name=contact.getUserName();
        return new File(headerPath,name).getAbsolutePath();
    }
    public String headerDir(String username){
        return new File(headerPath,username).getAbsolutePath();
    }

    public String msgCacheDir(){
        return msgCacheDir.getAbsolutePath();
    }

    public File getDataFileDir() {
        return dataFileDir;
    }

    public static byte[] getFileBytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static String getMimeType(String fileName) {
        String mime= MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileName.substring(fileName.lastIndexOf(".")+1));
        return mime;

    }
    public static int getFileSymbol(File file){
        String name=file.getName();
        if (name.endsWith("gif")||name.endsWith("png")||name.endsWith("jpg")||name.endsWith("jpeg"))
            return 0;
        else if (name.endsWith("mp4")||name.endsWith("avi")
                ||name.endsWith("3gp")||name.endsWith("wmv")
                ||name.endsWith("mkv")||name.endsWith("flv")
                ||name.endsWith("mpeg"))
            return 1;
        else if (name.endsWith("doc")||name.endsWith(".docx")||name.endsWith("xls")||name.endsWith("xlsx")||name.endsWith("ppt")||name.endsWith("pptx"))
            return 2;
        else
            return 3;
    }
}
