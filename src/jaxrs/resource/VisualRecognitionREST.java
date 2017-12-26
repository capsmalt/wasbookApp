package jaxrs.resource;

import java.io.FileNotFoundException;

import javax.enterprise.context.Dependent;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Classifier;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Classifiers;

import jaxrs.model.WasbookVR;

@Dependent
@Path("visualrecognition")
public class VisualRecognitionREST {

	/*
	 * デフォルト識別器で画像認識する
	 * 画像URLを受け取り、画像認識メソッドを呼出して、識別結果としてカテゴリとスコアを受取り、JSON形式で返す
	 */
	@GET
	@Path("/classify")
	@Produces(MediaType.APPLICATION_JSON)
	public Response classifyDefault(
			@FormParam("apiKey") String apiKey, //Watson APIを使用するための自身のAPIキー
			@FormParam("href") String href      //画像認識対象の画像URL
			) throws FileNotFoundException{
		WasbookVR wvr = new WasbookVR(href,apiKey);
		ClassifiedImages resultMessage = wvr.classify();
		return Response.ok(resultMessage).build();
	}
	
	/*
	 * 学習用画像を使用してカスタム識別器を生成する
	 * 画像URLを受け取り、識別器の学習・生成メソッドを呼出して、生成された識別器IDを受取り、JSON形式で返す
	 */
	@POST
	@Path("/classifiers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response classifiers(
			@FormParam("apiKey") String apiKey, //Watson APIを使用するための自身のAPIキー
			@FormParam("pos1") String pos1, @FormParam("className1") String pos_className1, //正解画像群1をインプット
			@FormParam("pos2") String pos2, @FormParam("className2") String pos_className2, //正解画像群2をインプット
			@FormParam("pos3") String pos3, @FormParam("className3") String pos_className3, //正解画像群3をインプット
			@FormParam("neg") String neg, //非正解画像群をインプット
			@FormParam("classifierName") String classifierName //生成する識別器の名前を指定
			) throws FileNotFoundException {
		
		String[] hrefs = new String[] { pos1, pos2, pos3, neg };
		String[] classNames = new String[] { pos_className1, pos_className2, pos_className3 };

		WasbookVR wvr = new WasbookVR(null,apiKey);
		Classifier resultMessage = wvr.classifierLearn(hrefs,classNames,classifierName);
		return Response.ok(resultMessage).build();
	}
	
	/*
	 * カスタム識別器で画像認識する
	 * 画像URLを受け取り、画像認識メソッドを呼出して、カスタム識別器を使用した識別結果としてカテゴリとスコアを受取り、JSON形式で返す
	 */
	@POST
	@Path("/classify")
	@Produces(MediaType.APPLICATION_JSON)
	public Response clussifyCustom(
			@FormParam("apiKey") String apiKey, //Watson APIを使用するための自身のAPIキー
			@FormParam("href") String href,     //画像認識対象の画像URL
			@FormParam("classifierId") String classifierId //使用する識別器のID
			) throws FileNotFoundException{
		WasbookVR wvr = new WasbookVR(href,apiKey);
		ClassifiedImages resultMessage = wvr.classifyCustom(classifierId);
		return Response.ok(resultMessage).build();
	}
	
	/*
	 * カスタム識別器を削除する
	 * 識別器IDを受け取り、識別器を削除するメソッドを呼出す
	 */
	@DELETE
	@Path("/classifiers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteClassifier(
			@FormParam("apiKey") String apiKey, //Watson APIを使用するための自身のAPIキー
			@FormParam("classifierId") String classifierId //削除する識別器のID
			) throws FileNotFoundException{
		WasbookVR wvr = new WasbookVR(null,apiKey);
		wvr.deleteClassifier(classifierId);
		return Response.ok().build();
	}
	
	/*
	 * 識別器を検索する
	 * 生成済の識別器の一覧を取得して、JSON形式で返す
	 */
	@GET
	@Path("/classifiers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveClassifier(
			@FormParam("apiKey") String apiKey //Watson APIを使用するための自身のAPIキー
			) {
		WasbookVR wvr = new WasbookVR(null, apiKey);
		Classifiers resultMessage = wvr.retrieveClassifier();
		return Response.ok(resultMessage).build();
	}
}
