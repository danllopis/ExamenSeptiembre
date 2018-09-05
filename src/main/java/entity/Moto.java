//Author: Miguel Pardal Martín

package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.*;

public class Moto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final String MOTO_ENTITY = "Moto";
	public static final String ID = "id";
	public static final String ESTADO = "estado";
	public static final String LOCALIZACION = "localizacion";
	public static final String DESTINO = "destino";
	public static final String AUTOR_RESERVA = "autor_reserva";
	public static final String PLAZAS = "plazas";
	public static final String PLAZASTOTALES = "plazas_totales";
	public static final String OCUPANTES = "ocupantes";
	
	
	//Key motoKey = KeyFactory.createKey(MOTO_ENTITY, 9);
	private Entity entity = new Entity (MOTO_ENTITY);
	
	private static int i = 0;
	private Key idMoto;
	
	
	//Constructor
	public Moto (String id, String estado, String localizacion, String destino) {
		entity.setProperty(ID, id);
		entity.setProperty(ESTADO, estado);
		entity.setProperty(LOCALIZACION, localizacion);
		entity.setProperty(DESTINO, destino);
		entity.setProperty(PLAZAS, String.valueOf(5));
		entity.setProperty(PLAZASTOTALES, String.valueOf(5));
	}
	
	public Moto (Entity entity) {
		if(entity == null) {
			entity = new Entity (MOTO_ENTITY, getNewID());
			entity.setProperty(ID, String.valueOf(i));
		}
		this.entity = entity;
	}
	
	private Key getNewID() {
		++i;
		idMoto = KeyFactory.createKey(MOTO_ENTITY, String.valueOf(i));
		return idMoto;
	}

	//Getteres y Setter
	
	public Entity getMotoEntity() {
		return entity;
	}

	public String getEstado() {
		return (String) entity.getProperty(ESTADO);
	}

	public String getLocalizacion() {
		return (String) entity.getProperty(LOCALIZACION);
	}
	
	public String getID() {
		return (String) entity.getProperty(ID);
	}
	
	public Key getKey() {
		return entity.getKey();
	}
	
	public String getDestino() {
		return (String) entity.getProperty(DESTINO);
	}
	
	public String getAutorReserva() {
		return (String) entity.getProperty(AUTOR_RESERVA);
	}
	
	public String getPlazas() {
		return (String) entity.getProperty(PLAZAS);
	}
	
	public String getPlazasTotales(){
		return (String)entity.getProperty(PLAZASTOTALES);
	}
	
	public List<String> getOcupantes(){
		return (List<String>) entity.getProperty(OCUPANTES);
	}
}
