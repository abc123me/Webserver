package net.net16.jeremiahlowe.webserver.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

public class ListFile {
	private ArrayList<String> items;
	
	public ListFile(){
		items = new ArrayList<String>();
	}
	
	public void addItem(String item){
		items.add(item);
	}
	public void removeItem(String item){
		items.removeIf(new Predicate<String>(){
			@Override
			public boolean test(String t){
				return t != null && item.contentEquals(t);
			}
		});
	}
	public boolean containsItem(String item){
		items.trimToSize();
		for(String s : items)
			if(s != null && item.contentEquals(s))
				return true;
		return false;
	}
	public void clearItems(){
		items.clear();
	}
	public void load(){
		
	}
	public void save(File to) throws IOException{ save(to, null); }
	public void save(File to, String comment) throws IOException {
		FileOutputStream fos = new FileOutputStream(to);
		if(comment != null) fos.write(("# " + comment + System.lineSeparator()).getBytes());
		for(String s : items)
			if(s != null)
				fos.write((s + System.lineSeparator()).getBytes());
		fos.close();
	}
}
