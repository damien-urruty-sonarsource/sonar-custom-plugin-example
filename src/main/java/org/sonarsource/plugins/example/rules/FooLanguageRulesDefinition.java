/*
 * Example Plugin for SonarQube
 * Copyright (C) 2009-2020 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarsource.plugins.example.rules;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonarsource.plugins.example.languages.FooLanguage;

public final class FooLanguageRulesDefinition implements RulesDefinition {

  private static final String PATH_TO_RULES_XML = "/example/foolint-rules.xml";

  public static final String FORBIDDEN_KEYWORD_RULE_KEY = "ForbiddenKeyword";

  public static final String REPO_KEY = FooLanguage.KEY;
  protected static final String REPO_NAME = FooLanguage.KEY;

  public static final RuleKey RULE_FORBIDDEN_KEYWORD = RuleKey.of(REPO_KEY, FORBIDDEN_KEYWORD_RULE_KEY);

  @Override
  public void define(Context context) {
    defineRulesForLanguage(context, REPO_KEY, REPO_NAME, FooLanguage.KEY);
  }

  private void defineRulesForLanguage(Context context, String repositoryKey, String repositoryName, String languageKey) {
    NewRepository repository = context.createRepository(repositoryKey, languageKey).setName(repositoryName);

    InputStream rulesXml = this.getClass().getResourceAsStream(rulesDefinitionFilePath());
    if (rulesXml != null) {
      RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
      rulesLoader.load(repository, rulesXml, StandardCharsets.UTF_8.name());
    }

    repository.done();
  }

  protected String rulesDefinitionFilePath() {
    return PATH_TO_RULES_XML;
  }

}
