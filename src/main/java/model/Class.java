package model;

import org.semanticweb.owlapi.model.IRI;

public class Class {

    private IRI iri;

    public IRI getIRI() {
        return iri;
    }

    public String getClassName() {
        return iri.getFragment();
    }

    public void setOWLClass(IRI iri) {
        this.iri = iri;
    }

    public Class(IRI iri) {
        setOWLClass(iri);
    }

    @Override
    public String toString() {
        return String.format ("%s", iri);
    }
}
