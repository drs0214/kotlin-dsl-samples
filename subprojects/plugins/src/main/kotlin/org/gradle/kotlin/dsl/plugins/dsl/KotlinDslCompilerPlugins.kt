/*
 * Copyright 2018 the original author or authors.
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

package org.gradle.kotlin.dsl.plugins.dsl

import org.gradle.api.HasImplicitReceiver
import org.gradle.api.Plugin
import org.gradle.api.Project

import org.gradle.kotlin.dsl.withType

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

import org.jetbrains.kotlin.samWithReceiver.gradle.SamWithReceiverExtension
import org.jetbrains.kotlin.samWithReceiver.gradle.SamWithReceiverGradleSubplugin


/**
 * Configures the Kotlin compiler to recognise Gradle functional interface
 * annotated with [HasImplicitReceiver].
 */
open class KotlinDslCompilerPlugins : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {

        plugins.apply(SamWithReceiverGradleSubplugin::class.java)
        extensions.configure(SamWithReceiverExtension::class.java) { samWithReceiver ->
            samWithReceiver.annotation(HasImplicitReceiver::class.qualifiedName!!)
        }

        tasks.withType<KotlinCompile> {
            enableSamConversionForKotlinFunctions()
        }
    }
}


fun KotlinCompile.enableSamConversionForKotlinFunctions() {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-Xprogressive",
            "-XXLanguage:+NewInference",
            "-XXLanguage:+SamConversionForKotlinFunctions"
        )
    }
}