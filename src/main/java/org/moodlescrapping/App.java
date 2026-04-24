package org.moodlescrapping;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.moodlescrapping.ai.GeminiService;
import org.moodlescrapping.client.MoodleClient;
import org.moodlescrapping.util.Util;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Main application entry point for scraping Moodle course content and
 * generating a summarized Markdown file using the Gemini API.
 * <p>
 * Workflow:
 * <ol>
 *     <li>Authenticate into Moodle</li>
 *     <li>Fetch course page</li>
 *     <li>Extract course sections</li>
 *     <li>Scrape unit content concurrently</li>
 *     <li>Send content to Gemini for summarization</li>
 *     <li>Generate final course summary file</li>
 * </ol>
 */
public class App {

    /**
     * Application entry point.
     *
     * @param args command-line arguments (not used)
     * @throws Exception if any critical I/O, network, or execution error occurs
     */
    public static void main(String[] args) throws Exception {

        final MoodleClient moodleClient = new MoodleClient();
        moodleClient.login(
                Objects.requireNonNull(System.getenv("MOODLE_USERNAME"), "MOODLE_USERNAME missing"),
                Objects.requireNonNull(System.getenv("MOODLE_PASSWORD"), "MOODLE_PASSWORD missing")
        );

        GeminiService geminiService = new GeminiService();

        Document courseHtml = moodleClient.get(
                Objects.requireNonNull(System.getenv("MOODLE_URL"), "MOODLE_URL missing")
                        + "/course/view.php?id="
                        + Objects.requireNonNull(System.getenv("MOODLE_COURSE_ID"), "MOODLE_COURSE_ID missing")
        );

        StringBuilder courseContent = new StringBuilder();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<String>> tasks = new ArrayList<>();

        Elements courseSections = courseHtml.select("div.section-item");
        String basePrompt = Util.loadPrompt("summarize-unit.txt");

        for (Element section : courseSections) {

            tasks.add(executor.submit(() -> {

                String unitContent = fetchUnitContent(section, moodleClient);

                if (unitContent == null || unitContent.isBlank()) {
                    return null;
                }

                String prompt = basePrompt + unitContent;
                return geminiService.request(prompt);
            }));
        }

        for (Future<String> f : tasks) {
            try {
                String result = f.get(2, TimeUnit.MINUTES);

                if (result != null) {
                    courseContent.append(result).append("\n\n\n");
                }

            } catch (Exception e) {
                System.err.println("Task failed:");
                e.printStackTrace();
            }
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);

        String finalPrompt =
                Util.loadPrompt("review-course-content.txt")
                        + courseContent;

        try (FileWriter writer = new FileWriter("course-content.md")) {
            writer.write(geminiService.request(finalPrompt));
        }

        System.out.println("\nCourse summarized!");
    }

    /**
     * Extracts and aggregates all textual content from a single Moodle course section.
     * <p>
     * This method:
     * <ul>
     *     <li>Identifies the unit name</li>
     *     <li>Finds all relevant Moodle page links</li>
     *     <li>Fetches each page content</li>
     *     <li>Extracts headers and main content blocks</li>
     * </ul>
     *
     * @param section the HTML element representing a course section
     * @param client  authenticated Moodle client used for HTTP requests
     * @return aggregated text content of the unit, or {@code null} if empty or unavailable
     */
    private static String fetchUnitContent(Element section, MoodleClient client) {

        Element unitEl = section.selectFirst("h3 a:matches(Unidade \\d+)");
        String unitName = unitEl != null ? Util.cleanText(unitEl.text()) : "Unknown Unit";

        Element ul = section.selectFirst("ul");
        if (ul == null) return null;

        List<String> links = ul.select("a[href]")
                .stream()
                .map(el -> el.attr("href"))
                .filter(link -> link.contains("/mod/page/view.php"))
                .toList();

        StringBuilder unitContent = new StringBuilder();

        for (String link : links) {
            try {
                Document page = client.get(link);

                Element content = page.selectFirst("div.box.py-3.generalbox");
                Element headerContainer = page.selectFirst("div.page-header-headings");

                Element header = (headerContainer != null && !headerContainer.children().isEmpty())
                        ? headerContainer.child(0)
                        : null;

                if (header != null) {
                    unitContent.append(header.text()).append("\n");
                }

                if (content != null) {
                    unitContent.append(content.text()).append("\n\n");
                }

            } catch (Exception e) {
                System.out.println("Error: " + link + " -> " + e.getMessage());
            }
        }

        if (unitContent.isEmpty()) return null;

        System.out.println(unitName + " content fetched!");
        return unitContent.toString();
    }
}