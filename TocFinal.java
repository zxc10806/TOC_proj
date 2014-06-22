/*
Name : 邱仲毅
Student ID: F74991722


 */
import org.json.*;
import java.net.*;
import java.io.*;
import java.util.*;
public class TocFinal
{
		public static int topk;
		public static int L;
		public static String [] findItems(URL url)
		{
				try{
						InputStream in = url.openStream();
						InputStreamReader r = new InputStreamReader(in,"UTF-8");
						BufferedReader br = new BufferedReader(r);
						StringBuffer m = new StringBuffer();
						String result = "";
						String tmp;
						while(true)
						{
								tmp =br.readLine();
								m.append(tmp);
								if(tmp.contains("}"))
										break;
						}
						result = m.toString();
						int j,k;
						j = 0;
						k = 0;
						for(int i = 0;i<result.length();i++)
						{
								if(result.charAt(i)=='{')
										j = i;
								if(result.charAt(i)=='}')
										k = i;
						}
						result =  result.substring(j+1,k);
						String []args = result.split(",");
						String []ans = new String[args.length];
						for(int i = 0;i<args.length;i++)
						{
								int ll,rr;
								rr = 0;
								if(args[i].contains("\""))
								{
										int count = 0;
										for(j=0;j<args[i].length();j++)
										{
												if(args[i].charAt(j)=='\"'&&count<2)
												{
														rr = j;
														count++;
												}
										}
										ans[i] = args[i].substring(1,rr);
								}
								else
								{
										for(j=0;j<args[i].length();j++)
										{
												if(args[i].charAt(j)==':')
												{
														ans[i] = args[i].substring(0,j);
												}
										}
								}
						}		
						return ans;
				}
				catch(Exception e)
				{
						System.out.println(e.getMessage());
				}
				return null;
		}
		public static void main(String [] args)
		{
				try
				{
						String sURL = new String(args[0]);
						topk = Integer.valueOf(args[1]);
						L  = Integer.valueOf(args[2]);
						URL url = new URL(sURL);
						HttpURLConnection request = (HttpURLConnection) url.openConnection();
						request.setRequestProperty("Accept-Charset", "UTF-8");
						request.connect();
						JSONTokener jt = new JSONTokener(new InputStreamReader((InputStream)request.getContent()));
						JSONArray rootobj = new JSONArray(jt);
						JSONObject first = rootobj.getJSONObject(0);
						request.connect();
						String [] test = findItems(url);
						for(int i = 0;i<test.length;i++)
								System.out.println(test[i]);
				}
				catch(Exception e)
				{
						System.out.println(e.getMessage());
				}
		}
}
