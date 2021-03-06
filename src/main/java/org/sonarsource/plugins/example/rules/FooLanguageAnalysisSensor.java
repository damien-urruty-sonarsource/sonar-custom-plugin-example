package org.sonarsource.plugins.example.rules;

import java.io.IOException;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.plugins.example.languages.FooLanguage;
import org.sonarsource.plugins.example.settings.FooLanguageProperties;

public class FooLanguageAnalysisSensor implements Sensor {

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor
            .name("Add issues on forbidden keyword")
            .onlyOnLanguage(FooLanguage.KEY)
            .createIssuesForRuleRepositories(FooLanguageRulesDefinition.REPO_KEY);
    }

    @Override
    public void execute(SensorContext context) {
        getFooFiles(context.fileSystem())
            .forEach(file -> searchForbiddenKeywordsIn(file, context));
    }

    private Iterable<InputFile> getFooFiles(FileSystem fs) {
        return fs.inputFiles(fs.predicates().hasLanguage(FooLanguage.KEY));
    }

    private void searchForbiddenKeywordsIn(InputFile file, SensorContext context) {
        try {
            String forbiddenKeyword = context.config()
                .get(FooLanguageProperties.FORBIDDEN_KEYWORD_KEY)
                .orElse(FooLanguageProperties.DEFAULT_FORBIDDEN_KEYWORD);
            new ForbiddenKeywordsLocator(forbiddenKeyword)
                .locateAllIn(file)
                .forEach(range -> raiseIssue(context, file, range));
        } catch (IOException e) {
            Loggers.get(getClass()).error("Unable to analyze " + file.uri(), e);
            throw new IllegalStateException(e);
        }
    }

    private void raiseIssue(SensorContext context, InputFile file, TextRange range) {
        NewIssue issue = context.newIssue().forRule(FooLanguageRulesDefinition.RULE_FORBIDDEN_KEYWORD);
        NewIssueLocation location = issue.newLocation().on(file).at(range);
        issue.at(location);
        issue.save();
    }

}