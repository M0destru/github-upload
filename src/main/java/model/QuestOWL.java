package model;

import it.unibz.inf.ontop.injection.OntopSQLOWLAPIConfiguration;
import it.unibz.inf.ontop.iq.IQ;
import it.unibz.inf.ontop.owlapi.OntopOWLFactory;
import it.unibz.inf.ontop.owlapi.OntopOWLReasoner;
import it.unibz.inf.ontop.owlapi.connection.OntopOWLConnection;
import it.unibz.inf.ontop.owlapi.connection.OntopOWLStatement;
import it.unibz.inf.ontop.owlapi.resultset.OWLBindingSet;
import it.unibz.inf.ontop.owlapi.resultset.TupleOWLResultSet;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.ArrayList;

public class QuestOWL {

	private OntopSQLOWLAPIConfiguration config;
	private OntopOWLReasoner reasoner;

	public QuestOWL(String OWLFile, String OBDAFile, String PropertyFile) {
		config = OntopSQLOWLAPIConfiguration.defaultBuilder()
				.ontologyFile(OWLFile)
				.nativeOntopMappingFile(OBDAFile)
				.propertyFile(PropertyFile)
				.enableTestMode()
				.build();
	}

	public boolean StartReasoner() {
		OntopOWLFactory factory = OntopOWLFactory.defaultFactory();

		/* Create the instance of Ontop OWL Reasoner.  */
		try {
			reasoner = factory.createReasoner(config);
		} catch (OWLOntologyCreationException ex) {
			System.out.println(ex.getMessage());
		}

		return reasoner != null;
	}

	public void StopReasoner() {
		try {
			if (!reasoner.getConnection().isClosed()) {
				reasoner.getConnection().close();
			}
			reasoner.dispose();
		} catch (OWLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public ArrayList<OWLBindingSet> ExecuteSparqlQuery(String sparqlQuery) throws Exception {
		ArrayList<OWLBindingSet> bindings = new ArrayList<>();
		/* Prepare the data connection for querying */
		try (OntopOWLConnection conn = reasoner.getConnection();
			 OntopOWLStatement st = conn.createStatement()) {

			TupleOWLResultSet rs = st.executeSelectQuery(sparqlQuery);
			while (rs.hasNext()) {
				OWLBindingSet bindingSet = rs.next();
				bindings.add(bindingSet);
			}
		}
		return bindings;
	}

	public String GetSQLQuery(String sparqlQuery) throws OWLException {
		String sqlQuery;
		try (OntopOWLConnection conn = reasoner.getConnection();
			 OntopOWLStatement st = conn.createStatement();
		)
		{
			IQ executableQuery = st.getExecutableQuery(sparqlQuery);
			sqlQuery = executableQuery.toString();
		}
		return sqlQuery;
	}
}


