package jaxrs.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

public class WasbookVR {
	private String inputImgUrl;
	private VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
	
	public WasbookVR(String inputImgUrl, String apiKey) {
		super();
		this.inputImgUrl = inputImgUrl;
		this.service.setApiKey(apiKey);
	}
	
	public VisualClassification classify() {
		// Classify
		System.out.println("Classifying an image...");
		ClassifyImagesOptions options = new ClassifyImagesOptions.Builder().images(getInputFileObject(inputImgUrl,"classifyTarget",".jpg")).build();
		VisualClassification result = service.classify(options).execute();
		System.out.println(result);
		return result;
	}
	
	public VisualClassification classifyCustom(String classifierId){
			System.out.println("Classifying using the custom classifier...");
			System.out.println("Using : " + classifierId);
		    ClassifyImagesOptions options = new ClassifyImagesOptions.Builder().images(getInputFileObject(inputImgUrl,"classifyTarget",".jpg")).classifierIds(classifierId).build();
		    VisualClassification result = service.classify(options).execute();
		    System.out.println(result);
		    return result;
	}
	
	public VisualClassifier classifierLearn(List<String> hrefs, List<String> classNames, String classifierName){
		System.out.println("Loadind images...");
		List<File> files = new ArrayList<File>();
		for (int i = 0;i < hrefs.size()-1;i++) {
			files.add(getInputFileObject(hrefs.get(i),classNames.get(i),".zip"));
		}
		if (files.size() != 3) {return null;} // 学習時には，Positiveクラスのデータ・セットは3種類を必須とする
		File neg = getInputFileObject(hrefs.get(3),"neg",".zip");

		// Learn (generate classifier)
		System.out.println("Leaning pos/neg images for generating classifier...");
	    ClassifierOptions createOptions = new ClassifierOptions.Builder().classifierName(classifierName)
	        .addClass(classNames.get(0), files.get(0))
	        .addClass(classNames.get(1), files.get(1))
	        .addClass(classNames.get(2), files.get(2))
	        .negativeExamples(neg).build();
	    VisualClassifier result = service.createClassifier(createOptions).execute();
	    return result;
	}
	
	public File getInputFileObject(String inputImgUrl,String filename, String Extension){
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
			
			// createTempFileのテスト
			File tempDir = new File("./");
			File temp = File.createTempFile(filename, Extension, tempDir);
			temp.deleteOnExit();
			OutputStream fos = new FileOutputStream(temp);
			while ((readSize = in.read(buf)) != -1) {
				fos.write(buf, 0, readSize);
			}
			fos.close();
			in.close();
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
