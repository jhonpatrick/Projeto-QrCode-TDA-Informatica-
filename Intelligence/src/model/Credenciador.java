package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="credenciador")
public class Credenciador {

	@DatabaseField(id=true)
	private long _id;
	@DatabaseField
	private String nome;
	@DatabaseField
	private String email;
	@DatabaseField
	private String senha;

	public Credenciador() {
//		deixe um contrutor vazio
	}

	public Credenciador(long _id, String nome, String email, String senha) {
		this._id = _id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public Credenciador(String nome, String email, String senha) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public long get_Id() {
		return _id;
	}

	public void set_Id(long id) {
		this._id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (_id ^ (_id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credenciador other = (Credenciador) obj;
		if (_id != other._id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Credenciador [_id=" + _id + ", nome=" + nome + ", email="
				+ email + ", senha=" + senha + "]";
	}

}
