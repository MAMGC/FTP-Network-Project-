import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedInputStream;

//import org.graalvm.compiler.core.GraalCompiler.Request;

import java.io.*;


public class Server extends Thread {
	// tow tcp connections
	static ServerSocket seversocket;
	static ServerSocket serversocket2;
	public static void main(String[] args) throws Exception{
		 try {
		 	 //Initialize Sockets one to pass control information
			 //other for data connection to send the data files to client.
			 seversocket= new ServerSocket(9999);
			 serversocket2=new ServerSocket(8888);
			 System.out.println("Server is booted up and  is  waiting for clients to connect.");
			 while (true){
					
					Socket clientsocket=seversocket.accept();
					System.out.println("New client is connected");
					Thread client = new ClientConnection(clientsocket,serversocket2);
					client.start();
					
				}
		 }
			 catch (IOException e) {
					System.out.println("there is a problem with serversocket");
					
				}
	}
		static  class ClientConnection extends Thread {
			   
			   Socket clientSocket; 
			   ServerSocket serverSocket2;
			   // Constructor
			   ClientConnection(Socket clientSocket,ServerSocket serverSocket2){
				   this.clientSocket=clientSocket;
				   this.serverSocket2=serverSocket2;
			   }
			   public void run()
			   {
				   try {
					   //input file data
					   String [] arr=new String [10];
					   File n_p=new File ("names&passwords.txt");
					   Scanner fileofinfo=new  Scanner (n_p);
					   int num=0;
                       while (fileofinfo.hasNextLine())
                       {
                    	   String namorpass=fileofinfo.nextLine();
                    	   arr[num]=namorpass;
                    	   num++;
                       }
                       fileofinfo.close();
                      //valid user
                       DataInputStream input=new DataInputStream(clientSocket.getInputStream());
    				   DataOutputStream output=new DataOutputStream(clientSocket.getOutputStream());
       				   output.writeUTF("connected");
       				   output.writeUTF("Enter your Username ");
       				   String name=input.readUTF();
       				   System.out.println("Clinet: "+name);
       				   boolean check=false;
       				   String password="";
       				   String reply="";
       				   //search for name and save password to check later
       				   int index=0;
       				   for(int i=0;i<10;i++) 
       				   {
    					
       					   if(arr[i].equalsIgnoreCase(name))
       					   {
       						   index=i;
       						   password=arr[i+1];
       						   reply="Username ok password required ";
       						   check=true;
       						   break;
    					}
    				 }
       				   if(check) // chech = true
       				   {
       					   	output.writeUTF(reply); 
       					   	output.writeUTF("Enter your Password ");
       					   	String userspass=input.readUTF();
       					   	if (userspass.equals(password))
       					   	{
       					   		output.writeUTF("login successfully");

       					   		while(true)
       					   		{
       					   		output.writeUTF("Enter your command ");
       					   		String command = input.readUTF();
       					   		
       					   		File dir =new File ("directories.txt");
       					   		Scanner dirinfo = new  Scanner (dir);
       					   		String [] arrdir =new String [60];
       					   		int number=0;
       					   		while (dirinfo.hasNextLine())
                        			{
                        			String dirc=dirinfo.nextLine();
                        			arrdir[number]=dirc;
                        			number++;
                        			}
                        		index/=2;
					   			index*=12;
					   			String exe=null;
					   			if (name.equalsIgnoreCase(arr[0])) {
					   			// equalsIgnoreCase compare two strings
       					   		if(command.equalsIgnoreCase("show my directories"))
       							{
       					   			for(int counter = 0 ; counter<3 ; counter++ )
       					   			{
       					   				output.writeUTF(arrdir[index+counter]);
       					   			}
       					   			output.writeUTF("Enter the directory you want to show: ");
       					   			String commandtoshow=input.readUTF();
       					   			if (commandtoshow.equalsIgnoreCase("show movies"))
       					   			{
       					   				exe=".mp4";

       					   				for(int counter = 0 ; counter<3 ; counter++ )
       					   				{
       					   					output.writeUTF(arrdir[index+counter+3]);
       					   				}

       					   			output.writeUTF("Enter the name of the file you want to download: ");/////////////////////////////
       					   			output.writeUTF(exe);
       					   			String nameoffile=input.readUTF();
       								Socket newsocket = serversocket2.accept();
       								//Specify the file
       								File file = new File("src/dirs/ashour/movies/"+nameoffile+exe);
       								FileInputStream fis = new FileInputStream(file);
       								BufferedInputStream bis = new BufferedInputStream(fis);
       								//Get socket's output stream
       								OutputStream os =newsocket.getOutputStream();
       								//Read File Contents into contents array
       								byte[] myfile;
       								long fileLength = file.length();
       								long current = 0;
       								while(current!=fileLength){
       								int size = 10000;
       								if(fileLength - current >= size) // 5000
       								current += size;
       								else{
       								size = (int)(fileLength - current);
       								current = fileLength;
       								}
       								myfile = new byte[size];
       								bis.read(myfile, 0, size);
       								os.write(myfile);
       								}
       								bis.close();
       								os.flush();
       								//newsocket.close();
       								//File transfer done. Close the socket connection!
       								
       								System.out.println("File sent succesfully!");
       								newsocket.close();
       								
       					   			}
       					   			else if (commandtoshow.equalsIgnoreCase("show music"))
       					   			{
       					   				exe=".mp3";
       					   				for(int counter = 0 ; counter<3 ; counter++ )
       					   				{
       					   					output.writeUTF(arrdir[index+counter+6]);
       					   				}
       					   			output.writeUTF("Enter the name of the file you want to download: ");
       					   			output.writeUTF(exe);
       					   			String nameoffile=input.readUTF();
       								Socket newsocket = serversocket2.accept();
       								File file = new File("src/dirs/ashour/music/"+nameoffile+exe);
       								FileInputStream fis = new FileInputStream(file);
       								BufferedInputStream bis = new BufferedInputStream(fis);
       								//Get socket's output stream
       								OutputStream os =newsocket.getOutputStream();
       								//Read File Contents into contents array
       								byte[] myfile;
       								long fileLength = file.length();
       								long current = 0;
       								while(current!=fileLength){
       								int size = 10000;
       								if(fileLength - current >= size)
       								current += size;
       								else{
       								size = (int)(fileLength - current);
       								current = fileLength;
       								}
       								myfile = new byte[size];
       								bis.read(myfile, 0, size);
       								os.write(myfile);
       								}
       								bis.close();
       								os.flush();
       								//File transfer done. Close the socket connection!
       								
       								System.out.println("File sent succesfully!");
       								newsocket.close();
       					   			}
       					   			else if (commandtoshow.equalsIgnoreCase("show docs"))
       					   			{

       					   			    exe=".pdf";
       					   				for(int counter = 0 ; counter<3 ; counter++ )
       					   				{
       					   					output.writeUTF(arrdir[index+counter+9]);
       					   				}
       					   			output.writeUTF("Enter the name of the file you want to download: ");
       					   			output.writeUTF(exe);
       					   			String nameoffile=input.readUTF();
       								Socket newsocket = serversocket2.accept();
       								File file = new File("src/dirs/ashour/docs/"+nameoffile+exe);
       								FileInputStream fis = new FileInputStream(file);
       								BufferedInputStream bis = new BufferedInputStream(fis);
       								//Get socket's output stream
       								OutputStream os =newsocket.getOutputStream();
       								//Read File Contents into contents array
       								byte[] myfile;
       								long fileLength = file.length();
       								long current = 0;
       								while(current!=fileLength){
       								int size = 10000;
       								if(fileLength - current >= size)
       								current += size;
       								else{
       								size = (int)(fileLength - current);
       								current = fileLength;
       								}
       								myfile = new byte[size];
       								bis.read(myfile, 0, size);
       								os.write(myfile);
       								}
       								bis.close();
       								os.flush();
       								//File transfer done. Close the socket connection!
       								
       								System.out.println("File sent succesfully!");
       								newsocket.close();
       					   			
       							}
       							}
       					   		else if(command.equalsIgnoreCase("close"))
       							{
       								System.out.println("client is terminated");
       								serversocket2.close();
       							}
       					   	else
   							{
   								output.writeUTF("Invalid Command");
   							}
					   			
					   			}
					   			else if (name.equalsIgnoreCase(arr[2])) {
	       					   		if(command.equalsIgnoreCase("show my directories"))
	       							{
	       					   			for(int counter = 0 ; counter<3 ; counter++ )
	       					   			{
	       					   				output.writeUTF(arrdir[index+counter]);
	       					   			}
	       					   			output.writeUTF("Enter the directory you want to show: ");
	       					   			String commandtoshow=input.readUTF();
	       					   			if (commandtoshow.equalsIgnoreCase("show music"))
	       					   			{
	       					   				exe=".mp3";

	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+3]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/hazem/music/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       					   			}
	       					   			else if (commandtoshow.equalsIgnoreCase("show picture"))
	       					   			{
	       					   				exe=".jpg";
	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+6]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/hazem/pictures/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       								break;
	       					   			}
	       					   			else if (commandtoshow.equalsIgnoreCase("show docs"))
	       					   			{

	       					   			    exe=".docx";
	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+9]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/hazem/docs/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       					   			}
	       					   			
	       							}
	       					   	else if(command.equalsIgnoreCase("close"))
       							{
       								System.out.println("client is terminated");
       								serversocket2.close();
       							}
					   			else
       							{
       								output.writeUTF("Invalid Command");
       							}
						   			}
					   			
					   			else if (name.equalsIgnoreCase(arr[4])) {
					   				if(command.equalsIgnoreCase("show my directories"))
	       							{
	       					   			for(int counter = 0 ; counter<3 ; counter++ )
	       					   			{
	       					   				output.writeUTF(arrdir[index+counter]);
	       					   			}
	       					   			output.writeUTF("Enter the directory you want to show: ");
	       					   			String commandtoshow=input.readUTF();
	       					   			if (commandtoshow.equalsIgnoreCase("show movies"))
	       					   			{
	       					   				exe=".mp4";

	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+9]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/mohsen/movies/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       								
	       					   			}
	       					   			else if (commandtoshow.equalsIgnoreCase("show music"))
	       					   			{
	       					   				exe=".mp3";
	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+6]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/mohsen/music/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       					   			}
	       					   			else if (commandtoshow.equalsIgnoreCase("show games"))
	       					   			{

	       					   			    exe=".exe";
	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+3]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/mohsen/games/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       					   			
	       							}
	       							}
	       					   		else if(command.equalsIgnoreCase("close"))
	       							{
	       								System.out.println("client is terminated");
	       								serversocket2.close();
	       							}
	       					   	else
	   							{
	   								output.writeUTF("Invalid Command");
	   							}
						   			
						   			}
					   			else if (name.equalsIgnoreCase(arr[6])) {
					   				if(command.equalsIgnoreCase("show my directories"))
	       							{
	       					   			for(int counter = 0 ; counter<3 ; counter++ )
	       					   			{
	       					   				output.writeUTF(arrdir[index+counter]);
	       					   			}
	       					   			output.writeUTF("Enter the directory you want to show: ");
	       					   			String commandtoshow=input.readUTF();
	       					   			if (commandtoshow.equalsIgnoreCase("show docs"))
	       					   			{
	       					   				exe=".docx";

	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+3]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/menna/docs/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       								
	       					   			}
	       					   			else if (commandtoshow.equalsIgnoreCase("show picture"))
	       					   			{
	       					   				exe=".jpg";
	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+9]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/menna/picture/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       					   			}
	       					   			else if (commandtoshow.equalsIgnoreCase("show games"))
	       					   			{

	       					   			    exe=".exe";
	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+6]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/menna/games/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       					   			
	       							}
	       							}
	       					   		else if(command.equalsIgnoreCase("close"))
	       							{
	       								System.out.println("client is terminated");
	       								serversocket2.close();
	       							}
	       					   	else
	   							{
	   								output.writeUTF("Invalid Command");
	   							}
						   			
						   			}
					   			else if (name.equalsIgnoreCase(arr[8])) {
					   				if(command.equalsIgnoreCase("show my directories"))
	       							{
	       					   			for(int counter = 0 ; counter<3 ; counter++ )
	       					   			{
	       					   				output.writeUTF(arrdir[index+counter]);
	       					   			}
	       					   			output.writeUTF("Enter the directory you want to show: ");
	       					   			String commandtoshow=input.readUTF();
	       					   			if (commandtoshow.equalsIgnoreCase("show docs"))
	       					   			{
	       					   				exe=".docx";

	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+9]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/khater/docs/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       								
	       					   			}
	       					   			else if (commandtoshow.equalsIgnoreCase("show picture"))
	       					   			{
	       					   				exe=".jpg";
	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+3]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/khater/picture/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       					   			}
	       					   			else if (commandtoshow.equalsIgnoreCase("show music"))
	       					   			{

	       					   			    exe=".mp3";
	       					   				for(int counter = 0 ; counter<3 ; counter++ )
	       					   				{
	       					   					output.writeUTF(arrdir[index+counter+6]);
	       					   				}
	       					   			output.writeUTF("Enter the name of the file you want to download: ");
	       					   			output.writeUTF(exe);
	       					   			String nameoffile=input.readUTF();
	       								Socket newsocket = serversocket2.accept();
	       								File file = new File("src/dirs/khater/music/"+nameoffile+exe);
	       								FileInputStream fis = new FileInputStream(file);
	       								BufferedInputStream bis = new BufferedInputStream(fis);
	       								//Get socket's output stream
	       								OutputStream os =newsocket.getOutputStream();
	       								//Read File Contents into contents array
	       								byte[] myfile;
	       								long fileLength = file.length();
	       								long current = 0;
	       								while(current!=fileLength){
	       								int size = 10000;
	       								if(fileLength - current >= size)
	       								current += size;
	       								else{
	       								size = (int)(fileLength - current);
	       								current = fileLength;
	       								}
	       								myfile = new byte[size];
	       								bis.read(myfile, 0, size);
	       								os.write(myfile);
	       								}
	       								bis.close();
	       								os.flush();
	       								//File transfer done. Close the socket connection!
	       								
	       								System.out.println("File sent succesfully!");
	       								newsocket.close();
	       					   			
	       							}
	       							}
	       					   		else if(command.equalsIgnoreCase("close"))
	       							{
	       								System.out.println("client is terminated");
	       								serversocket2.close();
	       							}
	       					   	else
	   							{
	   								output.writeUTF("Invalid Command");
	   							}
						   			
						   			}
       					   		}
       							
       					   	}
       					   	else {
       					   	output.writeUTF("login failed");
       					     clientSocket.close();
       					    
       					   	}
       				   }
       				   else
       				   {
       					reply="login failed and the connection will terminate ";
       					output.writeUTF(reply);
       	                System.out.println("client is terminated");
       					clientSocket.close();
       				   }
       				  
				   }
				   catch(Exception d)
				   {
					   System.out.println("problem ");
				   }
				   }
			   }
	}