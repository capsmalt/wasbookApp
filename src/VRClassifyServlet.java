

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;

import sun.security.action.GetBooleanSecurityPropertyAction;

/**
 * Servlet implementation class VRClassifyServlet
 */
@WebServlet("/VRClassifyServlet")
public class VRClassifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VRClassifyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static final String FILE = "upload-file";
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
		service.setApiKey("14d773bd9686a383b2508bf1d7a991f4b92a497f"); // apiKey
		String pathToTargetImage = request.getParameter("path_defaultClassify");
		response.getWriter().println("path: " + pathToTargetImage); 
		response.getWriter().println("getContextPath() : " + request.getContextPath());

		/*** 以下サンプル ***/
		// Classify
		System.out.println("Classify an image");
	    ClassifyImagesOptions options =
	    	new ClassifyImagesOptions.Builder().images(new File(pathToTargetImage)).build();
	    VisualClassification result = service.classify(options).execute();
	    System.out.println(result);
		response.getWriter().println(result);
	}
	
	private String getFileName(Part part) {
	    final String partHeader = part.getHeader("content-disposition");
	    for (String content : part.getHeader("content-disposition").split(";")) {
	        if (content.trim().startsWith("filename")) {
	            return content.substring(
	                    content.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	}
}
