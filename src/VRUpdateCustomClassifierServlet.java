

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class VRUpdateCustomClassifierServlet
 */
@WebServlet("/VRUpdateCustomClassifierServlet")
public class VRUpdateCustomClassifierServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VRUpdateCustomClassifierServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
	    // Update classifier with more positive images
	    System.out.println("Update a classifier with more positive images");
	    ClassifierOptions updateOptions = new ClassifierOptions.Builder()
	        .addClass("car", new File("src/test/resources/visual_recognition/car_positive.zip")).build();
	    VisualClassifier updatedFoo = service.updateClassifier(foo.getId(), updateOptions).execute();
	    System.out.println(updatedFoo);
		 **/
	}
}
