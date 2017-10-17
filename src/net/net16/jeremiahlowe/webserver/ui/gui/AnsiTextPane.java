package net.net16.jeremiahlowe.webserver.ui.gui;

import javax.swing.*;
import javax.swing.text.*;

import net.net16.jeremiahlowe.ansipro.*;

import java.awt.Color;
import java.util.ArrayList;

public class AnsiTextPane extends JTextPane{
	private static final long serialVersionUID = -7558428341856246570L;
	
	private AnsiColor defaultForeground, defaultBackground;
	private AnsiColor foreground, background;
	private ArrayList<AnsiStyle> styles;
	
	public AnsiTextPane(){
		super();
		styles = new ArrayList<AnsiStyle>();
		defaultForeground = AnsiColor.White;
		defaultBackground = AnsiColor.Black;
		foreground = defaultForeground;
		background = defaultBackground;
		setBackground(background.toColor());
		setForeground(foreground.toColor());
	}

	public void append(String str, AnsiColor fore, AnsiColor back, AnsiStyle... styles) {
		AttributeSet aset = Ansi.createAttributeSet(fore, back, styles);
		int len = getDocument().getLength(); 
		try {getDocument().insertString(len, str, aset);} 
		catch (BadLocationException e) {e.printStackTrace();}
	}

	public void appendAnsi(String s){
		boolean isAnsi = false;
		String curAnsi = "", text = "";
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if(c == '\033' && s.charAt(i + 1) == '['){
				isAnsi = true;
				atb(text); text = "";
				i++; //Skip the escape sequence and go on
			}
			else if(isAnsi){
				if(c == ';' || (c >= '0' && c <= '9')) curAnsi += c;
				else if(c == 'm'){
					isAnsi = false;
					updateAnsiBullshit(curAnsi);
					curAnsi = "";
					continue;
				}
				else throw new RuntimeException("Error parsing ANSI");
			}
			else text += c;
		}
		atb(text); text = "";
	}
	private void atb(String buf){
		styles.trimToSize();
		AnsiStyle[] styleArr = new AnsiStyle[styles.size()];
		styles.toArray(styleArr);
		append(buf, foreground, background, styleArr);
	}
	private void updateAnsiBullshit(String ansi){
		System.out.println("ANSI: " + ansi);
		String[] data = ansi.split(";");
		for(String idStr : data){
			int id = Integer.parseInt(idStr);
			if(Ansi.isValidColor(id)){
				boolean isBck = (id >= 40 && id <= 47) || (id >= 100 && id <= 107);
				if(isBck) id -= 10;
				AnsiColor c = AnsiColor.fromID(id);
				if(isBck) background = c;
				else foreground = c;
			}
			else{
				AnsiStyle s = AnsiStyle.fromID(id);
				if(s != null){
					if(s == AnsiStyle.Reset){
						foreground = defaultForeground;
						background = defaultBackground;
						styles.clear();
					}
					else
						styles.add(s);
				}
			}
		}
	}

	public Color getANSIColor(String text) {
		text = text.replace("\u001B[", "");
		String[] cmds = text.split(";");
		for(String cmd : cmds){
			if(cmd.endsWith("m")) 
				cmd = cmd.substring(0, cmd.length() - 1);
			int id = Integer.parseInt(cmd);
			if(Ansi.isValidColor(id))
				return AnsiColor.fromID(id).toColor();
		}
		return Color.BLACK;
	}

	public AnsiColor getDefaultForeground() {return defaultForeground;}
	public AnsiColor getDefaultBackground() {return defaultBackground;}
	public void setDefaultForeground(AnsiColor defaultForeground) {
		this.defaultForeground = defaultForeground;
		setForeground(defaultForeground.toColor());
	}
	public void setDefaultBackground(AnsiColor defaultBackground) {
		this.defaultBackground = defaultBackground;
		setBackground(defaultBackground.toColor());
	}
	public AnsiColor getCurrentForeground() {return foreground;}
	public AnsiColor getCurrentBackground() {return background;}
	public void setCurrentForeground(AnsiColor foreground) {this.foreground = foreground;}
	public void setCurrentBackground(AnsiColor background) {this.background = background;}
}