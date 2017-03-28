package jaxrs.resource;

import javax.enterprise.context.Dependent;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.QueryParam;

import javax.ws.rs.core.Response;

import com.ibm.websphere.jaxrs20.multipart.IMultipartBody;

import jaxrs.model.MyClassify;

@Dependent
@Path("visualrecognition")
public class VisualRecognitionREST {
	
	@POST
	@Path("/classify")
	@Consumes("multipart/form-data")
	//@Produces("multipart/form-data")
	public Response classifyDefault(IMultipartBody multipartBody){
		MyClassify mc = new MyClassify(multipartBody);
		//VisualClassification resultMessage = cd.classify();
		//return Response.ok(resultMessage).build();
		mc.classify();
		return Response.ok("ok").build();
	}
	
	@POST
	@Path("/classifyCustom")
	@Consumes("multipart/form-data")
	//@Produces("multipart/form-data")
	public Response clussifyCustom(
//			IMultipartBody multipartBody,
//			@QueryParam("classifierId") String classifierId){
//		System.out.println("ID : " + classifierId);
//		MyClassify mc = new MyClassify(multipartBody);
//		mc.classifyCustom(classifierId);
//		return Response.ok("ok").build();

			// @QueryParamとIMultipartBodyを併用できないので，とりあえずClassifierIdは直打ち
			IMultipartBody multipartBody
			){
		String classifierId = "myfavorite_1772425759";
		MyClassify mc = new MyClassify(multipartBody);
		mc.classifyCustom(classifierId);
		return Response.ok("ok").build();
	}
	
	@POST
	@Path("/test")
	@Consumes("multipart/form-data")
	//@Produces("multipart/form-data")
	public Response test(
			//IMultipartBody multipartBody,
			@QueryParam("test") String test){
		System.out.println("test : " + test);
		return Response.ok("ok").build();
	}
}
