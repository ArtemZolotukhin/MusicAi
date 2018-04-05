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

    public static final int GENRE = 10;
    public static final int PARAM = 8;

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        Scanner sc = new Scanner(System.in);
        System.out.println("выберите режим: newData, learn, test, getGenre, findParam, crossValid");
        String mode = sc.next();

        String file_path_training = "training2.txt";
        String file_path_test = "test2.txt";

        if (mode.equals("findParam")){
            int bestFirstParam=0;
            int bestSecondParam=0;
            double maxSum=0;
            for (int i=2; i<=16; i++){
                for (int j=2; j<=16; j++){
                    int[] x = {PARAM,i,j,GENRE};
                    String name = "alex";
                    name = name + i + "_" + j + ".txt";
                    learn(x,name,file_path_training);

                    double localSum=0;
                    double[] result = test(name,file_path_test);
                    for (int k=0; k<GENRE; k++){
                        localSum +=result[k];
                    }
                    if (localSum>maxSum){
                        bestFirstParam=i;
                        bestSecondParam=j;
                        maxSum = localSum;
                    }
                    System.out.println(i+" "+j+" = "+localSum/GENRE+"%");
                }
            }
            System.out.println(bestFirstParam+" and "+bestSecondParam+" have "+maxSum/GENRE+"%");

        }
        else work(mode,file_path_training,file_path_test);





    }

    public static void work(String mode,String file_path_training,String file_path_test) throws IOException, UnsupportedAudioFileException {
        if (mode.equals("crossValid")){
            int n=10;
            double[] sum= new double[GENRE];
            for (int k=0; k<GENRE; k++) {
                sum[k] =0;
            }
            for (int i=0; i<n; i++){
                int[] x = {PARAM,15,15,GENRE};
                String name = "alex" + i +  ".txt";
                file_path_training = "cvTraining" + i +  ".txt";
                file_path_test = "cvTest" + i +  ".txt";
                //createInputData(file_path_training,file_path_test,i*10,i*10+10);
                //learn(x,name,file_path_training);

                double[] result = test(name,file_path_test);
                for (int k=0; k<GENRE; k++) {
                    sum[k] += result[k];
                }
            }
            for (int k=0; k<GENRE; k++) {
                sum[k] =sum[k]/n;
                System.out.println(sum[k]+"%");
            }

        }
        if (mode.equals("newData")){
            createInputData(file_path_training,file_path_test);

        }

        if (mode.equals("learn")){
            learn(new int[]{PARAM,15,15,GENRE},"alex2.txt",file_path_training);

        }


        if (mode.equals("test")){
            double [] result = test("alex2.txt",file_path_test);
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

    public static DataSet getDataSet(int x, int y, String file_path_training)  throws FileNotFoundException {
        DataSet trainingSet = new DataSet(x,y);
        Scanner sc = new Scanner(new File(file_path_training));


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
            case 6:
                return "Blues music";
            case 7 :
                return "Country music";
            case 8:
                return "Disco music";
            case 9:
                return "Reggae music";


        }
        return null;
    }



    public static double[] test(String path, String file_path_test) throws FileNotFoundException {

        NeuralNetwork alex = NeuralNetwork.createFromFile(path);
        Scanner sc = new Scanner(new File(file_path_test));
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

    public static void createInputData(String file_path_training,String file_path_test) throws IOException, UnsupportedAudioFileException {
        createInputData(file_path_training,file_path_test,90,100);
    }
    public static void createInputData(String file_path_training,String file_path_test,int start,int end) throws IOException, UnsupportedAudioFileException {
        String path ="C:\\Users\\volkov\\Desktop\\genres";
        //amount of music
        int n=100;

        Processing p = new Processing();
        String [] result = createData(path,p,n);
        PrintWriter pw = new PrintWriter(file_path_training);
        for (int i=0; i<start*GENRE; i++){
            pw.println(result[i]);
        }
        for (int i=end*GENRE; i<n*GENRE; i++){
            pw.println(result[i]);
        }
        pw.close();

        pw = new PrintWriter(file_path_test);
        for (int i=start*GENRE; i<end*GENRE; i++){
            pw.println(result[i]);
        }
        pw.println();
        pw.close();
    }

    public static String[]  createData(String sourcePath, Processing p,int n) throws IOException, UnsupportedAudioFileException {

        String [] result = new String[n*GENRE];
        for (int j=0; j<100; j++){
            for (int i=0; i<GENRE; i++){
                int number = j*GENRE+i;
                result[number] ="";
            }
        }
        for (int j=0; j<100; j++){
            for (int i=0; i<GENRE; i++){
                int number = j*GENRE+i;
                Float[] a = p.getParameters(sourcePath+"\\1"+i+"\\"+j+".wav");
                for(int k=0; k<a.length; k++){
                    a[k] = a[k]*100;
                    result[number] += a[k].toString().replace('.',',')+" ";
                }
                result[number] += i;
            }
        }
        return result;

    }

    public static void learn(int[] x,String name,String file_path_training) throws FileNotFoundException {
        NeuralNetwork alex = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,x);
        DataSet trainingSet = getDataSet(PARAM,GENRE,file_path_training);
        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setLearningRate(0.01);
        backPropagation.setMaxIterations(10000);
        alex.learn(trainingSet,backPropagation);
        alex.save(name);
    }
}