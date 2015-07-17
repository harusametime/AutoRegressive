import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class main {

	public static void main(String[] args) {


		/*****************************
		 * 実験データの読み込み
		 *****************************/

		ArrayList<Double> textdata = new ArrayList<Double>();
		readData(textdata);

		/*****************************
		 * 直近のTの移動平均で変化点を検出する。 （T=1だと移動平均をとらない。T>0）
		 *****************************/

		int T = 1;

		/*****************************
		 * ARモデルのためのパラメータ
		 *****************************/

		// (n-1)次のARモデルとして推定する。
		int n = 2;
		
		
		/*****************************
		 * 時刻 t で逐次読み込み、データの尤度を計算する。
		 *****************************/
		for (int t = 1; t < textdata.size(); t++) { // ARモデル用に少なくともt=1から

			// 尤度を計算したい最新のデータ
			double newdata = textdata.get(t);

			// 利用する過去のデータ
			ArrayList<Double> data = new ArrayList<Double>();
			
			for (int i = 0; i < t; i++) {
				data.add(textdata.get(i));
			}
			


			// (n-1)次のARモデルには、(t-1)個の過去データが(n-1)個以上必要
			if (n + 1 >= t) {
				continue;
			}

			double p = 0;

			// ARモデルで尤度を求める
			// ARモデル
			ARmodel model = new ARmodel();
			
			// ARモデルの学習
			model.learn(data, n, 1);

			// ARモデルから確率の導出
			// newdataに対する確率密度pを求める
			p = model.getProbablity(newdata, data);

			// 尤度scを-log p で求めて、hist_scを更新する。
			double sc = -Math.log(p);


			// 移動平均を求めるのに必要なデータ数がない
			// 過去のデータT＋最新のデータn-1個が必要
			if (t < T + n + 1) {
				continue;
			}
		}

	}

	public static void readData(ArrayList<Double> data) {
		// 読み込むファイルの名前
		String inputFileName = "data.txt";

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
				data.add(Double.parseDouble(msg));
			}

			// 後始末
			br.close();

			// エラーがあった場合は、スタックトレースを出力
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static double getAverage(int i, ArrayList<Double> data) {

		ArrayList<Access> access = new ArrayList<Access>();
		access = readAccess();

		double sum = 0;
		int count = 0;
		for (int j = 0; j < i; j++) {
			if (access.get(i).show == access.get(j).show
					&& access.get(i).search == access.get(j).search) {
				sum += data.get(j);
				count++;
				break;
			}
		}
		if (count > 0) {
			return sum / count;
		} else {
			return 0;
		}
	}

	public static ArrayList<Access> readAccess() {

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

}
