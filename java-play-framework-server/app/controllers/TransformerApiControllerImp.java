package controllers;

import apimodels.GeneInfo;
import apimodels.TransformerInfo;
import apimodels.TransformerQuery;

import play.mvc.Http;
import producer.Producer;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileInputStream;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaPlayFrameworkCodegen", date = "2019-09-18T19:48:56.095Z")

public class TransformerApiControllerImp implements TransformerApiControllerImpInterface {
    @Override
    public List<GeneInfo> transformPost(TransformerQuery query) throws Exception {
    	return Producer.produceGeneSet(query);
    }

    @Override
    public TransformerInfo transformerInfoGet() throws Exception {
        return Producer.transformerInfo();
    }

}
