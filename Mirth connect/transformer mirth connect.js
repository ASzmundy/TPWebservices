channelMap.put('patientid',msg['PID']['PID.3']['PID.3.1'].toString());
channelMap.put('sejourid',msg['PV1']['PV1.19']['PV1.19.1'].toString());

channelMap.put('nompatient',msg['PID']['PID.5']['PID.5.1'].toString());
channelMap.put('prenompatient',msg['PID']['PID.5']['PID.5.2'].toString());
channelMap.put('civilite',msg['PID']['PID.5']['PID.5.5'].toString());
channelMap.put('nomusage',msg['PID']['PID.6']['PID.6.1'].toString());



var rawDate = msg['PID']['PID.7']['PID.7.1'].toString().trim();
var formatter = java.text.SimpleDateFormat("yyyyMMddHHmmss");
var date = formatter.parse(rawDate);


formatter = java.text.SimpleDateFormat("MM-dd-yyyy");
var cleaneddate = formatter.format(date);
channelMap.put('ddn',cleaneddate.toString());




channelMap.put('sexe',msg['PID']['PID.8']['PID.8.1'].toString());
channelMap.put('adress1',msg['PID']['PID.11']['PID.11.1'].toString());
channelMap.put('adress2',msg['PID']['PID.11']['PID.11.2'].toString());
channelMap.put('cp',msg['PID']['PID.11']['PID.11.5'].toString());
channelMap.put('ville',msg['PID']['PID.11']['PID.11.3'].toString());
channelMap.put('pays',msg['PID']['PID.11']['PID.11.6'].toString());
channelMap.put('telephone',msg['PID']['PID.13']['PID.13.1'].toString());
channelMap.put('paysnaissance',msg['PID']['PID.28']['PID.28.1'].toString());

var rawDateTime = msg['PV1']['PV1.44']['PV1.44.1'].toString().trim();
var formatterdatetime = java.text.SimpleDateFormat("yyyyMMddHHmmss");
var datetime = formatterdatetime.parse(rawDateTime);

formatterdatetime = java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
var cleaneddatetime = formatterdatetime.format(datetime);
channelMap.put('datedesejour',cleaneddatetime.toString());


channelMap.put('uf',msg['PV1']['PV1.3']['PV1.3.1'].toString());
channelMap.put('lit',msg['PV1']['PV1.3']['PV1.3.3'].toString());
channelMap.put('chambre',msg['PV1']['PV1.3']['PV1.3.2'].toString());
channelMap.put('ufmedecin',msg['ZFU']['ZFU.3']['ZFU.3.1'].toString());