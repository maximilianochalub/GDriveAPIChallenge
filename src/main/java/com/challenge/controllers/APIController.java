package com.challenge.controllers;
import com.challenge.connection.DriveConnection;
import com.challenge.domain.DriveFile;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@RestController
public class APIController {
	
	// buscamos los IDs de n usuarios pasando la cantidad n por parametro
	// solo sirve de prueba para colocar parametros validos en el endpoint /search-in-doc
	@RequestMapping(value = "/show-ids", 
					method = RequestMethod.GET)
	public ResponseEntity<List<File>> showIds(@RequestParam(value = "total", required = true) int total) throws IOException {
		
		    FileList result = DriveConnection.driveService.files().list() // listamos archivos
	                .setPageSize(total) // 
	                .setFields("nextPageToken, files(id, name)")
	                .execute();
	       
		    List<File> files = result.getFiles(); // almacenamos en una lista
	        
	        return new ResponseEntity<List<File>>(files, HttpStatus.OK); // devolvemos
	}

	// creamos archivo en el drive
	@RequestMapping(value = "/file", 
					method = RequestMethod.POST)
	public ResponseEntity<DriveFile> createFile(@RequestBody DriveFile driveFile) throws IOException, IllegalArgumentException, NullPointerException {
		
		File metadata = new File();
		metadata.setName(driveFile.getTitle());
		metadata.setDescription(driveFile.getDescription());

		File file = DriveConnection.driveService.files().create(metadata) // creamos 
		    .setFields("id, name, description")
		    .execute();
	
		driveFile.setId(file.getId()); // seteamos su ID
	
		return new ResponseEntity<DriveFile>(driveFile, HttpStatus.OK); // lo mostramos en la consola
		
	}
	
	// buscamos archivo en el drive y si se encuentra buscamos palabra en su contenido
	@RequestMapping(value = "/search-in-doc/{id}", 
					method = RequestMethod.GET)
	public ResponseEntity<?> searchInDoc(@PathVariable("id") String id, 
											  @RequestParam(value = "word", required = true) String word) throws IOException {

		boolean foundFile = false; // denota si el archivo fue encontrado
		
		String pageToken = null;
		do { // proceso de busqueda
			FileList result = DriveConnection.driveService.files().list() 
				  .setSpaces("drive")
			      .setFields("nextPageToken, files(id)") 
			      .setPageToken(pageToken)
			      .execute();
		
				  for (File file : result.getFiles()) {  
					  if (file.getId().equals(id)) {
							foundFile = true;
					  }
				  }
				  
			  pageToken = result.getNextPageToken();
		} while (pageToken != null);
		
		if (foundFile) {
			
			File content = DriveConnection.driveService.files().get(id).execute();	
			content.toString().contains(word);	
			return new ResponseEntity<>("Found", HttpStatus.OK); // mostramos rpta en consola
				
		} else {	
			
			return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND); // mostramos rpta en consola
			
		}
	
	}
	
}