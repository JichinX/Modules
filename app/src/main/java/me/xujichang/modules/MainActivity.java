package me.xujichang.modules;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codvision.base.services.Task;
import com.codvision.base.services.TaskCenter;

import me.xujichang.util.tool.LogTool;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Task task = new Task();
        task.setRunnable(new Runnable() {
            @Override
            public void run() {
                LogTool.d("This Runnable is created in Activity");
            }
        });
        TaskCenter.push(task);
    }
}
