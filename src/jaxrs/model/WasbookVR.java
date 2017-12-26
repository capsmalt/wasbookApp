package jaxrs.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Classifier;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Classifiers;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.CreateClassifierOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DeleteClassifierOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ListClassifiersOptions;

/*
 * Watson SDKを使用して、IBM Cloud上のVisual Recognitionサービスに画像認識や識別器生成のリクエストを送信する
 */
public class WasbookVR {
	private String inputImgUrl; // テスト用や学習用画像(群)のURL
	private VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20); // APIバージョンを指定
	private static final String ZIP = ".zip", JPG = ".jpg"; // 画像認識に使用するファイルオブジェクトに付与する拡張子

	/*
	 * 画像URLやapiKeyをセットするコンストラクタ
	 */
	public WasbookVR(String inputImgUrl, String apiKey) {
		this.inputImgUrl = inputImgUrl;
		this.service.setApiKey(apiKey);
	}

	/*
	 * デフォルトの識別器を使用して画像認識する 呼出元: GET /
	 */
	public ClassifiedImages classify() throws FileNotFoundException {
		System.out.println("Classifying an image...");
		ClassifyOptions options = new ClassifyOptions.Builder().imagesFile(getInputFileObject(inputImgUrl, JPG))
				.build(); // 認識対象の画像をセット
		ClassifiedImages result = service.classify(options).execute(); // 画像認識を実行
		System.out.println(result);
		return result;
	}

	/*
	 * 学習用画像を使ってカスタム識別器を生成する 呼出元: GET /
	 */
	public Classifier classifierLearn(String[] hrefs, String[] classNames, String classifierName)
			throws FileNotFoundException {
		if (hrefs.length != 4 || classNames.length != 3)
			return null;
		System.out.println("Loading images...");
		CreateClassifierOptions createOptions = new CreateClassifierOptions.Builder().name(classifierName) // 生成する識別器の名前をセット
				.addClass(classNames[0], getInputFileObject(hrefs[0], ZIP)) // 正解画像群1をセット
				.addClass(classNames[1], getInputFileObject(hrefs[1], ZIP)) // 正解画像群2をセット
				.addClass(classNames[2], getInputFileObject(hrefs[2], ZIP)) // 正解画像群3をセット
				.negativeExamples(getInputFileObject(hrefs[3], ZIP)).build(); // 非正解画像群をセット
		Classifier result = service.createClassifier(createOptions).execute(); // 識別器の生成を実行
		System.out.println(result);
		return result;
	}

	/*
	 * カスタム識別器を使用して画像認識する 呼出元: GET /
	 */
	public ClassifiedImages classifyCustom(String classifierId) throws FileNotFoundException {
		System.out.println("Classifying using the custom classifier...");
		System.out.println("Using : " + classifierId);
		String classifier = "{\"classifier_ids\": [\"" + classifierId + "\"]}";
		ClassifyOptions options = new ClassifyOptions.Builder().imagesFile(getInputFileObject(inputImgUrl, JPG))
				.parameters(classifier).build(); // 認識対象の画像と使用する識別器IDをセット
		ClassifiedImages result = service.classify(options).execute(); // 画像認識を実行
		System.out.println(result);
		return result;
	}

	/*
	 * カスタム識別器を削除する 呼出元: GET /
	 */
	public void deleteClassifier(String classifierId) {
		DeleteClassifierOptions options = new DeleteClassifierOptions.Builder(classifierId).build();
		service.deleteClassifier(options).execute();
	}

	/*
	 * 識別器を検索する Call元: GET /
	 */
	public Classifiers retrieveClassifier() {
		ListClassifiersOptions listClassifiersOptions = new ListClassifiersOptions.Builder().verbose(true).build();
		Classifiers result = service.listClassifiers(listClassifiersOptions).execute();
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
				while ((size = in.read(buf)) != -1)
					fos.write(buf, 0, size);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}
}
