package model;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="inscritos")
public class Inscritos {
	
	@DatabaseField(id=true)
	private long id_inscritos;
	@ForeignCollectionField(foreignFieldName="listAtividade")
	private List<Atividades> listAtividade;

	public Inscritos() {
//		deixe um contrutor vazio
	}

	public Inscritos(long id_inscritos, List<Atividades> listAtividade) {
		this.id_inscritos = id_inscritos;
		this.listAtividade = listAtividade;
	}

	public long getId_inscritos() {
		return id_inscritos;
	}

	public void setId_inscritos(long id_inscritos) {
		this.id_inscritos = id_inscritos;
	}

	
	public List<Atividades> getListAtividade() {
		return listAtividade;
	}

	public void setListAtividade(List<Atividades> listAtividade) {
		this.listAtividade = listAtividade;
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
		return "Inscritos [id_inscritos=" + id_inscritos + ", listAtividade="
				+ listAtividade.toString() + "]";
	}
}
