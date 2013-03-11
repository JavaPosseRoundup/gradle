/*
 * Copyright 2012 the original author or authors.
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

package org.gradle.tooling.provider.model.internal;

import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.tooling.provider.model.ToolingModelBuilder;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;

import java.util.ArrayList;
import java.util.List;

public class DefaultToolingModelBuilderRegistry implements ToolingModelBuilderRegistry {
    private final List<ToolingModelBuilder> builders = new ArrayList<ToolingModelBuilder>();

    public DefaultToolingModelBuilderRegistry() {
        register(new VoidToolingModelBuilder());
    }

    public void register(ToolingModelBuilder builder) {
        builders.add(builder);
    }

    public ToolingModelBuilder getBuilder(Class<?> modelType) {
        ToolingModelBuilder match = null;
        for (ToolingModelBuilder builder : builders) {
            if (builder.canBuild(modelType)) {
                if (match != null) {
                    throw new UnsupportedOperationException(String.format("Multiple builders are available to build a model of type '%s'.", modelType.getSimpleName()));
                }
                match = builder;
            }
        }
        if (match != null) {
            return match;
        }

        throw new UnsupportedOperationException(String.format("No builders are available to build a model of type '%s'.", modelType.getSimpleName()));
    }

    private static class VoidToolingModelBuilder implements ToolingModelBuilder {
        public boolean canBuild(Class<?> type) {
            return type.equals(Void.class);
        }

        public Object buildAll(Class<?> type, ProjectInternal project) {
            return null;
        }
    }
}
