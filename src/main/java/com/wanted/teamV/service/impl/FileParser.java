package com.wanted.teamV.service.impl;

import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class FileParser {
    private static final String PATH = "static/sgg_lat_lon.csv";

    public List<String> parse() {
        ClassPathResource resource = new ClassPathResource(PATH);

        try {
            Path path = Paths.get(resource.getURI());
            List<String> lines = Files.readAllLines(path);
            lines.remove(0);

            return lines;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAIL_READ_FILE);
        }
    }
}
