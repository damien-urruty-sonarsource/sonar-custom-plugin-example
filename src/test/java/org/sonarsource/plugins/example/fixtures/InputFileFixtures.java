package org.sonarsource.plugins.example.fixtures;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;

public class InputFileFixtures {
    public static InputFile aFileWithContent(String content) {
        return new TestInputFileBuilder("moduleKey", "relative/path/from/module/baseDir.java")
            .setContents(content)
            .build();
    }
}