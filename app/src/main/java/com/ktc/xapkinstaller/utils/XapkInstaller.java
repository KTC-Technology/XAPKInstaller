package com.ktc.xapkinstaller.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import com.ktc.xapkinstaller.InstallActivity;
import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipException;
import org.zeroturnaround.zip.ZipUtil;
import java.io.File;
import java.util.ArrayList;

/**
 * @author ArvinYoung
 * @date 2021/3/19
 * @description Xapk安装器
 */
public class XapkInstaller {

    /**
     * @description 安装Xapk
     * @param context ， xapkFilePath
     * @return void
     */
    public static void doInstallApk(Context context , String xapkFilePath) {
        if (xapkFilePath.isEmpty()) {
            return ;
        }
        File xapkFile = new File(xapkFilePath);
        String unzipOutputDirPath = getUnzipOutputDirPath(xapkFile);

        if (unzipOutputDirPath.isEmpty()) {
            return ;
        }

        File unzipOutputDir = new File(unzipOutputDirPath);
        //只保留apk文件和Android/obb下的文件,以及json文件用于获取主包（当有多个apk时）
        ZipUtil.unpack(xapkFile, unzipOutputDir, new NameMapper() {
            @Override
            public String map(String name) {
                if(name.endsWith(".apk")){
                    return name;
                }
                return null;
            }
        });

        File[] files = unzipOutputDir.listFiles();
        int apkSize = 0 ;
        for(File file : files){
            if(file.isFile() && file.getName().endsWith(".apk")){
                apkSize+=1;
            }
        }

        unzipObbToAndroidObbDir(xapkFile, new File(getMobileAndroidObbDir()));

        Log.i("yzh" , "apkSize:  "+apkSize);
        if(apkSize > 0){
            doInstallApk(context , xapkFilePath, unzipOutputDir);
        }
    }

    /**
     * @description 在xapk文件同级目录下创建解压文件夹并返回该路径
     * @param file
     * @return String
     */
    private static String getUnzipOutputDirPath(File file) {
        String filePathPex = file.getParent() + File.separator;
        String unzipOutputDir = filePathPex + FileUtils.getFileNameNoExtension(file);
        boolean result = FileUtils.createOrExistsDir(unzipOutputDir);
        if (result) {
            return unzipOutputDir;
        }else {
            return null;
        }
    }

    /**
     * @description 解压xapk中Android/obb文件到解压目录
     * @param xapkFile , unzipOutputDir
     * @return boolean
     */
    private static boolean unzipObbToAndroidObbDir(File xapkFile, File unzipOutputDir) {
        String prefix = "Android/obb";
        //只保留apk文件和Android/obb下的文件,以及json文件用于获取主包（当有多个apk时）
        ZipUtil.unpack(xapkFile, unzipOutputDir, new NameMapper() {
            @Override
            public String map(String name) {
                if(name.startsWith(prefix)){
                    return name.substring(prefix.length());
                }
                return null;
            }
        });
        return true;
    }

    /**
     * 得到手机的Android/obb目录
     * @return
     */
    public static String getMobileAndroidObbDir(){
        String path ;
        if(isSDCardEnableByEnvironment()) {
            path = Environment.getExternalStorageDirectory().getPath() + File.separator + "Android" + File.separator + "obb";
        } else {
            path = Environment.getDataDirectory().getParent().toString() + File.separator + "Android" + File.separator + "obb";
        }
        FileUtils.createOrExistsDir(path);
        return path;
    }

    private static boolean isSDCardEnableByEnvironment() {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState();
    }

    /**
     * @description 执行应用安装
     * @param context , xapkPath , xapkUnzipOutputDir
     * @return void
     */
    private static void doInstallApk(Context context, String xapkPath, File xapkUnzipOutputDir) {
        try {
            File[] files = xapkUnzipOutputDir.listFiles();
            if(files == null || files.length < 1){
                return;
            }
            ArrayList<String> apkFilePaths = new ArrayList<>();
            for(File file : files){
                if(file != null && file.isFile() && file.getName().endsWith(".apk")){
                    apkFilePaths.add(file.getAbsolutePath());
                }
            }

            Intent intent = new Intent(context, InstallActivity.class);
            intent.putExtra(InstallActivity.KEY_XAPK_PATH, xapkPath);
            intent.putStringArrayListExtra(InstallActivity.KEY_APK_PATHS, apkFilePaths);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK /*| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED*/);
            context.startActivity(intent);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

}
