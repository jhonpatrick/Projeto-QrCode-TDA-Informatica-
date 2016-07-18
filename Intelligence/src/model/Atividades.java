package model;

import java.util.Collection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="atividades")
public class Atividades {

	@DatabaseField(id=true)
	private long id;
	@DatabaseField
	private String nome;
	@DatabaseField(foreign=true, foreignAutoRefresh=true)
	private Eventos eventos;
	@ForeignCollectionField(foreignFieldName="listInscritos")
	private Collection<Inscritos> listInscritos;

	public Atividades() {
//		deixe um contrutor vazio
	}

	public Atividades(long id, String nome, Eventos eventos,
			Collection<Inscritos> listInscritos) {
		super();
		this.id = id;
		this.nome = nome;
		this.eventos = eventos;
		this.listInscritos = listInscritos;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Eventos getEventos() {
		return eventos;
	}

	public void setEventos(Eventos eventos) {
		this.eventos = eventos;
	}

	public Collection<Inscritos> getListInscritos() {
		return listInscritos;
	}

	public void setListInscritos(Collection<Inscritos> listInscritos) {
		this.listInscritos = listInscritos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Atividades [id=" + id + ", nome=" + nome + ", eventos="
				+ eventos.getId() + ", listInscritos=" + listInscritos.toString() + "]";
	}
	
}
