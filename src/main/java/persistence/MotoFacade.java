//Author: Miguel Pardal Martín

package persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.FetchOptions.Builder;

import entity.Moto;

public class MotoFacade implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public void insert() {
		DatastoreService ds = DSF.getDatastoreService();
		
		Moto moto1 = new Moto("1","disponible", "Ayuntamiento de Malaga", "Sevilla");
		Moto moto2 = new Moto("2","disponible", "Ayuntamiento de Malaga", "Jaen");
		Moto moto3 = new Moto("3","disponible", "Ayuntamiento de Malaga", "ETSI Informatica UMA");
		Moto moto4 = new Moto("4","disponible", "Ayuntamiento de Malaga", "Andorra");
		Moto moto5 = new Moto("5","disponible", "Ayuntamiento de Malaga", "Valencia");
		
		ds.put(moto1.getMotoEntity());
		ds.put(moto2.getMotoEntity());
		ds.put(moto3.getMotoEntity());
		ds.put(moto4.getMotoEntity());
		ds.put(moto5.getMotoEntity());
	}
	
	public List<Moto> findAll(){
		List<Moto> motos = new ArrayList<>();
		
		DatastoreService ds = DSF.getDatastoreService();
		
		Query query = configureQuery();
		FetchOptions fetchOptions = Builder.withDefaults();
		
		for(Entity e : ds.prepare(query).asList(fetchOptions)) {
			motos.add(new Moto(e));
		}
		
		return motos;
	}
	
	public void update (Moto moto) {
		DatastoreService ds = DSF.getDatastoreService();
		
		ds.put(moto.getMotoEntity());
	}
	
	public void delete(Moto moto) {
		DatastoreService ds = DSF.getDatastoreService();
		
		ds.delete(moto.getKey());
	}

	private Query configureQuery() {
		Query q = new Query(Moto.MOTO_ENTITY);

		return q;
	}

}
