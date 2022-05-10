CREATE  TABLE IF NOT EXISTS public.patient ( 
	patientid            integer  NOT NULL  ,
	nom                  varchar(100)  NOT NULL  ,
	prenom               varchar(50)  NOT NULL  ,
	civilite             VARCHAR(50)    ,
	nomusage             varchar(100)    ,
	datedenaissance      date NOT NULL  ,
	sexe                 char  NOT NULL  ,
	adresse1             varchar(500)    ,
	adresse2             varchar(500)    ,
	codepostal           varchar(10)    ,
	ville                varchar(100)    ,
	pays                 varchar(100)    ,
	telephone            varchar(20)    ,
	paysnaissance        varchar(100)    ,
	CONSTRAINT idx_entity PRIMARY KEY ( patientid )
 );
 
CREATE  TABLE IF NOT EXISTS public.sejour ( 
	sejourid             integer  NOT NULL  ,
	patientid            integer  NOT NULL  ,
	datedesejour         timestamp NOT NULL  ,
	uf                   varchar(100)    ,
	chambre              integer  NOT NULL  ,
	lit                  integer  NOT NULL  ,
	ufmedecin            varchar(100)  NOT NULL  ,
	datefinsejour	     timestamp,
	CONSTRAINT idx_sejour PRIMARY KEY ( sejourid ),
	CONSTRAINT fk_sejour_patient
      FOREIGN KEY(patientid) 
	  REFERENCES patient(patientid)
 );

