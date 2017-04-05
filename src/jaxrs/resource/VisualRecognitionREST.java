package jaxrs.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.Dependent;

import javax.ws.rs.FormParam;
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

	// URL入力バージョン
	@POST
	@Path("/classify")
	@Produces(MediaType.APPLICATION_JSON)
	public Response classifyDefault(
			@FormParam("href") String href){
		WasbookVR wvr = new WasbookVR(href);
		VisualClassification resultMessage = wvr.classify();
		return Response.ok(resultMessage).build();
	}

	@POST
	@Path("/classifyCustom")
	@Produces(MediaType.APPLICATION_JSON)
	public Response clussifyCustom(
			@FormParam("href") String href,
			@FormParam("classifierId") String classifierId
			){
		WasbookVR wvr = new WasbookVR(href);
		VisualClassification resultMessage = wvr.classifyCustom(classifierId);
		return Response.ok(resultMessage).build();
	}	
	
	@POST
	@Path("/classifiers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response classifiers(
			@FormParam("href1") String pos1, @FormParam("className1") String pos_className1,
			@FormParam("href2") String pos2, @FormParam("className2") String pos_className2,
			@FormParam("href3") String pos3, @FormParam("className3") String pos_className3,
			@FormParam("neg") String neg,
			@FormParam("classifierName") String classifierName
			) {
		List<String> hrefs = new ArrayList<String>();
		List<String> classNames = new ArrayList<String>();
		Collections.addAll(hrefs, pos1,pos2,pos3,neg);
		Collections.addAll(classNames, pos_className1,pos_className2,pos_className3);

		WasbookVR wvr = new WasbookVR(pos1);// 要修正 : 本来，このコンストラクタで，一つだけのURLを渡しても意味がない。 //他のメソッドのコンストラクタは一つのURLで良いが。
		VisualClassifier resultMessage = wvr.classifierLearn(hrefs,classNames,classifierName);
		return Response.ok(resultMessage).build();
	}
	
	
// IMultipart使用バージョン(ただし，API Discoveryが使えない)
//	@POST
//	@Path("/classify")
//	@Consumes(MediaType.MULTIPART_FORM_DATA) // @Consumes("multipart/form-data")でも同じ
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response classifyDefault(IMultipartBody multipartBody){
//		MyClassify mc = new MyClassify(multipartBody);
//		VisualClassification resultMessage = mc.classify();
//		return Response.ok(resultMessage).build();
//	}
	
//	@POST
//	@Path("/classifyCustom")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response clussifyCustom(
////			IMultipartBody multipartBody,
////			@QueryParam("classifierId") String classifierId){
////		System.out.println("ID : " + classifierId);
////		MyClassify mc = new MyClassify(multipartBody);
////		mc.classifyCustom(classifierId);
////		return Response.ok("ok").build();
//
//			// @QueryParamとIMultipartBodyを併用できないので，とりあえずClassifierIdは直打ち
//			IMultipartBody multipartBody
//			){
//		String classifierId = "myfavorite_1772425759";
//		MyClassify mc = new MyClassify(multipartBody);
//		VisualClassification resultMessage = mc.classifyCustom(classifierId);
//		return Response.ok(resultMessage).build();
//	}
}
