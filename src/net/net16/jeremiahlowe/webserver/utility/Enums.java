package net.net16.jeremiahlowe.webserver.utility;

import net.net16.jeremiahlowe.ansipro.*;

public interface Enums {
	public enum LogLevel{
		Debug(-3), Requests(-2), Detailed(-1), Info(0), Warn(1), Error(2), Severe(3), Fatal(4);
		
		private final int level;
		private LogLevel(int level){this.level = level;}
		
		public int getLevel(){return level;}
		public String getAnsi(){
			switch(this){
				case Debug: return Ansi.getSequence(AnsiColor.Purple) + "";
				case Warn: return Ansi.getSequence(AnsiColor.Yellow) + "";
				case Error: return Ansi.getSequence(AnsiColor.BrightRed) + "";
				case Severe: return Ansi.getSequence(AnsiColor.BrightRed, AnsiStyle.Bold);
				case Fatal: return Ansi.getSequence(AnsiColor.BrightRed, AnsiColor.BrightYellow, AnsiStyle.Bold);
				default: return Ansi.RESET;
			}
		}
		public String getPrefix(boolean ansi){return (ansi ? getAnsi() : "") + toString();}
		public String getPrefix(){return getPrefix(true);}
		@Override
		public String toString(){return "[" + super.toString().toUpperCase() + "]";}
	}
}
