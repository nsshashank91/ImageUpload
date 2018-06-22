package com.shash.file.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;






import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shash.file.model.ImageModel;
import com.shash.file.repository.ImageRepository;


@Service

public class StorageService {
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	ApplicationContext context;
	
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final Path rootLocation = Paths.get("upload-dir");

	public void store(MultipartFile file) {
		try {
			/*Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
			System.out.println(this.rootLocation.resolve(file.getOriginalFilename())+"");*/
			String name = file.getOriginalFilename();
			byte[] arrayPic = new byte[(int) file.getSize()];
			file.getInputStream().read(arrayPic);
			System.out.println(name);
			System.out.println("--------------------");
			ImageModel image = context.getBean(ImageModel.class);
			image.setName(name);
			image.setPic(arrayPic);
			imageRepository.save(image);
		} catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public Resource loadFile(ImageModel image) {
		try (FileOutputStream fos = new FileOutputStream(this.rootLocation+"/"+image.getName())) {
			   fos.write(image.getPic());
			   fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Path file = rootLocation.resolve(image.getName());
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	public void init() {
		try {
			Files.createDirectory(rootLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}
	
	public Iterable<ImageModel> getAll(){
		return imageRepository.findAll();
	}
	
	public ImageModel getImage(String imageName){
		return imageRepository.findByName(imageName);
	}
}
