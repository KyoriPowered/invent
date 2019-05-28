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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.javadoc.Javadoc;
import org.gradle.jvm.tasks.Jar;

public class JavadocJarPlugin implements Plugin<Project> {
  public static final String TASK_NAME = "javadocJar";
  public static final String CLASSIFIER = "javadoc";

  @Override
  public void apply(final Project project) {
    final PluginContainer plugins = project.getPlugins();
    plugins.apply(JavaPlugin.class);

    final TaskContainer tasks = project.getTasks();
    final Javadoc javadoc = tasks.withType(Javadoc.class).getByName(JavaPlugin.JAVADOC_TASK_NAME);
    final TaskProvider<Jar> jar = Invent.registerTask(tasks, TASK_NAME, Jar.class, provider -> provider.configure(task -> {
      task.getArchiveClassifier().set(CLASSIFIER);
      task.dependsOn(javadoc);
      task.from(javadoc.getDestinationDir());
    }));
    Invent.addArchive(project, jar);
  }
}
