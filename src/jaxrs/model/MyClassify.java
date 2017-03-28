package jaxrs.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.activation.DataHandler;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.websphere.jaxrs20.multipart.IAttachment;
import com.ibm.websphere.jaxrs20.multipart.IMultipartBody;

public class MyClassify {
	private IMultipartBody multipartBody;
	
	public MyClassify(IMultipartBody multipartBody) {
		this.multipartBody = multipartBody;
	}

	public VisualClassification classify(){
		List<IAttachment> attachments = multipartBody.getAllAttachments();
		DataHandler dataHandler = attachments.get(0).getDataHandler();
		try {
			InputStream is = dataHandler.getInputStream();
			File tempFile = new File("./","copied.jpg");
			try {
				Files.copy(is, tempFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			File inputFile = new File(tempFile.getPath());
			VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
			service.setApiKey("14d773bd9686a383b2508bf1d7a991f4b92a497f"); // apiKey
	
			// Classify
			System.out.println("Classify an image");
		    ClassifyImagesOptions options = new ClassifyImagesOptions.Builder().images(inputFile).build();
		    VisualClassification result = service.classify(options).execute();
		    System.out.println(result);
		    return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public VisualClassification classifyCustom(String classifierId){
		List<IAttachment> attachments = multipartBody.getAllAttachments();
		DataHandler dataHandler = attachments.get(0).getDataHandler();
		try {
			InputStream is = dataHandler.getInputStream();
			File tempFile = new File("./","copied.jpg");
			try {
				Files.copy(is, tempFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			File inputFile = new File(tempFile.getPath());
			VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
			service.setApiKey("14d773bd9686a383b2508bf1d7a991f4b92a497f"); // apiKey

			System.out.println("Classify using the 'myfavorite' classifier");
		    ClassifyImagesOptions options = new ClassifyImagesOptions.Builder().images(inputFile).classifierIds(classifierId).build();
		    VisualClassification result = service.classify(options).execute();
		    System.out.println(result);
		    return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}