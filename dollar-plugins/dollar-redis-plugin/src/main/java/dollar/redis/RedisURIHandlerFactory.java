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

package dollar.redis;

import dollar.api.uri.URI;
import dollar.api.uri.URIHandler;
import dollar.api.uri.URIHandlerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.JedisPoolConfig;

public class RedisURIHandlerFactory implements URIHandlerFactory {


    @NotNull
    private static final JedisPoolConfig poolConfig = new JedisPoolConfig();

    static {
        poolConfig.setMaxTotal(128);

    }

    @NotNull @Override
    public URIHandlerFactory copy() {
        return this;
    }

    @Nullable @Override
    public URIHandler forURI(@NotNull String scheme, @NotNull URI uri) {
        return new RedisURIHandler(uri, poolConfig);
    }

    @Override
    public boolean handlesScheme(@NotNull String scheme) {
        return "redis".equals(scheme);
    }
}

