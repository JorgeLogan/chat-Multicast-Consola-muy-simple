import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class SalaChat extends Thread implements Interfaz {
	// Atributos para enviar mensajes Multicast
	private MulticastSocket mcSocket;
	private InetAddress inetAddress;
	private DatagramPacket datagrama;
	
	// Para enviar noticiasç
	BufferedReader lector;
	
	private boolean salir = false;

	String host;
	String grupo;
	int puerto;
	

	// Constructor
	public SalaChat(String cadenaRecibida) {
		String[] partes = cadenaRecibida.split(";");
		this.host = partes[0];
		this.grupo = partes[1];
		this.puerto = Integer.parseInt(partes[2]);
	}
	

	
	@Override
	public void run() {
		try {
			this.inicializar();
			
			while(this.salir == false) {
				this.ejecucionEnBucle();
			}
			this.finalizar();
		}
		catch(Exception e) {}
		System.out.println("Finalizado metodo run de la sala");
	}
	
	@Override
	public void inicializar() throws Exception {
		this.mcSocket = new MulticastSocket();
		this.inetAddress = InetAddress.getByName(grupo);	
				
		this.lector = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Inicializada sala chat");
	}

	@Override
	public void ejecucionEnBucle() throws Exception {
		this.enviarNoticias();
	}

	private void enviarNoticias() throws Exception {
		System.out.println("Escribe noticia a enviar:");
		String noticia = this.lector.readLine();
		
		byte[] datos = noticia.getBytes();
		this.datagrama = new DatagramPacket(datos, datos.length, this.inetAddress, Interfaz.PUERTO);
		this.mcSocket.send(datagrama);
		
		if(noticia.equals(ADIOS)) this.salir = true;
	}



	@Override
	public void finalizar() throws Exception {
		if(this.mcSocket.isClosed() == false) {
			this.mcSocket.close();
			this.salir = true;
			System.out.println("Cerrada sala chat");
		}
	}

}
