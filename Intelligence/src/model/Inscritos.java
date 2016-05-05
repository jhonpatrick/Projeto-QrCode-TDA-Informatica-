package model;

public class Inscritos {
	
	private long id_inscritos;
	private long id_atividades;

	public Inscritos() {
		super();
	}

	public Inscritos(long id_inscritos, long id_atividades) {
		super();
		this.id_inscritos = id_inscritos;
		this.id_atividades = id_atividades;
	}

	public Inscritos(long id_inscritos) {
		super();
		this.id_inscritos = id_inscritos;
	}

	public long getId_inscritos() {
		return id_inscritos;
	}

	public void setId_inscritos(long id_inscritos) {
		this.id_inscritos = id_inscritos;
	}

	public long getId_atividades() {
		return id_atividades;
	}

	public void setId_atividades(long id_atividades) {
		this.id_atividades = id_atividades;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id_inscritos ^ (id_inscritos >>> 32));
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
		Inscritos other = (Inscritos) obj;
		if (id_inscritos != other.id_inscritos)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Inscritos [id_inscritos=" + id_inscritos + ", id_atividades="
				+ id_atividades + "]";
	}
}
