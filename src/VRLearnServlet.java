
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.http.RequestBuilder;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.util.ResponseConverterUtils;
import com.ibm.watson.developer_cloud.util.Validator;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;


/**
 * Servlet implementation class VRLearnServlet
 */
@WebServlet("/VRLearnServlet")
public class VRLearnServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VRLearnServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
		service.setApiKey("14d773bd9686a383b2508bf1d7a991f4b92a497f"); // apiKey
		
	    // Learn (generate classifier)
		System.out.println("Leaning pos/neg images for generating classifier...");
	    ClassifierOptions createOptions = new ClassifierOptions.Builder().classifierName("myfavorite")
	        .addClass("chandelier", new File("/Users/capsma1t/env/eclipse/4.6_Neon/WDT/wasbook_eclipse462/imgs/Learn/positive/myfavorite/chandelier.zip"))
	        .addClass("laptop", new File("/Users/capsma1t/env/eclipse/4.6_Neon/WDT/wasbook_eclipse462/imgs/Learn/positive/myfavorite/laptop.zip"))
	        .addClass("pizza", new File("/Users/capsma1t/env/eclipse/4.6_Neon/WDT/wasbook_eclipse462/imgs/Learn/positive/myfavorite/pizza.zip"))
	        .negativeExamples(new File("/Users/capsma1t/env/eclipse/4.6_Neon/WDT/wasbook_eclipse462/imgs/Learn/negative/negative.zip")).build();
	    VisualClassifier foo = service.createClassifier(createOptions).execute();
	    System.out.println(foo);
	    response.getWriter().println(foo);
	}
}
