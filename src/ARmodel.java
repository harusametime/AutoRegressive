import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ARmodel {

	double average;
	double variance = 0;
	double[] c;
	double[] omega;

	int ok = 0;
	int ng = 0;
	int start = 0;

	ARmodel() {
		average = 0;
		variance = 0;

		ok = 0;
		ng = 0;

	}

	public void learn(ArrayList<Double> data, int n, double r) {


		for (int i = start + n; i < data.size(); i++) {
			average += data.get(i);
		}
		average = average / (data.size() - n -start);

		c = new double[n];
		for (int j = 0; j < n; j++) {
			for (int i = start + n; i < data.size(); i++) {
				c[j] += (data.get(i) - average) * (data.get(i - j) - average);
			}
			c[j] = c[j] / (data.size() - n -start);

		}


		double[][] A = new double[n - 1][n - 1];
		double[] b = new double[n - 1];

		for (int i = 1; i < n; i++) {
			for (int j = 1; j < n; j++) {
				A[i - 1][j - 1] = c[Math.abs(i - j)];
				// System.out.print(A[i-1][j-1] +" ");
			}
			if (i < n) {
				b[i - 1] = c[i];
			}
		}

		GaussJordan.GaussJordanElimination gauss = new GaussJordan.GaussJordanElimination(
				A, b);
		omega = new double[n - 1];
		omega = gauss.getSolution(A, b);
		

		for (int i = omega.length + 1; i < data.size(); i++) {
			double x_hat = 0;
			for (int j = 0; j < omega.length; j++) {
				x_hat += omega[j] * (data.get(i - j) - average);
			}
			this.variance += (data.get(i) - x_hat - average)
					* (data.get(i) - x_hat - average);
			// System.out.println("var:"+
			// String.valueOf(data.get(i)-x_hat-average));
		}
		this.variance /= (data.size() - (omega.length + 1));
		//System.out.println("--avg:"+ this.average +"--var:"+ this.variance);
	}

	public double getProbablity(double x, ArrayList<Double> data) {
		double p = 0;
		double xi = 0;
		if (omega.length < 1) {
			System.out.print("Error: AR model fitting is not completed or 0-order AR model is specified.");
			System.exit(0);
		}

		for (int i = 0; i < omega.length; i++) {
			xi -= omega[i] * (data.get(data.size() - 1 - i) - average);
		}
		xi += x - average;

		p = 1 / Math.pow(2 * Math.PI, 0.5) / Math.pow(Math.abs(variance), 0.5)
				* Math.exp(-0.5 * Math.pow(xi, 2) / variance);

		// System.out.println(average + " " + variance + " " + xi);

		return p;
	}

	public double estimate(ArrayList<Double> data, double n) {
		double temp = 0;
		
		for(int i=0; i<n-1; i++){
			temp+= data.get(data.size()-1-i)*omega[i];
		}
		
		return temp;
	}

}
