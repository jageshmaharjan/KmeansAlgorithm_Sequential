import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jagesh on 04/20/2016.
 */
public class KMeans2
{

    public static void main(String[] args) throws IOException
    {
        KMeans2 km2 = new KMeans2();
        km2.readFile();
    }

    public void readFile() throws IOException
    {
        String path1 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\input\\kddcup.data_10_percent_corrected";
        String path2 = "C:\\Users\\jagesh\\IdeaProjects\\KMean\\input\\kddcup.testdata.unlabeled_10_percent";
        File file = new File(path2);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        Float[][] records = new Float[41][];
        int j=0;
        while ((line = br.readLine()) != null)
        {
            String[] splitstr = line.split(",");
            Float[] col = new Float[splitstr.length];

            for (int i = 0; i<splitstr.length;i++)
            {
                if (i == 1)
                {
                    col[i] = getFirstColValue(splitstr[i]);
                    records[i][j] = getFirstColValue(splitstr[i]);
                }
                else if (i == 2)
                {
                    col[i] = getSecondColValue(splitstr[i]);
                    records[i][j] = getFirstColValue(splitstr[i]);
                }
                else if (i == 3)
                {
                    col[i] = getThirdColValue(splitstr[i]);
                    records[i][j] = getFirstColValue(splitstr[i]);
                }
                else
                    col[i] = Float.valueOf(1);
                    records[i][j] = getFirstColValue(splitstr[i]);
            }
            j++;

            System.out.println(Arrays.deepToString(records));
        }

        br.close();


    }

    private Float getFirstColValue(String s)
    {
        Map<String,Float> map = new HashMap<String, Float>();
        map.put("icmp", Float.valueOf(1));
        map.put("tcp", Float.valueOf(2));
        map.put("udp", Float.valueOf(3));

        Float val = null;
        for (Map.Entry entry : map.entrySet())
        {
            if (s.equals(entry.getKey()))
                val = (Float) entry.getValue();
        }

        return val;
    }

    private Float getSecondColValue(String s)
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

        Float val = null;
        for(Map.Entry<String,Float> entry : map.entrySet())
        {
            if (s.equals(entry.getKey()))
                val = entry.getValue();
        }

        return val;
    }

    private Float getThirdColValue(String s)
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


        Float val = null;
        for (Map.Entry<String,Float> entry : map.entrySet())
        {
            if (s.equals(entry.getKey()))
                val = entry.getValue();
        }

        return val;
    }
}
