package mouse.univ;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RateLimitPluginTest {


    private RateLimitPlugin rateLimitPlugin;
    private final String IP = "127.0.0.1";

    @Test
    @Order(1)
    void createRateLimiter() {
        rateLimitPlugin = new RateLimitPlugin(0.01, 3);
    }

    @Test
    @Order(2)
    void fillBucket() {
        for (int i = 0; i < 3; i++) {
            boolean accept = rateLimitPlugin.accept(IP);
            assertTrue(accept);
        }
    }

    @Test
    @Order(3)
    void bucketRejects() {
        assertFalse(rateLimitPlugin.accept(IP));
        assertFalse(rateLimitPlugin.accept(IP));
        assertFalse(rateLimitPlugin.accept(IP));
    }

    @Test
    @Order(4)
    void bucketAcceptsIfInactive() {
        rateLimitPlugin.setActive(false);
        assertTrue(rateLimitPlugin.accept(IP));
    }

    @Test
    @Order(5)
    void bucketRejectsIfActive() {
        rateLimitPlugin.setActive(true);
        assertFalse(rateLimitPlugin.accept(IP));
    }

    @Test
    @Order(6)
    void bucketAcceptsAfterReset() {
        rateLimitPlugin.reset();
        for (int i = 0; i < 3; i++) {
            boolean accept = rateLimitPlugin.accept(IP);
            assertTrue(accept);
        }
    }

    @Test
    @Order(7)
    void bucketAcceptsDifferentIp() {
        rateLimitPlugin.reset();
        for (int i = 0; i < 3; i++) {
            boolean accept = rateLimitPlugin.accept("10.0.0.1");
            assertTrue(accept);
        }
    }

    @Test
    @Order(8)
    void bucketWithNewRate() {
        rateLimitPlugin = new RateLimitPlugin(1, 1);
    }

    @Test
    @Order(9)
    void bucketRecoversTokens() throws InterruptedException {
        assertTrue(rateLimitPlugin.accept(IP));
        Thread.sleep(1000);
        assertTrue(rateLimitPlugin.accept(IP));
    }
}