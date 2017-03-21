

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;

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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
		service.setApiKey("14d773bd9686a383b2508bf1d7a991f4b92a497f"); // apiKey

		/*** 以下サンプル ***/
		// Classify
		System.out.println("Classify an image");
	    ClassifyImagesOptions options =
	    	new ClassifyImagesOptions.Builder().images(new File("/Users/capsma1t/env/eclipse/4.6_Neon/WDT/wasbook_eclipse462/imgs/_single_test/singapura.jpg")).build();
	    VisualClassification result = service.classify(options).execute();
	    System.out.println(result);
		response.getWriter().println(result);
	}
}
