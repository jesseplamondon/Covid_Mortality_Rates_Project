package application;

import Jwiki.Jwiki;

public class jwikiTest {


	public static void main(String[] args) {
		Jwiki jwiki = new Jwiki("bob ross");
		String[] sentences = (String[]) jwiki.getExtractText().split("\\.");
		System.out.println(sentences[0] + ".");
	}

}
