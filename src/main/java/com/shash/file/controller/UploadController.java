package com.shash.file.controller;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.shash.file.model.ImageModel;
import com.shash.file.service.StorageService;

@Controller
public class UploadController {

	@Autowired
	StorageService storageService;

	List<ImageModel> files;

	@CrossOrigin(value="*")
	@PostMapping("/uploadFile")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
		storageService.deleteAll();
		storageService.init();
		String message = "";
		try {
			storageService.store(file);
			storageService.deleteAll();
			message = "You successfully uploaded " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
		
	}

	@CrossOrigin("*")
	@GetMapping("/getallfiles")
	public ResponseEntity<List<String>> getListFiles(Model model) {
		storageService.deleteAll();
		storageService.init();
		Iterable<ImageModel> images = storageService.getAll();
		files = new ArrayList<ImageModel>();
		for(ImageModel image: images){
			files.add(image);
		}
		List<String> fileNames = files
				.stream().map(file -> MvcUriComponentsBuilder
						.fromMethodName(UploadController.class, "getFile", file.getName()).build().toString())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok().body(fileNames);
	}

	@CrossOrigin("*")
	@GetMapping("/files/{fileName:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
		ImageModel image = storageService.getImage(fileName);
		Resource file = storageService.loadFile(image);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
}
