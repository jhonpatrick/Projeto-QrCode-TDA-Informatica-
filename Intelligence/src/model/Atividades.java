package model;

public class Atividades {

	private long _id;
	private String nome;
	
	public Atividades() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Atividades(long _id, String nome) {
		super();
		this._id = _id;
		this.nome = nome;
	}

	public Atividades(String nome) {
		super();
		this.nome = nome;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
		Atividades other = (Atividades) obj;
		if (_id != other._id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Atividades [_id=" + _id + ", nome=" + nome + "]";
	}
}