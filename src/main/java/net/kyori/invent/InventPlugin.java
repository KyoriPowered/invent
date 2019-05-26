/*
 * This file is part of invent, licensed under the MIT License.
 *
 * Copyright (c) 2019 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.invent;

import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.Convention;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.compile.CompileOptions;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.javadoc.Javadoc;
import org.gradle.api.tasks.testing.Test;
import org.gradle.external.javadoc.StandardJavadocDocletOptions;

import java.util.List;

public class InventPlugin implements Plugin<Project> {
  private static final String COMPILE_ENCODING = "UTF-8";
  private static final String JAVADOC_CHARSET = "UTF-8";
  private static final String JAVADOC_ENCODING = "UTF-8";

  private static final String COMPILE_ARGUMENT_PARAMETERS = "-parameters";
  private static final String JAVADOC_OPTION_HTML5 = "html5";

  @Override
  public void apply(final Project project) {
    final PluginContainer plugins = project.getPlugins();
    plugins.apply(JavaBasePlugin.class);

    final Convention convention = project.getConvention();
    this.configureJavaPluginConvention(convention.getPlugin(JavaPluginConvention.class));

    final TaskContainer tasks = project.getTasks();
    this.configureJavaCompile(tasks);
    this.configureJavadoc(tasks);
    this.configureTest(tasks);
  }

  /*
   * sourceCompatibility = JavaVersion.VERSION_1_8
   * targetCompatibility = JavaVersion.VERSION_1_8
   */
  private void configureJavaPluginConvention(final JavaPluginConvention convention) {
    final JavaVersion version = JavaVersion.VERSION_1_8;
    convention.setSourceCompatibility(version);
    convention.setTargetCompatibility(version);
  }

  /*
   * tasks.withType(JavaCompile) {
   *   options.compilerArgs += ['-parameters']
   *   options.deprecation = true
   *   options.encoding = 'UTF-8'
   * }
   */
  private void configureJavaCompile(final TaskContainer tasks) {
    tasks.withType(JavaCompile.class).configureEach(task -> {
      final CompileOptions options = task.getOptions();
      options.setDeprecation(true);
      options.setEncoding(COMPILE_ENCODING);
      final List<String> args = options.getCompilerArgs();
      if(!args.contains(COMPILE_ARGUMENT_PARAMETERS)) {
        args.add(COMPILE_ARGUMENT_PARAMETERS);
      }
    });
  }

  /*
   * javadoc {
   *   options.charSet = 'UTF-8'
   *   options.encoding = 'UTF-8'
   *   options.addBooleanOption('html5', true)
   * }
   */
  private void configureJavadoc(final TaskContainer tasks) {
    tasks.withType(Javadoc.class).configureEach(task -> {
      task.options(options -> {
        options.setEncoding(JAVADOC_ENCODING);

        // instanceof is not necessary, but we will be safe
        if(options instanceof StandardJavadocDocletOptions) {
          ((StandardJavadocDocletOptions) options).charSet(JAVADOC_CHARSET);
          if(JavaVersion.current().isJava9Compatible()) {
            ((StandardJavadocDocletOptions) options).addBooleanOption(JAVADOC_OPTION_HTML5, true);
          }
        }
      });
    });
  }

  /*
   * test {
   *   useJUnitPlatform()
   * }
   */
  private void configureTest(final TaskContainer tasks) {
    tasks.withType(Test.class).configureEach(Test::useJUnitPlatform);
  }
}
