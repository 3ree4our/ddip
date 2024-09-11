package org.threefour.ddip;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = DdipApplication.class)
class DdipApplicationTests {
    @Test
    void contextLoads() {
    }
}