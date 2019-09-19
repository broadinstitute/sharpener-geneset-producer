package producer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import apimodels.Attribute;
import apimodels.GeneInfo;
import apimodels.GeneInfoIdentifiers;
import apimodels.Parameter;
import apimodels.Property;
import apimodels.TransformerInfo;
import apimodels.TransformerQuery;

public class Producer {

	private static final TransformerInfo.FunctionEnum PRODUCER = TransformerInfo.FunctionEnum.PRODUCER;
	
	private static final Parameter.TypeEnum STRING = Parameter.TypeEnum.STRING;
	
	private static String geneSetName = "gene_set_name";
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	private static HashMap<String,ArrayList<GeneInfo>> geneSets = new HashMap<String,ArrayList<GeneInfo>>();

	static {
		try {
			parseMSigDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static TransformerInfo transformerInfo() {
		 
		try {
			String json = new String(Files.readAllBytes(Paths.get("transformer_info.json")));
			TransformerInfo info = mapper.readValue(json, TransformerInfo.class);
			geneSetName = info.getParameters().get(0).getName();
			return info;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	private static void parseMSigDB() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader("msigdb/msigdb.latest.entrez.gmt"));
		for (String line = input.readLine(); line != null; line = input.readLine()) {
			String[] row = line.split("\t");
			ArrayList<GeneInfo> genes = new ArrayList<GeneInfo>();
			for (int i = 2; i < row.length; i++) {
				String geneId = "NCBIGene:" + row[i];
				GeneInfo gene = new GeneInfo().geneId(geneId);
				gene.addAttributesItem(new Attribute().name("gene set").value(row[0]).source("MSigDB"));
				gene.setIdentifiers(new GeneInfoIdentifiers().entrez(geneId));
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
			if (geneSetName.equals(property.getName())) {
				return property.getValue();
			}
		}
		return null;
	}

}
