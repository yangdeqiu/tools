package com.ydq.tools.io.bio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * bio原生是否支持相对路径
 */
@Slf4j
public class TestXdPath {

    public static void main(String[] args) {
        String path = "/application.yml";
        File file = new File(path);
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(

                                // path="application.yml";
                                // Thread.currentThread().getContextClassLoader().getResourceAsStream(path), StandardCharsets.UTF_8));

                                // path="src/main/resources/application.yml";
                                // new FileInputStream(file), StandardCharsets.UTF_8));

                                // path="../../../../../application.yml";
                                // path="/application.yml";
                                //TestXdPath.class.getResourceAsStream(path), StandardCharsets.UTF_8));
                                new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8));
        ) {
            log.info(file.getAbsolutePath());
            String line;
            while ((line = br.readLine()) != null) {
                log.debug(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
