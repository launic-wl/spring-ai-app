package com.laurentiu.spring_ai_app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LlmResponseExtractor {

    private static final String START_MARKER = "<|begin_of_text|>";
    private static final String END_MARKER = "<|eot_id|>";

    private LlmResponseExtractor() {}



    public static String extractResponse(String input) {

        // Find the start index
        int startIndex = input.indexOf(START_MARKER);
        if (startIndex == -1) {
            return ""; // or return input if you want to return original
            // text when start marker is not found
        }

        // Calculate the actual start position (after the marker)
        startIndex += START_MARKER.length();

        // Find the end index
        int endIndex = input.indexOf(END_MARKER);

        // If end marker is not found, use the end of string
        if (endIndex == -1) {
            return input.substring(startIndex);
        }

        // Return the text between markers
        return input.substring(startIndex, endIndex);
    }

}
