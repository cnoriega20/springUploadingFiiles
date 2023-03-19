package com.cn.springUploadingfiiles.service.impl;

import com.cn.springUploadingfiiles.exception.StorageFileNotFoundException;
import com.cn.springUploadingfiiles.service.StorageService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.*;

@AutoConfigureMockMvc
@SpringBootTest
public class FileUploadTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;

    @Test
    public void shouldListALlFilesTest() throws Exception {
        given(this.storageService.loadAll())
                .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));
        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(model().attribute("files",
                        Matchers.contains("http://localhost/files/first.txt",
                                "http://localhost/files/second.txt")));
    }
    @Test
    public void shouldSaveUploadedFileTest() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Springboot File Upload Example".getBytes());
        this.mockMvc.perform(multipart("/").file(mockMultipartFile))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/"));
        then(this.storageService).should().store(mockMultipartFile);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should404MissingFileTest() throws Exception {
        given(this.storageService.loadAsResource("test.txt"))
                .willThrow(StorageFileNotFoundException.class);
        this.mockMvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
    }

}