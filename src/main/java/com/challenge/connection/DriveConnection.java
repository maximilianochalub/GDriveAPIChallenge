package com.challenge.connection;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class DriveConnection {

	public static final String credentialFolders = "credentials", clientSecretDir = "client_secret.json";
	public static List<String> scopes; // scopes a definir
    public static Drive driveService; // servicio drive a acceder 
   
    // realiza la conexion segun un scope determinado 0-buscar palabra | 1-crear archivo
    public static boolean connect(int s) { 
    	
        NetHttpTransport httpTransport;
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			driveService = new Drive.Builder(httpTransport, JacksonFactory.getDefaultInstance(), getCredentials(httpTransport, s))
	                .setApplicationName("mlchallenge")
	                .build();
		
			// Devolvemos true si se efectua la conexion
			return true;
		} catch (GeneralSecurityException | IOException e) {
			// Devolvemos false si hay excepcion y no se puede conectar
			return false;
		}
		
    }

    // obtiene credenciales del usuario
    private static Credential getCredentials(final NetHttpTransport httpTransport, int s) throws IOException {

    	// configuramos scope
    	if (s == 0) { scopes = Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY); } 
    	else { scopes = Collections.singletonList(DriveScopes.DRIVE); }
    	
    	// cargamos datos del cliente y gestionamos carpeta de credenciales
        InputStream in = DriveConnection.class.getResourceAsStream(clientSecretDir);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
        File storedCredentials = new File(credentialFolders + "\\StoredCredential");
        storedCredentials.delete();
        
        // Desencadenamos solicitud de autorizacion
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        		httpTransport, JacksonFactory.getDefaultInstance(), clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(credentialFolders)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

}
