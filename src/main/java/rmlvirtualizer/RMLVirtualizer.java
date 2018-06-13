package rmlvirtualizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import be.ugent.mmlab.rml.core.StdRMLEngine;
import be.ugent.mmlab.rml.mapdochandler.extraction.std.StdRMLMappingFactory;
import be.ugent.mmlab.rml.model.RMLMapping;
import be.ugent.mmlab.rml.model.dataset.RMLDataset;
import be.ugent.mmlab.rml.model.dataset.StdRMLDataset;

/**
 * This class wraps the official RML engine. Instead of materialising RDF (as
 * the RML engine does) this class aims at virtualize the RDF, i.e., keep the
 * RDF translation in memory
 * 
 * @author Andrea Cimmino
 */
public class RMLVirtualizer {

	// -- No constructors

	/**
	 * This method generates virtual RDF following a RML Mapping
	 * 
	 * @param mappingContent
	 *            A RML Mapping, notice that this is the content of the mapping and
	 *            not the file directory
	 * @param baseIRI
	 *            The IRI based that will be used, if needed, during the
	 *            virtualization
	 * @param format
	 *            The output RDF format to which the data in other formats will be
	 *            translated
	 * @return A String containing the virtualized RDF
	 */
	public String virtualize(String mappingContent, String baseIRI, RDFFormat outputFormat) {
		String rdfVirtualized = null;

		try {
			// Creating engine
			StdRMLEngine engine = new StdRMLEngine();

			// Reading mappings
			RMLMapping mapping = getMapping(mappingContent, baseIRI, RDFFormat.TURTLE);
			if (mapping != null) {
				// Virtualizing
				RMLDataset dataset = new StdRMLDataset();
				engine.generateRDFTriples(dataset, mapping, null, null, null);

				// Showing stuff
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				dataset.dumpRDF(outputStream, outputFormat);

				rdfVirtualized = new String(outputStream.toByteArray(), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			// Empty
		}

		return rdfVirtualized;
	}

	/**
	 * This method initialises a RML Mapping object
	 * 
	 * @param rmlMapping
	 *            A RML Mapping, notice that this is the content of the mapping and
	 *            not the file directory
	 * @param baseIRI
	 *            The IRI based that will be used, if needed, during the
	 *            virtualization
	 * @param format
	 *            The output RDF format to which the data in other formats will be
	 *            translated
	 * @return A RMLMapping object
	 */
	private RMLMapping getMapping(String rmlMapping, String baseIRI, RDFFormat format) {
		StdRMLMappingFactory mappingFactory = new StdRMLMappingFactory();
		Repository reposiotry = new SailRepository(new MemoryStore());
		RMLMapping mapping = null;

		try {
			// Write the mapping in the Repository
			reposiotry.initialize();
			RepositoryConnection repositoryConnection = reposiotry.getConnection();
			InputStream content = new ByteArrayInputStream(rmlMapping.getBytes(StandardCharsets.UTF_8));
			repositoryConnection.add(content, baseIRI, format);
			if (repositoryConnection.size() > 0) // if was correctly written, generate mapping object
				mapping = mappingFactory.extractRMLMapping(reposiotry);
			repositoryConnection.close();

		} catch (Exception e) {
			// Empty: since content is passed by argument no IO exception will be thrown
		}

		return mapping;
	}

}
