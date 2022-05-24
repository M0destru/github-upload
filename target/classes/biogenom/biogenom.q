[QueryItem="По типу биомаркера"]
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>

PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT ?diagnostic_report_id ?create_date ?collect_date {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :collectedAt ?collect_date ; :createdAt ?create_date.
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker rdfs:label "Биомаркер" ; :biomarkerName "Лейкоциты (WBC)" .
}
ORDER BY ?create_date ?collect_date
[QueryItem="По типу биоматериала"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id  {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker :hasBiomaterial ?biomaterial .
	?biomaterial :biomaterialName "Венозная кровь" .
}
[QueryItem="По временному промежутку"]
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>

PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report a :DiagnosticReport ; :createdAt ?create_date; :idDiagnosticReport ?diagnostic_report_id .
	FILTER (?create_date < "2022-01-26T19:00:00+05:00"^^xsd:dateTime)
}
[QueryItem="По значению"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report a :DiagnosticReport ; :idDiagnosticReport ?diagnostic_report_id .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	OPTIONAL {
		?observation :hasObservationEnumValue ?enum_value .
		?enum_value :enumValueName ?enum_value_name .
	}
	OPTIONAL {
		?observation :valueDecimal ?value_decimal .
	}
	FILTER (?enum_value_name = "обнаружены" || ?value_decimal < 15)
}
[QueryItem="По типу биомаркера, по типу биоматериала"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker :hasBiomaterial ?biomaterial ; :biomarkerName "Лейкоциты (WBC)" .
	?biomaterial :biomaterialName "Венозная кровь" .
}
[QueryItem="По типу биомаркера, по временному промежутку"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker :biomarkerName "Лейкоциты (WBC)" .
	FILTER (?create_date = "2022-01-26T19:00:00+05:00"^^xsd:dateTimeStamp)
}
[QueryItem="По типу биомаркера, по значению"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	OPTIONAL {
		?observation :hasObservationEnumValue ?enum_value .
		?enum_value :enumValueName ?enum_value_name .
	}
	OPTIONAL {
		?observation :valueDecimal ?value_decimal .
	}
	FILTER (?enum_value_name < 6.10 || ?value_decimal < 6.10)
	?observation :hasBiomarker ?biomarker .
	?biomarker :biomarkerName "Лейкоциты (WBC)" .
}
[QueryItem="По типу биоматериала, по временному промежутку"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :collectedAt ?collect_date ; :createdAt ?create_date.
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker :hasBiomaterial ?biomaterial .
	?biomaterial :biomaterialName "Венозная кровь" .
	FILTER (?create_date < "2022-01-26T19:00:00+05:00"^^xsd:dateTime)
}
[QueryItem="По типу биомаркера, по типу биоматериала, по временному промежутку"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :collectedAt ?collect_date ; :createdAt ?create_date.
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker :hasBiomaterial ?biomaterial ; :biomarkerName "Лейкоциты (WBC)" .
	?biomaterial :biomaterialName "Венозная кровь" .
	FILTER (?create_date < "2022-01-26T19:00:00+05:00"^^xsd:dateTime)
}
[QueryItem="По типу биомаркера, по типу биоматериала, по значению"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :collectedAt ?collect_date ; :createdAt ?create_date.
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	OPTIONAL {
		?observation :hasObservationEnumValue ?enum_value .
		?enum_value :enumValueName ?enum_value_name .
	}
	OPTIONAL {
		?observation :valueDecimal ?value_decimal .
	}
	FILTER (?enum_value_name = 6.09 || ?value_decimal = 6.09)
	?observation :hasBiomarker ?biomarker .
	?biomarker :hasBiomaterial ?biomaterial ; :biomarkerName "Лейкоциты (WBC)" .
	?biomaterial :biomaterialName "Венозная кровь" .
}
[QueryItem="По типу биомаркера, по временному промежутку, по значению"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	OPTIONAL {
		?observation :hasObservationEnumValue ?enum_value .
		?enum_value :enumValueName ?enum_value_name .
	}
	OPTIONAL {
		?observation :valueDecimal ?value_decimal .
	}
	FILTER (?enum_value_name < 7 || ?value_decimal < 7)
	?observation :hasBiomarker ?biomarker .
	?biomarker :biomarkerName "Лейкоциты (WBC)" .
	FILTER (?create_date < "2022-01-26T19:00:00+05:00"^^xsd:dateTime)
}
[QueryItem="По типу биомаркера, по типу биоматериала, по временному промежутку, по значению"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :collectedAt ?collect_date ; :createdAt ?create_date.
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	OPTIONAL {
		?observation :hasObservationEnumValue ?enum_value .
		?enum_value :enumValueName ?enum_value_name .
	}
	OPTIONAL {
		?observation :valueDecimal ?value_decimal .
	}
	FILTER (?enum_value_name < 7 || ?value_decimal < 7)
	?observation :hasBiomarker ?biomarker .
	?biomarker :hasBiomaterial ?biomaterial ; :biomarkerName "Лейкоциты (WBC)" .
	?biomaterial :biomaterialName "Венозная кровь" .
	FILTER (?create_date < "2022-01-26T19:00:00+05:00"^^xsd:dateTime)
}
[QueryItem="Без ограничений"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report a :DiagnosticReport ; :idDiagnosticReport ?diagnostic_report_id .
}
[QueryItem="По лаборатории"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :madeInLab ?lab ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?lab :laboratoryName "Лаб.МОВС" .
}
[QueryItem="По типу биомаркера, по лаборатории"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :madeInLab ?lab ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?lab :laboratoryName "Лаб.МОВС" .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker :biomarkerName "Лейкоциты (WBC)" .
}
[QueryItem="По типу биоматериала, по лаборатории"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :madeInLab ?lab ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?lab :laboratoryName "Лаб.МОВС" .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker :hasBiomaterial ?biomaterial .
	?biomaterial :biomaterialName "Венозная кровь" .
}
[QueryItem="По временному промежутку, по лаборатории"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :madeInLab ?lab ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?lab :laboratoryName "Лаб.МОВС" .
	FILTER (?create_date < "2022-01-26T19:00:00+05:00"^^xsd:dateTime)
}
[QueryItem="По типу биомаркера, по временному промежутку, по лаборатории"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :madeInLab ?lab ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?lab :laboratoryName "Лаб.МОВС" .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker :biomarkerName "Лейкоциты (WBC)" .
	FILTER (?create_date < "2022-01-26T19:00:00+05:00"^^xsd:dateTime)
}
[QueryItem="По типу биомаркера, по типу биоматериала, по лаборатории"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :madeInLab ?lab ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?lab :laboratoryName "Лаб.МОВС" .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker :hasBiomaterial ?biomaterial ; :biomarkerName "Лейкоциты (WBC)" .
	?biomaterial :biomaterialName "Венозная кровь" .
}
[QueryItem="По типу биомаркера, по лаборатории, по значению"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :madeInLab ?lab ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?lab :laboratoryName "Лаб.МОВС" .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	OPTIONAL {
		?observation :hasObservationEnumValue ?enum_value .
		?enum_value :enumValueName ?enum_value_name .
	}
	OPTIONAL {
		?observation :valueDecimal ?value_decimal .
	}
	FILTER (?enum_value_name < 6.10 || ?value_decimal < 6.10)
	?observation :hasBiomarker ?biomarker .
	?biomarker :biomarkerName "Лейкоциты (WBC)" .
}
[QueryItem="По типу биоматериала, по временному промежутку, по лаборатории"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :madeInLab ?lab ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?lab :laboratoryName "Лаб.МОВС" .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker :hasBiomaterial ?biomaterial .
	?biomaterial :biomaterialName "Венозная кровь" .
	FILTER (?create_date < "2022-01-26T19:00:00+05:00"^^xsd:dateTime)
}
[QueryItem="По типу биомаркера, по типу биоматериала, по временному промежутку, по лаборатории"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :madeInLab ?lab ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?lab :laboratoryName "Лаб.МОВС" .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	?observation :hasBiomarker ?biomarker .
	?biomarker :hasBiomaterial ?biomaterial ; :biomarkerName "Лейкоциты (WBC)" .
	?biomaterial :biomaterialName "Венозная кровь" .
	FILTER (?create_date < "2022-01-26T19:00:00+05:00"^^xsd:dateTime)
}
[QueryItem="По типу биомаркера, по типу биоматериала, по временному промежутку, по лаборатории, по значению"]
PREFIX : <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#>

SELECT DISTINCT ?diagnostic_report_id {
	?diagnostic_report :idDiagnosticReport ?diagnostic_report_id ; :madeInLab ?lab ; :collectedAt ?collect_date ; :createdAt ?create_date .
	?lab :laboratoryName "Лаб.МОВС" .
	?diagnostic_report_file :isDiagnosticReportFileOf ?diagnostic_report .
	?observation :hasRecordIn ?diagnostic_report_file .
	OPTIONAL {
		?observation :hasObservationEnumValue ?enum_value .
		?enum_value :enumValueName ?enum_value_name .
	}
	OPTIONAL {
		?observation :valueDecimal ?value_decimal .
	}
	FILTER (?enum_value_name < 7 || ?value_decimal < 7)
	?observation :hasBiomarker ?biomarker .
	?biomarker :hasBiomaterial ?biomaterial ; :biomarkerName "Лейкоциты (WBC)" .
	?biomaterial :biomaterialName "Венозная кровь" .
	FILTER (?create_date < "2022-01-26T19:00:00+05:00"^^xsd:dateTime)
}
[QueryItem="test"]
SELECT DISTINCT ?diagnostic_report_id ?create_date ?collect_date
WHERE {
	?observation <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#valueDecimal> ?valuedecimal  .
	?biomarker <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#biomarkerName> ?biomarkername  .
	?diagnosticreport <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#createdAt> ?createdat ; <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#collectedAt> ?collectedat  .
	?observation <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#hasBiomarker> ?biomarker .
	?observation <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#hasRecordIn> ?diagnosticreportfile .
	?diagnosticreportfile <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#isDiagnosticReportFileOf> ?diagnosticreport .
}
FILTER (?observation > 4.0999999999999996447286321199499070644378662109375 && ?biomarker = глюкоза )
ORDER BY ?create_date ?collect_date