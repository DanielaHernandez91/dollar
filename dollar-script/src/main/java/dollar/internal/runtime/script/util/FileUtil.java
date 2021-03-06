/*
 *    Copyright (c) 2014-2017 Neil Ellis
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package dollar.internal.runtime.script.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;


public final class FileUtil {
    @NotNull
    public static final UUID JVM_ID = UUID.randomUUID();

    @NotNull
    public static final String SHARED_RUNTIME_PATH = System.getProperty("user.home") + "/.dollar/runtime/";

    static {
        new File(System.getProperty("java.io.tmpdir") + "/dollar/runtime/" + JVM_ID).mkdirs();
        new File(SHARED_RUNTIME_PATH).mkdirs();
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> delete(new File(System.getProperty("java.io.tmpdir") + "/dollar/runtime/" + JVM_ID))));
    }

    @NotNull
    public static String threadTempPath() {
        return System.getProperty("java.io.tmpdir") + "/dollar/runtime/" + JVM_ID + "/" + Thread.currentThread().getId();
    }

    public static void delete(@NotNull File toDelete) {
        if (toDelete.isDirectory()) {
            File[] files = toDelete.listFiles();
            if (files != null) {
                for (File file : files) {
                    delete(file);
                }
            }
        }
        toDelete.delete();
    }

    @NotNull
    public static File getRuntimeDir(@NotNull String dirName) {
        File dir = new File(SHARED_RUNTIME_PATH, dirName);
        dir.mkdirs();
        return dir;
    }

    @NotNull
    public static File getTempDir(@NotNull String dirName) {
        File dir = new File(System.getProperty("java.io.tmpdir") + "/dollar/runtime/" + JVM_ID, dirName);
        dir.mkdirs();
        return dir;
    }
}
