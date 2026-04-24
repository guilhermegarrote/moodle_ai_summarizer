package org.moodlescrapping.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

/**
 * Service wrapper for interacting with the Gemini API via the Google GenAI client.
 * <p>
 * This class is responsible for:
 * <ul>
 *     <li>Initializing the API client</li>
 *     <li>Reading the model name from environment variables</li>
 *     <li>Sending prompt requests and returning generated text</li>
 * </ul>
 * <p>
 * Environment variables:
 * <ul>
 *     <li><b>GEMINI_MODEL</b> - The model identifier to use for requests</li>
 * </ul>
 */
public class GeminiService {

    /**
     * Underlying Google GenAI client used to perform requests.
     */
    private final Client client;

    /**
     * Name of the Gemini model used for content generation.
     */
    private final String model;

    /**
     * Creates a new {@code GeminiService}.
     * <p>
     * The constructor reads the model name from the {@code GEMINI_MODEL}
     * environment variable and initializes the API client.
     *
     * @throws IllegalStateException if {@code GEMINI_MODEL} is not set or blank
     */
    public GeminiService() {
        this.model = System.getenv("GEMINI_MODEL");

        if (model == null || model.isBlank()) {
            throw new IllegalStateException("GEMINI_MODEL not set");
        }

        this.client = new Client();
    }

    /**
     * Sends a prompt to the configured Gemini model and returns the generated text.
     *
     * @param prompt the input text prompt to send to the model
     * @return the generated response text from the model
     * @throws RuntimeException if the request fails for any reason
     */
    public String request(String prompt) {
        try {
            GenerateContentResponse response =
                    client.models.generateContent(
                            model,
                            prompt,
                            null
                    );

            return response.text();

        } catch (Exception e) {
            throw new RuntimeException("Gemini request failed", e);
        }
    }
}