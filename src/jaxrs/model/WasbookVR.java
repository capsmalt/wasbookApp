package jaxrs.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

public class WasbookVR {
	private String inputImgUrl;
	private VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
	private static final String ZIP = ".zip", JPG = ".jpg";

	public WasbookVR(String inputImgUrl, String apiKey) {
		this.inputImgUrl = inputImgUrl;
		this.service.setApiKey(apiKey);
	}

	public VisualClassification classify() {
		System.out.println("Classifying an image...");
		ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
				.images(getInputFileObject(inputImgUrl, JPG)).build();
		VisualClassification result = service.classify(options).execute();
		System.out.println(result);
		return result;
	}

	public VisualClassification classifyCustom(String classifierId) {
		System.out.println("Classifying using the custom classifier...");
		System.out.println("Using : " + classifierId);
		ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
				.images(getInputFileObject(inputImgUrl, JPG)).classifierIds(classifierId).build();
		VisualClassification result = service.classify(options).execute();
		System.out.println(result);
		return result;
	}

	public VisualClassifier classifierLearn(String[] hrefs, String[] classNames, String classifierName) {
		if (hrefs.length != 4 || classNames.length != 3) return null;
		System.out.println("Loadind images...");
		ClassifierOptions createOptions = new ClassifierOptions.Builder().classifierName(classifierName)
				.addClass(classNames[0], getInputFileObject(hrefs[0], ZIP))
				.addClass(classNames[1], getInputFileObject(hrefs[1], ZIP))
				.addClass(classNames[2], getInputFileObject(hrefs[2], ZIP))
				.negativeExamples(getInputFileObject(hrefs[3], ZIP)).build();
		VisualClassifier result = service.createClassifier(createOptions).execute();
		return result;
	}

	public File getInputFileObject(String inputImgUrl, String Extension) {
		File temp = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(inputImgUrl).openConnection();
			conn.connect();
			temp = File.createTempFile("temp", Extension);
			temp.deleteOnExit();
			try (InputStream in = conn.getInputStream(); OutputStream fos = new FileOutputStream(temp)) {
				byte[] buf = new byte[4096];
				int size;
				while ((size = in.read(buf)) != -1) fos.write(buf, 0, size);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}
}
