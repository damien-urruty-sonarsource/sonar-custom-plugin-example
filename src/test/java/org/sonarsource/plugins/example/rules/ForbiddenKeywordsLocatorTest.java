package org.sonarsource.plugins.example.rules;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sonarsource.plugins.example.fixtures.InputFileFixtures.aFileWithContent;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.groups.Tuple.tuple;

import org.assertj.core.groups.Tuple;
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

    assertThat(occurrences)
      .extracting(this::textRangeTuple)
      .containsExactly(tuple(1, 0, 1, 3));
  }

  @Test
  public void it_should_locate_two_forbidden_keywords_separated_by_a_space() throws IOException {
    List<TextRange> occurrences = locator.locateAllIn(aFileWithContent("foo foo"));

    assertThat(occurrences)
      .extracting(this::textRangeTuple)
      .containsExactly(
        tuple(1, 0, 1, 3),
        tuple(1, 4, 1, 7)
      );
  }

  @Test
  public void it_should_locate_three_forbidden_keywords_separated_by_spaces() throws IOException {
    List<TextRange> occurrences = locator.locateAllIn(aFileWithContent("foo  foo  foo"));

    assertThat(occurrences)
      .extracting(this::textRangeTuple)
      .containsExactly(
        tuple(1, 0, 1, 3),
        tuple(1, 5, 1, 8),
        tuple(1, 10, 1, 13)
      );
  }

  @Test
  public void it_should_locate_two_forbidden_keywords_separated_by_a_line_return() throws IOException {
    List<TextRange> occurrences = locator.locateAllIn(aFileWithContent("foo\nfoo"));

    assertThat(occurrences)
      .extracting(this::textRangeTuple)
      .containsExactly(
        tuple(1, 0, 1, 3),
        tuple(2, 0, 2, 3)
      );
  }

  @Test
  public void it_should_locate_two_forbidden_keywords_separated_by_an_empty_line() throws IOException {
    List<TextRange> occurrences = locator.locateAllIn(aFileWithContent("foo\n\nfoo"));

    assertThat(occurrences)
      .extracting(this::textRangeTuple)
      .containsExactly(
        tuple(1, 0, 1, 3),
        tuple(3, 0, 3, 3)
      );
  }

  private Tuple textRangeTuple(TextRange textRange) {
    return tuple(
      textRange.start().line(),
      textRange.start().lineOffset(),
      textRange.end().line(),
      textRange.end().lineOffset()
    );
  }

  private ForbiddenKeywordsLocator locator;
}