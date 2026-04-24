package org.moodlescrapping.util;

import org.moodlescrapping.App;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility helper class for common operations such as loading prompt files
 * and cleaning extracted text.
 */
public class Util {

    /**
     * Loads a prompt file from the classpath resources under {@code /prompts}.
     * <p>
     * The file is expected to be UTF-8 encoded.
     *
     * @param name the filename of the prompt (e.g., {@code summarize-unit.txt})
     * @return the full content of the prompt file as a String
     * @throws IOException if an I/O error occurs while reading the file
     * @throws RuntimeException if the resource cannot be found in the classpath
     */
    public static String loadPrompt(String name) throws IOException {
        try (InputStream is = App.class.getClassLoader()
                .getResourceAsStream("prompts/" + name)) {

            if (is == null) {
                throw new RuntimeException("Prompt not found: " + name);
            }

            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Cleans extracted text by removing symbols and non-character Unicode ranges.
     * <p>
     * Specifically removes:
     * <ul>
     *     <li>Symbol characters (So)</li>
     *     <li>Unassigned Unicode code points (Cn)</li>
     * </ul>
     *
     * @param text the raw input text
     * @return a cleaned version of the text with unwanted characters removed
     */
    public static String cleanText(String text) {
        return text.replaceAll("[\\p{So}\\p{Cn}]", "");
    }
}