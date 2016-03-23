package cn.whutjxl.prediction.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Utils {

	public static void getArffByExcel(String sourcePath, String destinationPath, int step, double threshold) {
		Workbook workbook = null;
		char[] features = new char[1000];
		int num = 0;
		double prev = 0;
		double now = 0;
		double diff = 0;
		try {
			workbook = new XSSFWorkbook(sourcePath);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> it = sheet.rowIterator();
			if (it.hasNext())
				it.next();
			if (it.hasNext())
				prev = it.next().getCell(4).getNumericCellValue();
			while (it.hasNext()) {
				now = it.next().getCell(4).getNumericCellValue();
				diff = now - prev;
				prev = now;
				if (diff - threshold > 0)
					features[num++] = 'U';
				else if (diff + threshold < 0)
					features[num++] = 'F';
				else
					features[num++] = '-';
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (workbook != null)
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		if (num >= step) {
			FileWriter writer = null;
			StringBuffer sb = null;
			try {
				writer = new FileWriter(destinationPath);
				String[] arr = destinationPath.split("[\\/\\.]");
				String fileName = arr[arr.length - 2];
				writer.write("@relation " + fileName + "\n\n");
				for (int i = step - 2; i >= 1; i--) {
					writer.write("@attribute day" + i + " {U, -, F}\n");
				}
				writer.write("@attribute today {U, -, F}\n");
				writer.write("@attribute tomorrow {U, -, F}\n\n");
				writer.write("@data\n");
				for (int j = step - 1; j < num; j++) {
					sb = new StringBuffer();
					for (int k = step - 1; k > 0; k--) {
						sb.append(features[j - k] + ",");
					}
					sb.append(features[j] + "\n");
					writer.write(sb.toString());
					writer.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (writer != null)
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	public static String createModel(String sourcePath, String classifierName, String[][] options) {
		String result = "";
		try {
			DataSource source = new DataSource(sourcePath);
			Instances train = source.getDataSet();
			train.setClassIndex(train.numAttributes() - 1);
			Classifier cls = (Classifier) Class.forName(classifierName).newInstance();
			if (options != null) {
				for (String[] strings : options) {
					cls.setOptions(strings);
				}
			}
			cls.buildClassifier(train);
			Evaluation eval = new Evaluation(train);
			eval.crossValidateModel(cls, train, 10, new Random(1));
			result = eval.toSummaryString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
