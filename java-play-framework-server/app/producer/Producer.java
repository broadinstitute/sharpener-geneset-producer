package producer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import apimodels.Attribute;
import apimodels.GeneInfo;
import apimodels.Parameter;
import apimodels.Property;
import apimodels.TransformerInfo;
import apimodels.TransformerQuery;

public class Producer {

	private static final String GENE_SET_NAME = "gene_set_name";

	private static HashMap<String,ArrayList<GeneInfo>> geneSets = new HashMap<String,ArrayList<GeneInfo>>();

	static {
		try {
			parseMSigDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static TransformerInfo transformerInfo() {
		TransformerInfo transformerInfo = new TransformerInfo().name("MSigDB gene-set producer").function("producer");
		transformerInfo.addParametersItem(new Parameter().name(GENE_SET_NAME).type("string"));
		return transformerInfo;
	}


	private static void parseMSigDB() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader("msigdb/msigdb.latest.entrez.gmt"));
		for (String line = input.readLine(); line != null; line = input.readLine()) {
			String[] row = line.split("\t");
			ArrayList<GeneInfo> genes = new ArrayList<GeneInfo>();
			for (int i = 2; i < row.length; i++) {
				GeneInfo gene = new GeneInfo().geneId("NCBIgene:" + row[i]);
				gene.addAttributesItem(new Attribute().name("entrez_gene_id").value(row[i]).source("MSigDB"));
				gene.addAttributesItem(new Attribute().name("gene set").value(row[0]).source("MSigDB"));
				genes.add(gene);
			}
			geneSets.put(row[0], genes);
		}
		System.out.println("Loaded " + geneSets.size() + " gene sets");
		input.close();
	}


	public static List<GeneInfo> produceGeneSet(final TransformerQuery query) {
		final String geneSetName = geneSetName(query);
		if (geneSetName != null) {
			ArrayList<GeneInfo> genes = geneSets.get(geneSetName);
			if (genes != null) {
				return genes;
			}
		}
		return new ArrayList<GeneInfo>();
	}


	private static String geneSetName(final TransformerQuery query) {
		for (Property property : query.getControls()) {
			if (GENE_SET_NAME.equals(property.getName())) {
				return property.getValue();
			}
		}
		return null;
	}

}
