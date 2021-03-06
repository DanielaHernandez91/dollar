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

package dollar.api.script;

import dollar.api.Scope;
import dollar.api.Value;
import dollar.api.VarKey;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;

public interface DollarParser {


    void export(@NotNull VarKey name, @NotNull Value export);

    @NotNull
    ParserOptions options();

    @NotNull
    Value parse(@NotNull Scope scope, @NotNull String source) throws Exception;

    @NotNull
    Value parse(@NotNull File file, boolean parallel) throws Exception;

    @NotNull
    Value parse(@NotNull InputStream in, boolean parallel, @NotNull Scope scope) throws Exception;

    @NotNull
    Value parse(@NotNull InputStream in, @NotNull String file, boolean parallel) throws Exception;

    @NotNull
    Value parse(@NotNull String source, boolean parallel) throws Exception;

    @NotNull
    Value parseMarkdown(@NotNull String source);

}
