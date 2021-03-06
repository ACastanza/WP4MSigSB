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
								var currenturl = new URL("http://data.wikipathways.org/current/gmt/");
								var br = new BufferedReader(new InputStreamReader(currenturl.openStream()));
								String line;
								var sb = new StringBuilder();
								while ((line = br.readLine()) != null) {
																sb.append(line);
																sb.append(System.lineSeparator());
								}
								String singleString = sb.toString();

								String str = singleString;
								String[] arrOfStr = str.split("\n");
								String gmtname = arrOfStr[28];
								String[] parsedname = gmtname.split("-");
								String parsedgmtname = parsedname[1];

								String release = parsedgmtname;
								Set<String> species = new HashSet<String>();
								species.add("Homo_sapiens");
								species.add("Mus_musculus");
//		species.add("Rattus_norvegicus");

								for(String s : species) {
																String url = "http://data.wikipathways.org/" + release + "/gmt/wikipathways-" + release + "-gmt-" + s + ".gmt";
																BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
																String inputLine;
																new File(release).mkdir();
																File output = new File(release, "WP_WikiPathways_" + release + "_" + s + ".txt");
																BufferedWriter writer = new BufferedWriter(new FileWriter(output));

																String chip;
																if (s == "Homo_sapiens") {
																								chip = "Human_NCBI_Gene_ID";
																} else if (s == "Mus_musculus") {
																								chip = "Mouse_NCBI_Gene_ID";
																} else if (s == "Rattus_norvegicus") {
																								chip = "Rat_NCBI_Gene_ID";
																} else {
																								chip = "NULL";
																}

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
																								stdName = stdName.replaceAll("__", "_");
																								stdName = stdName.replaceAll("_$", "");
																								stdName = stdName.replaceAll("_$", "");
																								stdName = stdName.replaceAll("^_", "");

																								writer.write("STANDARD_NAME\tWP_" + stdName +"\n");
																								writer.write("ORGANISM\t" + org +"\n");
																								writer.write("EXTERNAL_DETAILS_URL\t" + purl +"\n");
																								writer.write("EXACT_SOURCE\t" + id +"\n");
																								writer.write("CHIP\t"+ chip +"\n");
																								writer.write("CATEGORY_CODE\tC2" +"\n");
																								writer.write("SUB_CATEGORY_CODE\tCP:WIKIPATHWAYS" +"\n");
																								writer.write("CONTRIBUTOR\tWikiPathways" +"\n");
																								writer.write("CONTRIBUTOR_ORG\tWikiPathways" +"\n");
																								writer.write("DESCRIPTION_BRIEF\t" + name +"\n");
																								writer.write("MEMBERS\t" + ids +"\n");
																								writer.write("HISTORY_7.2\tInitial Version: WikiPathways Release " + release +"\n");
//																								writer.write("HISTORY_8.0\tUpdated to WikiPathways Release " + release +"\n");
																								writer.write("\n");
																}
																System.out.println("WikiPathways GMT input file for " + s + " with " + count + " gene sets: " + output.getAbsolutePath());
																in.close();
																writer.close();
								}
}
}
