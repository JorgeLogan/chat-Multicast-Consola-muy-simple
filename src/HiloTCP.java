import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class HiloTCP extends Thread implements Interfaz{
	ServerSocket serverSocket;
	Socket socket;
	boolean salir = false;
	DataInputStream datosEntrada;
	DataOutputStream datosSalida;
	
	List<String> nicksConectados = new LinkedList<>();
	
	
	// Constructor
	public HiloTCP(){}
	

	@Override
	public void run() {
		try {
			this.inicializar();
			
			while(this.salir == false) {
				try {
					this.ejecucionEnBucle();					
				}catch(Exception ex) {
					System.out.println("Error al recibir mensaje TCP: " + ex.getMessage());
				}

			}
			
			this.finalizar();	
		}
		catch(Exception e) {
			
		}
		System.out.println("Finalizada escucha de nicks");
	}


	@Override
	public void inicializar()  throws Exception{
		this.serverSocket = new ServerSocket(PUERTO);
		System.out.println("iniciada escucha de nicks");
	}


	@Override
	public void ejecucionEnBucle()  throws Exception{
		this.socket = this.serverSocket.accept();
		this.datosEntrada = new DataInputStream(this.socket.getInputStream());
		this.datosSalida = new DataOutputStream(this.socket.getOutputStream());
		
		String nick = this.datosEntrada.readUTF().trim();
		
		if(this.nicksConectados.contains(nick) == false) {
			this.datosSalida.writeUTF(DIR_SALA_AGORA);
			this.nicksConectados.add(nick);
		}
		else {
			this.datosSalida.writeUTF(NICK_USADO);
		}
	}


	@Override
	public void finalizar()  throws Exception{
		this.serverSocket.close();
	}
}
