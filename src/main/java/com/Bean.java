//Author: Miguel Pardal Martín

package com;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.google.appengine.api.datastore.Entity;

import entity.Moto;
import persistence.MotoFacade;

@ManagedBean
@SessionScoped
public class Bean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private MotoFacade motoFacade;
	private List<Moto> listadoMotos;
	
	private String estado;
	private String ubicacion;
	private String numPlazas;
	private String destino;

	private Moto motoSeleccionada;
	private String emailReserva;
	private boolean liberada;

	@PostConstruct
	public void init() {
		motoFacade = new MotoFacade();
		listadoMotos = new ArrayList<>();
		emailReserva = "";
		liberada = true;
		
		if(listadoMotos.isEmpty() && motoFacade.findAll().isEmpty()) {
			motoFacade.insert();
		}
		listadoMotos = motoFacade.findAll();
	}
	
	public String verMapa(Moto m) {
		motoSeleccionada = m;
		return "newMapa.xhtml";
	}
	
	public String mapaDireccion(Moto moto) {
		motoSeleccionada = moto;
		return "mapaDireccion.xhtml";
	}
	
	public boolean estadoDisponible(Moto moto) {
		return moto.getEstado().equals("disponible");
	}
	
	public boolean estadoOcupada(Moto moto) {
		return moto.getEstado().equals("ocupada");
	}
	
	public String reservarMoto(Moto moto) {
		motoSeleccionada = moto;
		
		return "reserva.xhtml";
	}
	
	public String reservar() {
		Entity entity = motoSeleccionada.getMotoEntity();
		entity.setProperty(Moto.AUTOR_RESERVA, emailReserva);
		entity.setProperty(Moto.ESTADO, "ocupada");
		entity.setProperty(Moto.PLAZAS, String.valueOf(0));
		motoFacade.update(motoSeleccionada);
		listadoMotos = motoFacade.findAll();
		emailReserva = "";
	
		return "hello.xhtml";
	}
	
	public String reservarPlaza(Moto moto) {
		motoSeleccionada = moto;
		
		return "reservarPlaza.xhtml";
	}
	
	public String reservarPlaza() {
		Entity entity = motoSeleccionada.getMotoEntity();
		List<String> ocupantes = motoSeleccionada.getOcupantes();
		if(ocupantes == null) {
			ocupantes = new ArrayList<>();
		}
		if(!ocupantes.contains(emailReserva)) {
			String res = (String) entity.getProperty(Moto.PLAZAS);
			int numPlazas = Integer.valueOf(res);
			if(numPlazas > 0) {
				ocupantes.add(emailReserva);
				entity.setProperty(Moto.PLAZAS, String.valueOf(--numPlazas));
				entity.setProperty(Moto.OCUPANTES, ocupantes);
			}
		}
		
		motoFacade.update(motoSeleccionada);
		listadoMotos = motoFacade.findAll();
		emailReserva = "";
	
		return "hello.xhtml";
	}
	
	public String liberarMoto(Moto moto) {
		motoSeleccionada = moto;
		
		return "liberar.xhtml";
	}
	
	public String liberar() {
		Entity entity = motoSeleccionada.getMotoEntity();
		if(entity.getProperty(Moto.AUTOR_RESERVA).equals(emailReserva)) {
			entity.setProperty(Moto.AUTOR_RESERVA, "");
			entity.setProperty(Moto.ESTADO, "disponible");
			entity.setProperty(Moto.PLAZAS, motoSeleccionada.getPlazasTotales());
			motoFacade.update(motoSeleccionada);
			
			liberada = true;
			emailReserva = "";
			
			listadoMotos = motoFacade.findAll();
		
			return "hello.xhtml";
		}else{
			liberada = false;
			emailReserva = "";
			return "liberar.xhtml";
		}
		
	}
	
	public String liberarUna(){
		Entity entity = motoSeleccionada.getMotoEntity();
		if(motoSeleccionada.getOcupantes().contains(emailReserva)) {
			motoSeleccionada.getOcupantes().remove(emailReserva);
			entity.setProperty(Moto.OCUPANTES, motoSeleccionada.getOcupantes());
			entity.setProperty(Moto.ESTADO, "disponible");
			int plazas = Integer.parseInt(motoSeleccionada.getPlazas())+1;
			entity.setProperty(Moto.PLAZAS, String.valueOf(plazas));
			if(Integer.parseInt(motoSeleccionada.getPlazas()) > Integer.parseInt(motoSeleccionada.getPlazasTotales())){
				entity.setProperty(Moto.PLAZAS, motoSeleccionada.getPlazasTotales());
			}
				
			motoFacade.update(motoSeleccionada);
			
			liberada = true;
			emailReserva = "";
			
			listadoMotos = motoFacade.findAll();
		
			return "hello.xhtml";
		}else{
			liberada = false;
			emailReserva = "";
			return "liberaruna.xhtml";
		}
	}
	
	public String eliminarMoto(Moto moto) {
		motoFacade.delete(moto);
		listadoMotos = motoFacade.findAll();
		
		return "hello.xhtml";
	}
	
	public String editarMoto(Moto moto) {
		motoSeleccionada = moto;
		estado = motoSeleccionada.getEstado();
		ubicacion = motoSeleccionada.getLocalizacion();
		 return "editarMoto.xhtml";
	}
	
	public String actualizarMoto() {
		Entity entity = motoSeleccionada.getMotoEntity();
		entity.setProperty(Moto.ESTADO, estado);
		entity.setProperty(Moto.LOCALIZACION, ubicacion);
		
		motoFacade.update(motoSeleccionada);
		listadoMotos = motoFacade.findAll();
		return "hello.xhtml";
	}
	
	public String nuevaMoto() {
		return "nuevaMoto.xhtml";
	}
	
	public String guardarMoto() {
		Entity entity = new Entity(Moto.MOTO_ENTITY);
		entity.setProperty(Moto.ESTADO, estado);
		entity.setProperty(Moto.LOCALIZACION, ubicacion);
		int id = getID();
		entity.setProperty(Moto.ID, String.valueOf(id));
		entity.setProperty(Moto.PLAZAS, numPlazas);
		entity.setProperty(Moto.DESTINO, destino);
		Moto m = new Moto(entity);
		
		motoFacade.update(m);
		listadoMotos = motoFacade.findAll();
		return "hello.xhtml";
	}
	
	public int getID() {
		int res = 0;
		for(Moto moto : listadoMotos) {
			if(Integer.valueOf(moto.getID()) > res) {
				res = Integer.valueOf(moto.getID());
			}
		}
		return res+1;
	}
	
	public int plazasDisponibles (Moto moto) {
		return Integer.parseInt(moto.getPlazas());
	}
	
	//Getters y Setter
	public List<Moto> getListadoMotos() {
		return listadoMotos;
	}

	public void setListadoMotos(List<Moto> listadoMotos) {
		this.listadoMotos = listadoMotos;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Moto getMotoSeleccionada() {
		return motoSeleccionada;
	}

	public void setMotoSeleccionada(Moto motoSeleccionada) {
		this.motoSeleccionada = motoSeleccionada;
	}

	public String getEmailReserva() {
		return emailReserva;
	}

	public void setEmailReserva(String emailReserva) {
		this.emailReserva = emailReserva;
	}

	public boolean isLiberada() {
		return liberada;
	}

	public void setLiberada(boolean liberada) {
		this.liberada = liberada;
	}
	
	public String getNumPlazas() {
		return numPlazas;
	}

	public void setNumPlazas(String numPlazas) {
		this.numPlazas = numPlazas;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}
	
	public String devolverUnaPlaza(Moto moto){
		motoSeleccionada = moto;
		return "liberaruna.xhtml";
	}
	
	public boolean menorPlazasTotales(Moto moto){
		boolean menor = Integer.parseInt(moto.getPlazas()) < Integer.parseInt(moto.getPlazasTotales());
		return menor;
	}
}
