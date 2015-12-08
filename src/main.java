import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class main {

	public static void main(String[] args) {


		/*****************************
		 * Read data
		 *****************************/

		ArrayList<Double> textdata = new ArrayList<Double>();
		readData(textdata);

		/*****************************
		 * Length to calculate Moving Average（Moving average is not considered when T=1.）
		 *****************************/

		int T = 1;

		/*****************************
		 * Order of AR model = n-1
		 *****************************/
		int n = 2;
		
		
		/*****************************
		 * Calculate Likelihood of exch data at time t
		 * with past data 
		 *****************************/
		for (int t = 1; t < textdata.size(); t++) {

			// Latest data whose likelihood is to be calculated 
			double newdata = textdata.get(t);

			// Past data for determining AR coefficients
			ArrayList<Double> data = new ArrayList<Double>();
			
			for (int i = 0; i < t; i++) {
				data.add(textdata.get(i));
			}
			


			// For (n-1) order AR model, we need more than n data for t 
			if (n + 1 >= t) {
				continue;
			}

			double p = 0;

			// Fitting AR model to past data
			ARmodel model = new ARmodel();
			model.learn(data, n, 1);

			// Calculate probability p for the latest data
			p = model.getProbablity(newdata, data);

			// Calculate log likelihood 
			double sc = -Math.log(p);


			// In case of fewer data for T, 
			// 
			if (t < T + n + 1) {
				continue;
			}
		}

	}

	public static void readData(ArrayList<Double> data) {
		// File name to be read
		// Text file consits of N row/1 column
		String inputFileName = "data.txt";
		
		// Read file
		File inputFile = new File(inputFileName);
		try {
			FileInputStream fis = new FileInputStream(inputFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			String msg;
			while ((msg = br.readLine()) != null) {
				data.add(Double.parseDouble(msg));
			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
