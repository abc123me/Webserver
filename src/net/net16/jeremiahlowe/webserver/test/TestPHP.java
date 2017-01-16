package net.net16.jeremiahlowe.webserver.test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class TestPHP {
	public static void main(String args[]) throws Exception{
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine php = sem.getEngineByExtension("php");
		listScriptEngines(sem);
		//php.eval("<?php echo hello;");
		php.eval("<html><body><p>Test</p><?php echo \"Hello!\";?></body>");
	}
	public static void listScriptEngines(ScriptEngineManager s){
		for(ScriptEngineFactory f : s.getEngineFactories()){
			System.out.println("-------------------------");
			System.out.println(f.getEngineName() + ": " + f.getEngineVersion());
			System.out.println(f.getLanguageName() + ": " + f.getLanguageVersion());
			String out = "";
			for(String e : f.getExtensions()) out += e + ", ";
			System.out.println("Extensions: " + (out.length() > 2 ? out.substring(0, out.length() - 2) : out));
		}
		System.out.println("-------------------------");
	}
}
