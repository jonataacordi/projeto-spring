package com.inovasoft.minhasfinancas.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Mapeando a classe como entidade de um banco de dados
@Entity

//Mapeando a tabela o schema da entidade do um banco de dados
@Table ( name = "usuario" , schema = "financas" )
@Builder
@Data
@NoArgsConstructor //Foi necessário inserir manualmente pois no Test do (Juni5) ele não encontrou o construtor default
@AllArgsConstructor //Como foi utilizado o (@NoArgsConstructor) foi necessário criar o (@AllArgsConstructor) para não ficar conflitando com o (@Builder)
public class Usuario {

	@Id //Informa que o id é a chame primaria dessa tabela
	@Column (name = "id")
	@GeneratedValue( strategy = GenerationType.IDENTITY ) //Gera o id de forma automática conforme configurado no BD
	private Long id;
	
	@Column (name = "nome")
	private String nome;
	
	@Column (name = "email")
	private String email;
	
	@Column (name = "senha") 
	private String senha;

	
	
}
