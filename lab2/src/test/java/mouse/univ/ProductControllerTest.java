package mouse.univ;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnProductForSimpleNumbers() throws Exception {
        mockMvc.perform(get("/api/product")
                        .param("a", "2")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("6"));

        mockMvc.perform(get("/api/product")
                        .param("a", "4")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string("20"));

        mockMvc.perform(get("/api/product")
                        .param("a", "-2")
                        .param("b", "6"))
                .andExpect(status().isOk())
                .andExpect(content().string("-12"));
    }

    @Test
    void shouldReturnZeroWhenMultipliedByZero() throws Exception {
        mockMvc.perform(get("/api/product")
                        .param("a", "123")
                        .param("b", "0"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    void shouldReturnProductForTwoMaxAllowedValues() throws Exception {
        mockMvc.perform(get("/api/product")
                        .param("a", "10000")
                        .param("b", "10000"))
                .andExpect(status().isOk())
                .andExpect(content().string("100000000"));
    }

    @Test
    void shouldReturnProductForTwoMinAllowedValues() throws Exception {
        mockMvc.perform(get("/api/product")
                        .param("a", "-10000")
                        .param("b", "-10000"))
                .andExpect(status().isOk())
                .andExpect(content().string("100000000"));
    }

    @Test
    void shouldReturnErrorWhenAIsBelowAllowed() throws Exception {
        mockMvc.perform(get("/api/product")
                        .param("a", "-10001")
                        .param("b", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid value for parameter a"));
    }

    @Test
    void shouldReturnErrorWhenBIsBelowAllowed() throws Exception {
        mockMvc.perform(get("/api/product")
                        .param("a", "1")
                        .param("b", "-10001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid value for parameter b"));
    }

    @Test
    void shouldReturnErrorWhenAIsAboveAllowed() throws Exception {
        mockMvc.perform(get("/api/product")
                        .param("a", "10001")
                        .param("b", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid value for parameter a"));
    }

    @Test
    void shouldReturnErrorWhenBIsAboveAllowed() throws Exception {
        mockMvc.perform(get("/api/product")
                        .param("a", "1")
                        .param("b", "10001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid value for parameter b"));
    }
}