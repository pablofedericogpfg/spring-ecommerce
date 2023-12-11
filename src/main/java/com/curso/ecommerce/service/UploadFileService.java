package com.curso.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.controller.ProductoController;

@Service
public class UploadFileService {
	private final Logger LOGGER = LoggerFactory.getLogger(UploadFileService.class);
	private String folder="images//";
	public String saveImage(MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			byte[] bytes=file.getBytes();
			Path path = Paths.get(folder+file.getOriginalFilename());
			Files.write(path, bytes);
			return file.getOriginalFilename();
			
			
		}
		
		return "default.jpg";
	}
	
	public void deleteImage(String nombre) {
		String ruta="images//";
		LOGGER.info("Nomber del archivo a borrar{}", ruta+nombre);
		File file=new File(ruta+nombre);
		file.delete();
		
		
	}
}
