package model;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Ontology {

    private OWLDataFactory owlDataFactory;
    private OWLOntology ontology;
    private ArrayList<Class> classes;
    private ArrayList<DataProperty> dataProperties;
    private ArrayList<ObjectProperty> objectProperties;

    public OWLOntology getOntology() { return ontology; }

    public ArrayList<Class> getOWLClasses() { return classes; }

    public ArrayList<DataProperty> getDataProperties() { return dataProperties; }

    public ArrayList<ObjectProperty> getObjectProperties() { return objectProperties; }

    public void setOntology(OWLOntology ontology) { this.ontology = ontology; }

    public boolean InitializeOntology(String pathToOntology) {
        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();

        try {
            ontology = owlOntologyManager.loadOntologyFromOntologyDocument(new File(pathToOntology));
        } catch (OWLOntologyCreationException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        classes = new ArrayList<>();
        objectProperties = new ArrayList<>();
        dataProperties = new ArrayList<>();

        for (var c : ontology.getClassesInSignature()) {
            classes.add(new Class(c.getIRI()));
        }

        for (var dataProperty : ontology.getDataPropertiesInSignature()) {
            for (var domain : ontology.getDataPropertyDomainAxioms(dataProperty)) {
                for (var range : ontology.getDataPropertyRangeAxioms(dataProperty)) {
                    var domainClass = SearchClassByIRI(domain.getDomain().asOWLClass().getIRI());
                    dataProperties.add(new DataProperty(dataProperty.getIRI(), domainClass, range.getRange()));
                }
            }
        }
        for (var objectProperty : ontology.getObjectPropertiesInSignature()) {
            for (var source : ontology.getObjectPropertyDomainAxioms(objectProperty)) {
                for (var target : ontology.getObjectPropertyRangeAxioms(objectProperty)) {
                    var sourceClass = SearchClassByIRI(source.getDomain().asOWLClass().getIRI());
                    var targetClass = SearchClassByIRI(target.getRange().asOWLClass().getIRI());
                    objectProperties.add(new ObjectProperty(objectProperty.getIRI(), sourceClass, targetClass));
                }
            }
        }
        return true;
    }

    /* найти класс по IRI */
    public Class SearchClassByIRI(IRI iri) {
        return classes.stream().filter(c -> c.getIRI().equals(iri)).findFirst().get();
    }

    public ObjectProperty GetObjectProperty (Class source, Class target) {
        /*return objectProperties.stream().filter(objProp -> objProp.getSource() == source && objProp.getTarget() == target).
                findFirst().orElse(null);*/
        return objectProperties.stream().filter(objProp -> (objProp.getSource() == source && objProp.getTarget() == target )
                || (objProp.getSource() == target && objProp.getTarget() == source)).findFirst().orElse(null);
    }

    public List<Class> GetNeighborClasses (Class owlclass) {
        /*return objectProperties.stream().filter(objProp -> objProp.getSource() == owlclass).
                map(ObjectProperty::getTarget).toList();*/
        return Stream.concat(objectProperties.stream().filter(objProp -> objProp.getTarget() == owlclass).map(ObjectProperty::getSource),
                objectProperties.stream().filter(objProp -> objProp.getSource() == owlclass).map(ObjectProperty::getTarget)).toList();
    }

    /* получить класс по метке */
    public OWLClass GetClassByLabel(String label) {
        for (OWLClass c : ontology.getClassesInSignature()) {
            // Get the annotations on the class that use the label property (rdfs:label)
            for (OWLAnnotation a : EntitySearcher.getAnnotations(c, ontology, owlDataFactory.getRDFSLabel())) {
                OWLAnnotationValue aVal = a.getValue();
                if (aVal instanceof OWLLiteral && ((OWLLiteral) aVal).getLiteral().equals(label)) {
                    return c;
                }
            }
        }
        return null;
    }
}
