package com.codvision.background.center;

import android.app.Application;
import android.os.Build;

import com.codvision.background.job.BaseJob;
import com.evernote.android.job.JobApi;
import com.evernote.android.job.JobConfig;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.xujichang.util.tool.LogTool;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/25 - 2:56 PM
 */
public class JobCenter {
    private static final long minIntervalM = TimeUnit.MINUTES.toMillis(1);
    private static final long minFlexM = TimeUnit.SECONDS.toMillis(30);
    private static volatile boolean allowSmallerIntervalMills = false;
    private static volatile List<Integer> jobIds = new ArrayList<>();

    public static void init(Application application) {
        init(application, null, false);
    }

    public static void init(Application application, boolean requireSmallerIntervalMills) {
        init(application, null, requireSmallerIntervalMills);
    }

    public static void init(Application application, JobCreator creator, boolean requireSmallerIntervalMills) {
        if (requireSmallerIntervalMills) {
            requireSmallerIntervalMills();
        }
        init(application, creator);
    }

    public static void init(Application application, JobCreator creator) {
        JobManager.create(application);
        if (null != creator) {
            addJobCreator(creator);
        }
    }

    public static void requireSmallerIntervalMills() {
        //禁用WorkerManager可以在M版本之前 实现更小的任务间隔
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            JobConfig.setAllowSmallerIntervalsForMarshmallow(true);
//            //禁用WorkerManager可以在M版本之前 实现更小的任务间隔
            JobConfig.setApiEnabled(JobApi.WORK_MANAGER, false);
            allowSmallerIntervalMills = true;
        } else {
            //M版本之后的任务,
            allowSmallerIntervalMills = false;
        }
    }

    /**
     * 添加任务建造者
     *
     * @param creator
     */
    public static void addJobCreator(JobCreator creator) {
        JobManager.instance().addJobCreator(creator);
    }

    /**
     * 执行周期任务
     *
     * @param tag
     */
    public static void startPeriodJob(String tag) {
        jobIds.add(createSimplePeriodJob(tag)
                .schedule());
        LogTool.d("startPeriodJob------tag  jobs:" + jobIds.size());
    }

    /**
     * 执行周期任务
     */
    public static void startPeriodJob(BaseJob job) {
        CenterConfig config = job.getUsedConfig();
        startPeriodJob(config);
    }

    /**
     * 执行周期任务
     */
    public static void startPeriodJob(CenterConfig config) {
        jobIds.add(createSimplePeriodJob(config)
                .schedule());
        LogTool.d("startPeriodJob------job  jobs:" + jobIds.size());
    }

    private static JobRequest createSimplePeriodJob(CenterConfig config) {
        LogTool.d("createSimplePeriodJob------config:" + config.toString());
        return new JobRequest
                .Builder(config.getTag())
                .setPeriodic(config.getMinInterval(), config.getMinFlex())
                .setRequiredNetworkType(config.getRequireNetType())
                .setRequiresBatteryNotLow(config.isRequireBatteryNotLow())
                .setRequiresDeviceIdle(config.isRequireDeviceIdle())
                .build();
    }

    /**
     * 创建简单的周期任务，所需参数均为默认
     *
     * @param tag
     * @return
     */
    private static JobRequest createSimplePeriodJob(String tag) {
        return new JobRequest
                .Builder(tag)
                .setPeriodic(JobRequest.MIN_INTERVAL, JobRequest.MIN_FLEX)
                .setRequiredNetworkType(JobRequest.NetworkType.ANY)
                .setRequiresBatteryNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
    }

    public static class CenterConfig {
        @Override
        public String toString() {
            return "CenterConfig{" +
                    "minInterval=" + minInterval +
                    ", minFlex=" + minFlex +
                    ", requireNetType=" + requireNetType +
                    ", requireBatteryNotLow=" + requireBatteryNotLow +
                    ", requireDeviceIdle=" + requireDeviceIdle +
                    ", tag='" + tag + '\'' +
                    '}';
        }

        /**
         * 最小的周期间隔
         */
        private long minInterval;
        /**
         * 最小可变间隔
         * <p>
         * 实际
         */
        private long minFlex;
        /**
         * 所需网络环境
         */
        private JobRequest.NetworkType requireNetType;
        /**
         * 低电量是否允许
         */
        private boolean requireBatteryNotLow;
        private boolean requireDeviceIdle;
        private String tag;

        private CenterConfig(Builder builder) {
            setMinInterval(builder.minInterval);
            setMinFlex(builder.minFlex);
            setRequireNetType(builder.requireNetType);
            setRequireBatteryNotLow(builder.requireBatteryNotLow);
            setRequireDeviceIdle(builder.requireDeviceIdle);
            setTag(builder.tag);
            checkInterval();
        }

        /**
         * 版本小于M的可以允许较小的时间
         */
        private void checkIntervalM() {
            if (minInterval < minIntervalM) {
                minInterval = minIntervalM;
            }
            if (minFlex < minFlexM) {
                minFlex = minFlexM;
            }
        }

        /**
         * 检测周期时间
         */
        private void checkInterval() {
            if (allowSmallerIntervalMills) {
                checkIntervalM();
            } else {
                if (minInterval < JobRequest.MIN_INTERVAL) {
                    minInterval = JobRequest.MIN_INTERVAL;
                }
                if (minFlex < JobRequest.MIN_FLEX) {
                    minFlex = JobRequest.MIN_FLEX;
                }
            }
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public static Builder newBuilder(CenterConfig copy) {
            Builder builder = new Builder();
            builder.minInterval = copy.getMinInterval();
            builder.minFlex = copy.getMinFlex();
            builder.requireNetType = copy.getRequireNetType();
            builder.requireBatteryNotLow = copy.isRequireBatteryNotLow();
            builder.requireDeviceIdle = copy.isRequireDeviceIdle();
            builder.tag = copy.getTag();
            return builder;
        }

        public long getMinInterval() {
            return minInterval;
        }

        public void setMinInterval(long minInterval) {
            this.minInterval = minInterval;
        }

        public long getMinFlex() {
            return minFlex;
        }

        public void setMinFlex(long minFlex) {
            this.minFlex = minFlex;
        }

        public JobRequest.NetworkType getRequireNetType() {
            return requireNetType;
        }

        public void setRequireNetType(JobRequest.NetworkType requireNetType) {
            this.requireNetType = requireNetType;
        }

        public boolean isRequireBatteryNotLow() {
            return requireBatteryNotLow;
        }

        public void setRequireBatteryNotLow(boolean requireBatteryNotLow) {
            this.requireBatteryNotLow = requireBatteryNotLow;
        }

        public boolean isRequireDeviceIdle() {
            return requireDeviceIdle;
        }

        public void setRequireDeviceIdle(boolean requireDeviceIdle) {
            this.requireDeviceIdle = requireDeviceIdle;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public static final class Builder {
            private long minInterval;
            private long minFlex;
            private JobRequest.NetworkType requireNetType = JobRequest.NetworkType.ANY;
            private boolean requireBatteryNotLow = false;
            private boolean requireDeviceIdle = false;
            private String tag = "default";

            public Builder() {
            }

            public Builder withMinInterval(long val) {
                minInterval = val;
                return this;
            }

            public Builder withMinFlex(long val) {
                minFlex = val;
                return this;
            }

            public Builder withRequireNetType(JobRequest.NetworkType val) {
                requireNetType = val;
                return this;
            }

            public Builder withRequireBatteryNotLow(boolean val) {
                requireBatteryNotLow = val;
                return this;
            }

            public Builder withRequireDeviceIdle(boolean val) {
                requireDeviceIdle = val;
                return this;
            }

            public Builder withTag(String val) {
                tag = val;
                return this;
            }

            public CenterConfig build() {
                return new CenterConfig(this);
            }
        }
    }
}
