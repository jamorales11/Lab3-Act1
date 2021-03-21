package servidor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Servidor 
{
	static int clientesPermitidos;
	static int clientesConectados;
	static int clientesListos;
	static String pathArchivo ="";
	static ArrayList<Transferencia> clientes = new ArrayList<>();
	
	public static void main(String[] args) throws NumberFormatException, IOException 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Cuantos clientes quiere atender simultaneamente?");
		clientesPermitidos = Integer.parseInt(br.readLine());
		
		System.out.println("Cual archivo desea transferir? (1 o 2)");
		System.out.println("1. Archivo 100MB");
		System.out.println("2. Archivo 200MB");
		
		int opcionElegida = Integer.parseInt(br.readLine());
		if(opcionElegida == 1)
		{
			pathArchivo = "";
		}
		else
		{
			pathArchivo = "";
		}
		
		ServerSocket socket = new ServerSocket(9000);
		System.out.println("El servidor FTP ha sido inicializado en el puerto 9000");
		while(clientesConectados < clientesPermitidos)
		{
			System.out.println("Esperando a " + (clientesPermitidos - clientesConectados) + " clientes por conectarse");
			Transferencia transferencia = new Transferencia(socket.accept());
			clientes.add(transferencia);
		}
		
	}

}
