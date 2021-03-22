package cliente;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TransferenciaCliente {
	public final static String PATH = "./files/client/";
	private String archivoSeleccionado;
	private int tamano;
	
	Socket socket = null;
	
	DataInputStream din;
	DataOutputStream dout;
	BufferedReader br;
	TransferenciaCliente(Socket soc) {
		try {
			socket=soc;
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(System.in));
			archivoSeleccionado = "";
		}
		catch(Exception ex) {
		}
	}
	
	
	void ReceiveFile() throws Exception {
		String msgFromServer=din.readUTF();

		if(msgFromServer.compareTo("Archivo no encontrado")==0) {
			System.out.println("No se encuentra el archivo en el servidor ...");
			return;
		}
		else if(msgFromServer.compareTo("LISTO")==0) {
			System.out.println("Recibiendo Archivo...");
			File archivo=new File(PATH + archivoSeleccionado);
			if(archivo.exists()) {
				String Option;
				System.out.println("El archivo ya existe. Sobreescribir (Y/N) ?");
				Option=br.readLine();            
				if(Option=="N") {
					dout.flush();
					return;    
				}                
			}
			FileOutputStream fout=new FileOutputStream(archivo);
			int ch;
			String temp;
			do {
				temp=din.readUTF();
				ch=Integer.parseInt(temp);
				if(ch!=-1) {	
					fout.write(ch);                    
				}
			} while(ch!=-1);
			fout.close();
			System.out.println(din.readUTF());
		}
	}
	
	public void mostrarMenu() throws Exception {
		while(true) {    
			System.out.println("Está listo para recibir archivos [Y/N]");
			String eleccion;
			eleccion= br.readLine();

			if(eleccion.equals("Y")) {
				dout.writeUTF("LISTO");
				archivoSeleccionado = din.readUTF();
				System.out.println("Archivo para recibir: " + archivoSeleccionado + " (" + tamano + " B)");
				
				try {
					ReceiveFile();
					System.exit(1);
					
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	
}
