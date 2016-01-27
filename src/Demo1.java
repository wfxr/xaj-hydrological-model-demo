import HydrologicalModel.*;

import static HydrologicalModelHelper.ModelEvaluation.NashSutcliffeEfficiency;

/**
 * Created by Wenxuan on 1/27/2016.
 * Email: wenxuan-zhang@outlook.com
 */
public class Demo1 {
    // 降雨量序列
    static double[] P = new double[]{
            10, 24.1, 20.4, 18.3, 10.1, 5.5, 0.6, 3.1, 1.9, 4.6, 5,
            4.8, 36.2, 29, 6, 3.6, 0.4, 0, 0.5, 3.8, 0, 1.8, 0.2, 0.3
    };

    // 蒸发皿蒸发量序列
    static double[] EI = new double[]{
            0.1, 0, 0.1, 0.5, 0.7, 0.9, 0.8, 0.7, 0.5, 0.3, 0.2,
            0.1, 0, 0, 0.1, 0.6, 0.8, 1, 0.9, 0.8, 0.7, 0.5, 0.3, 0.1
    };

    // 径流量实测序列
    static double[] ObsQ = new double[]{
            88.13, 220.9, 391.36, 452.51, 427.42, 296.26, 164.35, 88.81, 109.76,
            134.55, 232.84, 306.02, 966.71, 2162.41, 1665.03, 563.39, 219.63, 84.87,
            64.58, 100.6, 137.23, 88.17, 87.44, 70.01
    };

    public static void main(String[] args) {
        // 创建新安江水文模型
        XajModel xaj = new XajModel();

        // 设置模型参数
        xaj.SetSoilWaterStorageParam(100, 50, 100);
        xaj.SetEvapotranspirationParam(0.55, 0.10);
        xaj.SetRunoffGenerationParam(0.18, 0.00);
        xaj.SetSourcePartitionParam(15.89, 2.00, 0.65, 0.05);
        xaj.SetRunoffConcentrationParam(0.45, 0.05, 537);

        // 执行产流计算
        RunoffGenerationResult runoffGenerationResult =
                xaj.ComputeRunoffGeneration(P, EI, 42.41, 100.00, 43.07);

        // 执行流域划分计算
        SourcePartitionResult sourcePartitionResult =
                xaj.ComputeSourcePartition(runoffGenerationResult, 9.86, 2);

        // 执行汇流计算
        RunoffConcentrationResult runoffConcentrationResult =
                xaj.ComputeRunoffConcentration(sourcePartitionResult, 44.35, 19.23, 2);

        // 输出结果
        PrintResult(runoffConcentrationResult);
    }

    // 格式化输出模型的计算结果
    public static void PrintResult(RunoffConcentrationResult result) {
        System.out.printf("%12s%12s%12s%12s\n", "QRS", "QRSS", "QRG", "Q");
        for (int i = 0; i < result.Length; ++i)
            System.out.printf("%12.2f%12.2f%12.2f%12.2f\n", result.QRS[i], result.QRSS[i], result.QRG[i], result.Q[i]);
        System.out.printf("(NSE of Q: %f)\n", NashSutcliffeEfficiency(ObsQ, result.Q));
    }
}
