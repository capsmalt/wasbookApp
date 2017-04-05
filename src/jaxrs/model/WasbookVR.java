package jaxrs.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;
import com.ibm.websphere.jaxrs20.multipart.IAttachment;

public class WasbookVR {
	private String inputImgUrl;

	public WasbookVR(String inputImgUrl) {
		super();
		this.inputImgUrl = inputImgUrl;
	}

	public File getInputFileObject(String inputImgUrl){
		URL target;
		try {
			target = new URL(inputImgUrl);
			HttpURLConnection conn;
			conn = (HttpURLConnection) target.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			InputStream in = conn.getInputStream();
			byte[] buf = new byte[4096];
			int readSize;

			String filename = "./classifyTarget.jpg";

			OutputStream fos = new FileOutputStream(filename);
			while ((readSize = in.read(buf)) != -1) {
				fos.write(buf, 0, readSize);
			}
			fos.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File tmp = new File("./classifyTarget.jpg");
		return tmp;
	}
	
	public File getInputZipFileObject(String inputImgUrl, String className){
		URL target;
		try {
			target = new URL(inputImgUrl);
			HttpURLConnection conn;
			conn = (HttpURLConnection) target.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			InputStream in = conn.getInputStream();
			byte[] buf = new byte[4096];
			int readSize;

			String filename = "./"+className+".zip";

			OutputStream fos = new FileOutputStream(filename);
			while ((readSize = in.read(buf)) != -1) {
				fos.write(buf, 0, readSize);
			}
			// File.createTempFile("aaa", ".jpg");
			fos.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File tmp = new File("./"+className+".zip");
		return tmp;
	}
	
	public VisualClassification classify() {
		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
		service.setApiKey("14d773bd9686a383b2508bf1d7a991f4b92a497f"); // apiKey

		// Classify
		System.out.println("Classify an image");
		ClassifyImagesOptions options = new ClassifyImagesOptions.Builder().images(getInputFileObject(inputImgUrl)).build();
		VisualClassification result = service.classify(options).execute();
		System.out.println(result);
		return result;
	}
	
	public VisualClassification classifyCustom(String classifierId){
			VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
			service.setApiKey("14d773bd9686a383b2508bf1d7a991f4b92a497f"); // apiKey

			
			System.out.println("Classify using the 'myfavorite' classifier.");
			System.out.println("Using : " + classifierId);
		    ClassifyImagesOptions options = new ClassifyImagesOptions.Builder().images(getInputFileObject(inputImgUrl)).classifierIds(classifierId).build();
		    VisualClassification result = service.classify(options).execute();
		    System.out.println(result);
		    return result;
	}
	
	
	public VisualClassifier classifierLearn(List<String> hrefs, List<String> classNames, String classifierName){
		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
		service.setApiKey("14d773bd9686a383b2508bf1d7a991f4b92a497f"); // apiKey
		
		List<File> files = new ArrayList<File>();
		int i = 0;
		System.out.println("files.size() : " + files.size());
		for (;i < hrefs.size()-1;i++) {
			files.add(getInputZipFileObject(hrefs.get(i),classNames.get(i)));
		}
		if (files.size() != 3) {return null;} // 学習時には，Positiveクラスのデータ・セットは3種類を必須とする
		File neg = getInputZipFileObject(hrefs.get(3),"neg");

		// Learn (generate classifier)
		System.out.println("Leaning pos/neg images for generating classifier...");
	    ClassifierOptions createOptions = new ClassifierOptions.Builder().classifierName(classifierName)
	        .addClass(classNames.get(0), files.get(0))
	        .addClass(classNames.get(1), files.get(1))
	        .addClass(classNames.get(2), files.get(2))
	        .negativeExamples(neg).build();
	    VisualClassifier result = service.createClassifier(createOptions).execute();
	    return result;
//		return null;
	}
}
