/*
 * Copyright 2018 Rakuten Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rakuten.tech.mobile.manifestconfig.processor

import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler.javac
import com.google.testing.compile.JavaFileObjects
import org.junit.Test
import javax.tools.JavaFileObject

class ManifestConfigGeneratorSpec {

    @Test
    fun shouldGenerateManifestConfig() {
        val configInterface = JavaFileObjects.forSourceString(
            "com.rakuten.tech.mobile.manifestconfig.Sample",
            resourceFile("Sample.java")
            )

        val compilation = javac()
            .withProcessors(ManifestConfigProcessor())
            .compile(configInterface)

        assertThat(compilation).succeeded()

        val configImplementation = JavaFileObjects.forSourceString(
            "com.rakuten.tech.mobile.manifestconfig.SampleManifestConfig",
            resourceFile("SampleManifestConfig.java")
        )

        assertThat(compilation)
            .generatedSourceFile("com.rakuten.tech.mobile.manifestconfig.SampleManifestConfig")
            .hasSourceEquivalentTo(configImplementation)
    }

    private fun resourceFile(name: String) :String {
        return ManifestConfigGeneratorSpec::class.java.classLoader.getResource(name).readText()
    }

}

@Suppress("unused")
fun javaSource(java: JavaFileObject): String {
    return java.openInputStream().bufferedReader().use { it.readText() }
}