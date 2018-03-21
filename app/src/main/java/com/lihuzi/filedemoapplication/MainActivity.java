package com.lihuzi.filedemoapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity
{
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/" + "test.txt");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFile();
    }

    private void initView()
    {
        findViewById(R.id.act_main_open_tv).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (f.exists())
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri uri = FileProvider.getUriForFile(v.getContext(), "com.lihuzi.filedemoapplication.fileprovider", f);
                    i.setDataAndType(uri, "text/plain");
                    v.getContext().startActivity(i);
                }
                else
                {
                    initFile();
                }
            }
        });
    }

    private void initFile()
    {
        if (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            createFile();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    private void createFile()
    {
        if (!f.exists())
        {
            try
            {
                f.createNewFile();
                String str = "Hello world";
                FileOutputStream fileOutputStream = new FileOutputStream(f);
                fileOutputStream.write(str.getBytes());
                fileOutputStream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 100 && permissions.length > 0 && grantResults.length == permissions.length)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                createFile();
            }
        }
    }
}
