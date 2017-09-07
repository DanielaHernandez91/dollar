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

package dollar.api.execution;

import dollar.api.plugin.ExtensionPoint;
import dollar.api.script.SourceSegment;
import dollar.api.var;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Function;

public interface DollarExecutor extends ExtensionPoint<DollarExecutor> {


    /**
     * Execute job at some point, returns immediately.
     *
     * @param callable the job to perform
     * @param <T>      the return type
     * @return a future for the result
     */
    @NotNull <T> Future<T> executeInBackground(@NotNull Callable<T> callable);

    /**
     * Submit a job for execution <b>immediately</b> in an unspecified manner.
     *
     * @param <T>      the return type
     * @param callable the job to perform
     * @return the fork/join task
     */
    @NotNull <T> Future<T> executeNow(@NotNull Callable<T> callable);

    /**
     * Force stop, that is do not wait for tasks to finish.
     */
    void forceStop();

    /**
     * Stop the execution processing and restart it.
     */
    void restart();

    /**
     * Schedule a recurring job.
     *
     * @param millis   the time between executions
     * @param runnable the job to perform
     * @return a Future for the task, to enable cancellation
     */
    @NotNull
    Future<?> scheduleEvery(long millis, @NotNull Runnable runnable);

    /**
     * Submit a job for execution in an unspecified manner.
     *
     * @param <T>      the return type
     * @param callable the job to perform
     * @return the fork/join task
     */
    @NotNull <T> Future<T> submit(@NotNull Callable<T> callable);

    var forkAndReturnId(SourceSegment source, var in, Function<var, var> call);

    var fork(SourceSegment source, var in, Function<var, var> call);
}
