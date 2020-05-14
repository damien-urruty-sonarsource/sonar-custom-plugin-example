package org.sonarsource.plugins.example.rules;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sonarsource.plugins.example.fixtures.InputFileFixtures.aFileWithContent;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.TextRange;

public class ForbiddenKeywordsLocatorTest {

  @Before
  public void setUp() {
    locator = new ForbiddenKeywordsLocator("foo");
  }

  @Test
  public void it_should_not_locate_forbidden_keyword_if_file_is_empty() throws IOException {
    List<TextRange> occurrences = locator.locateAllIn(aFileWithContent(""));

    assertThat(occurrences).isEmpty();
  }

  @Test
  public void it_should_locate_forbidden_keyword_if_file_contains_only_the_keyword() throws IOException {
    List<TextRange> occurrences = locator.locateAllIn(aFileWithContent("foo"));

    assertThat(occurrences).hasSize(1);
    TextRange actualRange = occurrences.get(0);
    assertThat(actualRange.start().line()).isEqualTo(1);
    assertThat(actualRange.start().lineOffset()).isEqualTo(0);
    assertThat(actualRange.end().line()).isEqualTo(1);
    assertThat(actualRange.end().lineOffset()).isEqualTo(3);
  }

  @Test
  public void it_should_locate_two_forbidden_keywords_separated_by_a_space() throws IOException {
    List<TextRange> occurrences = locator.locateAllIn(aFileWithContent("foo foo"));

    assertThat(occurrences).hasSize(2);
    TextRange actualRange = occurrences.get(1);
    assertThat(actualRange.start().line()).isEqualTo(1);
    assertThat(actualRange.start().lineOffset()).isEqualTo(4);
    assertThat(actualRange.end().line()).isEqualTo(1);
    assertThat(actualRange.end().lineOffset()).isEqualTo(7);
  }

  @Test
  public void it_should_locate_three_forbidden_keywords_separated_by_spaces() throws IOException {
    List<TextRange> occurrences = locator.locateAllIn(aFileWithContent("foo  foo  foo"));

    assertThat(occurrences).hasSize(3);
    TextRange actualRange = occurrences.get(2);
    assertThat(actualRange.start().line()).isEqualTo(1);
    assertThat(actualRange.start().lineOffset()).isEqualTo(10);
    assertThat(actualRange.end().line()).isEqualTo(1);
    assertThat(actualRange.end().lineOffset()).isEqualTo(13);
  }

  @Test
  public void it_should_locate_two_forbidden_keywords_separated_by_a_line_return() throws IOException {
    List<TextRange> occurrences = locator.locateAllIn(aFileWithContent("foo\nfoo"));

    assertThat(occurrences).hasSize(2);
    TextRange actualRange = occurrences.get(1);
    assertThat(actualRange.start().line()).isEqualTo(2);
    assertThat(actualRange.start().lineOffset()).isEqualTo(0);
    assertThat(actualRange.end().line()).isEqualTo(2);
    assertThat(actualRange.end().lineOffset()).isEqualTo(3);
  }

  @Test
  public void it_should_locate_two_forbidden_keywords_separated_by_an_empty_line() throws IOException {
    List<TextRange> occurrences = locator.locateAllIn(aFileWithContent("foo\n\nfoo"));

    assertThat(occurrences).hasSize(2);
    TextRange actualRange = occurrences.get(1);
    assertThat(actualRange.start().line()).isEqualTo(3);
    assertThat(actualRange.start().lineOffset()).isEqualTo(0);
    assertThat(actualRange.end().line()).isEqualTo(3);
    assertThat(actualRange.end().lineOffset()).isEqualTo(3);
  }

  private ForbiddenKeywordsLocator locator;
}