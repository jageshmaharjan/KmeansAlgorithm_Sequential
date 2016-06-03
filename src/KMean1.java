import javax.xml.crypto.Data;
import java.util.ArrayList;

/**
 * Created by jagesh on 04/15/2016.
 */
public class KMean1
{
    private static final int NUM_CLUSTER = 2;
    private static final int TOTAL_DATA =7;

    private static final double SAMPLES[][] = new double[][]
            {   {1.0,1.0},
                {1.5,2.0},
                {3.0,4.0},
                {5.0,7.0},
                {3.5,5.0},
                {4.5,5.0},
                {3.5,4.5}
            };
    private static ArrayList<Data> dataset = new ArrayList<Data>();
    private static ArrayList<Centroid> centroids = new ArrayList<Centroid>();

    private static void initialize()
    {
        System.out.println("Centroids initialized at: ");
        centroids.add(new Centroid(1.0,1.0));
        centroids.add(new Centroid(5.0,7.0));
    }

    private static void KMeanCluster()
    {
        final double bigNumber = Math.pow(10,10);
        double minimum = bigNumber;
        double distance = 0.0;
        int samplenumber = 0;
        int cluster = 0;
        boolean isStillmoving = true;
        Data newData = null;

        while (dataset.size() < TOTAL_DATA)
        {
            newData = new Data(SAMPLES[samplenumber][0],SAMPLES[samplenumber][1]);
            dataset.add(newData);
            minimum = bigNumber;
            for (int i = 0; i<NUM_CLUSTER; i++)
            {
                distance = dist(newData,centroids.get(i));
                if (distance < minimum)
                {
                    minimum = distance;
                    cluster = i;
                }
            }
            newData.cluster(cluster);

            for (int i=0;i<NUM_CLUSTER; i++)
            {
                int totalX =0;
                int totalY=0;
                int totalInCluster = 0;
                for (int j=0;j<dataset.size();j++)
                {
                    if (dataset.get(j).cluster() == i)
                    {
                        totalX += dataset.get(j).X();
                        totalY += dataset.get(j).Y();
                        totalInCluster++;
                    }
                }
                if (totalInCluster > 0)
                {
                    centroids.get(i).X(totalX/totalInCluster);
                    centroids.get(i).Y(totalY/totalInCluster);
                }
            }
            samplenumber++;
        }

        while (isStillmoving)
        {
            for (int i =0 ; i<NUM_CLUSTER; i++)
            {
                int totalX =0;
                int totalY =0;
                int totalInCluster = 0;
                for (int j=0;j<dataset.size(); j++)
                {
                    if (dataset.get(j).cluster() == i)
                    {
                        totalX += dataset.get(j).X();
                        totalY += dataset.get(j).Y();
                        totalInCluster++;
                    }
                }
                if (totalInCluster > 0)
                {
                    centroids.get(i).X(totalX / totalInCluster);
                    centroids.get(i).Y(totalY / totalInCluster);
                }
            }

            isStillmoving = false;

            for (int i =0;i<dataset.size(); i++)
            {
                Data tempData = dataset.get(i);
                minimum = bigNumber;
                for (int j = 0; j < NUM_CLUSTER; j++)
                {
                    distance = dist(tempData, centroids.get(j));
                    if (distance < minimum)
                    {
                        minimum = distance;
                        cluster = j;
                    }
                }
                tempData.cluster(cluster);
                if (tempData.cluster() != cluster)
                {
                    tempData.cluster(cluster);
                    isStillmoving = true;
                }
            }
        }

        return;
    }

    private static double dist(Data d, Centroid c)
    {
        return Math.sqrt(Math.pow((c.Y() - d.Y()), 2) + Math.pow((c.X() - d.X()),2));
    }

    public static class Data
    {
        private double mX = 0;
        private double mY = 0;
        private int mCluster = 0;

        public Data()
        {
            return;
        }
        public Data(double x, double y)
        {
            this.X(x);
            this.Y(y);
            return;
        }
        public void X(double x)
        {
            this.mX = x;
            return;
        }
        public double X()
        {
            return this.mX;
        }
        public void Y(double y)
        {
            this.mY = y;
            return;
        }
        public double Y()
        {
            return this.mY;
        }
        public void cluster(int clusterNumber)
        {
            this.mCluster = clusterNumber;
            return;
        }
        public int cluster()
        {
            return this.mCluster;
        }
    }

    public static class Centroid
    {
        private double mX = 0.0;
        private double mY = 0.0;

        public Centroid(double newX, double newY)
        {
            this.mX = newX;
            this.mY = newY;
        }
        public void X(double newX)
        {
            this.mX = newX;
        }
        public double X()
        {
            return this.mX;
        }
        public void Y(double newY)
        {
            this.mY = newY;
        }
        public double Y()
        {
            return this.mY;
        }
    }

    public static void main(String[] args)
    {
        initialize();
        KMeanCluster();

        for (int i=0; i<NUM_CLUSTER; i++)
        {
            System.out.println("cluster " + i + "includes: ");
            for (int j = 0; j < TOTAL_DATA; j++)
            {
                if (dataset.get(j).cluster() == i)
                {
                    System.out.println("(" + dataset.get(j).X() + ", " + dataset.get(j).Y() + ")");
                }
            }
            System.out.println();
        }
        System.out.println("Centroids finalized at: ");
        for (int i=0;i<NUM_CLUSTER;i++)
        {
            System.out.println("(" + centroids.get(i).X() + ", "+centroids.get(i).Y()+")");
        }
        System.out.println("\n");
    }
}
