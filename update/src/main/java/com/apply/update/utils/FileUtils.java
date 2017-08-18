package com.apply.update.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.apply.update.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


//import org.apache.http.util.EncodingUtils;

/**
 * Created by 冯子杰(fengzijie@machine.com)
 * Date: 14/12/15
 */
public class FileUtils {



    /**
     * 递归查找文件夹下文件的大小，单位:byte
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (!dir.exists()) return 0;
        long size = 0;
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) return 0;
        for (File file : files) {
            if (file.isDirectory())
                size += getDirSize(file);
            else
                size += file.length();
        }
        return size;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static byte[] readDataOfFile(File file) {
        if (file == null || !file.exists()) return null;

        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return bytes;
    }



    public static boolean verifyFile(String packagePath, String crc) throws NoSuchAlgorithmException, IOException {
        MessageDigest sig = MessageDigest.getInstance("MD5");
        File packageFile = new File(packagePath);
        InputStream signedData = new FileInputStream(packageFile);
        byte[] buffer = new byte[4096];//每次检验的文件区大小
        long toRead = packageFile.length();
        long soFar = 0;
        while (soFar < toRead) {
            int read = signedData.read(buffer);
            soFar += read;
            sig.update(buffer, 0, read);
        }
        byte[] digest = sig.digest();
        String digestStr = bytesToHexString(digest);//将得到的MD5值进行移位转换
        if (digestStr == null) return false;

        digestStr = digestStr.toLowerCase();
        crc = crc.toLowerCase();
        if (digestStr.equals(crc)) {//比较两个文件的MD5值，如果一样则返回true
            return true;
        }

        return false;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        int i = 0;
        while (i < src.length) {
            int v;
            String hv;
            v = (src[i] >> 4) & 0x0F;
            hv = Integer.toHexString(v);
            stringBuilder.append(hv);
            v = src[i] & 0x0F;
            hv = Integer.toHexString(v);
            stringBuilder.append(hv);
            i++;
        }
        return stringBuilder.toString();
    }

    public static String loadStringContentFromFile(String filePath) {
        try {
            File file = new File(filePath);
            InputStream in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            StringBuilder buffer = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();
            return buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    static void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();
    }

    public static void deleteDir(String dirPath) {
        File file = new File(dirPath);
        if (file.isDirectory()) {
            DeleteRecursive(file);
        }
    }


    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i].getAbsolutePath()); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

    public static File createDir(String dir) {
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        return fileDir;
    }

    public static void createNoMedia(String dir) {
        try {
            File noMedia = new File(dir + File.separator + ".nomedia");
            if (!noMedia.exists()) {
                try {
                    noMedia.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    public static String readAssertResource(Context context, String fileName) {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                result += line;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 解压zip格式文件
     */
    public static void unzip(InputStream zipFileInputStream, String outputDirName) {
        ZipInputStream zis = null;
        try {
            File dirFile = createDir(outputDirName);
            zis = new ZipInputStream(zipFileInputStream);

            ZipEntry zipEntry = null;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    // 创建目录
                    String dirName = zipEntry.getName();
                    dirName = dirName.substring(0, dirName.length() - 1);
                    String dirPath = dirFile.getPath() + File.separator + dirName;
                    createDir(dirPath);
                } else {
                    // 解压单个文件
                    String filePath = dirFile.getPath() + File.separator + zipEntry.getName();
                    File file = createFile(filePath);
                    if (file.lastModified() != zipEntry.getTime()) {
                        FileOutputStream fos = new FileOutputStream(file);
                        if (fos != null) {
                            int c;
                            byte[] by = new byte[1024];
                            while ((c = zis.read(by)) != -1) {
                                fos.write(by, 0, c);
                                fos.flush();
                            }
                            try {
                                fos.close();
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                        file.setLastModified(zipEntry.getTime());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            if (zipFileInputStream != null) {
                try {
                    zipFileInputStream.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    /**
     * 对文件重命名
     * @param filePath  文件的路径
     */
    public static File chageFileName(String oldFilePath, String oldName, String filePath,String reName){
        File oldFile = new File(oldFilePath, oldName);
        File newFile = new File(filePath, reName);
        oldFile.renameTo(newFile);
        return newFile;
    }

    public static String getAppDir(Context context) {
        StringBuilder sb = new StringBuilder();
        if (checkSDCard()) {
            sb.append(Environment.getExternalStorageDirectory().getPath());
        } else {
            sb.append(context.getCacheDir().getPath());
        }
        String configDir = sb.append(File.separator).append(context.getResources().getString(R.string.app_name)).toString();
        createDir(configDir);
        return configDir;
    }



    /**
     * 创建文件(不存在时创建)
     */
    public static File createFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    public static File saveImage(Context context, Bitmap bmp, @NonNull String path) {
        String dir = path;
        File file = new File(path);
        if (!file.isDirectory()) {
            dir = path.substring(0, path.lastIndexOf(File.separator));
        }
        createDir(dir);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            file = null;
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {

            }
        }

        return file;
    }

    /**
     * 扫描指定的文件
     *
     * @param file
     */
    public static void scanMediaFile(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**
     * 获取指定文件所在的相对目录路径,即去掉了文件系统根目录和文件名后的目录路径,如文件路径
     * /storage/emulated/0/Pictures/zaikan/1474604596747.jpg
     * /storage/emulated/0为系统文件系统路径, 1474604596747.jpg为文件名, 去掉此两项,返回的结果为
     * Pictures/zaikan
     *
     * @param file
     */
    public static String getFileRelativePath(Context context, File file) {
        if (file == null || !file.exists()) {
            return null;
        }

        String root = null;
        if (FileUtils.checkSDCard()) {
            root = Environment.getExternalStorageDirectory().getPath();
        } else {
            root = context.getCacheDir().getPath();
        }

        String path = file.getAbsolutePath();
        String fileDir = path.replace(root + "/", "");
        int index = fileDir.lastIndexOf("/");
        String relateDir = fileDir.substring(0, index);
        return relateDir;
    }

    public static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? true : false;
    }


    public static String getFilePathByContentResolver(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        String filePath = null;
        if (null == c) {
            throw new IllegalArgumentException(
                    "Query on " + uri + " returns null result.");
        }
        try {
            if ((c.getCount() != 1) || !c.moveToFirst()) {
            } else {
                filePath = c.getString(
                        c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            }
        } finally {
            c.close();
        }
        return filePath;
    }

    public static boolean exists(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();
    }

    public static long size(String absPath) {
        if (absPath == null) {
            return 0;
        }
        File file = new File(absPath);
        return file.length();
    }
}
