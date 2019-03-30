package br.com.teste;

import java.util.ArrayList;
import java.util.List;

public class Teste {
	
	public static void montarLista(){
		List<String> lista1 = new ArrayList<>();
		List<String> lista2 = new ArrayList<>();
		List<List<String>> lista3 = new ArrayList<>();
		
		lista1.add("a");
		lista1.add("b");
		lista1.add("c");
		
		lista2.add("x");
		lista2.add("y");
		lista2.add("z");
		
		lista3.add(lista1);
		lista3.add(lista2);
		
		System.out.println(lista3.size());
		
		for (String s : lista3.get(1)){
			System.out.println(s);
		}
		
	}
	
	public static void main(String []args){
		montarLista();
	}
}
