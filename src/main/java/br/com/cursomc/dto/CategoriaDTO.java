package br.com.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import br.com.cursomc.domain.Categoria;

public class CategoriaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	@NotEmpty(message = "PREENCHIMENTO OBRIGATÓRIO")
	@Length(min=5, max=80, message = "O TAMANHO DEVE SER ENTRE 5 E 80 CARACTERES")
	private String nome;
	
	public CategoriaDTO() {
	}
	
	public CategoriaDTO(Categoria obj) {
		id = obj.getId();
		nome = obj.getNome();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
