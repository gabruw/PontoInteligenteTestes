package com.kazale.pontointeligente.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kazale.pontointeligente.api.dtos.LancamentoDto;
import com.kazale.pontointeligente.api.entities.Funcionario;
import com.kazale.pontointeligente.api.entities.Lancamento;
import com.kazale.pontointeligente.api.enums.TipoEnum;
import com.kazale.pontointeligente.api.services.FuncionarioService;
import com.kazale.pontointeligente.api.services.LancamentoService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GabrielMFTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private LancamentoService lancamentoService;
	
	@MockBean
	private FuncionarioService funcionarioService;
	
	// Fields
	private static final String URL_BASE = "/api/lancamentos/";
	private static final Date DATA = new Date();
	private static final Long ID_LANCAMENTO = 1L;
	private static final Long ID_FUNCIONARIO = 1L;
	private static final String TIPO = TipoEnum.INICIO_TRABALHO.name();
	
	// Pattern
	private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON;
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Test
	@WithMockUser
	public void registerLancamentoWithSuccess() throws Exception {
		// Criação do objeto lançamento
		Lancamento lancamento = new Lancamento();
		lancamento.setId(ID_LANCAMENTO);
		lancamento.setData(DATA);
		lancamento.setTipo(TipoEnum.valueOf(TIPO));
		lancamento.setFuncionario(new Funcionario());
		lancamento.getFuncionario().setId(ID_FUNCIONARIO);
		
		// O Mockito irá buscar um funcionário com qualquer ID "Long" e retornará para uma instância de um objeto "Funcionario"
		BDDMockito.given(this.funcionarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Funcionario()));
		// O Mockito ir persistir um lançamento com dados mocados e retornará estes resultados para o "lancamento"
		BDDMockito.given(this.lancamentoService.persistir(Mockito.any(Lancamento.class))).willReturn(lancamento);
		
		String json = gerarJson();
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(json) // Conteúdo de envio no POST
				.contentType(CONTENT_TYPE) // Tipo de conteúdo a ser enviado
				.accept(CONTENT_TYPE)) // Tipo de conteúdo aceito pelo método da requerido
				.andExpect(status().isOk()) // Status aguardado no retorno da requisição
				.andExpect(jsonPath("$.data.id").value(ID_LANCAMENTO)) // Verifica se o ID_LANCAMENTO retornado do cadastro é igual ao ID_LANCAMENTO solicitado
				.andExpect(jsonPath("$.data.tipo").value(TIPO)) // Verifica se o TIPO retornado do cadastro é igual ao TIPO solicitado
				.andExpect(jsonPath("$.data.data").value(DATE_FORMAT.format(DATA))) // Verifica se a DATA retornada do cadastro é igual ao DATA solicitada
				.andExpect(jsonPath("$.data.funcionarioId").value(ID_FUNCIONARIO)) // Verifica se o ID_FUNCIONARIO retornado do cadastro é igual ao ID_FUNCIONARIO solicitado
				.andExpect(jsonPath("$.errors").isEmpty()); // Verifica se nenhum erro foi retornando
	}
	
	@Test
	@WithMockUser
	public void registerLancamentoWithEmptyFuncionario() throws Exception {
		// O Mockito irá buscar um funcionário com qualquer ID "Long" e retornará para um objeto vazio
		BDDMockito.given(this.funcionarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.empty());

		String json = gerarJson();
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(json)
				.contentType(CONTENT_TYPE)
				.accept(CONTENT_TYPE))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Funcionário não encontrado. ID inexistente."))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	private String gerarJson() throws JsonProcessingException {
		LancamentoDto lancamentoDto = new LancamentoDto();
		lancamentoDto.setId(null);
		lancamentoDto.setTipo(TIPO);
		lancamentoDto.setFuncionarioId(ID_FUNCIONARIO);
		lancamentoDto.setData(this.DATE_FORMAT.format(DATA));
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(lancamentoDto);
	}
}
