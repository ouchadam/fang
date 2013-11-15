package com.ouchadam.fang.presentation;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;


public class DiskUtils {

    private static final long MEGA_BYTE = 1048576;

    public enum DiskLocation {
        INTERNAL {
            @Override
            StatFs getStats() {
                return new StatFs(Environment.getRootDirectory().getAbsolutePath());
            }
        },
        EXTERNAL {
            @Override
            StatFs getStats() {
                return new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            }
        },
        CUSTOM {
            @Override
            StatFs getStats() {
                return new StatFs(this.customLocation);
            }
        };

        String customLocation;

        public void setCustomLocation(String customLocation) {
            this.customLocation = customLocation;
        }

        abstract StatFs getStats();

    }

    public interface IDiskUtils {
        long totalSpace(DiskLocation location);
        long freeSpace(DiskLocation location);
        long busySpace(DiskLocation location);
    }

    public static IDiskUtils getInstance() {
        return isJellyBeanMR2() ? new JellyBeanMR2DiskUtils() : new DeprecatedDiskUtils();
    }

    private static boolean isJellyBeanMR2() {
        return getApiLevel() >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    private static int getApiLevel() {
        return android.os.Build.VERSION.SDK_INT;
    }

    private DiskUtils() {}

    private static class DeprecatedDiskUtils implements IDiskUtils {

        @Override
        public long totalSpace(DiskLocation location) {
            StatFs statFs = location.getStats();
            return (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / MEGA_BYTE;
        }

        @Override
        public long freeSpace(DiskLocation location) {
            StatFs statFs = location.getStats();
            long availableBlocks = statFs.getAvailableBlocks();
            long blockSize = statFs.getBlockSize();
            long freeBytes = availableBlocks * blockSize;

            return (freeBytes / MEGA_BYTE);
        }

        @Override
        public long busySpace(DiskLocation location) {
            StatFs statFs = location.getStats();
            long total = (statFs.getBlockCount() * statFs.getBlockSize());
            long free = (statFs.getAvailableBlocks() * statFs.getBlockSize());

            return ((total - free) / MEGA_BYTE);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static class JellyBeanMR2DiskUtils implements IDiskUtils {

        @Override
        public long totalSpace(DiskLocation location) {
            return toMb(getTotalBytes(location.getStats()));
        }

        @Override
        public long freeSpace(DiskLocation location) {
            return toMb(getFreeBytes(location.getStats()));
        }

        @Override
        public long busySpace(DiskLocation location) {
            StatFs statFs = location.getStats();
            return toMb((getTotalBytes(statFs) - getFreeBytes(statFs)));
        }

        private long getTotalBytes(StatFs statFs) {
            return statFs.getBlockCountLong() * (statFs.getBlockSizeLong());
        }

        private long getFreeBytes(StatFs statFs) {
            return statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
        }

        private long toMb(long from) {
            return from / MEGA_BYTE;
        }
    }

}
