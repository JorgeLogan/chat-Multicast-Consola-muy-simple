
public class DatosSala {
	public String host;
	public String grupo;
	public int puerto;
	
	
	public DatosSala(String cadena) {
		String[] partes = cadena.split(";");
		
		this.host = partes[0];
		this.grupo = partes[1];
		this.puerto = Integer.parseInt(partes[2]);
	}
}
