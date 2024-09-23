package com.inovasoft.minhasfinancas.service;

import java.util.List;

import com.inovasoft.minhasfinancas.model.entity.Lancamento;
import com.inovasoft.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {
	
	/*Métodos de CRUD. Receberá um novo lançamento que não está salvo na base dados, onde irá salvar e retornar 
	 * a instância com (id) salva na base dados. Ao salvar o usuário ele virá com o status(PENDENTE)*/
	Lancamento salvar(Lancamento salvarLancamento);
	
	//Precisa que já haja um lançamento com (id) para poder buscar esse lançamento 
	Lancamento atualizar(Lancamento atualizarLancamento);
	
	//Precisa que já haja um lançamento com (id) buscar esse lançamento e deletar o registro
	public void deletar(Lancamento deletarLancamento);
	
	//Esse método de busca retornará uma lista de lançamento
	List<Lancamento> buscarLancamento(Lancamento lancamentoFiltro);
	
	//Esse método servirá para atualizar o status de (PENDENTE) para (CANCELADO,EFETIVADO)
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	//Esse método validará dos dados prenchidos(descricao, mes, ano...) na entidade (Lancamento).
	public void validarLancamento(Lancamento lancamento);
}
