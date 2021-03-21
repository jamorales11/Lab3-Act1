package cliente;

import java.net.Socket;

public class Cliente {

	public static void main(String[] args) throws Exception{
		Socket socket =  new Socket("localhost", 9000);
		TransferenciaCliente transferencia = new TransferenciaCliente(socket);
		transferencia.mostrarMenu();
		
	}

}
