package jaxrs.resource;

import javax.enterprise.context.Dependent;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

import jaxrs.model.WasbookVR;

@Dependent
@Path("visualrecognition")
public class VisualRecognitionREST {

	// 1) "GET /rest/visualrecognition/classify" で呼ばれ、デフォルト識別器で画像認識する
	// 用途: 画像URLを受け取り、画像認識メソッド(WasbookVR.classify())を呼出す
	@GET
	@Path("/classify")
	@Produces(MediaType.APPLICATION_JSON)
	public Response classifyDefault(
			@FormParam("apiKey") String apiKey, //Watson APIを使用するための自身のAPIキー
			@FormParam("href") String href      //画像認識対象の画像URL
			){
		WasbookVR wvr = new WasbookVR(href,apiKey);
		VisualClassification resultMessage = wvr.classify();
		return Response.ok(resultMessage).build();
	}
	
	// 2) "POST /rest/visualrecognition/classifiers" で呼ばれ、学習用画像を使用して識別器を生成する
	// 用途: 画像URLを受け取り、識別器の学習・生成メソッド(WasbookVR.classifierLearn())を呼出す
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
			) {
		
		String[] hrefs = new String[] { pos1, pos2, pos3, neg };
		String[] classNames = new String[] { pos_className1, pos_className2, pos_className3 };

		WasbookVR wvr = new WasbookVR(null,apiKey);
		VisualClassifier resultMessage = wvr.classifierLearn(hrefs,classNames,classifierName);
		return Response.ok(resultMessage).build();
	}
	
	// 3) "POST /rest/visualrecognition/classify" で呼ばれ、独自に生成した識別器で画像認識する
	// 用途: 画像URLを受け取り、自身で作成した識別器を使用した画像認識メソッド(WasbookVR.classifyCustom())を呼出す
	@POST
	@Path("/classify")
	@Produces(MediaType.APPLICATION_JSON)
	public Response clussifyCustom(
			@FormParam("apiKey") String apiKey, //Watson APIを使用するための自身のAPIキー
			@FormParam("href") String href,     //画像認識対象の画像URL
			@FormParam("classifierId") String classifierId //使用する識別器のID
			){
		WasbookVR wvr = new WasbookVR(href,apiKey);
		VisualClassification resultMessage = wvr.classifyCustom(classifierId);
		return Response.ok(resultMessage).build();
	}	
}
