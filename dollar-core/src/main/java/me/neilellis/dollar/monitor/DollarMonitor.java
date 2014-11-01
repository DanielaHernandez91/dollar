/*
 * Copyright (c) 2014 Neil Ellis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.neilellis.dollar.monitor;

import me.neilellis.dollar.plugin.ExtensionPoint;

import java.util.function.Supplier;

/**
 * @author <a href="http://uk.linkedin.com/in/neilellis">Neil Ellis</a>
 */
public interface DollarMonitor extends ExtensionPoint<DollarMonitor> {

    /**
     * Dump metrics and related information to the console.
     */
    void dump();

    void dumpThread();

    <R> R run(String simpleLabel, String namespacedLabel, String info, Supplier<R> code);

    void run(String simpleLabel, String namespacedLabel, String info, Runnable code);
}