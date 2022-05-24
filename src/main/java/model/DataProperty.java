package model;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataRange;

public class DataProperty {

    private IRI iri;
    private Class domain;
    private OWLDataRange range;

    public IRI getIRI() {
        return iri;
    }

    public Class getDomain() {
        return domain;
    }

    public OWLDataRange getRange() {
        return range;
    }

    public void setDataProperty(IRI iri, Class domain, OWLDataRange range) {
        this.iri = iri;
        this.domain = domain;
        this.range = range;
    }

    public DataProperty(IRI iri, Class domain, OWLDataRange range) {
        setDataProperty(iri, domain, range);
    }
}
