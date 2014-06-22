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
		public static String [] Field;
		public static HashMap<String,String> Count;
		public static class Info 
		{
				int [] ar;
				String value;
				public Info(String v)
				{
						value = new String(v);
						ar = new int[10];
						for(int i = 0;i<10;i++)
								ar[i] = -1;
				}
		}
		public static ArrayList<Info> All;
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
		public static int [] used;
		public static int [] FF;
		public static void comb(JSONObject o,int k,int s)
		{

				if(k==L)
				{
						try{
								String tmp="";
								String res;
								res = String.valueOf(o.get(Field[FF[0]]));
								tmp=Field[FF[0]]+":"+res;
								for(int i = 1;i<L;i++)
								{
										res = String.valueOf(o.get(Field[FF[i]]));
										tmp+=","+Field[FF[i]]+":"+res;
								}
								if(Count.get(tmp)==null)
								{
										Count.put(tmp,"1");
										Info test = new Info(tmp);
										for(int i = 0;i<L;i++)
												test.ar[i] = FF[i];
										All.add(test);
								}
								else
								{
										String n = Count.get(tmp);
										int a = Integer.valueOf(n);
										a++;
										Count.put(tmp,String.valueOf(a));
								}
						}
						catch(Exception e)
						{
						}
						return ;
				}

				for(int i=s;i<Field.length;i++)
						if(used[i]==0)
						{
								used[i] = 1;
								FF[k] = i;
								comb(o,k+1,i+1);
								used[i] = 0;
						}
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
						Field = findItems(url);//find Fields of JSONObject
						used = new int[Field.length];
						FF = new int[Field.length];
						All = new ArrayList();
						Count = new HashMap<String,String>();
						for(int i = 0;i<rootobj.length();i++)
						{
								JSONObject obj = rootobj.getJSONObject(i);
								for(int j = 0;j<Field.length;j++)
								{
										String  v = String.valueOf(obj.get(Field[j]));
								}
								Arrays.fill(used,0);
								comb(obj,0,0);
						}				
						Collections.sort(All,new Comparator(){
										public int compare(Object o1,Object o2)
										{
										Info i1 = (Info)o1;
										Info i2 = (Info)o2;
										int c1 = Integer.valueOf(Count.get(i1.value));
										int c2 = Integer.valueOf(Count.get(i2.value));
										if(c1!=c2)
										return c2-c1;
										for(int i = 0;i<L;i++)
										if(i2.ar[i]!=i1.ar[i])
										return i1.ar[i]-i2.ar[i]; 
										return 0;
										}
										});
						int tk = 0;
						String v,v1;
						v1 = "";
						for(int i = 0;i<All.size()&&tk<topk+1;i++)
						{
								if(tk==0)
								{
										v = All.get(i).value;
										System.out.println(v+";"+Count.get(v));
										v1 = new String(v);
										tk++;
								}
								else
								{
										v = All.get(i).value;
										if(Count.get(v).equals(Count.get(v1)))
										{
												
												if(tk<=topk)
												System.out.println(v+";"+Count.get(v));
												v1 = new String(v);
												tk++;
										}
										else
										{
												if(tk+1<=topk)
												{
												System.out.println(v+";"+Count.get(v));
												}
												
												v1 = new String(v);
												tk++;
										}
								}
						}
				}
				catch(Exception e)
				{
						System.out.println(e.getMessage());
				}
		}
}
