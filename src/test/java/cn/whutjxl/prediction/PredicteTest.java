package cn.whutjxl.prediction;

import java.util.HashMap;
import java.util.Map;

import cn.whutjxl.prediction.utils.Utils;

public class PredicteTest {

	public static void main(String[] args) {
		String classiferName = "weka.classifiers.trees.J48";
		/**
		 * trees
		 */
//		 classiferName = "weka.classifiers.trees.BFTree"; //54.63
//		 classiferName = "weka.classifiers.trees.DecisionStump"; //58.53
//		 classiferName = "weka.classifiers.trees.FT"; //58.04
//		 classiferName = "weka.classifiers.trees.Id3"; //46.82
//		 classiferName = "weka.classifiers.trees.J48"; //55.12
//		 classiferName = "weka.classifiers.trees.J48graft"; //55.12
//		 classiferName = "weka.classifiers.trees.LADTree"; //60
//		 classiferName = "weka.classifiers.trees.LMT"; //54.14
//		 classiferName = "weka.classifiers.trees.NBTree"; //57.56
//		 classiferName = "weka.classifiers.trees.RandomForest"; //48.78
//		 classiferName = "weka.classifiers.trees.RandomTree"; //49.26
//		 classiferName = "weka.classifiers.trees.REPTree"; //57.56
//		 classiferName = "weka.classifiers.trees.SimpleCart"; //52.19
		/**
		 * rules
		 */
//		 classiferName = "weka.classifiers.rules.ConjunctiveRule";//51.21
//		 classiferName = "weka.classifiers.rules.DecisionTable"; //55.60
//		 classiferName = "weka.classifiers.rules.DTNB"; //56.58
//		 classiferName = "weka.classifiers.rules.JRip"; //54.14
//		 classiferName = "weka.classifiers.rules.NNge"; //50.73
//		 classiferName = "weka.classifiers.rules.OneR"; //58.53
//		 classiferName = "weka.classifiers.rules.PART"; //56.09
//		 classiferName = "weka.classifiers.rules.Prism"; //46.34
//		 classiferName = "weka.classifiers.rules.Ridor"; //52.68
//		 classiferName = "weka.classifiers.rules.ZeroR"; //48.78
		/**
		 * misc
		 */
//		 classiferName = "weka.classifiers.misc.HyperPipes"; //49.75
//		 classiferName = "weka.classifiers.misc.VFI"; //50.24
		/**
		 * meta
		 */
//		 classiferName = "weka.classifiers.meta.AdaBoostM1"; //56.09
//		 classiferName = "weka.classifiers.meta.AttributeSelectedClassifier";//59.02
//		 classiferName = "weka.classifiers.meta.Bagging"; //52.19
//		 classiferName = "weka.classifiers.meta.ClassificationViaClustering";//54.63
//		 classiferName = "weka.classifiers.meta.ClassificationViaRegression";//58.04
//		 classiferName = "weka.classifiers.meta.CVParameterSelection"; //48.78
//		 classiferName = "weka.classifiers.meta.Dagging"; //53.17
//		 classiferName = "weka.classifiers.meta.Decorate"; //50.73
//		 classiferName = "weka.classifiers.meta.END"; //55.12
//		 classiferName = "weka.classifiers.meta.FilteredClassifier"; //55.12
//		 classiferName = "weka.classifiers.meta.Grading"; //48.78
//		 classiferName = "weka.classifiers.meta.LogitBoost"; //55.60
//		 classiferName = "weka.classifiers.meta.MultiBoostAB"; //57.07
//		 classiferName = "weka.classifiers.meta.MultiClassClassifier"; //55.60
//		 classiferName = "weka.classifiers.meta.MultiScheme"; //48.78
//		 classiferName = "weka.classifiers.meta.OrdinalClassClassifier";//55.12
//		 classiferName = "weka.classifiers.meta.RacedIncrementalLogitBoost";//48.78
//		 classiferName = "weka.classifiers.meta.RandomCommittee"; //48.78
//		 classiferName = "weka.classifiers.meta.RandomSubSpace"; //61.95
//		 classiferName = "weka.classifiers.meta.RotationForest"; //56.09
//		 classiferName = "weka.classifiers.meta.Stacking"; //48.78
//		 classiferName = "weka.classifiers.meta.StackingC"; //49.75
//		 classiferName = "weka.classifiers.meta.Vote"; //48.78
//		 classiferName ="weka.classifiers.meta.nestedDichotomies.ClassBalancedND"; //55.12
//		 classiferName ="weka.classifiers.meta.nestedDichotomies.DataNearBalancedND"; //55.12
//		 classiferName = "weka.classifiers.meta.nestedDichotomies.ND"; //55.12
		/**
		 * lazy
		 */
//		 classiferName = "weka.classifiers.lazy.IB1"; //49.75
//		 classiferName = "weka.classifiers.lazy.IBk"; //49.75
//		 classiferName = "weka.classifiers.lazy.KStar"; //54.14
//		 classiferName = "weka.classifiers.lazy.LBR"; //60
//		 classiferName = "weka.classifiers.lazy.LWL"; //50.24
		/**
		 * functions
		 */
//		 classiferName = "weka.classifiers.functions.LibSVM"; //57.56
//		 classiferName = "weka.classifiers.functions.Logistic"; //55.60
//		 classiferName = "weka.classifiers.functions.MultilayerPerceptron";//50.24
//		 classiferName = "weka.classifiers.functions.RBFNetwork"; //61.46
//		 classiferName = "weka.classifiers.functions.SimpleLogistic"; //56.58
//		 classiferName = "weka.classifiers.functions.SMO"; //56.58
		/**
		 * bayes
		 */
//		 classiferName = "weka.classifiers.bayes.AODE"; //62.92
//		 classiferName = "weka.classifiers.bayes.AODEsr"; //62.92
//		 classiferName = "weka.classifiers.bayes.BayesNet"; //58.04
//		 classiferName = "weka.classifiers.bayes.HNB"; //60
//		 classiferName = "weka.classifiers.bayes.NaiveBayes"; //58.04
//		 classiferName = "weka.classifiers.bayes.NaiveBayesSimple"; //58.04
//		 classiferName = "weka.classifiers.bayes.NaiveBayesUpdateable"; //58.04
//		 classiferName = "weka.classifiers.bayes.WAODE"; // 62.92

		/**
		 * src/main/resources/stock.xlsx
		 * D:/Ksoftware/stock.xlsx
		 * ‪C:/Users/whutjxl/Desktop/stock.xlsx‪
		 */
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (int i = 4; i < 80; i++) {
			Utils.getArffByExcel("C:/Users/whutjxl/Desktop/stock.xlsx", "src/main/resources/stock.arff", i, 0);
			String s = Utils.createModel("src/main/resources/stock.arff", classiferName, null);
			map.put(i, s);
		}
		for (Integer inte : map.keySet()) {
			System.out.println(inte + "\t----->\t" + map.get(inte));
		}
	}
}
