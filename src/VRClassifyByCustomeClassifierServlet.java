

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
 * Servlet implementation class VRClassifyByCustomeClassifierServlet
 */
@WebServlet("/VRClassifyByCustomeClassifierServlet")
public class VRClassifyByCustomeClassifierServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VRClassifyByCustomeClassifierServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
		service.setApiKey("14d773bd9686a383b2508bf1d7a991f4b92a497f"); // apiKey

	    // Classify using generated classifier
	    System.out.println("Classify using the 'myfavorite' classifier");
	    // trueイメージのテスト
	    ClassifyImagesOptions options = new ClassifyImagesOptions.Builder().images(new File("/Users/capsma1t/env/eclipse/4.6_Neon/WDT/wasbook_eclipse462/imgs/Classify_test/true/chandelier_0102.jpg"))
	        .classifierIds("myfavorite_1772425759").build();
	    // falseイメージのテスト
//	    options = new ClassifyImagesOptions.Builder().images(new File("/Users/capsma1t/env/eclipse/4.6_Neon/WDT/wasbook_eclipse462/imgs/Classify_test/false/lamp_0017.jpg"))
//		        .classifierIds("myfavorite_1772425759").build();
	    VisualClassification result = service.classify(options).execute();
	    System.out.println(result);
	    response.getWriter().println(result);
	}

}
