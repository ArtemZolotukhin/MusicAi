package com.teamname.musicai;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.error.ErrorFunction;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by volkov on 18.03.2018.
 */
public class Main {

    public static final int GENRE = 6;
    public static final int PARAM = 8;
    public static final String FILE_PATH_TRAINING = "training.txt";
    public static final String FILE_PATH_TEST = "test.txt";
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        Scanner sc = new Scanner(System.in);
        System.out.println("выберите режим: newData, learn, test, getGenre, findParam");
        String mode = sc.next();


        if (mode.equals("findParam")){
            int bestFirstParam=0;
            int bestSecondParam=0;
            double maxSum=0;
            for (int i=2; i<=16; i++){
                for (int j=2; j<=16; j++){
                    int[] x = {PARAM,i,j,GENRE};
                    String name = "alex";
                    name = name + i + "_" + j + ".txt";
                    learn(x,name);

                    double localSum=0;
                    double[] result = test(name);
                    for (int k=0; k<GENRE; k++){
                        localSum +=result[k];
                    }
                    if (localSum>maxSum){
                        bestFirstParam=i;
                        bestSecondParam=j;
                        maxSum = localSum;
                    }
                }
            }
            System.out.println(bestFirstParam+" and "+bestSecondParam+" have "+maxSum/GENRE+"%");

        }
        else work(mode);





    }

    public static void work(String mode) throws IOException, UnsupportedAudioFileException {
        if (mode.equals("newData")){
            createInputData();

        }

        if (mode.equals("learn")){
            learn(new int[]{PARAM,8,8,GENRE},"alex.txt");

        }


        if (mode.equals("test")){
            double [] result = test("alex8_15.txt");
            for (int i=0; i<GENRE; i++){
                System.out.println(result[i] + "%");
            }

        }

        if (mode.equals("getGenre")){
            NeuralNetwork alex = NeuralNetwork.createFromFile("alex.txt");
            Scanner sc = new Scanner(System.in);
            while ( true){
                System.out.println("Введите путь к файлу wav");
                String path = sc.next();

                Processing p = new Processing();
                Float[] a = p.getParameters(path);
                double[] param= new double[PARAM];
                for(int i=0; i<PARAM; i++){
                    param[i] = a[i].doubleValue()*100;
                    //System.out.println(param[i]);
                }

                alex.setInput(param);
                alex.calculate();
                double [] networkOutput = alex.getOutput();
                for (int i=0; i<GENRE;i++){
                    System.out.println(networkOutput[i]);
                }

                System.out.println(getGenreString(networkOutput));

            }
        }

    }

    public static DataSet getDataSet(int x, int y)  throws FileNotFoundException {
        DataSet trainingSet = new DataSet(x,y);
        Scanner sc = new Scanner(new File(FILE_PATH_TRAINING));


        while (sc.hasNext()){
            double [] a = new double[x];
            double [] b = new double[y];
            for (int i=0; i<x; i++){
                a[i] = sc.nextDouble();
            }
            b[sc.nextInt()] = 1;
            trainingSet.addRow(a,b);
        }
        return  trainingSet;
    }

    public static int getGenreInt(double [] networkOutput){
        double max = 0;
        int result = 0;
        for (int i=0; i<networkOutput.length;i++){
            if (networkOutput[i] > max ){
                max = networkOutput[i];
                result = i;
            }
        }

        return result;
    }

    public static String getGenreString(double[] networkOutput){

        switch (getGenreInt(networkOutput)){
            case 0:
                return "Classical music";
            case 1:
                return "Hip hop music";
            case 2:
                return "Jazz music";
            case 3 :
                return "Metal music";
            case 4:
                return "Pop music";
            case 5:
                return "Rock music";

        }
        return null;
    }



    public static double[] test(String path) throws FileNotFoundException {

        NeuralNetwork alex = NeuralNetwork.createFromFile(path);
        Scanner sc = new Scanner(new File(FILE_PATH_TEST));
        double [] find = new double[GENRE];
        double [] all = new double[GENRE];
        while ( sc.hasNext()){


            double [] param = new double[PARAM];
            for(int i=0; i<PARAM; i++){
                param[i] = sc.nextDouble();
            }
            int expected = sc.nextInt();
            all[expected] += 1 ;
            alex.setInput(param);
            alex.calculate();
            double [] networkOutput = alex.getOutput();
            int result = getGenreInt(networkOutput);
            if (result == expected) find[expected] +=1;
        }

        double [] result = new double[GENRE];
        for (int i=0; i<GENRE; i++){
            result[i]=find[i]/all[i]*100;
        }
        return result;

    }

    public static void createInputData() throws IOException, UnsupportedAudioFileException {
        String path ="C:\\Users\\volkov\\Desktop\\genres";
        Processing p = new Processing();
        createData(path,FILE_PATH_TRAINING,p,0,90);
        createData(path,FILE_PATH_TEST,p,90,100);
    }

    public static void createData(String sourcePath,String savePath, Processing p,int start,int end) throws IOException, UnsupportedAudioFileException {
        PrintWriter pw = new PrintWriter(savePath);

        for (int j=start; j<end; j++){
            for (int i=0; i<GENRE; i++){
                Float[] a = p.getParameters(sourcePath+"\\1"+i+"\\"+j+".wav");
                for(int k=0; k<a.length; k++){
                    a[k] = a[k]*100;
                    pw.print(a[k].toString().replace('.',',')+" ");
                }
                pw.println(i);
            }
        }
        pw.close();

    }

    public static void learn(int[] x,String name) throws FileNotFoundException {
        NeuralNetwork alex = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,x);
        DataSet trainingSet = getDataSet(PARAM,GENRE);
        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setLearningRate(0.01);
        backPropagation.setMaxIterations(10000);
        alex.learn(trainingSet,backPropagation);
        alex.save(name);
    }
}