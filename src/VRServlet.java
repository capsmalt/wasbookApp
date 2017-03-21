// 識別対象の画像のフルパスで，結果を返すサーブレット

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
 * Servlet implementation class VRServlet
 */
@WebServlet("/VRServlet")
public class VRServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VRServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
		service.setApiKey("14d773bd9686a383b2508bf1d7a991f4b92a497f"); //api key

		System.out.println("Classify an image");
		ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
		    .images(new File("/Users/capsma1t/env/eclipse/4.6_Neon/WDT/wasbook_eclipse462/imgs/test/singapura.jpg"))
		    .build(); // 現在は絶対パスを使用。car.pngとsingapura.jpgが使用可能
		VisualClassification result = service.classify(options).execute();
		System.out.println(result);
		resp.getWriter().println(result);
	}
}
