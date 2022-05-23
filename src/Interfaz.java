
public interface Interfaz {
	public static final int PUERTO = 2000;
	public static final String MI_HOST = "localhost";
	public static final String GRUPO = "225.0.0.1";
	public static final String NICK_USADO = "nick usado";
	public static final String SALA_AGORA = "agora";
	public static final int TAM_MENSAJE = 1000;
	public static final String DIR_SALA_AGORA = MI_HOST + ";" + GRUPO + ";" + PUERTO;
	public static final String CADENA_AGORA = MI_HOST + ";" + GRUPO + ";" + PUERTO;
	public static final String ADIOS = "adios";
	
	public void inicializar() throws Exception;
	public void ejecucionEnBucle() throws Exception;
	public void finalizar() throws Exception;
}
