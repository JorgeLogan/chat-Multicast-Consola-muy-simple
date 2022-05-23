import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;

public class Cliente extends Thread implements Interfaz{
	// Atributos
	boolean salir = false;
	String nick ="";
	Socket socket;
	DataInputStream datosEntrada;
	DataOutputStream datosSalida;
	
	DatagramPacket paquete;	
	BufferedReader lector;
	String datosConexion;
	
	DatosSala datosSala;
	
	// Atributo para recibir de multicast
	MulticastSocket mc;
	InetAddress inetAddress;
	InetSocketAddress inetSocket;
	NetworkInterface network;
	
	// Constructor
	public Cliente(){
		try {
			this.inicializar();
			
			if(this.comprobarNick() == true) {
				this.extraerSala();
				this.configurarMulticast();
				this.start();
				
				while(this.salir == false) {
					this.ejecucionEnBucle();
				}
			}
			
			this.finalizar();
		} 
		catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
		}
	}

	
	
	
	
	// Metodo para comprobar el nick en el emisor
	private boolean comprobarNick()  {
		boolean resultado = false;
		
		try {
			System.out.println("Escribe el nick que deseas para enviarlo al servidor:");
			this.nick = this.lector.readLine();
			
			// Envio datos
			this.datosSalida.writeUTF(this.nick);
		
			// Espero respuesta
			this.datosConexion = this.datosEntrada.readUTF();
			
			System.out.println(this.datosConexion);
			
			if(this.datosConexion.equals(NICK_USADO) == false) resultado = true;
					
			
		}catch(Exception e) {
			System.out.println("No se pudo conectar al servidor: " + e.getMessage());
			salir = true;
		}
		
		
		return resultado;
	}


	// Metodo principal
	public static void main(String[] args) {
		new Cliente();
	}


	@Override
	public void inicializar() throws Exception {
		this.socket = new Socket(Interfaz.MI_HOST, Interfaz.PUERTO);
		this.datosEntrada = new DataInputStream(this.socket.getInputStream());
		this.datosSalida = new DataOutputStream(this.socket.getOutputStream());
		this.lector = new BufferedReader(new InputStreamReader(System.in));
	}


	@Override
	public void ejecucionEnBucle() throws Exception {
		System.out.println("Puedes enviar mensajes");
		String mi_noticia = this.lector.readLine();
		byte[] datos = mi_noticia.getBytes();
		DatagramPacket paqueteSalida = new DatagramPacket(datos, datos.length, 
				this.inetAddress, this.datosSala.puerto);
		
		if(mi_noticia.equals(Interfaz.ADIOS)) this.salir = true;
		if(mc!= null &&mc.isClosed() == false)this.mc.send(paqueteSalida);
	}


	private void configurarMulticast() throws Exception {
		this.mc = new MulticastSocket(datosSala.puerto);
		this.inetAddress = InetAddress.getByName(datosSala.grupo);
		this.inetSocket = new InetSocketAddress(this.inetAddress, this.datosSala.puerto);
		this.network = NetworkInterface.getByName(datosSala.host);
		this.mc.joinGroup(inetSocket, network);
	}


	private void extraerSala() {
		this.datosSala= new DatosSala(this.datosConexion);
	}


	private void escucharSala(){
		System.out.println("Escuchando noticias");
		while(salir == false) {
			try {
				byte[] datos = new byte[Interfaz.TAM_MENSAJE];
				this.paquete = new DatagramPacket(datos, datos.length);
				this.mc.receive(paquete);
				String mensaje = new String(datos).trim();
				System.out.println("Noticia: " + mensaje);
				
				if(mensaje.equals(Interfaz.ADIOS)) this.salir = true;	
			}
			catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
				this.salir = true;
			}			
		}
		System.out.println("Saliendo de noticias");
	}


	@Override
	public void finalizar() throws Exception {
		this.socket.close();
		System.out.println("Cerrado cliente " + this.nick);
		
	}
	
	@Override
	public void run() {
		while(salir == false) {
			this.escucharSala();			
		}
	}
}
