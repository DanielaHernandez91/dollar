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

package com.innowhere.relproxy.impl.jproxy.core.clsmgr;

/**
 * @author jmarranz
 */
public class ClassDescriptorInner extends ClassDescriptor {
    protected ClassDescriptorSourceUnit parent;

    public ClassDescriptorInner(String className, ClassDescriptorSourceUnit parent) {
        super(className);
        this.parent = parent;
    }

    public boolean isInnerClass() {
        return true;
    }

    public ClassDescriptorSourceUnit getClassDescriptorSourceUnit() {
        return parent;
    }
}
