package producer;

import apimodels.Parameter;
import apimodels.TransformerInfo;

public class Producer {

	public static TransformerInfo transformerInfo() {
		TransformerInfo transformerInfo = new TransformerInfo().name("MSigDB gene-set producer").function("producer");
		transformerInfo.addParametersItem(new Parameter().name("gene_set_name").type("string"));
		return transformerInfo;
	}
}
