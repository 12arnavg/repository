package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/*
 * author: Arnav Gupta
 * version: May 5, 2020
 * description: Implements an A-B-C-D perceptron, containing one input layer, two hidden layers, and one output layer
 * Can set up and configure the weights, training set, hyperparameters, and the number of activations in each layer
 * Can both run the network and train the network using a feed-forward algorithm and a backpropagation algorithm
 * 
 * contents:
 * public Perceptron()
 * public void setUpStandardNetworkDimensions()
 * public void setUpCustomNetworkDimensions()
 * public void setUpStandardWeights()
 * public void setUpCustomWeights()
 * public void setUpRandomWeights()
 * public double findRandomDouble(int minBound, int maxBound)
 * public void setUpStandardTrainingSet()
 * public void setUpCustomTrainingSet()
 * public void setUpTrainingSet(String fileName)
 * public void setUpHyperparameters()
 * public void setUpNetworkArrays()
 * public void runNetwork()
 * public void trainNetwork()
 * public double evaluateNetwork(int x)
 * public void trainNetworkForOneIteration()
 * public double threshold(int input)
 * public double thresholdDerivative(int input)
 * public void printHyperparameters()
 * public void printNetworkDimensions()
 * public void printTrainingSetOutputs()
 * public void printWeightsToFile()
 * public static void main(String[] args)
 */
public class Perceptron
{
   int numActivationsM;
   int numActivationsK;
   int numActivationsJ;
   int numActivationsI;

   double[][] weightsMK;
   double[][] weightsKJ;
   double[][] weightsJI;

   int trainingSetLength;
   double[][] trainingSetInputs;
   double[][] trainingSetOutputs;

   double weightsMinBound;
   double weightsMaxBound;
   double learningFactor;
   double errorThreshold;
   int maxIterations;
   
   Random random;

   double[] am;
   double[] Θk;
   double[] ak;
   double[] Θj;
   double[] aj;
   double[] Θi;
   double[] ai;

   double[] ωi;
   double[] ψi;
   
   double[] Ωj;
   double[] ψj;
   double[] Ωk;
   double[] ψk;

/*
 * description: Sets up and runs the perceptron
 * Decides whether to run the network or train the network based on user input
 * Decides whether to run the standard setup or the custom setup based on user input
 */
   public Perceptron()
   {
      random = new Random();

      Scanner scanner = new Scanner(System.in);
      System.out.println("Do you want to run the network or train the network? (run/train)");
      String userInput = scanner.nextLine();

      if (userInput.equals("run"))
      {
         System.out.println("Do you want to run the standard setup or the custom setup? (standard/custom)");
         userInput = scanner.nextLine();

         if (userInput.equals("standard"))
         {
            setUpStandardNetworkDimensions();
            setUpStandardWeights();
            setUpStandardTrainingSet();
         }
         else if (userInput.equals("custom"))
         {
            setUpCustomNetworkDimensions();
            setUpCustomWeights();
            setUpCustomTrainingSet();
         }

         setUpNetworkArrays();
         runNetwork();
      } // if (userInput.equals("run"))
      else if (userInput.equals("train"))
      {
         setUpHyperparameters();
         setUpCustomNetworkDimensions();

         setUpCustomTrainingSet();
         setUpRandomWeights();

         setUpNetworkArrays();
         trainNetwork();
      } // else if (userInput.equals("train"))

   } // public Perceptron()

/*
 * description: Sets up the network dimensions for a standard 2-2-2-2 network
 */
   public void setUpStandardNetworkDimensions()
   {
      numActivationsM = 2;
      numActivationsK = 2;
      numActivationsJ = 2;
      numActivationsI = 2;
   }

/*
 * description: Sets up the network dimensions, asking the user for the dimensions' values
 */
   public void setUpCustomNetworkDimensions()
   {
      System.out.println("\n" + "Network Dimensions");
      Scanner scanner = new Scanner(System.in);

      System.out.println("Enter the number of activations in the M layer:");
      numActivationsM = scanner.nextInt();

      System.out.println("Enter the number of activations in the K layer:");
      numActivationsK = scanner.nextInt();

      System.out.println("Enter the number of activations in the J layer:");
      numActivationsJ = scanner.nextInt();

      System.out.println("Enter the number of activations in the I layer:");
      numActivationsI = scanner.nextInt();
   } // public void setUpCustomNetworkDimensions()

/*
 * description: Sets up the standard MK, KJ, and JI weights
 */
   public void setUpStandardWeights()
   {
      weightsMK = new double[2][2];
      weightsMK[0][0] = 2;
      weightsMK[0][1] = 3;
      weightsMK[1][0] = 4;
      weightsMK[1][1] = 5;

      weightsKJ = new double[2][2];
      weightsKJ[0][0] = 3;
      weightsKJ[0][1] = 2;
      weightsKJ[1][0] = 5;
      weightsKJ[1][1] = 4;

      weightsJI = new double[2][2];
      weightsJI[0][0] = 3;
      weightsJI[0][1] = 5;
      weightsJI[1][0] = 4;
      weightsJI[1][1] = 2;
   } // public void setUpStandardWeights()

/*
 * description: Sets up the MK, KJ, and JI weights, asking the user for the weights' values
 */
   public void setUpCustomWeights()
   {
      System.out.println("\n" + "Weights");
      Scanner scanner = new Scanner(System.in);

      weightsMK = new double[numActivationsM][numActivationsK];

      for (int m = 0; m < numActivationsM; m++)
      {

         for (int k = 0; k < numActivationsK; k++)
         {
            System.out.println("Enter W0" + Integer.toString(m) + Integer.toString(k) + ":");
            weightsMK[m][k] = scanner.nextDouble();
         }

      } // for (int m = 0; m < numActivationsM; m++)

      weightsKJ = new double[numActivationsK][numActivationsJ];

      for (int k = 0; k < numActivationsK; k++)
      {

         for (int j = 0; j < numActivationsJ; j++)
         {
            System.out.println("Enter W1" + Integer.toString(k) + Integer.toString(j) + ":");
            weightsKJ[k][j] = scanner.nextDouble();
         }

      } // for (int k = 0; k < numActivationsK; k++)

      weightsJI = new double[numActivationsJ][numActivationsI];

      for (int j = 0; j < numActivationsJ; j++)
      {

         for (int i = 0; i < numActivationsI; i++)
         {
            System.out.println("Enter W2" + Integer.toString(j) + Integer.toString(i) + ":");
            weightsJI[j][i] = scanner.nextDouble();
         }

      } // for (int j = 0; j < numActivationsJ; j++)

   } // public void setUpCustomWeights()

/*
 * description: Sets up the MK, KJ, and JI weights using random values between the weight's bounds
 */
   public void setUpRandomWeights()
   {
      weightsMK = new double[numActivationsM][numActivationsK];

      for (int m = 0; m < numActivationsM; m++)
      {

         for (int k = 0; k < numActivationsK; k++)
         {
            weightsMK[m][k] = findRandomDouble(weightsMinBound, weightsMaxBound);
         }

      }

      weightsKJ = new double[numActivationsK][numActivationsJ];

      for (int k = 0; k < numActivationsK; k++)
      {

         for (int j = 0; j < numActivationsJ; j++)
         {
            weightsKJ[k][j] = findRandomDouble(weightsMinBound, weightsMaxBound);
         }

      }

      weightsJI = new double[numActivationsJ][numActivationsI];

      for (int j = 0; j < numActivationsJ; j++)
      {

         for (int i = 0; i < numActivationsI; i++)
         {
            weightsJI[j][i] = findRandomDouble(weightsMinBound, weightsMaxBound);
         }

      }

   } // public void setUpRandomWeights()

/* 
 * description: Generates a random double between the two given bounds
 * param: minBound, the lower bound for the random double
 * param: maxBound, the upper bound for the random double
 * return: the generated random double
 */
   public double findRandomDouble(double minBound, double maxBound)
   {
      double randomDouble = (weightsMinBound + ((weightsMaxBound - weightsMinBound) * random.nextDouble()));

      return randomDouble;
   }

/*
 * description: Sets up the training set using the default training set file
 */
   public void setUpStandardTrainingSet()
   {
      setUpTrainingSet("standardTrainingSet.txt");
   }

/* 
 * description: Sets up the training set, asking the user for the training set file's name
 */
   public void setUpCustomTrainingSet()
   {
      System.out.println("\n" + "Training Set");
      Scanner scanner = new Scanner(System.in);

      System.out.println("Enter the name of the training set file:");
      String fileName = scanner.nextLine();
      setUpTrainingSet(fileName);
   }

/*
 * description: Sets up a training set using a file containing training set information
 * param: fileName, the name of the training set file
 */
   public void setUpTrainingSet(String fileName)
   {
      try
      {
         File file = new File(fileName);
         Scanner scanner = new Scanner(file);

         trainingSetLength = scanner.nextInt();
         scanner.nextLine();

         trainingSetInputs = new double[trainingSetLength][numActivationsM];

         for (int x = 0; x < trainingSetLength; x++)
         {
            String str = scanner.nextLine();
            String[] arr = str.split(" ");

            for (int m = 0; m < numActivationsM; m++)
            {
               trainingSetInputs[x][m] = Double.parseDouble(arr[m]);
            }

         } // for (int x = 0; x < trainingSetLength; x++)

         trainingSetOutputs = new double[trainingSetLength][numActivationsI];

         for (int x = 0; x < trainingSetLength; x++)
         {
            String str = scanner.nextLine();
            String[] arr = str.split(" ");

            for (int i = 0; i < numActivationsI; i++)
            {
               trainingSetOutputs[x][i] = Double.parseDouble(arr[i]);
            }

         } // for (int x = 0; x < trainingSetLength; x++)

      } // try
      catch (FileNotFoundException exception)
      {
         System.out.println("FileNotFoundException");
      }
   } // public void setUpTrainingSet(String fileName)

/*
 * description: Sets up the hyperparameters, asking the user for the hyperparameters’ values
 */
   public void setUpHyperparameters()
   {
      System.out.println("\n" + "Hyperparameters");
      Scanner scanner = new Scanner(System.in);

      System.out.println("Enter minimum bound for the weights: ");
      weightsMinBound = scanner.nextDouble();

      System.out.println("Enter maximum bound for the weights: ");
      weightsMaxBound = scanner.nextDouble();

      System.out.println("Enter the learning factor:");
      learningFactor = scanner.nextDouble();

      System.out.println("Enter the error threshold:");
      errorThreshold = scanner.nextDouble();

      System.out.println("Enter the maximum number of iterations:");
      maxIterations = scanner.nextInt();
   } // public void setUpHyperparameters()

/*
 * description: Sets up the arrays needed by the feed-forward and backpropagation algorithms
 */
   public void setUpNetworkArrays()
   {
      am = new double[numActivationsM];

      Θk = new double[numActivationsK];
      ak = new double[numActivationsK];

      Θj = new double[numActivationsJ];
      aj = new double[numActivationsJ];

      Θi = new double[numActivationsI];
      ai = new double[numActivationsI];

      ωi = new double[numActivationsI];
      ψi = new double[numActivationsI];

      Ωj = new double[numActivationsJ];
      ψj = new double[numActivationsJ];

      Ωk = new double[numActivationsK];
      ψk = new double[numActivationsK];
   } // public void setUpNetworkArrays()

/*
 * description: Runs the network for all training cases using a feed-forward algorithm
 */
   public void runNetwork()
   {
      System.out.println("\n" + "Outputs");

      for (int x = 0; x < trainingSetLength; x++)
      {
         evaluateNetwork(x);

         for (int i = 0; i < numActivationsI - 1; i++)
         {
            System.out.print(Double.toString(ai[i]) + " ");
         }

         System.out.print(Double.toString(ai[numActivationsI - 1]) + "\n");
      } // for (int x = 0; x < trainingSetLength; x++)

   } // public void runNetwork()

/*
 * description: Trains the network using a feed-forward algorithm and a backpropagation algorithm
 */
   public void trainNetwork()
   {
      int iterations = 0;
      double averageError = Double.MAX_VALUE;

      while (iterations < maxIterations && averageError >= errorThreshold)
      {
         double totalError = 0.0;

         for (int x = 0; x < trainingSetLength; x++)
         {
            totalError += evaluateNetwork(x);
            trainNetworkForOneIteration();
         }

         averageError = (totalError / (double)(trainingSetLength));
         iterations++;
      } // while (iterations < maxIterations && averageError >= errorThreshold)

      printHyperparameters();
      printNetworkDimensions();

      runNetwork();
      printTrainingSetOutputs();

      if (averageError < errorThreshold)
      {
         System.out.println("\n" + "Network Finished Training");
         System.out.println("Iterations: " + Integer.toString(iterations));

         printWeightsToFile();
      }
      else
      {
         System.out.println("\n" + "Training Timed Out");
      }

   } // public void trainNetwork()

/*
 * description: Evaluates the network for a specific training case using a feed forward algorithm
 * param: x, the training case for which to evaluate the network
 * return: the error found while running the network for a specific training case
 */
   public double evaluateNetwork(int x)
   {
      double error = 0.0;

      for (int m = 0; m < numActivationsM; m++)
      {
         am[m] = trainingSetInputs[x][m];
      }

      for (int i = 0; i < numActivationsI; i++)
      {
         Θi[i] = 0.0;

         for (int j = 0; j < numActivationsJ; j++)
         {
            Θj[j] = 0.0;

            for (int k = 0; k < numActivationsK; k++)
            {
               Θk[k] = 0.0;

               for (int m = 0; m < numActivationsM; m++)
               {
                  Θk[k] += am[m] * weightsMK[m][k];
               }

               ak[k] = threshold(Θk[k]);
               Θj[j] += (ak[k] * weightsKJ[k][j]);
            } // for (int k = 0; k < numActivationsK; k++)

            aj[j] = threshold(Θj[j]);
            Θi[i] += (aj[j] * weightsJI[j][i]);
         } // for (int j = 0; j < numActivationsJ; j++)

         ai[i] = threshold(Θi[i]);

         ωi[i] = trainingSetOutputs[x][i] - ai[i];
         ψi[i] = (ωi[i] * thresholdDerivative(Θi[i]));

         error += (0.5 * ωi[i] * ωi[i]);
      } // for (int i = 0; i < numActivationsI; i++)

      return error;
   } // public double evaluateNetwork(int x)

/*
 * description: Trains the network for one iteration using a backpropagation algorithm
 */
   public void trainNetworkForOneIteration()
   {
      for (int m = 0; m < numActivationsM; m++)
      {

         for (int k = 0; k < numActivationsK; k++)
         {
            Ωk[k] = 0.0;

            for (int j = 0; j < numActivationsJ; j++)
            {
               Ωj[j] = 0.0;

               for (int i = 0; i < numActivationsI; i++)
               {
                  Ωj[j] += (ψi[i] * weightsJI[j][i]);
                  weightsJI[j][i] += (learningFactor * aj[j] * ψi[i]);
               }

               ψj[j] = (Ωj[j] * thresholdDerivative(Θj[j]));
               Ωk[k] += (ψj[j] * weightsKJ[k][j]);
               weightsKJ[k][j] += (learningFactor * ak[k] * ψj[j]);
             } // for (int j = 0; j < numActivationsJ; j++)

             ψk[k] = (Ωk[k] * thresholdDerivative(Θk[k]));
             weightsMK[m][k] += (learningFactor * am[m] * ψk[k]);
          } // for (int k = 0; k < numActivationsK; k++)

      } // for (int m = 0; m < numActivationsM; m++)

   } // public void trainNetworkForOneIteration()

/*
 * description: the threshold function of the network
 * param: input, the input of the function
 * return: the output of the function
 */
   public double threshold(double input)
   {
      double output = (1.0 / (1.0 + Math.exp(input * -1.0)));

      return output;
   }

/*
 * description: the derivative of the threshold function
 * param: input, the input of the function
 * return: the output of the function
 */
   public double thresholdDerivative(double input)
   {
      double thresholdOutput = threshold(input);
      double output = (thresholdOutput * (1.0 - thresholdOutput));

      return output;
   }

/*
 * description: Prints the network’s hyperparameters
 */
   public void printHyperparameters()
   {
      System.out.println("\n" + "Hyperparameters");

      System.out.println("Weights Range: " + Double.toString(weightsMinBound) + " to " + Double.toString(weightsMaxBound));
      System.out.println("Learning Factor: " + Double.toString(learningFactor));

      System.out.println("Error Threshold: " + Double.toString(errorThreshold));
      System.out.println("Maximum Iterations: " + Integer.toString(maxIterations));
   } // public void printHyperparameters()

/*
 * description: Prints the network dimensions
 */
   public void printNetworkDimensions()
   {
      System.out.println("\n" + "Network Dimensions");

      System.out.println("Number of Activations in the M Layer: " + Integer.toString(numActivationsM));
      System.out.println("Number of Activations in the K Layer: " + Integer.toString(numActivationsK));

      System.out.println("Number of Activations in the J Layer: " + Integer.toString(numActivationsJ));
      System.out.println("Number of Activations in the I Layer: " + Integer.toString(numActivationsI));
   } // public void printNetworkDimensions()

/*
 * description: Prints the network’s training set outputs
 */
   public void printTrainingSetOutputs()
   {
      System.out.println("\n" + "Expected Outputs");

      for (int x = 0; x < trainingSetLength; x++)
      {

         for (int i = 0; i < numActivationsI - 1; i++)
         {
            System.out.print(Double.toString(trainingSetOutputs[x][i]) + " ");
         }

         System.out.print(Double.toString(trainingSetOutputs[x][numActivationsI - 1]) + "\n");
      } // for (int x = 0; x < trainingSetLength; x++)

   } // public void printTrainingSetOutputs()

/*
 * description: Creates a file, asking the user for the file's name
 * Prints the network dimensions and weights to the file
 */
   public void printWeightsToFile()
   {
      try 
      {
         System.out.println("\n" + "Saving Weights");
         Scanner scanner = new Scanner(System.in);

         System.out.println("Enter the name of the weights file:");
         String fileName = scanner.nextLine();

         File file = new File(fileName);
         FileWriter writer = new FileWriter(file, false);

         writer.write(Integer.toString(numActivationsM) + " " + Integer.toString(numActivationsK) + " "); 
         writer.write(Integer.toString(numActivationsJ) + " " + Integer.toString(numActivationsI) + "\n");

         for (int m = 0; m < numActivationsM; m++)
         {

            for (int k = 0; k < numActivationsK - 1; k++)
            {
               writer.write(Double.toString(weightsKJ[m][k]) + " ");
            }

            writer.write(Double.toString(weightsKJ[m][numActivationsK - 1]) + "\n");
         } // for (int m = 0; m < numActivationsM; m++)

         for (int k = 0; k < numActivationsK; k++)
         {

            for (int j = 0; j < numActivationsJ - 1; j++)
            {
               writer.write(Double.toString(weightsKJ[k][j]) + " ");
            }

            writer.write(Double.toString(weightsKJ[k][numActivationsJ - 1]) + "\n");
         } // for (int k = 0; k < numActivationsK; k++)

         for (int j = 0; j < numActivationsJ; j++)
         {

            for (int i = 0; i < numActivationsI - 1; i++)
            {
               writer.write(Double.toString(weightsJI[j][i]) + " ");
            }

            writer.write(Double.toString(weightsJI[j][numActivationsI - 1]) + "\n");
         } // for (int j = 0; j < numActivationsJ; j++)

         writer.close();
      } // try
      catch (IOException exception) 
      {
         System.out.println("IOException");
      }
   } // public void printWeightsToFile()

/*
 * description: the main method of the Perceptron class
 * param: args, the command line arguments of the main method
 */
   public static void main(String[] args)
   {
      Perceptron perceptron = new Perceptron();
   }
} // public class Peceptron