package com.inovasoft.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.inovasoft.minhasfinancas.model.enums.StatusLancamento;
import com.inovasoft.minhasfinancas.model.enums.TipoLancamento;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table ( name = "lancamento" , schema = "financas")
@Builder
@Data
public class Lancamento {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "id")
	private Long id;
	
	@Column (name = "descricao")
	private String descricao;
	
	@Column (name = "mes")
	private Integer mes;
	
	@Column (name = "ano")
	private Integer ano;
	
	@Column (name = "valor")
	private BigDecimal valor;
	
	
	@Column (name = "tipo")
	@Enumerated (value = EnumType.STRING) //Porque esse (TipoLancamento) é um tipo (enum)
	private TipoLancamento tipo;
	
	@Column (name = "status")
	@Enumerated (value = EnumType.STRING)
	private StatusLancamento status;

	
	//Relacionanmento entre (lancamento) com a entidade (usuario). Um (usuario) poderá possuir vários (lancamentos) porém cada (lancamento) poderá conter apenas um único (usuario) 
	//Geralmentw quando trabalhamos com uma chave estrangeira (id_usuario) de uma outra tabela (usuario) utilizamos o (@ManyToOne e @JoinColumn)
	@ManyToOne //(Many) - indica a entidade atual (lancamento) e o (ToOne) indica a entidade em que está mapeando (Usuario), ou seja, muitos lancamentos para um usuario.
	@JoinColumn (name = "id_usuario") //Pois é uma coluna de relacionamento
	private Usuario usuario;


	@Column (name = "data_cadastro")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate dataCadastro;
	
	
	
}
