package me.xujichang.step.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import me.xujichang.step.R;
import me.xujichang.step.center.StepCenter;
import me.xujichang.step.counter.CounterListener;

/**
 * @author xujichang
 */
public class MainActivity extends AppCompatActivity {
    /**
     * 当前记步数
     */
    private Button btnCurrentStep;
    /**
     * 计步数据展示
     */
    private TextView tvResult;

    private TextView tvTempCount;
    /**
     * 单次计步
     */
    private Button btnOnceStep;

    private TextView tvStepCount;
    private TextView tvStatus;
    private boolean isTemp = false;
    private TextView tvStepTempCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnCurrentStep = findViewById(R.id.btn_currentStep);
        tvResult = findViewById(R.id.tv_result);
        tvTempCount = findViewById(R.id.tv_temp);
        btnOnceStep = findViewById(R.id.btn_once_Step);
        tvStatus = findViewById(R.id.tv_status);
        tvStepCount = findViewById(R.id.tv_step_count);
        tvStepTempCount = findViewById(R.id.tv_step_temp_count);

        btnCurrentStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前记步数
                tvStepCount.setText(String.format("今天记步数：%d 步", StepCenter.instance().getStepCount()));
                tvStepTempCount.setText(String.format("本次单次记步数：%d 步", StepCenter.instance().getTempStepCount()));
            }
        });
        btnOnceStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTemp = !isTemp;
                btnOnceStep.setText(isTemp ? "停止单次计步" : "开始单次计步");
                if (isTemp) {
                    StepCenter.instance().startCounter();
                } else {
                    int step = StepCenter.instance().stopCounter();

                    tvStepTempCount.setText(String.format("本次单次记步数：%d 步", step));
                }
            }
        });
        //监听状态
        StepCenter.instance().registerCounterListener(new CounterListener() {
            @Override
            public void onStepCount(float sum) {
                //总记步数
                tvResult.setText(String.format("系统计步数：%d 步", (int) sum));
            }

            @Override
            public void onStepOnce(int step) {
                //获取到一次
                tvStatus.setText(String.format("获取到一次计步数： %d", step));
            }

            @Override
            public void onStepTemp(int count) {
                //单次记录的现在记步数
                tvTempCount.setText(String.format("单次计步：%d 步", count));
            }
        });
    }

    private Context getContext() {
        return this;
    }
}
