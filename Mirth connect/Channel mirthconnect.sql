INSERT INTO patient (patientid, nom, prenom, civilite, nomusage, datedenaissance, sexe, adresse1, adresse2, codepostal, ville, pays, telephone, paysnaissance)
VALUES (CAST(${patientid} AS INT), ${nompatient}, ${prenompatient}, ${civilite}, ${nomusage}, CAST(${ddn} AS DATE), ${sexe}, ${adress1}, ${adress2}, ${cp}, ${ville}, ${pays}, ${telephone}, ${paysnaissance});

INSERT INTO sejour (sejourid, patientid, datedesejour, uf, chambre, lit, ufmedecin)
VALUES (CAST(${sejourid} AS INT), CAST(${patientid} AS INT), CAST(${datedesejour} AS TIMESTAMP), ${uf}, CAST(${chambre} AS INT), CAST(${lit} AS INT),${ufmedecin})