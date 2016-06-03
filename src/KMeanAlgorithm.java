import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by jagesh on 04/20/2016.
 * Implementation of K-Means algorithm for K=2 (Two Clusters) and K=5 (Five Clusters)
 */
public class KMeanAlgorithm
{
    public KMeanAlgorithm()
    {
        mapFirstCol = mapFirstData();
        mapSecondCol = mapSecondData();
        mapThirdCol = mapThirdData();
    }
    int cols = 41;    //Matrix Dimension for no.s of Columns
    int rows;    //Matrix Dimension for no.s of Rows

    public static Map<String,Float> mapFirstCol = new HashMap<String, Float>();
    public static Map<String,Float> mapSecondCol = new HashMap<String, Float>();
    public static Map<String,Float> mapThirdCol = new HashMap<String, Float>();

    public static String path1 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\input\\kddcup.data_10_percent_corrected";
    public static String path2 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\input\\kddcup.testdata.unlabeled_10_percent";
    public static String path3 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\input\\kddcup.testdata.unlabeled";

    public static void main(String[] args) throws IOException
    {
        KMeanAlgorithm kmean = new KMeanAlgorithm();
        //ts1.getmatrixDimenstion();
        double st = System.currentTimeMillis();
        kmean.readFile(new File(args[1]));
        System.out.println("Total Execution Time: " + (System.currentTimeMillis() - st));
    }

    public void getmatrixDimenstion() throws IOException
    {
        File file = new File(path2);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String l;

        line = br.readLine();
        String[] split = line.split(",");
        cols = split.length;

        //readFile(cols,file);
    }

    public void readFile(File file) throws IOException
    {
        Long starttime = System.currentTimeMillis();

        Set<String> set = new HashSet<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        int j=0;
        while ((line = br.readLine()) != null)
        {
            set.add(line);
        }
        br.close();
        rows = set.size();
        Float[][] records = new Float[set.size()][cols];
        for (String setrec : set)
        {
            String[] splitstr = setrec.split(",");
            Float[] col = new Float[splitstr.length];

            for (int i = 0; i<cols;i++)
            {
                if (i == 1)
                {   //col[i] = getFirstColValue(splitstr[i]);
                    records[j][i] = getFirstColValue(splitstr[i]);
                }
                else if (i == 2)
                {   //col[i] = getSecondColValue(splitstr[i]);
                    records[j][i] = getSecondColValue(splitstr[i]);
                }
                else if (i == 3)
                {   //col[i] = getThirdColValue(splitstr[i]);
                    records[j][i] = getThirdColValue(splitstr[i]);
                }
                else
                    records[j][i] = Float.valueOf(splitstr[i]);
            }
            j++;
        }

        //Performing Data Normalization (May required or May not Required)
        Float[][] normalizedRec = normalizeRecords(records);

        System.out.println("Data I/O Load time: " + (System.currentTimeMillis() - starttime));
        System.out.println("Total records: " + records.length);

        double k2_t1 = System.currentTimeMillis();
//        evaluateK2MeanOperation(normalizedRec,rows,cols);
        evaluateK5MeanOperation(normalizedRec,rows,cols);
        System.out.println("Total execution time for Kmean(k=5) operation: " + (System.currentTimeMillis() - k2_t1));

        //evaluateK2MeanOperation(records,rows,cols);
        //evaluateK5MeanOperation(records,rows,cols);
    }

    /*
    Performing Normalization on the given record set
     */
    private Float[][] normalizeRecords(Float[][] records)
    {
        Float[][] normalizedRec = new Float[rows][cols];
        for (int i=0;i<rows;i++)
        {
            float magnitude = calculateMagnitude(records[i]);
            for (int j=0;j<cols;j++)
            {
                normalizedRec[i][j] = records[i][j]/magnitude;
            }
        }
        return normalizedRec;
    }

    /*
    To calculate the magnitude of each record in the DataSet
     */
    public float calculateMagnitude(Float[] rec)
    {
        float quantity =0;
        for (int i=0;i<cols;i++)
        {
            quantity += Math.pow(rec[i],2);
        }
        return (float) Math.sqrt(quantity);
    }

    /*
    This method will evaluate the K-mean clustering for the K=2, provided dataset with all dimension and no.s of records
     */
    public void evaluateK2MeanOperation(Float[][] records, int rows, int cols)
    {
        //For k Cluster = 2
        Float[] centroid1 = new Float[cols];
        Float[] centroid2 = new Float[cols];
        Float[] newCentroid1 = new Float[cols];
        Float[] newCentroid2 = new Float[cols];
        ArrayList<Float[]> cluster1 = new ArrayList<>();
        ArrayList<Float[]> cluster2 = new ArrayList<>();

        Random r = new Random();
        int randomNumber1 = r.nextInt(rows - 1) + 1;
        for (int i=0;i<cols;i++)
        {
            centroid1[i] = records[randomNumber1][i];
        }
        int randomNumber2 = r.nextInt(randomNumber1 -1) +1;
        for (int i=0;i<cols;i++)
        {
            centroid2[i] = records[randomNumber2][i];
        }

        while (true)
        {
            cluster1.clear();
            cluster2.clear();

            float distance1,distance2;
            for (int i=0;i<rows;i++)
            {
                distance1 = findDistance(centroid1, records[i]);
                distance2 = findDistance(centroid2,records[i]);
                if (distance1 <= distance2)
                    cluster1.add(records[i]);
                else
                    cluster2.add(records[i]);
            }

            newCentroid1 = calculateNewCentroid(cluster1);
            newCentroid2 = calculateNewCentroid(cluster2);

            if (!(Arrays.equals(centroid1,newCentroid1) && Arrays.equals(centroid2,newCentroid2)))
            {
                centroid1 = newCentroid1;
                centroid2 = newCentroid2;
            }
            else
                break;

        }

        System.out.println("Cluster size 1: " + cluster1.size());
        System.out.println("Cluster size 2: " + cluster2.size());
        clusterwriteToFile(cluster1,cluster2);
    }

    /*
    This method will evaluate the K-mean clustering for the K=5, provided dataset with all dimension and no.s of records
    */
    public void evaluateK5MeanOperation(Float[][] records,int rows,int cols)
    {
        //For k Cluster = 5
        Float[] centroid1 = new Float[cols];
        Float[] centroid2 = new Float[cols];
        Float[] centroid3 = new Float[cols];
        Float[] centroid4 = new Float[cols];
        Float[] centroid5 = new Float[cols];
        Float[] newCentroid1 = new Float[cols];
        Float[] newCentroid2 = new Float[cols];
        Float[] newCentroid3 = new Float[cols];
        Float[] newCentroid4 = new Float[cols];
        Float[] newCentroid5 = new Float[cols];
        ArrayList<Float[]> cluster1 = new ArrayList<Float[]>();
        ArrayList<Float[]> cluster2 = new ArrayList<Float[]>();
        ArrayList<Float[]> cluster3 = new ArrayList<Float[]>();
        ArrayList<Float[]> cluster4 = new ArrayList<Float[]>();
        ArrayList<Float[]> cluster5 = new ArrayList<Float[]>();

        Random r = new Random();
        int randomNumber1 = r.nextInt(rows - 1) + 1;
        for (int i=0;i<cols;i++)
        {
            centroid1[i] = records[randomNumber1][i];
        }
        int randomNumber2 = r.nextInt(randomNumber1 -1) +1;
        for (int i=0;i<cols;i++)
        {
            centroid2[i] = records[randomNumber2][i];
        }
        int randomNumber3 = r.nextInt(randomNumber2 - 1) +1;
        for (int i=0;i<cols;i++)
        {
            centroid3[i] = records[randomNumber3][i];
        }
        int randomNumber4 = r.nextInt(randomNumber3 -1) +1;
        for (int i=0;i<cols;i++)
        {
            centroid4[i] = records[randomNumber4][i];
        }
        int randomeNumber5 = r.nextInt(randomNumber4 - 1) +1;
        for (int i=0;i<cols;i++)
        {
            centroid5[i] = records[randomeNumber5][i];
        }

        while(true)
        {
            cluster1.clear();
            cluster2.clear();
            cluster3.clear();
            cluster4.clear();
            cluster5.clear();

            float distance1, distance2, distance3, distance4, distance5;
            for (int i = 0; i < rows; i++) {
                distance1 = findDistance(centroid1, records[i]);
                distance2 = findDistance(centroid2, records[i]);
                distance3 = findDistance(centroid3, records[i]);
                distance4 = findDistance(centroid4, records[i]);
                distance5 = findDistance(centroid5, records[i]);

                float leastDistance = findLeastDistance(distance1, distance2, distance3, distance4, distance5);
                if (leastDistance == distance1)
                    cluster1.add(records[i]);
                else if (leastDistance == distance2)
                    cluster2.add(records[i]);
                else if (leastDistance == distance3)
                    cluster3.add(records[i]);
                else if (leastDistance == distance4)
                    cluster4.add(records[i]);
                else if (leastDistance == distance5)
                    cluster5.add(records[i]);

            }

            newCentroid1 = calculateNewCentroid(cluster1);
            newCentroid2 = calculateNewCentroid(cluster2);
            newCentroid3 = calculateNewCentroid(cluster3);
            newCentroid4 = calculateNewCentroid(cluster4);
            newCentroid5 = calculateNewCentroid(cluster5);

            if (!(Arrays.equals(centroid1, newCentroid1) && Arrays.equals(centroid2, newCentroid2) && Arrays.equals(centroid3, newCentroid3) &&
                    Arrays.equals(centroid4, newCentroid4) && Arrays.equals(centroid5, newCentroid5)))
            {
                centroid1 = newCentroid1;
                centroid2 = newCentroid2;
                centroid3 = newCentroid3;
                centroid4 = newCentroid4;
                centroid5 = newCentroid5;
            } else
                break;

        }
        System.out.println("cluster size 1: " + cluster1.size());
        System.out.println("cluster size 2: " +cluster2.size());
        System.out.println("cluster size 3: " +cluster3.size());
        System.out.println("cluster size 4: " +cluster4.size());
        System.out.println("cluster size 5: " +cluster5.size());

        clusterwriteToFile(cluster1,cluster2,cluster3,cluster4,cluster5);
    }

    /*
    calculate the least distance between the given distances
     */
    public float findLeastDistance(float distance1,float distance2,float distance3,float distance4,float distance5)
    {
        float leastval = 0;
        if (distance1 < distance2 && distance1<distance3 && distance1<distance4 && distance1<distance5)
            leastval = distance1;
        else if (distance2 < distance1 && distance2<distance3 && distance2<distance4 && distance2<distance5)
            leastval = distance2;
        else if (distance3 < distance1 && distance3<distance2 && distance3<distance4 && distance3<distance5)
            leastval = distance3;
        else if (distance4 < distance1 && distance4<distance2 && distance4<distance3 && distance4<distance5)
            leastval = distance4;
        else if (distance5 < distance1 && distance5<distance2 && distance5<distance4 && distance5<distance4)
            leastval = distance5;

        return leastval;

    }

    /*
    Calculate the Euclidian Distance between two Data-Points.
     */
    private float findDistance(Float[] centroid1, Float[] record)
    {
        float distanceResult = 0;
        for (int i=0;i<centroid1.length;i++)
        {
            //System.out.println(record[i] + " , " + centroid1[i]);
            distanceResult += Math.pow((centroid1[i]) - ((record[i])),2);
        }
        return (float) Math.sqrt(distanceResult);
    }

    /*
    Calculate the new Centroid from the supplied cluster
     */
    private Float[] calculateNewCentroid(ArrayList<Float[]> cluster)
    {
        Float[] centroid = new Float[cols];
        for (int i=0; i<cols; i++)
        {
            float sum=0;
            for (int j=0;j<cluster.size();j++)
            {
                sum += cluster.get(j)[i];
            }
            centroid[i] = sum/cluster.size();
        }

        return centroid;
    }

    /*
    This method will write the content of cluster into the file (2 clusters)
     */
    public void clusterwriteToFile(ArrayList<Float[]> cluster1,ArrayList<Float[]> cluster2)
    {
        String savepath1 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\clusterOutput\\Kmean2cluster1.txt";
        String savepath2 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\clusterOutput\\Kmean2cluster2.txt";

        try
        {
            File file1 = new File(savepath1);
            File file2 = new File(savepath2);
            file1.delete();
            file2.delete();
            if (!file1.exists())
                file1.createNewFile();
            if (!file2.exists())
                file2.createNewFile();

            FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
            FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
            BufferedWriter bw1 = new BufferedWriter(fw1);
            BufferedWriter bw2 = new BufferedWriter(fw2);
            for (Float[] content : cluster1)
            {
                bw1.write(Arrays.toString(content));
                bw1.write("\n");
            }
            bw1.close();
            for (Float[] content : cluster2)
            {
                bw2.write(Arrays.toString(content));
                bw2.write("\n");
            }
            bw2.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
    This method will write the content of cluster into the file (5 clusters)
     */
    public void clusterwriteToFile(ArrayList<Float[]> c1,ArrayList<Float[]> c2,ArrayList<Float[]> c3,ArrayList<Float[]> c4,ArrayList<Float[]> c5)
    {
        String savepath1 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\clusterOutput\\Kmean5C1.txt";
        String savepath2 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\clusterOutput\\Kmean5C2.txt";
        String savepath3 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\clusterOutput\\Kmean5C3.txt";
        String savepath4 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\clusterOutput\\Kmean5C4.txt";
        String savepath5 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\clusterOutput\\Kmean5C5.txt";

        try
        {
            File file1 = new File(savepath1);
            File file2 = new File(savepath2);
            File file3 = new File(savepath3);
            File file4 = new File(savepath4);
            File file5 = new File(savepath5);
            file1.delete();
            file2.delete();
            file3.delete();
            file4.delete();
            file5.delete();
            FileWriter fw1 = new FileWriter(file1);
            FileWriter fw2 = new FileWriter(file2);
            FileWriter fw3 = new FileWriter(file3);
            FileWriter fw4 = new FileWriter(file4);
            FileWriter fw5 = new FileWriter(file5);
            BufferedWriter bw1 = new BufferedWriter(fw1);
            BufferedWriter bw2 = new BufferedWriter(fw2);
            BufferedWriter bw3 = new BufferedWriter(fw3);
            BufferedWriter bw4 = new BufferedWriter(fw4);
            BufferedWriter bw5 = new BufferedWriter(fw5);
            for (Float[] content : c1)
            {
                bw1.write(Arrays.toString(content));
                bw1.write("\n");
            }
            bw1.close();
            for (Float[] content : c2)
            {
                bw2.write(Arrays.toString(content));
                bw2.write("\n");
            }
            bw2.close();
            for (Float[] content : c3)
            {
                bw3.write(Arrays.toString(content));
                bw3.write("\n");
            }
            bw3.close();
            for (Float[] content : c4)
            {
                bw4.write(Arrays.toString(content));
                bw4.write("\n");
            }
            bw4.close();
            for (Float[] content : c5)
            {
                bw5.write(Arrays.toString(content));
                bw5.write("\n");
            }
            bw5.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private Float getFirstColValue(String s)
    {
        Float val = null;
        for (Map.Entry entry : mapFirstCol.entrySet())
        {
            if (s.equals(entry.getKey()))
                val = (Float) entry.getValue();
        }

        return val;
    }

    private Float getSecondColValue(String s)
    {
        Float val = null;
        for(Map.Entry<String,Float> entry : mapSecondCol.entrySet())
        {
            if (s.equals(entry.getKey()))
                val = entry.getValue();
        }

        return val;
    }

    private Float getThirdColValue(String s)
    {
        Float val = null;
        for (Map.Entry<String,Float> entry : mapThirdCol.entrySet())
        {
            if (s.equals(entry.getKey()))
                val = entry.getValue();
        }

        return val;
    }

    public static Map<String,Float> mapFirstData()
    {
        Map<String,Float> map = new HashMap<String, Float>();
        map.put("icmp", Float.valueOf(1));
        map.put("tcp", Float.valueOf(2));
        map.put("udp", Float.valueOf(3));

        return map;
    }

    public static Map<String,Float> mapSecondData()
    {
        Map<String,Float> map = new HashMap<String, Float>();
        map.put("aol", Float.valueOf(1));
        map.put("auth", Float.valueOf(2));
        map.put("bgp", Float.valueOf(3));
        map.put("courier", Float.valueOf(4));
        map.put("csnet_ns", Float.valueOf(5));
        map.put("ctf", Float.valueOf(6));
        map.put("daytime", Float.valueOf(7));
        map.put("discard",Float.valueOf(8));
        map.put("domain",Float.valueOf(9));
        map.put("domain_u",Float.valueOf(10));
        map.put("echo",Float.valueOf(11));
        map.put("eco_i",Float.valueOf(12));
        map.put("ecr_i",Float.valueOf(13));
        map.put("efs",Float.valueOf(14));
        map.put("exec",Float.valueOf(15));
        map.put("finger",Float.valueOf(16));
        map.put("ftp",Float.valueOf(17));
        map.put("ftp_data",Float.valueOf(18));
        map.put("gopher",Float.valueOf(19));
        map.put("harvest",Float.valueOf(20));
        map.put("hostnames",Float.valueOf(21));
        map.put("http",Float.valueOf(22));
        map.put("http_2784",Float.valueOf(23));
        map.put("http_443",Float.valueOf(24));
        map.put("http_8001",Float.valueOf(25));
        map.put("imap4",Float.valueOf(26));
        map.put("IRC",Float.valueOf(27));
        map.put("iso_tsap",Float.valueOf(28));
        map.put("klogin",Float.valueOf(29));
        map.put("kshell",Float.valueOf(30));
        map.put("ldap",Float.valueOf(31));
        map.put("link",Float.valueOf(32));
        map.put("login",Float.valueOf(33));
        map.put("mtp",Float.valueOf(34));
        map.put("name",Float.valueOf(35));
        map.put("netbios_dgm",Float.valueOf(36));
        map.put("netbios_ns",Float.valueOf(37));
        map.put("netbios_ssn",Float.valueOf(38));
        map.put("netstat",Float.valueOf(39));
        map.put("nnsp",Float.valueOf(40));
        map.put("nntp",Float.valueOf(41));
        map.put("ntp_u",Float.valueOf(42));
        map.put("other",Float.valueOf(43));
        map.put("pm_dump",Float.valueOf(44));
        map.put("pop_2",Float.valueOf(45));
        map.put("pop_3",Float.valueOf(46));
        map.put("printer",Float.valueOf(47));
        map.put("private",Float.valueOf(48));
        map.put("remote_job",Float.valueOf(49));
        map.put("rje",Float.valueOf(50));
        map.put("shell",Float.valueOf(51));
        map.put("smtp",Float.valueOf(52));
        map.put("sql_net",Float.valueOf(53));
        map.put("ssh",Float.valueOf(54));
        map.put("sunrpc",Float.valueOf(55));
        map.put("supdup",Float.valueOf(56));
        map.put("systat",Float.valueOf(57));
        map.put("telnet",Float.valueOf(58));
        map.put("tftp_u",Float.valueOf(59));
        map.put("tim_i",Float.valueOf(60));
        map.put("time",Float.valueOf(61));
        map.put("urp_i",Float.valueOf(62));
        map.put("uucp",Float.valueOf(63));
        map.put("uucp_path",Float.valueOf(64));
        map.put("vmnet",Float.valueOf(65));
        map.put("whois",Float.valueOf(66));
        map.put("X11",Float.valueOf(67));
        map.put("Z39_50",Float.valueOf(68));
        map.put("urh_i",Float.valueOf(69));
        map.put("icmp",Float.valueOf(70));

        return map;
    }

    public static Map<String,Float> mapThirdData()
    {
        Map<String,Float> map = new HashMap<String, Float>();
        map.put("OTH",Float.valueOf(1));
        map.put("REJ",Float.valueOf(2));
        map.put("RSTO",Float.valueOf(3));
        map.put("RSTOS0",Float.valueOf(4));
        map.put("RSTR",Float.valueOf(5));
        map.put("S0",Float.valueOf(6));
        map.put("S1",Float.valueOf(7));
        map.put("S2",Float.valueOf(8));
        map.put("S3",Float.valueOf(9));
        map.put("SF",Float.valueOf(10));
        map.put("SH",Float.valueOf(11));

        return map;
    }
}
