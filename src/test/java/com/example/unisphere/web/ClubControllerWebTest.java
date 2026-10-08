package com.example.unisphere.web;

import com.example.unisphere.controller.ClubController;
import com.example.unisphere.dto.club.ClubResponse;
import com.example.unisphere.dto.club.CreateClubRequest;
import com.example.unisphere.dto.club.UpdateClubRequest;
import com.example.unisphere.exception.ClubNotFoundException;
import com.example.unisphere.service.ClubService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClubController.class)
public class ClubControllerWebTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ClubService clubService;
    @Test
    void getClubByIdReturnsClub() throws Exception {
        ClubResponse expectedResponse = new ClubResponse();
        expectedResponse.setId(1L);
        when(clubService.getClubById(1L)).thenReturn(expectedResponse);
        mockMvc.perform(get("/clubs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        verify(clubService).getClubById(1L);
    }
    @Test
    void getClubByIdReturnsNotFound() throws Exception{
        when(clubService.getClubById(1L)).thenThrow( new ClubNotFoundException());
        mockMvc.perform(get("/clubs/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Club not found"));
    }
    @Test
    void getClubsReturnsClubs() throws Exception {
        ClubResponse expectedResponse = new ClubResponse();
        expectedResponse.setId(1L);
        ClubResponse expectedResponse1 = new ClubResponse();
        expectedResponse1.setId(2L);
        when(clubService.getAllClubs()).thenReturn(List.of(expectedResponse,expectedResponse1));
        mockMvc.perform(get("/clubs")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
        verify(clubService).getAllClubs();
    }
    @Test
    void createClubReturnsCreated()throws Exception{
        String requestBody = """
        {
            "name": "Test name",
            "description": "Test description",
            "imageUrl": "Test image url",
            "category": "SPORTS"
        }
        """;
        ClubResponse expectedResponse = new ClubResponse();
        expectedResponse.setId(1L);
        when(clubService.createClub(any(CreateClubRequest.class))).thenReturn(expectedResponse);
        mockMvc.perform(post("/clubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
    @Test
    void createClubReturnsBadRequestWhenFieldIsMissing() throws Exception {
        String requestBody = """
        {
             "description": "Test description",
             "imageUrl": "Test image url",
             "category": "SPORTS"
        }
        """;
        mockMvc.perform(post(("/clubs"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name").exists());
    }
    @Test
    void createClubReturnsBadRequestWhenCategoryIsMissing() throws Exception {
        String requestBody = """
        {
            "name": "Test name",
            "description": "Test description",
            "imageUrl": "Test image url"
        }
        """;
        mockMvc.perform(post(("/clubs"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.category").exists());
    }
    @Test
    void createClubReturnsBadRequestWhenInvalidRequestBody() throws Exception {
        String requestBody = """
        {
             "name": "Test name",
             "description": "Test description",
             "imageUrl": "Test image url",
             "category": "SPORTS"
        """;
        mockMvc.perform(post("/clubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request body"));
    }
    @Test
    void updateClubReturnsUpdatedClub() throws Exception {
        String requestBody = """
        {
             "name": "Test name",
             "description": "Test description",
             "imageUrl": "Test image url",
             "category": "SPORTS"
        }
        """;
        ClubResponse expectedResponse = new ClubResponse();
        expectedResponse.setId(1L);
        when(clubService.updateClub(any(UpdateClubRequest.class),eq(1L)))
                .thenReturn(expectedResponse);
        mockMvc.perform(
                        put("/clubs/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
    @Test
    void updateClubReturnsNotFound() throws Exception {
        String requestBody = """
        {
            "name": "Test name",
            "description": "Test description",
            "imageUrl": "Test image url",
            "category": "SPORTS"
        }
        """;
        when(clubService.updateClub(any(UpdateClubRequest.class),eq(1L)))
                .thenThrow(new ClubNotFoundException());
        mockMvc.perform(put("/clubs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Club not found"));
    }
    @Test
    void updateClubReturnsBadRequestWhenInvalidRequestBody() throws Exception {
        String requestBody = """
        {
             "name": "Test name",
             "description": "Test description",
             "imageUrl": "Test image url",
             "category": "SPORTS"
        """;
        mockMvc.perform(put("/clubs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request body"));
    }
    @Test
    void updateClubReturnsBadRequestWhenFieldIsMissing() throws Exception {
        String requestBody = """
        {
                "description": "Test description",
                "imageUrl": "Test image url",
                "category": "SPORTS"
        }
        """;
        mockMvc.perform(put(("/clubs/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name").exists());
    }
    @Test
    void updateClubReturnsBadRequestWhenIdIsInvalid() throws Exception {
        mockMvc.perform(put("/clubs/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "name": "Test name",
                        "description": "Test description",
                        "imageUrl": "Test image url",
                        "category": "SPORTS"
                    }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request parameter"));
    }
    @Test
    void updateClubReturnsBadRequestWhenCategoryIsMissing() throws Exception {
        String requestBody = """
        {
            "name": "Test name",
            "description": "Test description",
            "imageUrl": "Test image url"
        }
        """;
        mockMvc.perform(put(("/clubs/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.category").exists());
    }
    @Test
    void deleteClubByIdReturnsNoContent() throws Exception {
        mockMvc.perform(delete ("/clubs/1"))
                .andExpect(status().isNoContent());
        verify(clubService).deleteClubById(1L);
    }
    @Test
    void deleteClubByIdReturnsNotFound() throws Exception {
        doThrow(new ClubNotFoundException()).when(clubService).deleteClubById(1L);
        mockMvc.perform(delete("/clubs/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Club not found"));
    }
    @Test
    void unsupportedHttpMethodReturnsMethodNotAllowed() throws Exception {
        mockMvc.perform(patch("/clubs/1"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message").value("HTTP method not allowed"));
    }


}
