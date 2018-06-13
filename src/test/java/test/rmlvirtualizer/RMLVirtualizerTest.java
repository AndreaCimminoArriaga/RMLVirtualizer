package test.rmlvirtualizer;


import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.Test;

import junit.framework.Assert;
import rmlvirtualizer.RMLVirtualizer;


/**
 * Created by Andrea Cimmino on 12/06/18
 */
public class RMLVirtualizerTest {


	/**
     * Test case 1: Virtualize JSON data
     */
    @Test
    public void testAdditionAllPositive() {
    		// init
    		String mappingContent = "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
    				"@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
    				"@prefix ql: <http://semweb.mmlab.be/ns/ql#>.\n" + 
    				"@prefix transit: <http://vocab.org/transit/terms/>.\n" + 
    				"@prefix xsd: <http://www.w3.org/2001/XMLSchema#>.\n" + 
    				"@prefix wgs84_pos: <http://www.w3.org/2003/01/geo/wgs84_pos#>.\n" + 
    				"\n" + 
    				"<#AirportMapping>\n" + 
    				"  rml:logicalSource [\n" + 
    				"    rml:source \"./src/test/resources/test1-rmlvirtualizer.csv\" ;\n" + 
    				"    rml:referenceFormulation ql:CSV \n" + 
    				"  ];\n" + 
    				"  rr:subjectMap [\n" + 
    				"    rr:template \"http://airport.example.com/{id}\";\n" + 
    				"    rr:class transit:Stop \n" + 
    				"  ];\n" + 
    				"\n" + 
    				"  rr:predicateObjectMap [\n" + 
    				"    rr:predicate transit:route;\n" + 
    				"    rr:objectMap [\n" + 
    				"      rml:reference \"stop\";\n" + 
    				"      rr:datatype xsd:int\n" + 
    				"      ]\n" + 
    				"    ];\n" + 
    				"\n" + 
    				"  rr:predicateObjectMap [\n" + 
    				"    rr:predicate wgs84_pos:lat;\n" + 
    				"    rr:objectMap [\n" + 
    				"      rml:reference \"latitude\"\n" + 
    				"    ]\n" + 
    				"  ];\n" + 
    				"\n" + 
    				"  rr:predicateObjectMap [\n" + 
    				"    rr:predicate wgs84_pos:long;\n" + 
    				"    rr:objectMap [\n" + 
    				"      rml:reference \"longitude\"\n" + 
    				"    ]\n" + 
    				"  ].";
    		
    		String baseIRI = "http://pegaso.com/example#";
    		RDFFormat outputFormat= RDFFormat.N3;
    		// virtualize
    		RMLVirtualizer virtualizer = new RMLVirtualizer();
    		String virtualizedRDF =  virtualizer.virtualize(mappingContent, baseIRI, outputFormat).trim();
    		// gold std
        String desiredResult = "<http://airport.example.com/6523> a <http://vocab.org/transit/terms/Stop> ;\n" + 
        		"	<http://www.w3.org/2003/01/geo/wgs84_pos#long> \"4.484444\" ;\n" + 
        		"	<http://www.w3.org/2003/01/geo/wgs84_pos#lat> \"50.901389\" ;\n" + 
        		"	<http://vocab.org/transit/terms/route> \"25\"^^<http://www.w3.org/2001/XMLSchema#int> .";
        
       
        Boolean aggregator = true & !virtualizedRDF.isEmpty();
        String[] lines = virtualizedRDF.split("\n");
        for(int index=0; index<lines.length; index++) {
        		String line = lines[index].trim();
        		aggregator &= desiredResult.contains(line);
        }
        	
        	Assert.assertTrue(aggregator);
    }
	
}
