package com.terragrouplabs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TerraGroupLabsLandingPageApplicationTests {

    @Test
    void contextLoads() {
        // This test will now run with application-test.properties
    }

}