package org.sonarsource.plugins.example.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;

public class ForbiddenKeywordsLocator {

    public ForbiddenKeywordsLocator(String forbiddenKeyword) {
        this.forbiddenKeyword = forbiddenKeyword;
    }

    public List<TextRange> locateAllIn(InputFile file) throws IOException {
        List<TextRange> ranges = new ArrayList<>();
        int lineNumber = 1;
        for (String line : splitLines(file.contents())) {
            ranges.addAll(locateAllIn(file, line, lineNumber));
            lineNumber++;
        }
        return ranges;
    }

    private String[] splitLines(String string) {
        return string.split("\\r?\\n");
    }

    private List<TextRange> locateAllIn(InputFile file, String line, int lineNumber) {
        List<TextRange> ranges = new ArrayList<>();
        int lineOffset = 0;
        int wordStartIndex;
        do {
            wordStartIndex = line.indexOf(forbiddenKeyword);
            if (wordStartIndex != -1) {
                int wordEndIndex = wordStartIndex + forbiddenKeyword.length();
                ranges.add(file.newRange(lineNumber, lineOffset + wordStartIndex, lineNumber, lineOffset + wordEndIndex));
                line = line.substring(wordEndIndex);
                lineOffset += wordEndIndex;
            }
        } while (wordStartIndex != -1);
        return ranges;
    }

    private final String forbiddenKeyword;

}