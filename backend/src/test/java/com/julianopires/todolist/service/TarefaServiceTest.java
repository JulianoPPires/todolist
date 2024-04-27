package com.julianopires.todolist.service;

import com.julianopires.todolist.dto.request.TarefaDTO;
import com.julianopires.todolist.dto.response.TarefaResponseDTO;
import com.julianopires.todolist.fixture.TarefaFixture;
import com.julianopires.todolist.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TarefaServiceTest {

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    TarefaRepository tarefaRepository;

    @BeforeEach
    void setUp() {
        tarefaRepository.deleteAll();
    }

    @Test
    void criarTarefa_DeveRetornarTarefaResponseDTOQuandoTarefaCriadaComSucesso() {
        TarefaDTO tarefaDTO = TarefaFixture.criarTarefa();

        TarefaResponseDTO responseDTO = tarefaService.criarTarefa(tarefaDTO);

        TarefaResponseDTO expectedResponseDTO = TarefaResponseDTO.builder()
                .id(1L)
                .titulo(tarefaDTO.getTitulo())
                .descricao(tarefaDTO.getDescricao())
                .dataCriacao(tarefaDTO.getDataCriacao())
                .dataConclusao(tarefaDTO.getDataConclusao())
                .status(tarefaDTO.getStatus())
                .build();

        assertEquals(expectedResponseDTO, responseDTO);
    }

    @Test
    void buscarTodasTarefas_DeveRetornarListaDeTarefas() {
        TarefaDTO tarefaDTO = TarefaFixture.criarTarefa();
        TarefaDTO tarefaDTO2 = TarefaFixture.criarTarefa();

        tarefaService.criarTarefa(tarefaDTO);
        tarefaService.criarTarefa(tarefaDTO2);

        List<TarefaResponseDTO> tarefasDTO = tarefaService.buscarTodasTarefas();

        assertEquals(tarefasDTO.size(), 2);
    }


    @Test
    void editarTarefa_DeveEditarTarefaComSucessoQuandoTarefaEncontrada() {
        TarefaDTO tarefaDTO = TarefaFixture.criarTarefa();
        tarefaService.criarTarefa(tarefaDTO);

        TarefaDTO tarefaEditada = TarefaFixture.criarTarefa2();

        List<TarefaResponseDTO> tarefasDTO = tarefaService.buscarTodasTarefas();

        TarefaResponseDTO responseEdicao = tarefaService.editarTarefa(tarefaEditada, tarefasDTO.get(0).getId());

        TarefaResponseDTO responseEsperada = TarefaFixture.criarTarefaResponseEdicao(tarefasDTO.get(0).getId());

        assertEquals(responseEsperada, responseEdicao);
    }

    @Test
    void editarTarefa_DeveLancarEntityNotFoundExceptionQuandoTarefaNaoEncontrada() {
        TarefaDTO tarefaEditada = TarefaFixture.criarTarefa2();

        assertThrows(EntityNotFoundException.class, () -> tarefaService.editarTarefa(tarefaEditada, 1L));
    }

    @Test
    void removerTarefa_DeveRemoverTarefaQuandoExistir() {
        TarefaDTO tarefaDTO = TarefaFixture.criarTarefa();

        tarefaService.criarTarefa(tarefaDTO);

        List<TarefaResponseDTO> tarefasDTO = tarefaService.buscarTodasTarefas();

        assertDoesNotThrow(() -> tarefaService.removerTarefa(tarefasDTO.get(0).getId()));
        assertThrows(EntityNotFoundException.class, () -> tarefaService.buscarTarefa(tarefasDTO.get(0).getId()));
    }

    @Test
    void removerTarefa_DeveLancarEntityNotFoundExceptionQuandoTarefaNaoExistir() {
        assertThrows(EntityNotFoundException.class, () -> tarefaService.removerTarefa(1L));
    }

}