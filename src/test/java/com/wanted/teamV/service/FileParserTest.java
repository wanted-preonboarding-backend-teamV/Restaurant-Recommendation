package com.wanted.teamV.service;

import com.wanted.teamV.service.impl.FileParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FileParserTest {

    @Autowired
    FileParser fileParser;

    @Nested
    @DisplayName("파일을 읽는다.")
    class parse {

        @Test
        @DisplayName("성공")
        void success() {
            //given

            //when
            List<String> data = fileParser.parse();

            //then
            assertThat(data).isNotEmpty();
        }
    }
}
