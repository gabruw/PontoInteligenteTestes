package com.kazale.pontointeligente.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.kazale.pontointeligente.api.dtos.EmpresaDto;
import com.kazale.pontointeligente.api.entities.Empresa;
import com.kazale.pontointeligente.api.response.Response;
import com.kazale.pontointeligente.api.services.EmpresaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GabrielSMTest {

	@Mock
	EmpresaService service;

	@InjectMocks
	EmpresaController controller;

	Empresa empresa;

	@Before
	public void setup() {
		empresa = new Empresa();
		empresa.setId(1l);
		empresa.setCnpj("82198127000121");
		empresa.setRazaoSocial("Teste razão social");
		when(service.buscarPorCnpj("82198127000121")).thenReturn(Optional.of(empresa));
		when(service.buscarPorCnpj("123456")).thenReturn(Optional.empty());	
	}

	@Test
	public void buscarPorCnpjTest() {		
		assertThat(controller.buscarPorCnpj("82198127000121").getBody()).isNotNull();
		assertEquals(HttpStatus.OK, controller.buscarPorCnpj("82198127000121").getStatusCode());
	}
	
	@Test
	public void buscarPorCnpjFalhoTest() {
		ResponseEntity<Response<EmpresaDto>> res = controller.buscarPorCnpj("123456");
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
}
