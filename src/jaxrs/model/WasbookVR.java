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

/*
 * Watson SDKを使用して、IBM Cloud上のVisual Recognitionサービスに画像認識や識別器生成のリクエストを送信する
 */
public class WasbookVR {
	private String inputImgUrl; //テスト用や学習用画像(群)のURL
	private VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20); //APIバージョンを指定
	private static final String ZIP = ".zip", JPG = ".jpg"; //画像認識に使用するファイルオブジェクトに付与する拡張子

	/*
	 * 画像URLやapiKeyをセットするコンストラクタ
	 */
	public WasbookVR(String inputImgUrl, String apiKey) {
		this.inputImgUrl = inputImgUrl;
		this.service.setApiKey(apiKey);
	}

	/*
	 * デフォルトの識別器を使用して画像認識する
	 */
	public VisualClassification classify() {
		System.out.println("Classifying an image...");
		ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
				.images(getInputFileObject(inputImgUrl, JPG)).build(); //認識対象の画像をセット
		VisualClassification result = service.classify(options).execute(); //画像認識を実行
		System.out.println(result);
		return result;
	}

	/*
	 * 学習用画像を使って独自の識別器を生成する
	 */
	public VisualClassifier classifierLearn(String[] hrefs, String[] classNames, String classifierName) {
		if (hrefs.length != 4 || classNames.length != 3) return null;
		System.out.println("Loading images...");
		ClassifierOptions createOptions = new ClassifierOptions.Builder().classifierName(classifierName) //生成する識別器の名前をセット
				.addClass(classNames[0], getInputFileObject(hrefs[0], ZIP)) //正解画像群1をセット
				.addClass(classNames[1], getInputFileObject(hrefs[1], ZIP)) //正解画像群2をセット
				.addClass(classNames[2], getInputFileObject(hrefs[2], ZIP)) //正解画像群3をセット
				.negativeExamples(getInputFileObject(hrefs[3], ZIP)).build(); //非正解画像群をセット
		VisualClassifier result = service.createClassifier(createOptions).execute(); //識別器の生成を実行
		return result;
	}

	/*
	 * 独自に生成した識別器を使用して画像認識する
	 */
	public VisualClassification classifyCustom(String classifierId) {
		System.out.println("Classifying using the custom classifier...");
		System.out.println("Using : " + classifierId);
		ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
				.images(getInputFileObject(inputImgUrl, JPG)).classifierIds(classifierId).build(); //認識対象の画像と使用する識別器IDをセット
		VisualClassification result = service.classify(options).execute(); //画像認識を実行
		System.out.println(result);
		return result;
	}
	
	/*
	 * 画像URLからファイルオブジェクトを生成する
	 */
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
