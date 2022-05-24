package model;

import org.semanticweb.owlapi.model.IRI;

public class ObjectProperty {

    private IRI iri;
    private Class source;
    private Class target;

    public IRI getIRI() {
        return iri;
    }

    public Class getSource() {
        return source;
    }

    public String getSourceStr() {
        return "<" + source + ">";
    }

    public Class getTarget() {
        return target;
    }

    public void SetDataProperty(IRI iri, Class source, Class target) {
        this.iri = iri;
        this.source = source;
        this.target = target;
    }

    public ObjectProperty(IRI iri, Class source, Class target) {
        SetDataProperty(iri, source, target);
    }
}
