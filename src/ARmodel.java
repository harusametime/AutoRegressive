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

		// cの初期化
		c = new double[n];
		for (int j = 0; j < n; j++) {
			for (int i = start + n; i < data.size(); i++) {
				c[j] += (data.get(i) - average) * (data.get(i - j) - average);
			}
			c[j] = c[j] / (data.size() - n -start);

		}


		// omegaを求めるための係数行列Aと定数項b
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

		// 連立方程式をといてomegaをもとめる。
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
			System.out.print("ARモデルの学習が済んでいないか、0次のARモデルです。");
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

	public ArrayList<Access> readAccess() {

		ArrayList<Access> temp = new ArrayList<Access>();

		// 読み込むファイルの名前
		String inputFileName = "access.txt";

		// ファイルオブジェクトの生成
		File inputFile = new File(inputFileName);

		try {
			// 入力ストリームの生成
			FileInputStream fis = new FileInputStream(inputFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			// テキストファイルからの読み込み
			String msg;
			while ((msg = br.readLine()) != null) {
				String[] line = msg.split("\t");
				Access data = new Access();
				data.show = Integer.valueOf(line[0]);
				data.search = Integer.valueOf(line[1]);
				temp.add(data);
			}

			// 後始末
			br.close();

			// エラーがあった場合は、スタックトレースを出力
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp;
	}

	public double getAverage(int i, int n, ArrayList<Double> data,
			ArrayList<Access> access) {

		double sum = 0;
		int count = 0;
	
		if (count < 0) {
			ok++;
			return sum / count;
		} else {
			for (int j = n; j < data.size(); j++) {
				sum += data.get(j);
			}
			ng++;
			return sum / (data.size() - n);
		}
	}

	public double estimate(ArrayList<Double> data, double n) {
		double temp = 0;
		
		for(int i=0; i<n-1; i++){
			temp+= data.get(data.size()-1-i)*omega[i];
		}
		
		return temp;
	}

}
