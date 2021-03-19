package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Transferencia extends Thread
{
	Socket socketCliente;
	DataInputStream din;
	DataOutputStream dout;
	
	Transferencia(Socket socket)
	{
		try 
		{
			socketCliente = socket;
			din = new DataInputStream(socketCliente.getInputStream());
			dout = new DataOutputStream(socketCliente.getOutputStream());
			System.out.println("FTP Cliente conectado");
			Servidor.clientesConectados++;
			start();
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void enviarArchivo(DataOutputStream dos) throws IOException
	{
		File archivo = new File(Servidor.pathArchivo);
		if(!archivo.exists())
		{
			dos.writeUTF("Archivo no encontrado");
			return;
		}
		else
		{
			dos.writeUTF("LISTO");
			FileInputStream fis = new FileInputStream(archivo);
			int bytes;
			do
			{
				bytes = fis.read();
				dos.writeUTF(String.valueOf(bytes));
			}
			while(bytes != -1);
			
			fis.close();
			dos.writeUTF("Archivo enviado exitosamente");
		}
	}
	
	public void run()
	{
		while(true)
		{
			if(Servidor.clientesListos == Servidor.clientesConectados && Servidor.clientesConectados == Servidor.clientesPermitidos)
			{
				System.out.println("Enviando archivo");
				try 
				{
					for(Transferencia cliente: Servidor.clientes)
					{
						enviarArchivo(cliente.dout);
						System.out.println("Archivo enviado!");
					}
					System.out.println("El archivo ha sido enviado a todos los clientes!");
					System.exit(1);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
			}
			else
			{
				System.out.println("Esperando a" + (Servidor.clientesConectados - Servidor.clientesListos) + "clientes por estar listos");
				try 
				{
					System.out.println("Esperando por confirmacion");
					String statusConfirmacion = din.readUTF();
					if(statusConfirmacion.compareTo("LISTO") == 0)
					{
						System.out.println("Confirmacion 'LISTO' del cliente recibida");
						Servidor.clientesListos++;
						dout.writeUTF(Servidor.pathArchivo.split("/")[3]);
						continue;
					}
					else if(statusConfirmacion.compareTo("DESCONECTAR") == 0)
					{
						System.out.println("Mensaje de desconeccion del cliente 'DESCONECTAR' recibido");
						System.exit(1);
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
