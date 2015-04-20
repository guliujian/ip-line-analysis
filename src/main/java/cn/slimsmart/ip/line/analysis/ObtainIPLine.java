package cn.slimsmart.ip.line.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ObtainIPLine {

	public static void main(String[] args) {
		File srcFile = new File("src/main/resources/cn-ipv4");
		File desFile = new File("src/main/resources/ip-line");
		analysis(srcFile, desFile);
	}

	public static void analysis(File srcFile, File desFile) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(srcFile));
			BufferedWriter wr = new BufferedWriter(new FileWriter(desFile));
			String line = null;
			String ip = null;
			while ((line = br.readLine()) != null) {
				if (!line.trim().equals("")) {
					String[] strArr = line.split("\\|");
					ip = strArr[3];
					String[] aa = getIpLine(ip);
					if(aa==null){
						wr.write(ip+"   "+strArr[4]);
					}else{
						System.out.println(ip+"   "+strArr[4]+"   "+aa[0]+"   "+aa[1]);
						wr.write(ip+"   "+strArr[4]+"   "+aa[0]+"   "+aa[1]);
					}
				}
				wr.newLine();
				wr.flush();
			}
			wr.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String[] getIpLine(String ip) {
		String content = null;
		try {
			URL url = new URL("http://wq.apnic.net/whois-search/query?searchtext=" + ip);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			content = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(content!=null){
			String q1 = "{\"name\":\"netname\",\"values\":[\"";
			int start = content.indexOf(q1);
			int end = content.indexOf("\"]},{\"",start+ q1.length());
			String netname = content.substring(start+ q1.length(), end);
			String q2 = "{\"name\":\"mnt-lower\",\"values\":[\"";
			start = content.indexOf(q2);
			end = content.indexOf("\"]},{\"",start+ q2.length());
			String mntLower = content.substring(start+ q2.length(), end);
			String[] ss = {netname,mntLower};
			return ss;
		}
		return null;
	}
}
