package controllers;

import apimodels.GeneInfo;
import apimodels.TransformerInfo;
import apimodels.TransformerQuery;

import play.mvc.Http;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileInputStream;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaPlayFrameworkCodegen", date = "2019-07-25T22:54:00.554Z")

public class DefaultApiControllerImp implements DefaultApiControllerImpInterface {
    @Override
    public List<GeneInfo> transformPost(TransformerQuery query) throws Exception {
        //Do your magic!!!
        return new ArrayList<GeneInfo>();
    }

    @Override
    public TransformerInfo transformerInfoGet() throws Exception {
        //Do your magic!!!
        return new TransformerInfo();
    }

}
