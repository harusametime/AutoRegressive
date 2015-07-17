import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class main {

	public static void main(String[] args) {


		/*****************************
		 * �����f�[�^�̓ǂݍ���
		 *****************************/

		ArrayList<Double> textdata = new ArrayList<Double>();
		readData(textdata);

		/*****************************
		 * ���߂�T�̈ړ����ςŕω��_�����o����B �iT=1���ƈړ����ς��Ƃ�Ȃ��BT>0�j
		 *****************************/

		int T = 1;

		/*****************************
		 * AR���f���̂��߂̃p�����[�^
		 *****************************/

		// (n-1)����AR���f���Ƃ��Đ��肷��B
		int n = 2;
		
		
		/*****************************
		 * ���� t �Œ����ǂݍ��݁A�f�[�^�̖ޓx���v�Z����B
		 *****************************/
		for (int t = 1; t < textdata.size(); t++) { // AR���f���p�ɏ��Ȃ��Ƃ�t=1����

			// �ޓx���v�Z�������ŐV�̃f�[�^
			double newdata = textdata.get(t);

			// ���p����ߋ��̃f�[�^
			ArrayList<Double> data = new ArrayList<Double>();
			
			for (int i = 0; i < t; i++) {
				data.add(textdata.get(i));
			}
			


			// (n-1)����AR���f���ɂ́A(t-1)�̉ߋ��f�[�^��(n-1)�ȏ�K�v
			if (n + 1 >= t) {
				continue;
			}

			double p = 0;

			// AR���f���Ŗޓx�����߂�
			// AR���f��
			ARmodel model = new ARmodel();
			
			// AR���f���̊w�K
			model.learn(data, n, 1);

			// AR���f������m���̓��o
			// newdata�ɑ΂���m�����xp�����߂�
			p = model.getProbablity(newdata, data);

			// �ޓxsc��-log p �ŋ��߂āAhist_sc���X�V����B
			double sc = -Math.log(p);


			// �ړ����ς����߂�̂ɕK�v�ȃf�[�^�����Ȃ�
			// �ߋ��̃f�[�^T�{�ŐV�̃f�[�^n-1���K�v
			if (t < T + n + 1) {
				continue;
			}
		}

	}

	public static void readData(ArrayList<Double> data) {
		// �ǂݍ��ރt�@�C���̖��O
		String inputFileName = "data.txt";

		// �t�@�C���I�u�W�F�N�g�̐���
		File inputFile = new File(inputFileName);

		try {
			// ���̓X�g���[���̐���
			FileInputStream fis = new FileInputStream(inputFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			// �e�L�X�g�t�@�C������̓ǂݍ���
			String msg;
			while ((msg = br.readLine()) != null) {
				data.add(Double.parseDouble(msg));
			}

			// ��n��
			br.close();

			// �G���[���������ꍇ�́A�X�^�b�N�g���[�X���o��
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

		// �ǂݍ��ރt�@�C���̖��O
		String inputFileName = "access.txt";

		// �t�@�C���I�u�W�F�N�g�̐���
		File inputFile = new File(inputFileName);

		try {
			// ���̓X�g���[���̐���
			FileInputStream fis = new FileInputStream(inputFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			// �e�L�X�g�t�@�C������̓ǂݍ���
			String msg;
			while ((msg = br.readLine()) != null) {
				String[] line = msg.split("\t");
				Access data = new Access();
				data.show = Integer.valueOf(line[0]);
				data.search = Integer.valueOf(line[1]);
				temp.add(data);
			}

			// ��n��
			br.close();

			// �G���[���������ꍇ�́A�X�^�b�N�g���[�X���o��
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp;
	}

}
