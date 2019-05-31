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
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.plugins.PublishingPlugin;

public class SonatypeRepositoryPlugin implements Plugin<Project> {
  private static final String REPOSITORY_NAME = "sonatype";

  private static final String REPOSITORY_URL_RELEASE_STAGING = "https://oss.sonatype.org/service/local/staging/deploy/maven2/";
  private static final String REPOSITORY_URL_SNAPSHOT = "https://oss.sonatype.org/content/repositories/snapshots/";

  private static final String PROPERTY_USERNAME = "sonatype.username";
  private static final String PROPERTY_PASSWORD = "sonatype.password";

  @Override
  public void apply(final Project project) {
    final PluginContainer plugins = project.getPlugins();
    plugins.apply(PublishingPlugin.class);

    if(project.hasProperty(PROPERTY_USERNAME) && project.hasProperty(PROPERTY_PASSWORD)) {
      final ExtensionContainer extensions = project.getExtensions();
      final PublishingExtension publishing = extensions.getByType(PublishingExtension.class);

      final RepositoryHandler repositories = publishing.getRepositories();

      repositories.maven(repository -> {
        repository.setName(REPOSITORY_NAME);
        repository.setUrl(this.isSnapshot(project) ? REPOSITORY_URL_SNAPSHOT : REPOSITORY_URL_RELEASE_STAGING);
        repository.credentials(credentials -> {
          credentials.setUsername((String) project.property(PROPERTY_USERNAME));
          credentials.setPassword((String) project.property(PROPERTY_PASSWORD));
        });
      });
    } else {
      project.getLogger().debug("Cannot add {} repository for publication - '{}' and '{}' properties are not defined.", REPOSITORY_NAME, PROPERTY_USERNAME, PROPERTY_PASSWORD);
    }
  }

  private boolean isSnapshot(final Project project) {
    return String.valueOf(project.getVersion()).endsWith("-SNAPSHOT");
  }
}
