package com.starteam.network.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by KevinHo on 2016/8/12 0012.
 */
public enum FileUtils {

    INSTANCE;

    private String SDPath;

    public String getSDPath() {
        return SDPath;
    }

    public void setSDPath(String sDPath) {
        SDPath = sDPath;
    }

    FileUtils() {

        //获取当前外部存储设备的目录
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if(AppInstanceUtils.INSTANCE.getExternalCacheDir() == null){
                SDPath = Environment.getExternalStorageDirectory()+"/Android/data/"+ AppInstanceUtils.INSTANCE.getPackageName()+"/cache/";
            }else {
                SDPath = AppInstanceUtils.INSTANCE.getExternalCacheDir().getPath() + File.separator;
            }
        } else {
            SDPath = AppInstanceUtils.INSTANCE.getCacheDir().getPath()+ File.separator;
        }
    }

    //在sd卡的路径下创建文件
    public File createSDFile(String filename) throws IOException {
        File file = new File(SDPath + filename);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
//       boolean b = file.createNewFile();
        return file;
    }

    //在sd卡的路径下创建文件
    public File createFile(String filename) throws IOException {
        File file = new File(filename);
        file.createNewFile();
        return file;
    }

    //在sd卡的路径下创建目录
    public File createSDDir(String dirname) {
        File dir = new File(dirname);
        boolean result = dir.mkdir();
        return dir;
    }

    //判断文件是否已经存在
    public boolean isFileExist(String filename) {
        File file = new File(SDPath + filename);
        return file.exists();
    }

    //将一个InputStream里的数据写进SD卡
    public File inputSD(String path, String fileName, InputStream inputstream) {
        File file = null;
        OutputStream outputstream = null;
        int byteread = 0; // 读取的字节数
        try {
//            createSDDir(path+File.separator);

            boolean isCreatedDirSuccess = isExitsPath(FileUtils.INSTANCE.getSDPath() + path + File.separator);

            if (!isCreatedDirSuccess) {
                return null;
            }


            file = createSDFile(path + File.separator + fileName);
            outputstream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            while ((byteread = inputstream.read(buffer)) != -1) {
                outputstream.write(buffer, 0, byteread);
            }
            outputstream.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                outputstream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return file;
    }

    public void createFlieInputSD(String path, InputStream inputstream) {
        File file = null;
        OutputStream outputstream = null;
        if (TextUtils.isEmpty(path) || inputstream == null) {
            return;
        }
        int byteread = 0; // 读取的字节数
        try {
            file = new File(path);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            outputstream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            while ((byteread = inputstream.read(buffer)) != -1) {
                outputstream.write(buffer, 0, byteread);
            }
            outputstream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputstream.close();
                inputstream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void copyFile(String path, String fileName, InputStream inputstream) {
        File file = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        OutputStream outputstream = null;
        int byteread = 0; // 读取的字节数
        try {
            file = createSDFile(path + File.separator + fileName);
            outputstream = new FileOutputStream(file);
            dis = new DataInputStream(inputstream);
            dos = new DataOutputStream(outputstream);
            byte[] buffer = new byte[4 * 1024];
            while ((byteread = dis.read(buffer)) != -1) {
                dos.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                outputstream.close();
                dos.close();
                dis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    // 递归删除文件及文件夹
    public static boolean delete(File file) {
        if (file.isFile()) {
            file.delete();
            return true;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return true;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            return file.delete();
        }
        return false;
    }


    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public boolean deleteFile(String filePath) {
        try {
            if (filePath == null) {
                return false;
            }
            File f = new File(filePath);

            if (f == null || !f.exists()) {
                return false;
            }

            // if (f.isDirectory()) {
            // return false;
            // }
            return f.delete();
        } catch (Exception e) {
            // Log.d(FILE_TAG, e.getMessage());
            return false;
        }
    }


//    /**
//     * 复制一个目录及其子目录、文件到另外一个目录
//     * @param src
//     * @param dest
//     * @throws IOException
//     */
//    private void copyFolder(InputStream srcPathIs, File dest) throws IOException {
//
//        File src  = new File()
//
//        if (src.isDirectory()) {
//            if (!dest.exists()) {
//                dest.mkdir();
//            }
//            String files[] = src.list();
//            for (String file : files) {
//                File srcFile = new File(src, file);
//                File destFile = new File(dest, file);
//                // 递归复制
//                copyFolder(srcFile, destFile);
//            }
//        } else {
//            InputStream in = new FileInputStream(src);
//            OutputStream out = new FileOutputStream(dest);
//
//            byte[] buffer = new byte[1024];
//
//            int length;
//
//            while ((length = in.read(buffer)) > 0) {
//                out.write(buffer, 0, length);
//            }
//            in.close();
//            out.close();
//        }
//    }

    //文件流转file类型
//    public void inputStreamToFile(InputStream ins,File file) throws IOException {
//        OutputStream os = new FileOutputStream(file);
//        int bytesRead = 0;
//        byte[] buffer = new byte[8192];
//        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
//            os.write(buffer, 0, bytesRead);
//        }
//        os.close();
//        ins.close();
//    }


    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     */
    public boolean copyFilesAssets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
            return false;
        }
        return true;
    }


    public void copyFile(InputStream inPutStream, String newPath)
            throws Exception {

        isExitsPath(newPath);//判断创建本地放置资源文件路径

        int bytesum = 0;
        int byteread = 0;
        FileOutputStream outPutStream = null;


        try {

            // oldPath的文件copy到新的路径下，如果在新路径下有同名文件，则覆盖源文件
//            inPutStream = new FileInputStream(oldPath);
            outPutStream = new FileOutputStream(newPath);
            byte[] buffer = new byte[4096];

            while ((byteread = inPutStream.read(buffer)) != -1) {
                bytesum += byteread;
                outPutStream.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // inPutStreamを关闭
            if (inPutStream != null) {
                inPutStream.close();
                inPutStream = null;
            }

            // inPutStream关闭
            if (outPutStream != null) {
                outPutStream.close();
                outPutStream = null;
            }

        }
    }

    public boolean isExitsPath(String path) throws InterruptedException {
        String[] paths = path.split(File.separator);
        StringBuffer fullPath = new StringBuffer();
        for (int i = 0; i < paths.length; i++) {
            if (TextUtils.isEmpty(paths[i])) {
                fullPath.append(paths[i]).append(File.separator);
                continue;
            }
            fullPath.append(paths[i]).append(File.separator);
            File file = new File(fullPath.toString());
            if (!file.exists()) {
                boolean result = file.mkdir();
                Thread.sleep(1500);
            }
        }
        File file = new File(fullPath.toString());//目录全路径
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 读取输入流数据
     * //此方法是用于缓存H5网络请求数据，解决inputStream对象不能重复复用的问题
     */
    public static byte[] streamToJson(InputStream uristream) {
        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = uristream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            return outStream.toByteArray();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if(uristream !=null) {
                    uristream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 使用文件通道的方式复制文件
     *
     * @param s 源文件
     * @param t 复制到的新文件
     */
    public void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取缓存文件夹
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if(AppInstanceUtils.INSTANCE.getExternalCacheDir() == null){
                cachePath = Environment.getExternalStorageDirectory()+"/Android/data/"+ AppInstanceUtils.INSTANCE.getPackageName()+"/cache/";
            }else {
                cachePath = AppInstanceUtils.INSTANCE.getExternalCacheDir().getPath() + File.separator;
            }
        } else {
            cachePath = AppInstanceUtils.INSTANCE.getCacheDir().getPath();
        }
        return new File(cachePath+ File.separator  + uniqueName);
    }

    /**
     * 获取文件MD5名称
     * @param key
     * @return
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 获取手机外部可用存储空间
     * @return 以M,G为单位的容量
     */
    public static long getAvailableExternalMemorySize() {
        long size = 0;
        if(isExternalStorageAvailable()) {
            File file = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(file.getPath());
            long availableBlocksLong = 0;
            long blockSizeLong = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableBlocksLong = statFs.getAvailableBlocksLong();
                blockSizeLong = statFs.getBlockSizeLong();
            }
            size = availableBlocksLong * blockSizeLong;
        }
        if (size == 0) {
            size = 1024 * 1024 * 1024;
        }
        return size;
    }

    //外部存储是否可用 (存在且具有读写权限)
    private static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    public static boolean writeFile(InputStream inputStream, String path, String filename) {
        if (inputStream == null) {
            return false;
        }
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            File fDir = new File(path);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            File file = new File(path, filename);
            if (file.exists() && file.isFile()) {
                file.delete();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] bytes = new byte[2048];

            int len;
            while ((len = inputStream.read(bytes, 0, 2048)) != -1) {
                bos.write(bytes, 0, len);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    public static boolean writeFile(InputStream inputStream, OutputStream outputStream) {
        if (inputStream == null) {
            return false;
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(outputStream);
            byte[] bytes = new byte[2048];
            int len;
            while ((len = inputStream.read(bytes, 0, 2048)) != -1) {
                bos.write(bytes, 0, len);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
