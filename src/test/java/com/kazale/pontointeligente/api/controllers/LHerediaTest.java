package com.kazale.pontointeligente.api.controllers;

import org.aspectj.lang.annotation.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.kazale.pontointeligente.api.services.EmpresaService;


@RunWith(SpringRunner.class) //notação para explicitar para o JUNIT qual é o tipo de execução(no caso é de teste).
@SpringBootTest				 
@ActiveProfiles("test")		 
public class LHerediaTest {
	
	@Mock
	private EmpresaService empresaService;
	
	@InjectMocks
	private EmpresaController empresaController;
	
	@org.junit.Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	// Variáveis
	private static final  String CNPJ_EMPTY = "";
	private static final String  CNPJ_INVALID = "084cvl cvlk 3lk4985789437dçlbmfdkjn'13214";
	
	// Testa se o CNPJ tem os 19 digitos necessários.
	@Test
	public void testCNPJbySize() {		
		// O cnpj tem 14 digitos + 5 digitos.
		// String tem que ter 19 digitos. Criar a possibilidade de criar um cnpj inválido em tamanho/tipo de caracter e inserir no banco.
		// Verificar se o sistema está preparado para validar o tamanho do CPNJ.
		
		try {
			empresaController.buscarPorCnpj(CNPJ_EMPTY);
			Assert.fail("O sistema tem retorno de Empresa com CNPJ inválido.");			
			}
		catch (Exception e) {
			Assert.assertTrue(e.getMessage(),true);			
			e.printStackTrace();
			
			}
				
		
	}
	
}
