//Copyright 2020 mkutmon
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple script to create GMT description file for MSigDb
 * Loads GMT file from release folder and creates description file for human, mouse and rat. 
 * @author mkutmon
 *
 */
public class WP4MSigDB {

	public static void main(String[] args) throws Exception {
		
		String release = "20200710";
		Set<String> species = new HashSet<String>();
		species.add("Homo_sapiens");
		species.add("Mus_musculus");
		species.add("Rattus_norvegicus");
		
		for(String s : species) {
			String url = "http://data.wikipathways.org/" + release + "/gmt/wikipathways-" + release + "-gmt-" + s + ".gmt"; 
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String inputLine;
			File output = new File("GMT_WP_" + release + "_" + s + ".txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));

			int count = 0;
			while ((inputLine = in.readLine()) != null) {
				count++;
				String [] buffer = inputLine.split("\t");
				String [] buffer1 = buffer[0].split("%");
				
				String name = buffer1[0];
				String org = buffer1[3];
				String id = buffer1[2];
				String purl = buffer[1];
				
				String ids = "";
				for(int i = 2; i < buffer.length; i++) {
					ids = ids + buffer[i] + ",";
				}
				ids = ids.substring(0, ids.length() - 1);
				
				String stdName = name.toUpperCase().replace(" ", "_");
				stdName = stdName.replaceAll("[^a-zA-Z0-9_]", "");
				
				writer.write("STANDARD NAME\tWP_" + stdName +"\n");
				writer.write("ORGANISM\t" + org +"\n");
				writer.write("EXTERNAL_DETAILS_URL\t" + purl +"\n");
				writer.write("PMID\t" +"\n");
				writer.write("GEOID\t" +"\n");
				writer.write("EXACT_SOURCE\t" + id +"\n");
				writer.write("CHIP\tEntrezGeneIds" +"\n");
				writer.write("CONTRIBUTOR\t" + buffer1[1] + "\nCONTRIBUTOR_ORG\tWikiPathways\nCONTRIBUTOR_EMAIL\t\n");
				writer.write("DESCRIPTION_BRIEF\t" + name +"\n");
				writer.write("DESCRIPTION_FULL\t" +"\n");
				writer.write("MEMBERS\t" + ids +"\n");
				writer.write("\n");
			}
			System.out.println("WikiPathways GMT input file for " + s + " with " + count + " gene sets: " + output.getAbsolutePath());
			in.close();
			writer.close();
		}
	}
}
