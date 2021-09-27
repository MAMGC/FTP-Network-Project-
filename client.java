import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class client {

	public static void main(String[] args)throws Exception {
		try {

			//InetAddress specification
			InetAddress ip = InetAddress.getByName("localhost");
			Socket clientsocket = new Socket(ip,9999);
			DataInputStream input=new DataInputStream(clientsocket.getInputStream());
			DataOutputStream output=new DataOutputStream(clientsocket.getOutputStream());
			Scanner scanner=new Scanner(System.in);
			
			
			String connect =input.readUTF();
			System.out.println("server :"+connect);
			String request=input.readUTF();
			System.out.println("server :"+request);

			//to get name from the user
			String nam = scanner.nextLine();
			output.writeUTF(nam);
			String namevalidation=input.readUTF();
			System.out.println("Server: "+namevalidation);

			//to get password from user
			request=input.readUTF();
			System.out.println("Server: "+request);
			String password= scanner.nextLine();
			output.writeUTF(password);
			String passwordvalidation=input.readUTF();
			System.out.println("Server: "+passwordvalidation);
			while(true) {
				request=input.readUTF();
				System.out.println("server :"+request);
				String command = scanner.nextLine();
				output.writeUTF(command);
				Socket clientSocket2 = new Socket ("localhost",8888);
				if(command.equalsIgnoreCase("show my directories"))
				{
					for(int counter = 0 ; counter<3 ; counter++ )
					{
						String directory= input.readUTF();
						System.out.println(directory);
					}
					String askforcommand=input.readUTF();
					System.out.println("server :"+askforcommand);
					String usercommand= scanner.nextLine();
					output.writeUTF(usercommand);
					for(int counter = 0 ; counter<3 ; counter++ )
					{
						String contain= input.readUTF();
						System.out.println(contain);
					}
					askforcommand=input.readUTF();
					String extention=input.readUTF();
					System.out.println("server :"+askforcommand);
					usercommand= scanner.nextLine();
					output.writeUTF(usercommand);
					byte[] contents = new byte[10000];
					//Initialize the FileOutputStream to the output file's full path.
					FileOutputStream fos = new FileOutputStream("src/clientdown/"+usercommand+"copy"+extention);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					InputStream is = clientSocket2.getInputStream();
					//No of bytes read in one read() call
					int bytesRead = 0;
					while((bytesRead=is.read(contents))!=-1)
							bos.write(contents, 0, bytesRead);
					System.out.println("File saved successfully!");
					bos.flush();
					bos.close();
					clientSocket2.close();
				}
				else if (command.equalsIgnoreCase("close"))
				{
					System.out.println("client is terminated");
					clientSocket2.close();
				}
			}
			
			
			}
		catch(IOException  e)
		{
			System.out.println("there is a problem with serversocket");
		}

	}

}
