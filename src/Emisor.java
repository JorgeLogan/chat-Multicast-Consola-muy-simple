import java.io.IOException;

public class Emisor {
	HiloTCP hiloEscuchaNicks = null;
	SalaChat sala = null;
	
	public Emisor(){
		crearEscuchaTCP();
		crearSalaAgora();
		finalizar();
	}
	
	private void finalizar() {
		System.out.println("Se pide cerrar el emisor");
		if(sala!= null) {
			try {
				sala.finalizar();
				sala =null;
			} catch (Exception e) {	
				System.out.println("Excepcion al cerrar la sala " + e.getMessage());
			}			
		}
		
		if(this.hiloEscuchaNicks!= null) {
			this.hiloEscuchaNicks.salir = true;
			try {
				this.hiloEscuchaNicks.serverSocket.close();
			} catch (Exception e) {
				System.out.println("Error al cerrar el hilo de escucha tcp: " + e.getMessage());
			}
		}
		System.out.println("Cerrando emisor");
	}

	private void crearSalaAgora() {
		sala = new SalaChat(Interfaz.CADENA_AGORA);
		sala.start();
		try {
			sala.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void crearEscuchaTCP() {
		// Creo un hilo para la escucha de nuevos clientes.
		hiloEscuchaNicks = new HiloTCP();
		hiloEscuchaNicks.start();
	}

	public static void main(String[] args) {
		new Emisor();
	}
}
