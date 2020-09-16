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

import net.kyori.invent.data.Issues;
import net.kyori.invent.data.License;
import net.kyori.invent.data.SCM;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.publish.plugins.PublishingPlugin;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.plugins.signing.Sign;
import org.gradle.plugins.signing.SigningExtension;
import org.gradle.plugins.signing.SigningPlugin;

public class InventPublishingPlugin implements Plugin<Project> {
  private static final String PUBLICATION = "maven";

  @Override
  public void apply(final Project project) {
    final PluginContainer plugins = project.getPlugins();
    plugins.apply(PublishingPlugin.class);
    plugins.apply(MavenPublishPlugin.class);
    plugins.apply(SigningPlugin.class);

    final ExtensionContainer extensions = project.getExtensions();
    final InventExtension invent = extensions.getByType(InventExtension.class);
    extensions.configure(PublishingExtension.class, extension -> {
      extension.publications(publications -> {
        publications.register(PUBLICATION, MavenPublication.class, publication -> {
          publication.pom(pom -> {
            pom.getDescription().set(project.getDescription());
            pom.getName().set(project.getName());
            pom.getUrl().set(invent.scm.get().url());

            pom.issueManagement(issueManagement -> {
              final Issues config = invent.issues.get();
              issueManagement.getSystem().set(config.system());
              issueManagement.getUrl().set(config.url());
            });
            pom.licenses(licenses -> {
              licenses.license(license -> {
                final License config = invent.license.get();
                license.getName().set(config.name());
                license.getUrl().set(config.url());
              });
            });
            pom.scm(scm -> {
              final SCM config = invent.scm.get();
              scm.getConnection().set(config.connection());
              scm.getDeveloperConnection().set(config.developerConnection());
              scm.getUrl().set(config.url());
            });
          });
        });
      });
    });
    extensions.configure(SigningExtension.class, extension -> {
      extension.useGpgCmd();
      extension.sign(extensions.getByType(PublishingExtension.class).getPublications().getByName(PUBLICATION));
    });

    final TaskContainer tasks = project.getTasks();
    tasks.withType(Sign.class).configureEach(new Action<Sign>() {
      @Override
      public void execute(final Sign sign) {
        sign.onlyIf(new Spec<Task>() {
          @Override
          public boolean isSatisfiedBy(final Task task) {
            return false;
          }
        });
      }
    });
  }
}
