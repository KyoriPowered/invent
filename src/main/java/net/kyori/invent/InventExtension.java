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

import net.kyori.invent.data.GitHubIssues;
import net.kyori.invent.data.GitHubSCM;
import net.kyori.invent.data.Issues;
import net.kyori.invent.data.License;
import net.kyori.invent.data.SCM;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

public class InventExtension {
  final Property<Issues> issues;
  final Property<License> license;
  final Property<SCM> scm;

  public InventExtension(final ObjectFactory objects) {
    this.issues = objects.property(Issues.class);
    this.license = objects.property(License.class);
    this.scm = objects.property(SCM.class);
  }

  public void github(final String user, final String repo) {
    this.issues.set(new GitHubIssues(user, repo));
    this.scm.set(new GitHubSCM(user, repo));
  }

  public void githubIssues(final String user, final String repo) {
    this.issues.set(new GitHubIssues(user, repo));
  }

  public void githubSCM(final String user, final String repo) {
    this.scm.set(new GitHubSCM(user, repo));
  }

  // Licenses

  public void license(final String name, final String url) {
    this.license.set(new License(name, url));
  }

  public void mitLicense() {
    this.license("The MIT License", "https://opensource.org/licenses/MIT");
  }
}
