package com.ktc.xapkinstaller;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ktc.xapkinstaller.utils.XapkInstaller;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author: ArvinYoung
 * @date: 2021/3/19
 */
public class MainActivity extends AppCompatActivity {

    private EditText edit ;
    private Button btnInstall ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = findViewById(R.id.edit);

        String path = edit.getText().toString();
        findViewById(R.id.btn_install).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(path != null && !TextUtils.isEmpty(path) && path.endsWith(".xapk")){
                    File xapkFile = new File(path);
                    if(xapkFile != null && xapkFile.exists()){
                        install(path);
                    }
                }else{
                    Toast.makeText(MainActivity.this , "文件错误!!!" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void install(String XapkPath) {
        ExecutorService installExecutor = Executors.newSingleThreadExecutor();
        installExecutor.execute(new Runnable() {
            @Override
            public void run() {
                XapkInstaller.doInstallApk(MainActivity.this, XapkPath);
            }
        });
    }
}
