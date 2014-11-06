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

package com.innowhere.relproxy.impl.jproxy;

import com.innowhere.relproxy.RelProxyException;
import com.innowhere.relproxy.RelProxyOnReloadListener;
import com.innowhere.relproxy.impl.GenericProxyConfigBaseImpl;
import com.innowhere.relproxy.jproxy.JProxyConfig;
import com.innowhere.relproxy.jproxy.JProxyDiagnosticsListener;

/**
 * @author jmarranz
 */
public class JProxyConfigImpl extends GenericProxyConfigBaseImpl implements JProxyConfig {
    protected String inputPath;
    protected String classFolder;
    protected long scanPeriod = -1;
    protected Iterable<String> compilationOptions;
    protected JProxyDiagnosticsListener diagnosticsListener;
    protected boolean test = false;

    @Override
    public JProxyConfig setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public JProxyConfig setRelProxyOnReloadListener(RelProxyOnReloadListener relListener) {
        this.relListener = relListener;
        return this;
    }

    @Override
    public JProxyConfig setInputPath(String inputPath) {
        this.inputPath = inputPath;
        return this;
    }

    @Override
    public JProxyConfig setClassFolder(String classFolder) {
        this.classFolder = classFolder;
        return this;
    }

    @Override
    public JProxyConfig setScanPeriod(long scanPeriod) {
        if (scanPeriod == 0) throw new RelProxyException("scanPeriod cannot be zero");
        this.scanPeriod = scanPeriod;
        return this;
    }

    @Override
    public JProxyConfig setCompilationOptions(Iterable<String> compilationOptions) {
        this.compilationOptions = compilationOptions;
        return this;
    }

    @Override
    public JProxyConfig setJProxyDiagnosticsListener(JProxyDiagnosticsListener diagnosticsListener) {
        this.diagnosticsListener = diagnosticsListener;
        return this;
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getClassFolder() {
        return classFolder;
    }

    public long getScanPeriod() {
        return scanPeriod;
    }

    public Iterable<String> getCompilationOptions() {
        return compilationOptions;
    }

    public JProxyDiagnosticsListener getJProxyDiagnosticsListener() {
        return diagnosticsListener;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

}
