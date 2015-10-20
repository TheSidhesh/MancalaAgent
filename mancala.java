import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class mancala
{
	static ArrayList<Integer> pits = new ArrayList<Integer>();	
	static ArrayList<Integer> pitfinal = new ArrayList<Integer>();
	static ArrayList<String> pitname = new ArrayList<String>();
	static int myplayer,task,depth,mancalaindex1,mancalaindex2,minplayer,maxplayer,firstflag=1;
	static int finalmaxvalue=Integer.MIN_VALUE;
	static int finalminvalue=Integer.MAX_VALUE;
	static File file = new File("traverse_log.txt");
	static FileWriter fw;
	static BufferedWriter bw;
	
	static int getstones(int pitindex,int currentstones, ArrayList<Integer> pitstemp)
	{
		currentstones=pitstemp.get(pitindex);
		pitstemp.set(pitindex, 0);
		return currentstones;
	}
	
	static int opponentmancalacheck(int pitindex,int currplayer)
	{
		if(currplayer==1)
		{
			if(pitindex==mancalaindex2)
			{
				return 1;
			}
		}
		if(currplayer==2)
		{
			if(pitindex==mancalaindex1 || pitindex>=pits.size())
			{
				return 1;
			}
		}
		return 0;
	}
	
	static int findnextpit(int pitindex,int currplayer)
	{
		if(currplayer==1)
		{
			pitindex++;
			if(opponentmancalacheck(pitindex,currplayer)==1)
			{
				pitindex = 0;
			}	
		}
		else
		{
			pitindex++;
			if(opponentmancalacheck(pitindex,currplayer)==1)
			{
				if(pitindex>=pits.size())
					pitindex = 0;
				else
					pitindex++;
			}
			
		}
		return pitindex;
	}
	
	static int putstone(int pitindex,int currentstones,ArrayList<Integer> pitstemp)
	{
		int stones = pitstemp.get(pitindex);
		stones++;
		pitstemp.set(pitindex, stones);
		currentstones--;
		return currentstones;
	}
	
	static int startturn(int pitindex,int currplayer,ArrayList<Integer> pitstemp)
	{
		int currentstones=0;
		currentstones=getstones(pitindex,currentstones,pitstemp);
		while(currentstones != 0)
		{
			pitindex = findnextpit(pitindex,currplayer);
			currentstones = putstone(pitindex,currentstones,pitstemp); 	
		}	
		return pitindex;
	}
	
	static int checkpitempty(ArrayList<Integer> pitstemp, int currplayer)
	{
		int i;
		if(currplayer==1)
		{
			i=0;
			while(i!=mancalaindex1)
			{
				if(pitstemp.get(i)!=0)
				{
					return 0;	
				}
				i++;
			}
			
		}
		else if(currplayer==2)
		{
			i=mancalaindex2-1;
			while(i!=mancalaindex1)
			{
				if(pitstemp.get(i)!=0)
				{
					return 0;	
				}
				i--;
			}
		}
		return 1;
	}
	
	static void fillopponentmancala(ArrayList<Integer> pitstemp, int currplayer)
	{
		int i,totalstones=0;
		if(currplayer==1)
		{
			totalstones=pitstemp.get(mancalaindex2);
			i = mancalaindex2 - 1;
			while(i!=mancalaindex1)
			{
				totalstones+=pitstemp.get(i);
				pitstemp.set(i, 0);
				i--;
			}
			pitstemp.set(mancalaindex2,totalstones);	
		}
		else if(currplayer==2)
		{
			i=0;
			totalstones=pitstemp.get(mancalaindex1);
			while(i!=mancalaindex1)
			{
				totalstones+=pitstemp.get(i);	
				pitstemp.set(i, 0);
				i++;
			}
			pitstemp.set(mancalaindex1,totalstones);
		}
	}
	
	static int checksnatch(ArrayList<Integer> pitstemp, int pitindex, int currplayer)
	{
		if(currplayer==1)
		{
			if(pitindex>=mancalaindex1)
			{
				return 0;
			}
			else if(pitstemp.get(pitindex)==1)
			{
				return 1;
			}
		}
		else
		{
			if(pitindex<=mancalaindex1 || pitindex == mancalaindex2)
			{
				return 0;
			}
			else if(pitstemp.get(pitindex)==1)
			{
				return 1;
			}
		}
		return 0;
	}
	
	
	static int playmancala(int exploredindex,int currplayer, ArrayList<Integer> pitstemp)
	{
		int snatchfromindex=0,snatchedstones=0,stonesinmancala=0,pitindex;
		
		pitindex = startturn(exploredindex,currplayer, pitstemp);
		
		if(currplayer==1)
		{
			if(checkpitempty(pitstemp,currplayer)==1)
			{
				fillopponentmancala(pitstemp,currplayer);
			}
			else if(checksnatch(pitstemp,pitindex,currplayer)==1)
			{
				snatchfromindex = mancalaindex1 - pitindex;
				snatchfromindex = mancalaindex1 + snatchfromindex;
				snatchedstones = getstones(snatchfromindex, snatchedstones,pitstemp);
				stonesinmancala=pitstemp.get(mancalaindex1);
				pitstemp.set(mancalaindex1, stonesinmancala+snatchedstones+1);
				pitstemp.set(pitindex, 0);
				if(checkpitempty(pitstemp,currplayer)==1)
				{
					fillopponentmancala(pitstemp,currplayer);			
				}
				if(checkpitempty(pitstemp,2)==1)
				{
					fillopponentmancala(pitstemp,2);			
				}
				
			}		
		}
		else
		{
			if(checkpitempty(pitstemp,currplayer)==1)
			{
				fillopponentmancala(pitstemp,currplayer);
			}
			else if(checksnatch(pitstemp,pitindex,currplayer)==1)
			{
				snatchfromindex = pitindex - mancalaindex1;
				snatchfromindex = mancalaindex1 - snatchfromindex;
				snatchedstones = getstones(snatchfromindex, snatchedstones,pitstemp);
				stonesinmancala=pitstemp.get(mancalaindex2);
				pitstemp.set(mancalaindex2, stonesinmancala+snatchedstones+1);
				pitstemp.set(pitindex, 0);
				if(checkpitempty(pitstemp,currplayer)==1)
				{
					fillopponentmancala(pitstemp,currplayer);			
				}
				if(checkpitempty(pitstemp,1)==1)
				{
					fillopponentmancala(pitstemp,1);			
				}
			}
		}
		return pitindex;
	}
	
	static int eval(int currplayer, ArrayList<Integer> pitstemp)
	{
		if(currplayer==1)
		{
			return (pitstemp.get(mancalaindex1) - pitstemp.get(mancalaindex2));
		}
		else
		{
			return (pitstemp.get(mancalaindex2) - pitstemp.get(mancalaindex1));
		}
	}
	
	static int evalminimax(ArrayList<Integer> pitstemp)
	{
		if(myplayer==1)
		{
			return (pitstemp.get(mancalaindex1) - pitstemp.get(mancalaindex2));
		}
		else
		{
			return (pitstemp.get(mancalaindex2) - pitstemp.get(mancalaindex1));
		}
	}
	
	static void greedy(int currplayer, ArrayList<Integer> pitsorig)
	{
		int exploredindex,pitindex,value;
		ArrayList<Integer> pitstemp = new ArrayList<Integer>();
		if(currplayer==1)
		{
			exploredindex = 0;
		}
		else
		{	
			exploredindex = mancalaindex2 - 1;
		}
			
		while(exploredindex != mancalaindex1)
		{
			pitstemp.clear();
			pitstemp.addAll(pitsorig);
			if(pitstemp.get(exploredindex)!=0)
			{
				pitindex = playmancala(exploredindex,currplayer, pitstemp);
				if(currplayer==1)
				{
					if(pitindex==mancalaindex1)
					{
						greedy(currplayer,pitstemp);
					}	
					else
					{
						value = eval(currplayer,pitstemp);
						if(value>finalmaxvalue)
						{
							finalmaxvalue=value;
							pitfinal.clear();
							pitfinal.addAll(pitstemp);
						}
					}
				}
				else
				{
					if(pitindex==mancalaindex2)
					{
						greedy(currplayer,pitstemp);
						
					}	
					else
					{
						value = eval(currplayer,pitstemp);
						if(value>finalmaxvalue)
						{
							finalmaxvalue=value;
							pitfinal.clear();
							pitfinal.addAll(pitstemp);
						}
					}
				}		
			}	
			
			if(currplayer==1)
			{
				exploredindex++;
			}
			else
			{
				exploredindex--;
			}	
		}
	}	
	static int minimax(int currplayer, ArrayList<Integer> pitsorig,int currdepth,ArrayList<Integer> pitschild,int lastmove, int lastdepth) throws IOException
	{
		int exploredindex,pitindex=0,value=0,flag2=0,novalctr=0;
		String last = "null";
		int minvalue=Integer.MAX_VALUE,maxvalue=Integer.MIN_VALUE;
		ArrayList<Integer> pitstemp = new ArrayList<Integer>();
		if(currplayer==1)
		{
			exploredindex = 0;
		}
		else
		{	
			exploredindex = mancalaindex2 - 1;
		}
		if(currdepth%2==0)
		{
			value=Integer.MIN_VALUE;
		}
		else
		{
			value=Integer.MAX_VALUE;
		}
		
		while(exploredindex != mancalaindex1) 
		{
			flag2=0;
			pitstemp.clear();
			pitstemp.addAll(pitsorig);
			
			
			if(currdepth!=depth)
			{
				if(pitstemp.get(exploredindex)!=0)
				{
					
					pitindex = playmancala(exploredindex,currplayer, pitstemp);
	
					if(currplayer==1)
					{
						if(pitindex==mancalaindex1)
						{
							last=pitname.get(exploredindex);
							bw.write(last+",");
							//System.out.print(last+",");
							bw.write(currdepth+1+",");
							
							if(checkpitempty(pitstemp,currplayer)!=1)
							{
								if(currplayer==minplayer)
								{
									
									bw.write("Infinity");
									//System.out.print("Infinity\n");
									bw.append("\n");
								}
								else 
								{
									bw.write("-Infinity");
									//System.out.print("-Infinity\n");
									bw.append("\n");
								}			
							}
							else
							{
								if(currplayer==minplayer)
								{
									
									bw.write("Infinity");
									//System.out.print("Infinity\n");
									bw.append("\n");
								}
								else 
								{
									bw.write("-Infinity");
									//System.out.print("-Infinity\n");
									bw.append("\n");
								}
								bw.write(last+",");
								bw.write(currdepth+1+",");
							}
							
							//System.out.print("Extra Chance:\n");
							value = minimax(currplayer,pitstemp,currdepth,pitschild,exploredindex,currdepth+1);
							
							if(lastmove==-1)
							{
								bw.write("root,0,");
								//System.out.print("root,0,");
							}
							else
							{
								last=pitname.get(lastmove);
								bw.write(last+",");
								//System.out.print(last+",");
								bw.write((lastdepth)+",");
								//System.out.print((lastdepth)+",");
							}	
							if(currdepth==0)
							{
								flag2=1;
							}
							
						}
						else
						{
							if(currdepth!=depth)
							{	
								last=pitname.get(exploredindex);
								bw.write(last+",");
								//System.out.print(last+",");
								bw.write((currdepth+1)+",");
								//System.out.print((currdepth+1)+",");
								if(currdepth+1!=depth && checkpitempty(pitstemp,currplayer)!=1)
								{
									if((currdepth+1)%2!=0)
									{
										bw.write("Infinity");
										bw.append("\n");
									}
									else
									{
										bw.write("-Infinity");
										bw.append("\n");
									}
								}	
								else if(currdepth+1!=depth && checkpitempty(pitstemp,currplayer)==1)
								{
									if((currdepth+1)%2!=0)
									{
										bw.write("Infinity");
										bw.append("\n");
									}
									else
									{
										bw.write("-Infinity");
										bw.append("\n");
									}
									bw.write(last+",");
									bw.write((currdepth+1)+",");
								}
								
								
								//System.out.print("increase depth:\n");
								value = minimax(2,pitstemp,currdepth+1,pitschild,exploredindex,currdepth+1);
								
								if(lastmove==-1)
								{
									last="root";
									bw.write("root,0,");
									//System.out.print("root,0,");
								}
								else
								{
									last=pitname.get(lastmove);
									bw.write(last+",");
									//System.out.print(last+",");
									bw.write((lastdepth)+",");
									//System.out.print((lastdepth)+",");
								}
								
								
								//System.out.print("decrease depth:\n");
								if(currdepth==0)
								{
									flag2=1;
								}
								
							}
						}
					 	
						if(currplayer==1 && currplayer==maxplayer)
						{
							if(value>maxvalue)
							{
								maxvalue=value;
								bw.write(String.valueOf(maxvalue));
								bw.append("\n");
								//System.out.print(maxvalue+"\n");
								if(flag2==1)
								{
									if(firstflag==1)
									{
										finalmaxvalue=maxvalue;
										firstflag=0;
										pitschild.clear();
										pitschild.addAll(pitstemp);
									}
									if(finalmaxvalue<maxvalue)
									{
										pitschild.clear();
										pitschild.addAll(pitstemp);
										finalmaxvalue=maxvalue;
									}
								}
								
								
							}
							else
							{
								bw.write(String.valueOf(maxvalue));
								bw.append("\n");
								//System.out.print(maxvalue+"\n");
							}
						}
						else if(currplayer==1 && currplayer==minplayer)
						{
							if(value<minvalue)
							{
								minvalue=value;
								bw.write(String.valueOf(minvalue));
								bw.append("\n");
								//System.out.print(minvalue+"\n");
								
							}
							else
							{
								bw.write(String.valueOf(minvalue));
								bw.append("\n");
								//System.out.print(minvalue+"\n");
							}
						}
						 	
					}
					else if(currplayer==2)
					{
						if(pitindex==mancalaindex2)
						{
							last=pitname.get(exploredindex);
							bw.write(last+",");
							//System.out.print(last+",");
							bw.write(currdepth+1+",");
							//System.out.print((currdepth+1)+",");
							
							if(checkpitempty(pitstemp,currplayer)!=1)
							{
								if(currplayer==minplayer)
								{
									
									bw.write("Infinity");
									//System.out.print("Infinity\n");
									bw.append("\n");
								}
								else 
								{
									bw.write("-Infinity");
									//System.out.print("-Infinity\n");
									bw.append("\n");
								}			
							}
							else
							{
								if(currplayer==minplayer)
								{
									
									bw.write("Infinity");
									//System.out.print("Infinity\n");
									bw.append("\n");
								}
								else 
								{
									bw.write("-Infinity");
									//System.out.print("-Infinity\n");
									bw.append("\n");
								}
								bw.write(last+",");
								//System.out.print(last+",");
								bw.write(currdepth+1+",");
							}
							
							//System.out.print("Extra Chance:\n");
							value = minimax(currplayer,pitstemp,currdepth,pitschild,exploredindex,currdepth+1);
							if(lastmove==-1)
							{
								//last="root";
								bw.write("root,0,");
								//System.out.print("root,0,");
							}
							else
							{
								last=pitname.get(lastmove);
								bw.write(last+",");
								//System.out.print(last+",");
								bw.write((lastdepth)+",");
								//System.out.print((lastdepth)+",");
							}
							if(currdepth==0)
							{
								flag2=1;
							}
						}
						else
						{
							if(currdepth!=depth)
							{
								last=pitname.get(exploredindex);
								bw.write(last+",");
								//System.out.print(last+",");
								bw.write((currdepth+1)+",");
								//System.out.print((currdepth+1)+",");
								if(currdepth+1!=depth && checkpitempty(pitstemp,currplayer)!=1)
								{
									if((currdepth+1)%2!=0)
									{
										bw.write("Infinity");
										//System.out.print("Infinity\n");
										bw.append("\n");
									}
									else
									{
										bw.write("-Infinity");
										//System.out.print("-Infinity\n");
										bw.append("\n");
									}
								}
								else if(currdepth+1!=depth && checkpitempty(pitstemp,currplayer)==1)
								{
									if((currdepth+1)%2!=0)
									{
										bw.write("Infinity");
										bw.append("\n");
									}
									else
									{
										bw.write("-Infinity");
										bw.append("\n");
									}
									bw.write(last+",");
									bw.write((currdepth+1)+",");
								}
								
								//System.out.print("increase depth:\n");
								value = minimax(1,pitstemp,currdepth+1,pitschild,exploredindex,currdepth+1);
								//System.out.print("decrease depth:\n");
								
								if(lastmove==-1)
								{
									last="root";
									bw.write("root,0,");
									//System.out.print("root,0,");
								}
								else
								{
									last=pitname.get(lastmove);
									bw.write(last+",");
									//System.out.print(last+",");
									bw.write((lastdepth)+",");
									//System.out.print((lastdepth)+",");
								}
								
								if(currdepth==0)
								{
									flag2=1;
								}	
							}
						}
						
						if(currplayer==2 && currplayer==maxplayer)
						{
							if(value>maxvalue)
							{
								maxvalue=value;
								bw.write(String.valueOf(maxvalue));
								bw.append("\n");
								//System.out.print(maxvalue+"\n");
								if(flag2==1)
								{
									if(firstflag==1)
									{
										finalmaxvalue=maxvalue;
										firstflag=0;
										pitschild.clear();
										pitschild.addAll(pitstemp);
									}
									if(finalmaxvalue<maxvalue)
									{
										pitschild.clear();
										pitschild.addAll(pitstemp);
										finalmaxvalue=maxvalue;
									}
										
								}		
							}
							else
							{
								bw.write(String.valueOf(maxvalue));
								bw.append("\n");
								//System.out.print(maxvalue+"\n");
							}
								
						}
						else if(currplayer==2 && currplayer==minplayer)
						{
							if(value<minvalue)
							{
								minvalue=value;
								bw.write(String.valueOf(minvalue));
								bw.append("\n");
								//System.out.print(minvalue+"\n");
							}
							else
							{
								bw.write(String.valueOf(minvalue));
								bw.append("\n");
								//System.out.print(minvalue+"\n");
							}
						}
					}	
				}
				else
				{
					//System.out.print("No move\n");
					novalctr++;
				}
			}
			else
			{	
				value = evalminimax(pitstemp);
				bw.write(String.valueOf(value));
				bw.append("\n");
				//System.out.print(value+"\n");
				return value;
			}
			
			if(currplayer==1)
			{
				exploredindex++;
			}
			else if(currplayer==2)
			{
				exploredindex--;
			}
			if(exploredindex==mancalaindex1 && novalctr==mancalaindex1)
			{
				if(currplayer==1 && currplayer==maxplayer)
				{
					maxvalue=evalminimax(pitstemp);
					bw.write(String.valueOf(maxvalue));
					bw.append("\n");
					novalctr=0;
				}	
				else if(currplayer==1 && currplayer==minplayer)
				{
					minvalue=evalminimax(pitstemp);
					bw.write(String.valueOf(minvalue));
					bw.append("\n");
					novalctr=0;
				}
				else if(currplayer==2 && currplayer==maxplayer)
				{
					maxvalue = evalminimax(pitstemp);
					bw.write(String.valueOf(maxvalue));
					bw.append("\n");
					novalctr=0;
				}
				else if(currplayer==2 && currplayer==minplayer)
				{
					minvalue = evalminimax(pitstemp);
					bw.write(String.valueOf(minvalue));
					bw.append("\n");
					novalctr=0;
				}
			}		
		}
		if(currdepth % 2==0)
		{
			return maxvalue;
		}
		else
		{
			return minvalue;
		}
		
	}
	
	static int alphabeta(int currplayer, ArrayList<Integer> pitsorig,int currdepth,ArrayList<Integer> pitschild,int lastmove, int lastdepth,int alpha,int beta) throws IOException
	{
		int exploredindex,pitindex=0,value=0,flag2=0,novalctr=0;
		String last = "null";
		int minvalue=Integer.MAX_VALUE,maxvalue=Integer.MIN_VALUE;
		ArrayList<Integer> pitstemp = new ArrayList<Integer>();
		if(currplayer==1)
		{
			exploredindex = 0;
		}
		else
		{	
			exploredindex = mancalaindex2 - 1;
		}
		if(currdepth%2==0)
		{
			value=Integer.MIN_VALUE;
		}
		else
		{
			value=Integer.MAX_VALUE;
		}
		
		while(exploredindex != mancalaindex1) 
		{
			flag2=0;
			
			if(currplayer==maxplayer)
			{
				
			}
			else if(currplayer==minplayer)
			{
				
			}
			
			pitstemp.clear();
			pitstemp.addAll(pitsorig);
			if(currdepth!=depth)
			{
				if(pitstemp.get(exploredindex)!=0)
				{
					
					pitindex = playmancala(exploredindex,currplayer, pitstemp);
					if(currplayer==1)
					{
						if(pitindex==mancalaindex1)
						{
							last=pitname.get(exploredindex);
							bw.write(last+",");
							//System.out.print(last+",");
							bw.write(currdepth+1+",");
							//System.out.print((currdepth+1)+",");
							if(checkpitempty(pitstemp,currplayer)!=1)
							{
								if(currplayer==minplayer)
								{
									
									bw.write("Infinity,");
									//System.out.print("Infinity\n");
									
								}
								else 
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity\n");
									
								}	
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
									//System.out.print(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									//System.out.print("Infinity\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									//System.out.print(beta+"\n");
								}
								
							}
							else
							{
								if(currplayer==minplayer)
								{
									
									bw.write("Infinity,");
								}
								else 
								{
									bw.write("-Infinity,");
								}
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									
								}
								bw.write(last+",");
								bw.write(currdepth+1+",");
							}
							
							
					
							value = alphabeta(currplayer,pitstemp,currdepth,pitschild,exploredindex,currdepth+1,alpha,beta);
							
							if(lastmove==-1)
							{
								bw.write("root,0,");
								//System.out.print("root,0,");
							}
							else
							{
								last=pitname.get(lastmove);
								bw.write(last+",");
								//System.out.print(last+",");
								bw.write((lastdepth)+",");
								//System.out.print((lastdepth)+",");
							}	
							if(currdepth==0)
							{
								flag2=1;
							}
							
						}
						else
						{
							if(currdepth!=depth)
							{	
								last=pitname.get(exploredindex);
								bw.write(last+",");
								bw.write((currdepth+1)+",");
								if(currdepth+1!=depth && checkpitempty(pitstemp,currplayer)!=1)
								{
									if((currdepth+1)%2!=0)
									{
										bw.write("Infinity,");
									}
									else
									{
										bw.write("-Infinity,");
									}
									if(alpha==Integer.MIN_VALUE)
									{
										bw.write("-Infinity,");
									}
									else
									{
										bw.write(alpha+",");
									}
									if(beta==Integer.MAX_VALUE)
									{
										bw.write("Infinity");
										bw.append("\n");	
									}
									else
									{
										bw.write(String.valueOf(beta));
										bw.append("\n");
									}
								}
								else if(currdepth+1!=depth && checkpitempty(pitstemp,currplayer)==1)
								{
									if((currdepth+1)%2!=0)
									{
										bw.write("Infinity,");
									}
									else
									{
										bw.write("-Infinity,");
									}
									if(alpha==Integer.MIN_VALUE)
									{
										bw.write("-Infinity,");
									}
									else
									{
										bw.write(alpha+",");
									}
									if(beta==Integer.MAX_VALUE)
									{
										bw.write("Infinity");
										bw.append("\n");
										
									}
									else
									{
										bw.write(String.valueOf(beta));
										bw.append("\n");
									}
									bw.write(last+",");
									bw.write((currdepth+1)+",");
								}
								
								value = alphabeta(2,pitstemp,currdepth+1,pitschild,exploredindex,currdepth+1,alpha,beta);
								
								if(lastmove==-1)
								{
									last="root";
									bw.write("root,0,");
									//System.out.print("root,0,");
								}
								else
								{
									last=pitname.get(lastmove);
									bw.write(last+",");
									//System.out.print(last+",");
									bw.write((lastdepth)+",");
									//System.out.print((lastdepth)+",");
								}
							
								if(currdepth==0)
								{
									flag2=1;
								}
								
							}
						}
					 	
						if(currplayer==1 && currplayer==maxplayer)
						{
							if(value>maxvalue)
							{
								maxvalue=value;
								if(value>=beta)
								{
									bw.write(maxvalue+",");
									//System.out.print(maxvalue+",");
									if(alpha==Integer.MIN_VALUE)
									{
										bw.write("-Infinity,");
										//System.out.print("-Infinity,");
									}
									else
									{
										bw.write(alpha+",");
										//System.out.print(alpha+",");
									}
									if(beta==Integer.MAX_VALUE)
									{
										bw.write("Infinity");
										bw.append("\n");
										//System.out.print("Infinity\n");
										
									}
									else
									{
										bw.write(String.valueOf(beta));
										bw.append("\n");
										//System.out.print(beta+"\n");
									}
									break;
								}
								if(value>alpha)
								{
									alpha=value;
								}
								bw.write(maxvalue+",");
								//System.out.print(maxvalue+",");
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
									//System.out.print(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									//System.out.print("Infinity\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									//System.out.print(beta+"\n");
								}
								
								if(flag2==1)
								{
									if(firstflag==1)
									{
										finalmaxvalue=maxvalue;
										firstflag=0;
										pitschild.clear();
										pitschild.addAll(pitstemp);
									}
									if(finalmaxvalue<maxvalue)
									{
										pitschild.clear();
										pitschild.addAll(pitstemp);
										finalmaxvalue=maxvalue;
									}
								}
								
								
							}
							else
							{
								bw.write(maxvalue+",");
								//System.out.print(maxvalue+",");
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
									//System.out.print(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									//System.out.print("Infinity\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									//System.out.print(beta+"\n");
								}
							}
						}
						else if(currplayer==1 && currplayer==minplayer)
						{
							if(value<minvalue)
							{
								minvalue=value;
								if(value<=alpha)
								{
									bw.write(minvalue+",");
									//System.out.print(minvalue+",");
									if(alpha==Integer.MIN_VALUE)
									{
										bw.write("-Infinity,");
										//System.out.print("-Infinity,");
									}
									else
									{
										bw.write(alpha+",");
										//System.out.print(alpha+",");
									}
									if(beta==Integer.MAX_VALUE)
									{
										bw.write("Infinity");
										bw.append("\n");
										//System.out.print("Infinity\n");
										
									}
									else
									{
										bw.write(String.valueOf(beta));
										bw.append("\n");
										//System.out.print(beta+"\n");
									}
									break;
								}
								if(value<beta)
								{
									beta=value;
								}
								bw.write(minvalue+",");
								//System.out.print(minvalue+",");
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
									//System.out.print(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									//System.out.print("Infinity\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									//System.out.print(beta+"\n");
								}
								
							}
							else
							{
								bw.write(minvalue+",");
								//System.out.print(minvalue+",");
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
									//System.out.print(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									//System.out.print("Infinity\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									//System.out.print(beta+"\n");
								}
							}
						}
						 	
					}
					else if(currplayer==2)
					{
						if(pitindex==mancalaindex2)
						{

							last=pitname.get(exploredindex);
							bw.write(last+",");
							//System.out.print(last+",");
							bw.write(currdepth+1+",");
							//System.out.print((currdepth+1)+",");
							if(checkpitempty(pitstemp,currplayer)!=1)
							{
								if(currplayer==minplayer)
								{
									
									bw.write("Infinity,");
									//System.out.print("Infinity\n");
									
								}
								else 
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity\n");
									
								}	
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
									//System.out.print(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									//System.out.print("Infinity\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									//System.out.print(beta+"\n");
								}
								
							}
							else
							{
								if(currplayer==minplayer)
								{
									
									bw.write("Infinity,");
								}
								else 
								{
									bw.write("-Infinity,");
								}
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									
								}
								bw.write(last+",");
								bw.write(currdepth+1+",");
							}
							value = alphabeta(currplayer,pitstemp,currdepth,pitschild,exploredindex,currdepth+1,alpha,beta);
							
							if(lastmove==-1)
							{
								//last="root";
								bw.write("root,0,");
								//System.out.print("root,0,");
							}
							else
							{
								last=pitname.get(lastmove);
								bw.write(last+",");
								//System.out.print(last+",");
								bw.write((lastdepth)+",");
								//System.out.print((lastdepth)+",");
							}
							if(currdepth==0)
							{
								flag2=1;
							}
						}
						else
						{
							if(currdepth!=depth)
							{
								last=pitname.get(exploredindex);
								bw.write(last+",");
								//System.out.print(last+",");
								bw.write((currdepth+1)+",");
								//System.out.print((currdepth+1)+",");
								if(currdepth+1!=depth && checkpitempty(pitstemp,currplayer)!=1)
								{
									if((currdepth+1)%2!=0)
									{
										bw.write("Infinity,");
										//System.out.print("Infinity,");
									}
									else
									{
										bw.write("-Infinity,");
										//System.out.print("-Infinity,");
									}
									if(alpha==Integer.MIN_VALUE)
									{
										bw.write("-Infinity,");
										//System.out.print("-Infinity,");
									}
									else
									{
										bw.write(alpha+",");
										//System.out.print(alpha+",");
									}
									if(beta==Integer.MAX_VALUE)
									{
										bw.write("Infinity");
										bw.append("\n");
										//System.out.print("Infinity\n");
										
									}
									else
									{
										bw.write(String.valueOf(beta));
										bw.append("\n");
										//System.out.print(beta+"\n");
									}
								}	
								else if(currdepth+1!=depth && checkpitempty(pitstemp,currplayer)==1)
								{
									if((currdepth+1)%2!=0)
									{
										bw.write("Infinity,");
									}
									else
									{
										bw.write("-Infinity,");
									}
									if(alpha==Integer.MIN_VALUE)
									{
										bw.write("-Infinity,");
									}
									else
									{
										bw.write(alpha+",");
									}
									if(beta==Integer.MAX_VALUE)
									{
										bw.write("Infinity");
										bw.append("\n");
										
									}
									else
									{
										bw.write(String.valueOf(beta));
										bw.append("\n");
									}
									bw.write(last+",");
									bw.write((currdepth+1)+",");
								}
								
							
								value = alphabeta(1,pitstemp,currdepth+1,pitschild,exploredindex,currdepth+1,alpha,beta);
					
								if(lastmove==-1)
								{
									last="root";
									bw.write("root,0,");
									//System.out.print("root,0,");
								}
								else
								{
									last=pitname.get(lastmove);
									bw.write(last+",");
									//System.out.print(last+",");
									bw.write((lastdepth)+",");
									//System.out.print((lastdepth)+",");
								}
								
								if(currdepth==0)
								{
									flag2=1;
								}	
							}
						}
						
						if(currplayer==2 && currplayer==maxplayer)
						{
							if(value>maxvalue)
							{
								maxvalue=value;
								if(value>=beta)
								{
									bw.write(maxvalue+",");
									//System.out.print(maxvalue+",");
									if(alpha==Integer.MIN_VALUE)
									{
										bw.write("-Infinity,");
										//System.out.print("-Infinity,");
									}
									else
									{
										bw.write(alpha+",");
										//System.out.print(alpha+",");
									}
									if(beta==Integer.MAX_VALUE)
									{
										bw.write("Infinity");
										bw.append("\n");
										//System.out.print("Infinity\n");
										
									}
									else
									{
										bw.write(String.valueOf(beta));
										bw.append("\n");
										//System.out.print(beta+"\n");
									}
									break;
								}
								if(value>alpha)
								{
									alpha=value;
								}
								bw.write(maxvalue+",");
								//System.out.print(maxvalue+",");
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
									//System.out.print(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									//System.out.print("Infinity\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									//System.out.print(beta+"\n");
								}
								if(flag2==1)
								{
									if(firstflag==1)
									{
										finalmaxvalue=maxvalue;
										firstflag=0;
										pitschild.clear();
										pitschild.addAll(pitstemp);
									}
									if(finalmaxvalue<maxvalue)
									{
										pitschild.clear();
										pitschild.addAll(pitstemp);
										finalmaxvalue=maxvalue;
									}
								}
								
								
							}
							else
							{
								bw.write(maxvalue+",");
								//System.out.print(maxvalue+",");
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
									//System.out.print(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									//System.out.print("Infinity\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									//System.out.print(beta+"\n");
								}
							}
								
						}
						else if(currplayer==2 && currplayer==minplayer)
						{
							if(value<minvalue)
							{
								minvalue=value;
								if(value<=alpha)
								{
									bw.write(minvalue+",");
									
									if(alpha==Integer.MIN_VALUE)
									{
										bw.write("-Infinity,");
										
									}
									else
									{
										bw.write(alpha+",");
										//System.out.print(alpha+",");
									}
									if(beta==Integer.MAX_VALUE)
									{
										bw.write("Infinity");
										bw.append("\n");
										//System.out.print("Infinity\n");
										
									}
									else
									{
										bw.write(String.valueOf(beta));
										bw.append("\n");
										//System.out.print(beta+"\n");
									}
									break;
								}
								if(value<beta)
								{
									beta=value;
								}
								bw.write(minvalue+",");
								//System.out.print(minvalue+",");
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
									//System.out.print(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									//System.out.print("Infinity\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									//System.out.print(beta+"\n");
								}
								
							}
							else
							{
								bw.write(minvalue+",");
								//System.out.print(minvalue+",");
								if(alpha==Integer.MIN_VALUE)
								{
									bw.write("-Infinity,");
									//System.out.print("-Infinity,");
								}
								else
								{
									bw.write(alpha+",");
									//System.out.print(alpha+",");
								}
								if(beta==Integer.MAX_VALUE)
								{
									bw.write("Infinity");
									bw.append("\n");
									//System.out.print("Infinity\n");
									
								}
								else
								{
									bw.write(String.valueOf(beta));
									bw.append("\n");
									//System.out.print(beta+"\n");
								}
							}
						}
					}	
				}
				else
				{
					novalctr++;
				}
			}
			else
			{	
				value = evalminimax(pitstemp);
				bw.write(value+",");
				//System.out.print(value+",");
				if(alpha==Integer.MIN_VALUE)
				{
					bw.write("-Infinity,");
					//System.out.print("-Infinity,");
				}
				else
				{
					bw.write(alpha+",");
					//System.out.print(alpha+",");
				}
				if(beta==Integer.MAX_VALUE)
				{
					bw.write("Infinity");
					bw.append("\n");
					//System.out.print("Infinity\n");
					
				}
				else
				{
					bw.write(String.valueOf(beta));
					bw.append("\n");
					//System.out.print(beta+"\n");
				}
				return value;
			}
			
			if(currplayer==1)
			{
				exploredindex++;
			}
			else if(currplayer==2)
			{
				exploredindex--;
			}
			if(exploredindex==mancalaindex1 && novalctr==mancalaindex1)
			{
				if(currplayer==1 && currplayer==maxplayer)
				{
						maxvalue=evalminimax(pitstemp);
						bw.write(maxvalue+",");
						if(alpha==Integer.MIN_VALUE)
						{
							bw.write("-Infinity,");
							//System.out.print("-Infinity,");
						}
						else
						{
							bw.write(alpha+",");
							//System.out.print(alpha+",");
						}
						if(beta==Integer.MAX_VALUE)
						{
							bw.write("Infinity");
							bw.append("\n");
							//System.out.print("Infinity\n");
							
						}
						else
						{
							bw.write(String.valueOf(beta));
							bw.append("\n");
							//System.out.print(beta+"\n");
						}
						novalctr=0;
				}	
				else if(currplayer==1 && currplayer==minplayer)
				{
						minvalue=evalminimax(pitstemp);
						bw.write(minvalue+",");
						if(alpha==Integer.MIN_VALUE)
						{
							bw.write("-Infinity,");
							//System.out.print("-Infinity,");
						}
						else
						{
							bw.write(alpha+",");
							//System.out.print(alpha+",");
						}
						if(beta==Integer.MAX_VALUE)
						{
							bw.write("Infinity");
							bw.append("\n");
							//System.out.print("Infinity\n");
							
						}
						else
						{
							bw.write(String.valueOf(beta));
							bw.append("\n");
							//System.out.print(beta+"\n");
						}
						novalctr=0;
				}
				else if(currplayer==2 && currplayer==maxplayer)
				{
						maxvalue = evalminimax(pitstemp);
						bw.write(maxvalue+",");
						if(alpha==Integer.MIN_VALUE)
						{
							bw.write("-Infinity,");
							//System.out.print("-Infinity,");
						}
						else
						{
							bw.write(alpha+",");
							//System.out.print(alpha+",");
						}
						if(beta==Integer.MAX_VALUE)
						{
							bw.write("Infinity");
							bw.append("\n");
							//System.out.print("Infinity\n");
							
						}
						else
						{
							bw.write(String.valueOf(beta));
							bw.append("\n");
							//System.out.print(beta+"\n");
						}
						novalctr=0;
				}
				else if(currplayer==2 && currplayer==minplayer)
				{
					minvalue = evalminimax(pitstemp);
					bw.write(minvalue+",");
					if(alpha==Integer.MIN_VALUE)
					{
						bw.write("-Infinity,");
						//System.out.print("-Infinity,");
					}
					else
					{
						bw.write(alpha+",");
						//System.out.print(alpha+",");
					}
					if(beta==Integer.MAX_VALUE)
					{
						bw.write("Infinity");
						bw.append("\n");
						//System.out.print("Infinity\n");
						
					}
					else
					{
						bw.write(String.valueOf(beta));
						bw.append("\n");
						//System.out.print(beta+"\n");
					}
					novalctr=0;
				}
			}		
		}
		if(currdepth % 2==0)
		{
			return maxvalue;
		}
		else
		{
			return minvalue;
		}
		
	}
	
	static void makepitname()
	{
		String name;
		int x=2,i;
		for(i=0;i<=mancalaindex2;i++)
		{
			pitname.add("-");
		}
		for(i=0;i<mancalaindex1;i++)
		{
			name="B"+(i+2);
			pitname.set(i,name);	
		}
		
		for(i=mancalaindex2-1;i>mancalaindex1;i--)
		{
			name="A"+ x;
			pitname.set(i,name);
			x++;	
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		String line;
		int i,maxvalue=0;
		String[] dest,dest1;
		File fileout = new File("next_state.txt");
		ArrayList<Integer> pitsorig = new ArrayList<Integer>();
		ArrayList<Integer> pitschild = new ArrayList<Integer>();
		FileReader fileReader = new FileReader(args[1]);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		task = Integer.parseInt(bufferedReader.readLine());
		myplayer = Integer.parseInt(bufferedReader.readLine());
		maxplayer=myplayer;
		depth = Integer.parseInt(bufferedReader.readLine());
		line = bufferedReader.readLine();
		dest = line.split(" ");
		line = bufferedReader.readLine();
		dest1 = line.split(" ");
		
		mancalaindex1 = dest.length;
		mancalaindex2 = mancalaindex1 + dest.length + 1;
		
		for(i=0;i<dest1.length;i++) 
		{
			pits.add(Integer.parseInt(dest1[i]));
		}	
		pits.add(0);
		
		for(i=dest.length-1;i>=0;i--) 
		{
			pits.add(Integer.parseInt(dest[i]));
		}	
		pits.add(0);
		
		i = Integer.parseInt(bufferedReader.readLine());
		pits.set(mancalaindex2, i);
		i = Integer.parseInt(bufferedReader.readLine());
		pits.set(mancalaindex1, i);
		pitsorig.addAll(pits);
		
		makepitname(); 
		
		if(myplayer==1)
			minplayer=2;
		else
			minplayer=1;
		
		
 		switch(task)
		{
			case 1: 
					if(myplayer==1)
					{
						greedy(1,pitsorig);
					}	
					else
					{
						greedy(2,pitsorig);
						
					}
					FileWriter fwnextstate = new FileWriter(fileout,false);
					BufferedWriter bwnextstate = new BufferedWriter(fwnextstate);
					for(int x = mancalaindex2-1;x > mancalaindex1;x--)
					{
						if(x!=mancalaindex1+1)
						{
							bwnextstate.append(pitfinal.get(x)+" ");
						}
						else
						{
							bwnextstate.write(String.valueOf(pitfinal.get(x)));
						}
						
					}
					bwnextstate.write("\n");
					for(int x=0;x<mancalaindex1;x++)
					{
						if(x!=mancalaindex1-1)
						{
							bwnextstate.write(pitfinal.get(x)+" ");
						}
						else
						{
							bwnextstate.write(String.valueOf(pitfinal.get(x)));
						}	
					}
					bwnextstate.write("\n");
					bwnextstate.write(String.valueOf(pitfinal.get(mancalaindex2)));
					bwnextstate.write("\n");
					bwnextstate.write(String.valueOf(pitfinal.get(mancalaindex1)));
					bwnextstate.close();
					break;
			case 2: 
					fw = new FileWriter(file, false);
			        bw = new BufferedWriter(fw);
			        bw.close();
			        fw = new FileWriter(file, true);
			        bw = new BufferedWriter(fw);
					if(myplayer==1)
					{					
						bw.write("Node,Depth,Value");
						bw.append("\n");
						//System.out.println("Node,Depth,Value");
						//System.out.println("root,0,-Infinity");
						bw.write("root,0,-Infinity");
				 		bw.append("\n");
						maxvalue=minimax(1,pitsorig,0,pitschild,-1,0);
						bw.close();
						
					}
					else
					{
						bw.write("Node,Depth,Value");
						bw.append("\n");
						bw.write("root,0,-Infinity");
				 		bw.append("\n");
						maxvalue=minimax(2,pitsorig,0,pitschild,-1,0);
						bw.close();
					}
					
					
					break;
			case 3: 
					fw = new FileWriter(file, false);
			        bw = new BufferedWriter(fw);
			        bw.close();
			        fw = new FileWriter(file, true);
			        bw = new BufferedWriter(fw);
					if(myplayer==1)
					{
						bw.write("Node,Depth,Value,Alpha,Beta");
						bw.append("\n");
						//System.out.println("Node,Depth,Value,Alpha,Beta");
						//System.out.println("root,0,-Infinity,-Infinity,Infinity");
						bw.write("root,0,-Infinity,-Infinity,Infinity");
				 		bw.append("\n");
						maxvalue=alphabeta(1,pitsorig,0,pitschild,-1,0,Integer.MIN_VALUE,Integer.MAX_VALUE);
						bw.close();
					}
					else
					{
						bw.write("Node,Depth,Value,Alpha,Beta");
						bw.append("\n");
						//System.out.println("Node,Depth,Value,Alpha,Beta");
						//System.out.println("root,0,-Infinity,-Infinity,Infinity");
						bw.write("root,0,-Infinity,-Infinity,Infinity");
				 		bw.append("\n");
						maxvalue=alphabeta(2,pitsorig,0,pitschild,-1,0,Integer.MIN_VALUE,Integer.MAX_VALUE);
						bw.close();
					}
					
					break;				
		}	
 		
 		System.out.print(" ->"+maxvalue);
 		System.out.println("The time is " + (double)(System.currentTimeMillis() - start)/1000);
		if(task==2 || task==3)
		{
			FileWriter fwnextstate = new FileWriter(fileout,false);
			BufferedWriter bwnextstate = new BufferedWriter(fwnextstate);
			for(int x = mancalaindex2-1;x > mancalaindex1;x--)
			{
				if(x!=mancalaindex1+1)
				{
					bwnextstate.append(pitschild.get(x)+" ");
				}
				else
				{
					bwnextstate.write(String.valueOf(pitschild.get(x)));
				}
				
			}
			bwnextstate.write("\n");
			for(int x=0;x<mancalaindex1;x++)
			{
				if(x!=mancalaindex1-1)
				{
					bwnextstate.write(pitschild.get(x)+" ");
				}
				else
				{
					bwnextstate.write(String.valueOf(pitschild.get(x)));
				}	
			}
			bwnextstate.write("\n");
			bwnextstate.write(String.valueOf(pitschild.get(mancalaindex2)));
			bwnextstate.write("\n");
			bwnextstate.write(String.valueOf(pitschild.get(mancalaindex1)));
			bwnextstate.close();
		}
		
		bufferedReader.close();
		
	}
}
