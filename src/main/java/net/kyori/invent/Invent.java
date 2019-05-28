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

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.jvm.tasks.Jar;

import java.util.function.Consumer;

public class Invent {
  private static final String ARCHIVES = "archives";

  /**
   * Registers a task, and allows {@code consumer} to configure it.
   *
   * @param tasks the task container
   * @param name the task name
   * @param type the task type
   * @param consumer the task configurer
   * @param <T> the task type
   * @return the task provider
   */
  public static <T extends Task> TaskProvider<T> registerTask(final TaskContainer tasks, final String name, final Class<T> type, final Consumer<TaskProvider<T>> consumer) {
    final TaskProvider<T> task = tasks.register(name, type);
    consumer.accept(task);
    return task;
  }

  public static void addArchive(final Project project, final TaskProvider<Jar> task) {
    project.artifacts(artifacts -> artifacts.add(ARCHIVES, task));
  }
}
