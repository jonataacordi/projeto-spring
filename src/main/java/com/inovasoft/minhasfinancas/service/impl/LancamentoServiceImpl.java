package com.inovasoft.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inovasoft.minhasfinancas.exception.RegraNegocioException;
import com.inovasoft.minhasfinancas.model.entity.Lancamento;
import com.inovasoft.minhasfinancas.model.enums.StatusLancamento;
import com.inovasoft.minhasfinancas.model.repository.LancamentoRepository;
import com.inovasoft.minhasfinancas.service.LancamentoService;


@Service
public class LancamentoServiceImpl implements LancamentoService{
	
	private LancamentoRepository repository; //Essa será a dependência que será injetada nos métodos
	
	//As dependências serão injetadas por meio desse construtor
	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento salvarLancamento) {
		validarLancamento(salvarLancamento);
		salvarLancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(salvarLancamento); //Esse método tanto salva quanto atualiza;
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento atualizarLancamento) {
		Objects.requireNonNull(atualizarLancamento.getId()); //Garante que ao atualizar o lançamento seja passado com (id), caso contrário levantará um (NullPointerException)
		validarLancamento(atualizarLancamento);
		return repository.save(atualizarLancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento deletarLancamento) {
		Objects.requireNonNull(deletarLancamento.getId());
		repository.delete(deletarLancamento);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscarLancamento(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro, 
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
		
	}

	@Override
	public void validarLancamento(Lancamento lancamento) {
		//Lançando exceção caso seja passada uma descrição nula ou vazia
		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma Descrição válida.");
		}
		
		//Lançando exceção caso o valor seja nulo, ou se o mês for menor que (1) ou maior que (12)
		if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês válido");
		}
		
		//Lançando exceção caso o valor seja nulo, ou se o ano for diferente de (4)
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}
		
		//Lançando exceção caso o (usuario) ou o valor do (id) que está vindo do (usuario) seja nulo. Pois precisa já vir um usuário 
		//cadastrado, e como já está cadastrado posuuirá um (id).
		if(lancamento.getUsuario() == null || lancamento.getId() == null) {
			throw new RegraNegocioException("Informe um Usuário válido.");
		}
		
		//Lança uma exceção caso os valores do "Bigdecimal" passados sejam menor que (1 centavo)
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um valor válido.");
		}
		
		//Lança uma exceção caso o tipo de lanamento não seja passado e tente salvar.
		if(lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um Tipo de lançamento.");
		}
	}

}
