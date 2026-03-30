package org.duo.duo.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageService {

    @Value("${file.board-dir}")
    private String boardDir;

    private static final Pattern BASE64_IMG_PATTERN =
            Pattern.compile("src\\s*=\\s*\"data:image/([a-zA-Z]+);base64,([^\"]+)\"");

    public String processBase64Images(String content) {
        if (content == null || !content.contains("data:image")) {
            return content;
        }

        Matcher matcher = BASE64_IMG_PATTERN.matcher(content);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String extension = matcher.group(1);
            String base64Data = matcher.group(2);

            try {
                String fileName = UUID.randomUUID() + "." + extension;
                Path dirPath = Paths.get(boardDir);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }

                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                Files.write(dirPath.resolve(fileName), imageBytes);

                matcher.appendReplacement(result, "src=\"/uploads/board/" + fileName + "\"");
            } catch (IOException e) {
                // Base64 디코딩/저장 실패 시 원본 유지
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }
}