/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tests.fused.host;

import static org.junit.Assert.assertTrue;

import com.android.tradefed.device.ITestDevice;
import com.android.tradefed.testtype.DeviceJUnit4ClassRunner;
import com.android.tradefed.testtype.junit4.BaseHostJUnit4Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Runs the FuseDaemon tests.
 */
@RunWith(DeviceJUnit4ClassRunner.class)
public class FuseDaemonHostTest extends BaseHostJUnit4Test {

    private boolean isExternalStorageSetup = false;

    /**
     * Runs the given phase of FilePathAccessTest by calling into the device.
     * Throws an exception if the test phase fails.
     */
    private void runDeviceTest(String phase) throws Exception {
        assertTrue(runDeviceTests("com.android.tests.fused",
                "com.android.tests.fused.FilePathAccessTest",
                phase));
    }

    private String executeShellCommand(String cmd) throws Exception {
        return getDevice().executeShellCommand(cmd);
    }

    private void setupExternalStorage() throws Exception {
        if (!isExternalStorageSetup) {
            runDeviceTest("setupExternalStorage");
            isExternalStorageSetup = true;
        }
    }

    @Before
    public void setup() throws Exception {
        setupExternalStorage();
        executeShellCommand("mkdir /sdcard/Android/data/com.android.shell -m 2770");
        executeShellCommand("mkdir /sdcard/Android/data/com.android.shell/files -m 2770");
    }

    @Before
    public void revokeStoragePermissions() throws Exception {
        revokePermissions("android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE");
    }

    @After
    public void tearDown() throws Exception {
        executeShellCommand("rm -r /sdcard/Android/data/com.android.shell");
    }

    @Test
    public void testTypePathConformity() throws Exception {
        runDeviceTest("testTypePathConformity");
    }

    @Test
    public void testCreateFileInAppExternalDir() throws Exception {
        runDeviceTest("testCreateFileInAppExternalDir");
    }

    @Test
    public void testCreateFileInOtherAppExternalDir() throws Exception {
        runDeviceTest("testCreateFileInOtherAppExternalDir");
    }

    @Test
    public void testContributeMediaFile() throws Exception {
        runDeviceTest("testContributeMediaFile");
    }

    @Test
    public void testCreateAndDeleteEmptyDir() throws Exception {
        runDeviceTest("testCreateAndDeleteEmptyDir");
    }

    @Test
    public void testCantDeleteOtherAppsContents() throws Exception {
        runDeviceTest("testCantDeleteOtherAppsContents");
    }

    @Test
    public void testOpendirRestrictions() throws Exception {
        runDeviceTest("testOpendirRestrictions");
    }

    @Test
    public void testLowLevelFileIO() throws Exception {
        runDeviceTest("testLowLevelFileIO");
    }

    @Test
    public void testListDirectoriesWithMediaFiles() throws Exception {
        runDeviceTest("testListDirectoriesWithMediaFiles");
    }

    @Test
    public void testListDirectoriesWithNonMediaFiles() throws Exception {
        runDeviceTest("testListDirectoriesWithNonMediaFiles");
    }

    @Test
    public void testListFilesFromExternalFilesDirectory() throws Exception {
        runDeviceTest("testListFilesFromExternalFilesDirectory");
    }

    @Test
    public void testListFilesFromExternalMediaDirectory() throws Exception {
        runDeviceTest("testListFilesFromExternalMediaDirectory");
    }

    @Test
    public void testListUnsupportedFileType() throws Exception {
        final ITestDevice device = getDevice();
        final boolean isAdbRoot = device.isAdbRoot() ? true : false;
        // Adb shell should run as 'root' for test to bypass some of FUSE & MediaProvider checks.
        if (!isAdbRoot) {
            device.enableAdbRoot();
        }
        runDeviceTest("testListUnsupportedFileType");
        if (!isAdbRoot) {
            device.disableAdbRoot();
        }
    }

    @Test
    public void testMetaDataRedaction() throws Exception {
        runDeviceTest("testMetaDataRedaction");
    }

    @Test
    public void testVfsCacheConsistency() throws Exception {
        runDeviceTest("testOpenFilePathFirstWriteContentResolver");
        runDeviceTest("testOpenContentResolverFirstWriteContentResolver");
        runDeviceTest("testOpenFilePathFirstWriteFilePath");
        runDeviceTest("testOpenContentResolverFirstWriteFilePath");
        runDeviceTest("testOpenContentResolverWriteOnly");
        runDeviceTest("testOpenContentResolverDup");
        runDeviceTest("testContentResolverDelete");
        runDeviceTest("testContentResolverUpdate");
        runDeviceTest("testOpenContentResolverClose");
    }

    @Test
    public void testCaseInsensitivity() throws Exception {
        runDeviceTest("testCreateLowerCaseDeleteUpperCase");
        runDeviceTest("testCreateUpperCaseDeleteLowerCase");
        runDeviceTest("testCreateMixedCaseDeleteDifferentMixedCase");
    }

    @Test
    public void testCallingIdentityCacheInvalidation() throws Exception {
        // General IO access
        runDeviceTest("testReadStorageInvalidation");
        runDeviceTest("testWriteStorageInvalidation");
        // File manager access
        runDeviceTest("testManageStorageInvalidation");
        // Default gallery
        runDeviceTest("testWriteImagesInvalidation");
        runDeviceTest("testWriteVideoInvalidation");
        // EXIF access
        runDeviceTest("testAccessMediaLocationInvalidation");

        runDeviceTest("testAppUpdateInvalidation");
        runDeviceTest("testAppReinstallInvalidation");
    }

    @Test
    public void testRenameFile() throws Exception {
        runDeviceTest("testRenameFile");
    }

    @Test
    public void testRenameFileType() throws Exception {
        runDeviceTest("testRenameFileType");
    }

    @Test
    public void testRenameAndReplaceFile() throws Exception {
        runDeviceTest("testRenameAndReplaceFile");
    }

    @Test
    public void testRenameFileNotOwned() throws Exception {
        runDeviceTest("testRenameFileNotOwned");
    }

    @Test
    public void testRenameDirectory() throws Exception {
        runDeviceTest("testRenameDirectory");
    }

    @Test
    public void testRenameDirectoryNotOwned() throws Exception {
        runDeviceTest("testRenameDirectoryNotOwned");
    }

    @Test
    public void testRenameEmptyDirectory() throws Exception {
        runDeviceTest("testRenameEmptyDirectory");
    }

    @Test
    public void testSystemGalleryAppHasFullAccessToImages() throws Exception {
        runDeviceTest("testSystemGalleryAppHasFullAccessToImages");
    }

    @Test
    public void testSystemGalleryAppHasNoFullAccessToAudio() throws Exception {
        runDeviceTest("testSystemGalleryAppHasNoFullAccessToAudio");
    }

    @Test
    public void testSystemGalleryCanRenameImagesAndVideos() throws Exception {
        runDeviceTest("testSystemGalleryCanRenameImagesAndVideos");
    }

    @Test
    public void testManageExternalStorageCanCreateFilesAnywhere() throws Exception {
        runDeviceTest("testManageExternalStorageCanCreateFilesAnywhere");
    }

    @Test
    public void testManageExternalStorageCanDeleteOtherAppsContents() throws Exception {
        runDeviceTest("testManageExternalStorageCanDeleteOtherAppsContents");
    }

    @Test
    public void testManageExternalStorageReaddir() throws Exception {
        runDeviceTest("testManageExternalStorageReaddir");
    }

    @Test
    public void testManageExternalStorageCanRenameOtherAppsContents() throws Exception {
        runDeviceTest("testManageExternalStorageCanRenameOtherAppsContents");
    }

    @Test
    public void testCantAccessOtherAppsContents() throws Exception {
        runDeviceTest("testCantAccessOtherAppsContents");
    }

    @Test
    public void testCanCreateHiddenFile() throws Exception {
        runDeviceTest("testCanCreateHiddenFile");
    }

    @Test
    public void testCanRenameHiddenFile() throws Exception {
        runDeviceTest("testCanRenameHiddenFile");

    }

    @Test
    public void testHiddenDirectory() throws Exception {
        runDeviceTest("testHiddenDirectory");
    }

    @Test
    public void testHiddenDirectory_nomedia() throws Exception {
        runDeviceTest("testHiddenDirectory_nomedia");
    }

    @Test
    public void testListHiddenFile() throws Exception {
        runDeviceTest("testListHiddenFile");
    }

    @Test
    public void testOpenPendingAndTrashed() throws Exception {
        runDeviceTest("testOpenPendingAndTrashed");
    }

    @Test
    public void testDeletePendingAndTrashed() throws Exception {
        runDeviceTest("testDeletePendingAndTrashed");
    }

    @Test
    public void testListPendingAndTrashed() throws Exception {
        runDeviceTest("testListPendingAndTrashed");
    }

    @Test
    public void testCanCreateDefaultDirectory() throws Exception {
        runDeviceTest("testCanCreateDefaultDirectory");
    }

    @Test
    public void testManageExternalStorageQueryOtherAppsFile() throws Exception {
        runDeviceTest("testManageExternalStorageQueryOtherAppsFile");
    }

    @Test
    public void testSystemGalleryQueryOtherAppsFiles() throws Exception {
        runDeviceTest("testSystemGalleryQueryOtherAppsFiles");
    }

    @Test
    public void testQueryOtherAppsFiles() throws Exception {
        runDeviceTest("testQueryOtherAppsFiles");
    }

    @Test
    public void testSystemGalleryCanRenameImageAndVideoDirs() throws Exception {
        runDeviceTest("testSystemGalleryCanRenameImageAndVideoDirs");
    }

    @Test
    public void testCreateCanRestoreDeletedRowId() throws Exception {
        runDeviceTest("testCreateCanRestoreDeletedRowId");
    }

    @Test
    public void testRenameCanRestoreDeletedRowId() throws Exception {
        runDeviceTest("testRenameCanRestoreDeletedRowId");
    }

    @Test
    public void testCantCreateOrRenameFileWithInvalidName() throws Exception {
        runDeviceTest("testCantCreateOrRenameFileWithInvalidName");
    }

    @Test
    public void testAccess_file() throws Exception {
        grantPermissions("android.permission.READ_EXTERNAL_STORAGE");
        try {
            runDeviceTest("testAccess_file");
        } finally {
            revokePermissions("android.permission.READ_EXTERNAL_STORAGE");
        }
    }

    @Test
    public void testAccess_directory() throws Exception {
        grantPermissions("android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE");
        try {
            runDeviceTest("testAccess_directory");
        } finally {
            revokePermissions("android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.READ_EXTERNAL_STORAGE");
        }
    }

    private void grantPermissions(String... perms) throws Exception {
        for (String perm : perms) {
            executeShellCommand("pm grant com.android.tests.fused " + perm);
        }
    }

    private void revokePermissions(String... perms) throws Exception {
        for (String perm : perms) {
            executeShellCommand("pm revoke com.android.tests.fused " + perm);
        }
    }
}
