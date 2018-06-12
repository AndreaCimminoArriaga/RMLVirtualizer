# RML-Virtualizer


RML-Virtualizer is an extension of the [RML Mapper project](https://github.com/RMLio/RML-Mapper) that aims at keeping the RDF generated in memory rather than stored in a file.

## Instalation

## Usage

RML-Virtualizer only has a class with one method, find below a snippet of how can be invoked

```
// init

String mappingContent = ... # read the mapping and return here its content
String baseIRI = ... # Not null
RDFFormat outputFormat = RDFFormat.JSONLD # Check the javadoc of class RDFFormat to find more formats

RMLVirtualizer virtualizer = new RMLVirtualizer();
String virtualizedRDF =  virtualizer.virtualize(mappingContent, baseIRI, outputFormat);
```
