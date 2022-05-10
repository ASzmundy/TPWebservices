import datetime
import json
import requests

import PySimpleGUI as sg

sg.theme('BluePurple')  # Couleur

while True:
    # Programme menu principal
    # Menu d'accueil
    mainlayout = [
        [sg.Text('URL Webservice'), sg.InputText('http://localhost:8800/service/request', key='url')]  # Identifiant
        ,[sg.Button('Ajouter patient')]
        ,[sg.Button('Voir patient')]
        ,[sg.Button('Ajouter séjour')]
        ,[sg.Button('Fin de séjour')]
        ,[sg.Button('Modifier patient')]
        ,[sg.Button('Supprimer patient')]
        ,[sg.Button('Quitter')]

    ]
    mainwindow = sg.Window('GUI TP Webservices', mainlayout, icon='Images/icone.ico').Finalize()
    event, values = mainwindow.read()
    if event in (None, 'Quitter'):  # if user closes window or clicks cancel
        break



    if event in ('Ajouter patient'):
        url = values['url']
        mainwindow.close()
        mainlayout.clear()
        # Layout menu ajout
        addlayout = [
            [sg.Text('* = obligatoire')],
            [sg.Text('Nom du patient*'), sg.InputText(key='namebox')],  # Nom
            [sg.Text('Prénom du patient*'), sg.InputText(key='firstnamebox')],  # Prénom
            [sg.Text('Civilité du patient'), sg.InputText(key='civilitybox')],  # civilité
            [sg.Text('Nom d\'usage du patient'), sg.InputText(key='nommarbox')],  # nom de marié
            [sg.Text('Date de naissance*'), sg.Input(key='birthdate', size=(20, 1)),
             sg.CalendarButton('Choisir date', target='birthdate', default_date_m_d_y=(1, None, 2022), locale='fr_FR',
                               begin_at_sunday_plus=1, format='%d/%m/%Y')],
            [sg.Text('Pays de naissance du patient'), sg.InputText(key='birthcountrybox')],
            [sg.Text('Sexe*'),sg.Combo(['H','F'], key='sexbox')],
            [sg.Text('Adresse du patient'), sg.InputText(key='address1box')],
            [sg.Text('Adresse 2 du patient'), sg.InputText(key='address2box')],
            [sg.Text('Code postal'), sg.InputText(key='CPbox')],
            [sg.Text('Ville'), sg.InputText(key='citybox')],
            [sg.Text('Pays'), sg.InputText(key='countrybox')],
            [sg.Text('Numéro de téléphone'), sg.InputText(key='phonebox')],
            [sg.Text('Date de séjour*'), sg.Input(key='sejourdate', size=(20, 1)),
             sg.CalendarButton('Choisir date', target='sejourdate', default_date_m_d_y=(1, None, 2022), locale='fr_FR',
                               begin_at_sunday_plus=1, format='%d/%m/%Y')],
            [sg.Text('Heure d\'entrée')],
            [[sg.Spin([i for i in range(0, 23)], initial_value=datetime.datetime.now().hour, key='hours'),
              sg.Text('Heures'),
              sg.Spin([i for i in range(0, 59)], initial_value=datetime.datetime.now().minute, key='minutes'),
              sg.Text('Minutes'),
              sg.Spin([i for i in range(0, 59)], initial_value=datetime.datetime.now().minute, key='seconds'),
              sg.Text('Secondes')]],
            [sg.Text('Unité du patient'), sg.InputText(key='UFbox')],
            [sg.Text('Chambre*'), sg.InputText(key='roombox')],
            [sg.Text('Numéro de lit*'), sg.InputText(key='bedbox')],
            [sg.Text('UF du médecin*'), sg.InputText(key='UFMEDbox')],

            [sg.Button('Ok'), sg.Button('Quitter')]
        ]
        addwindow = sg.Window('Ajouter patient', addlayout, icon='Images/icone.ico').Finalize()
        # window.Maximize()
        # Event Loop to process "events" and get the "values" of the inputs
        while True:
            event, values = addwindow.read()
            if event in (None,'Quitter'):
                break


            if len(str(values['hours'])) < 2:
                heuresdds = '0' + str(values['hours'])
            else :
                heuresdds = str(values['hours'])

            if len(str(values['minutes'])) < 2:
                minutesdds = '0' + str(values['minutes'])
            else :
                minutesdds = str(values['minutes'])

            if len(str(values['seconds'])) < 2:
                secondesdds = '0' + str(values['seconds'])
            else :
                secondesdds = str(values['seconds'])

            dateentree = values['sejourdate'] + ' ' + heuresdds + ':' + secondesdds + ':' + minutesdds
            requestjson = json.dumps(
                {'requestType': 'add', 'nom': values['namebox'], 'prenom': values['firstnamebox']
                 ,'intit': values['civilitybox'], 'nommar': values['nommarbox']
                 ,'ddn': values['birthdate'], 'sexe': values['sexbox']
                 ,'adr1': values['address1box'], 'adr2': values['address2box'], 'cp': values['CPbox']
                 ,'ville': values['citybox'], 'pays': values['countrybox'], 'tel': values['phonebox']
                 ,'paysn': values['birthcountrybox'], 'dds': dateentree, 'uf': values['UFbox']
                 ,'chambre': values['roombox'], 'lit': values['bedbox'], 'ufmed': values['UFMEDbox']
                 ,'numpas': '0'})  # Génère la requête JSON à partir des entrées
            print(requestjson)
            try:
                response = requests.post(url, data=requestjson, headers={'Content-Type': 'application/json'})
                print(response)
            except requests.exceptions.RequestException as e:
                # Popup d'erreur
                w, h = sg.Window.get_screen_size()
                errorlayout = [
                    [sg.Text('Erreur lors de l\'exécution de la requête !')],
                    [sg.Multiline(e, size=(200, 18), disabled=True)]
                ]
                errorwindow = sg.Window('CPT', errorlayout, background_color='red', size=(int(w / 3), int(h / 3)))
                errorevent = errorwindow.read()
                if (errorevent in (None, 'Ok')):
                    errorwindow.close()
        addwindow.close()



    if event in ('Supprimer patient'):
        url = values['url']
        mainwindow.close()
        mainlayout.clear()
        # Layout menu ajout
        # Layout menu ajout
        dellayout = [
            [sg.Text('* = obligatoire')],
            [sg.Text('Identifiant permanent du patient*'), sg.InputText(key='ID')],  # Identifiant
            [sg.Text('OU')],
            [sg.Text('Nom du patient*'), sg.InputText(key='namebox')],  # Nom
            [sg.Text('Prénom du patient*'), sg.InputText(key='firstnamebox')],  # Prénom
            [sg.Text('Date de naissance*'), sg.Input(key='birthdate', size=(20, 1)),
             sg.CalendarButton('Choisir date', target='birthdate', default_date_m_d_y=(1, None, 2022), locale='fr_FR',
                               begin_at_sunday_plus=1, format='%d/%m/%Y')],

            [sg.Button('Ok'), sg.Button('Quitter')]
        ]
        delwindow = sg.Window('Supprimer patient', dellayout, icon='Images/icone.ico').Finalize()
        while True:
            event, values = delwindow.read()
            if event in (None,'Quitter'):
                break

            requestjson = json.dumps(
                {'requestType': 'del','ipp': values['ID'], 'nom': values['namebox'], 'prenom': values['firstnamebox'],
                 'ddn': values['birthdate'],
                 })  # Génère la requête JSON à partir des entrées
            print(requestjson)
            try:
                response = requests.post(url, data=requestjson, headers={'Content-Type': 'application/json'})
                print(response)
            except requests.exceptions.RequestException as e:
                # Popup d'erreur
                w, h = sg.Window.get_screen_size()
                errorlayout = [
                    [sg.Text('Erreur lors de l\'exécution de la requête !')],
                    [sg.Multiline(e, size=(200, 18), disabled=True)]
                ]

                errorwindow = sg.Window('CPT', errorlayout, background_color='red', size=(int(w / 3), int(h / 3)))
                errorevent = errorwindow.read()
                if (errorevent in (None, 'Ok')):
                    errorwindow.close()
        delwindow.close()




    if event in ('Voir patient'):
        url = values['url']
        mainwindow.close()
        mainlayout.clear()
        # Layout menu extraction
        seelayout = [
            [sg.Text('* = obligatoire')],
            [sg.Text('Identifiant permanent du patient'), sg.InputText(key='ID')],  # Identifiant
            [sg.Text('OU')],
            [sg.Text('Nom du patient'), sg.InputText(key='namebox')],  # Nom
            [sg.Text('Prénom du patient'), sg.InputText(key='firstnamebox')],  # Prénom
            [sg.Text('Date de naissance*'), sg.Input(key='birthdate', size=(20, 1)),
             sg.CalendarButton('Choisir date', target='birthdate', default_date_m_d_y=(1, None, 2022), locale='fr_FR',
                               begin_at_sunday_plus=1, format='%d/%m/%Y')],

            [sg.Button('Ok'), sg.Button('Quitter')]
        ]
        seewindow = sg.Window('Voir patient', seelayout, icon='Images/icone.ico').Finalize()
        while True:
            event, values = seewindow.read()
            if event in (None, 'Quitter'):
                break

            requestjson = json.dumps(
                {'requestType': 'see', 'ipp': values['ID'], 'nom': values['namebox'],
                 'prenom': values['firstnamebox'], 'ddn': values['birthdate'],
                 })  # Génère la requête JSON à partir des entrées
            print(requestjson)
            try:
                response = requests.post(url, data=requestjson, headers={'Content-Type': 'application/json'})
                response = str(response.json()).replace('\'','\"').replace('None','null')
                print(response)
                patient = json.loads(response)
                if(len(patient)>0):
                    patient = patient[0]
                    #Affichage de la réponse
                    responselayout = [
                        [sg.Text("Nom : " + patient["nom"]),
                        sg.Text("Prénom : " + patient["prenom"])],
                        [sg.Text("Nom d'usage : " + patient["nommar"])],
                        [sg.Text("Sexe : " + patient["sexe"]),
                         sg.Text("Civilité : " + patient["intit"])],
                        [sg.Text("Date de naissance : "+ patient["ddn"])],
                        [sg.Text("Pays de naissance : "+ patient["paysn"])],
                        [sg.Text("Adresse principale : "+ patient["adr1"])],
                        [sg.Text("Complément adresse : " + patient["adr2"])],
                        [sg.Text("Code postal : " + patient["cp"]),
                        sg.Text("Ville : " + patient["ville"])],
                        [sg.Text("Pays : " + patient["pays"])],
                        [sg.Text("Téléphone : " + patient["tel"])]
                    ]
                    i=0
                    sejourcolumn = []
                    for sejour in patient["sejours"]:
                        i = i + 1
                        sejourcolumn.append(
                            [sg.Text("\nSéjour " + str(i))],
                        )
                        sejourcolumn.append(
                            [sg.Text("Date d'entrée : "+ str(sejour["dds"]))],
                        )
                        if sejour["dfs"] is not None :
                            sejourcolumn.append(
                                [sg.Text("Date de sortie : " + str(sejour["dfs"]))],
                            )
                        sejourcolumn.append(
                            [sg.Text("Unité fonctionnelle : " + sejour["uf"])]
                        )
                        sejourcolumn.append(
                            [sg.Text("Chambre : " + str(sejour["chambre"]))]
                        )
                        sejourcolumn.append(
                            [sg.Text("Lit : " + str(sejour["lit"]))]
                        )
                        sejourcolumn.append(
                            [sg.Text("Unité fonctionnelle du médecin : " + sejour["ufmed"])]
                        )
                    responselayout.append([sg.Column(sejourcolumn, scrollable=True, vertical_scroll_only=True)])

                    responsewindow = sg.Window("Patient trouvé !", responselayout)
                    responseevent = responsewindow.read()


            except requests.exceptions.RequestException as e:
                # Popup d'erreur
                w, h = sg.Window.get_screen_size()
                errorlayout = [
                    [sg.Text('Erreur lors de l\'exécution de la requête !')],
                    [sg.Multiline(e, size=(200, 18), disabled=True)]
                ]

                errorwindow = sg.Window('CPT', errorlayout, background_color='red', size=(int(w / 3), int(h / 3)))
                errorevent = errorwindow.read()
                if (errorevent in (None, 'Ok')):
                    errorwindow.close()


        seewindow.close()


    if event in ('Modifier patient'):
        url = values['url']
        mainwindow.close()
        mainlayout.clear()
        # Layout menu modifier patient
        modlayout = [
            [sg.Text('* = obligatoire')],
            [sg.Text('Identifiant permanent du patient*'), sg.InputText(key='ID')],  # Identifiant
            [sg.Text('OU')],
            [sg.Text('Nom du patient*'), sg.InputText(key='namebox')],  # Nom
            [sg.Text('Prénom du patient*'), sg.InputText(key='firstnamebox')],  # Prénom
            [sg.Text('Date de naissance*'), sg.Input(key='birthdate', size=(20, 1)),
             sg.CalendarButton('Choisir date', target='birthdate', default_date_m_d_y=(1, None, 2022),
                               locale='fr_FR',
                               begin_at_sunday_plus=1, format='%d/%m/%Y')],

            [sg.Text('\nNouveau nom du patient'), sg.InputText(key='newnamebox')],  # Nom
            [sg.Text('Nouveau prénom du patient'), sg.InputText(key='newfirstnamebox')],  # Prénom
            [sg.Text('Nouvelle date de naissance'), sg.Input(key='newbirthdate', size=(20, 1)),
             sg.CalendarButton('Choisir date', target='newbirthdate', default_date_m_d_y=(1, None, 2022), locale='fr_FR',
                               begin_at_sunday_plus=1, format='%d/%m/%Y')],
            [sg.Text('Civilité du patient'), sg.InputText(key='civilitybox')],  # civilité
            [sg.Text('Nom d\'usage du patient'), sg.InputText(key='nommarbox')],  # nom de marié
            [sg.Text('Pays de naissance du patient'), sg.InputText(key='birthcountrybox')],
            [sg.Text('Sexe'), sg.Combo(['H', 'F'], key='sexbox')],
            [sg.Text('Adresse du patient'), sg.InputText(key='address1box')],
            [sg.Text('Adresse 2 du patient'), sg.InputText(key='address2box')],
            [sg.Text('Code postal'), sg.InputText(key='CPbox')],
            [sg.Text('Ville'), sg.InputText(key='citybox')],
            [sg.Text('Pays'), sg.InputText(key='countrybox')],
            [sg.Text('Numéro de téléphone'), sg.InputText(key='phonebox')],

            [sg.Button('Ok'), sg.Button('Quitter')]
        ]
        modwindow = sg.Window('Modifier Patient', modlayout, icon='Images/icone.ico').Finalize()
        while True:
            event, values = modwindow.read()
            if event in (None,'Quitter'):
                break

            requestjson = json.dumps(
                {'requestType': 'mod','ipp':values["ID"] ,'nom': values['namebox'], 'prenom': values['firstnamebox'],
                 'nvnom': values['newnamebox'], 'nvprenom': values['newfirstnamebox'], 'nvddn': values['newbirthdate']
                 ,'intit': values['civilitybox'], 'nommar': values['nommarbox']
                 ,'ddn': values['birthdate'], 'sexe': values['sexbox']
                 ,'adr1': values['address1box'], 'adr2': values['address2box'], 'cp': values['CPbox']
                 ,'ville': values['citybox'], 'pays': values['countrybox'], 'tel': values['phonebox']
                 ,'paysn': values['birthcountrybox']}
            )  # Génère la requête JSON à partir des entrées
            print(requestjson)
            try:
                response = requests.post(url, data=requestjson, headers={'Content-Type': 'application/json'})
                print(response)
            except requests.exceptions.RequestException as e:
                # Popup d'erreur
                w, h = sg.Window.get_screen_size()
                errorlayout = [
                    [sg.Text('Erreur lors de l\'exécution de la requête !')],
                    [sg.Multiline(e, size=(200, 18), disabled=True)]
                ]
                errorwindow = sg.Window('CPT', errorlayout, background_color='red', size=(int(w / 3), int(h / 3)))
                errorevent = errorwindow.read()
                if (errorevent in (None, 'Ok')):
                    errorwindow.close()
        modwindow.close()


    if event in ('Ajouter séjour'):
        url = values['url']
        mainwindow.close()
        mainlayout.clear()
        # Layout menu ajout séjour
        addsejlayout = [
            [sg.Text('* = obligatoire')],
            [sg.Text('Identifiant permanent du patient*'), sg.InputText(key='ID')],  # Identifiant
            [sg.Text('OU')],
            [sg.Text('Nom du patient*'), sg.InputText(key='namebox')],  # Nom
            [sg.Text('Prénom du patient*'), sg.InputText(key='firstnamebox')],  # Prénom
            [sg.Text('Date de naissance*'), sg.Input(key='birthdate', size=(20, 1)),
             sg.CalendarButton('Choisir date', target='birthdate', default_date_m_d_y=(1, None, 2022), locale='fr_FR',
                               begin_at_sunday_plus=1, format='%d/%m/%Y')],
            [sg.Text(' ')],
            [sg.Text('Date de séjour*'), sg.Input(key='sejourdate', size=(20, 1)),
             sg.CalendarButton('Choisir date', target='sejourdate', default_date_m_d_y=(1, None, 2022), locale='fr_FR',
                               begin_at_sunday_plus=1, format='%d/%m/%Y')],
            [sg.Text('Heure d\'entrée')],
            [[sg.Spin([i for i in range(0,23)], initial_value=datetime.datetime.now().hour, key='hours'), sg.Text('Heures'), sg.Spin([i for i in range(0,59)], initial_value=datetime.datetime.now().minute, key='minutes'), sg.Text('Minutes'),sg.Spin([i for i in range(0,59)], initial_value=datetime.datetime.now().minute, key='seconds'), sg.Text('Secondes')]],
            [sg.Text('Unité du patient'), sg.InputText(key='UFbox')],
            [sg.Text('Chambre*'), sg.InputText(key='roombox')],
            [sg.Text('Numéro de lit*'), sg.InputText(key='bedbox')],
            [sg.Text('UF du médecin*'), sg.InputText(key='UFMEDbox')],

            [sg.Button('Ok'), sg.Button('Quitter')]
        ]
        addsejwindow = sg.Window('Ajouter séjour', addsejlayout, icon='Images/icone.ico').Finalize()
        while True:
            event, values = addsejwindow.read()
            if event in (None, 'Quitter'):
                break

            dateentree = values['sejourdate'] + ' ' + str(values['hours']) + ':' + str(values['minutes']) + ':' + str(values[
                'seconds'])
            requestjson = json.dumps(
                {'requestType': 'addsej', 'ipp': values['ID'], 'nom': values['namebox'], 'ddn': values['birthdate'],
                 'prenom': values['firstnamebox'], 'dds': dateentree, 'uf': values['UFbox']
                 ,'chambre': values['roombox'], 'lit': values['bedbox'], 'ufmed': values['UFMEDbox']
                 ,'numpas': '0'
                 })  # Génère la requête JSON à partir des entrées
            print(requestjson)
            try:
                response = requests.post(url, data=requestjson, headers={'Content-Type': 'application/json'})
                print(response)
            except requests.exceptions.RequestException as e:
                # Popup d'erreur
                w, h = sg.Window.get_screen_size()
                errorlayout = [
                    [sg.Text('Erreur lors de l\'exécution de la requête !')],
                    [sg.Multiline(e, size=(200, 18), disabled=True)]
                ]

                errorwindow = sg.Window('CPT', errorlayout, background_color='red', size=(int(w / 3), int(h / 3)))
                errorevent = errorwindow.read()
                if (errorevent in (None, 'Ok')):
                    errorwindow.close()
        addsejwindow.close()


    if event in ('Fin de séjour'):
        url = values['url']
        mainwindow.close()
        mainlayout.clear()
        # Layout menu fin de séjour
        endsejlayout = [
            [sg.Text('* = obligatoire')],
            [sg.Text('Identifiant permanent du patient*'), sg.InputText(key='ID')],  # Identifiant
            [sg.Text('OU')],
            [sg.Text('Nom du patient*'), sg.InputText(key='namebox')],  # Nom
            [sg.Text('Prénom du patient*'), sg.InputText(key='firstnamebox')],  # Prénom
            [sg.Text('Date de naissance*'), sg.Input(key='birthdate', size=(20, 1)),
             sg.CalendarButton('Choisir date', target='birthdate', default_date_m_d_y=(1, None, 2022), locale='fr_FR',
                               begin_at_sunday_plus=1, format='%d/%m/%Y')],
            [sg.Text(' ')],
            [sg.Text('Date de fin séjour (vide = aujourd\'hui): '), sg.Input(key='finsejourdate', size=(20, 1)),
             sg.CalendarButton('Choisir date', target='finsejourdate', default_date_m_d_y=(1, None, 2022), locale='fr_FR',
                               begin_at_sunday_plus=1, format='%d/%m/%Y')],
            [sg.Text('Heure de sortie')],
            [[sg.Spin([i for i in range(0, 23)], initial_value=datetime.datetime.now().hour, key='hours'),
              sg.Text('Heures'),
              sg.Spin([i for i in range(0, 59)], initial_value=datetime.datetime.now().minute, key='minutes'),
              sg.Text('Minutes'),
              sg.Spin([i for i in range(0, 59)], initial_value=datetime.datetime.now().minute, key='seconds'),
              sg.Text('Secondes')]],

            [sg.Button('Ok'), sg.Button('Quitter')]
        ]
        endsejwindow = sg.Window('Fin séjour', endsejlayout, icon='Images/icone.ico').Finalize()
        while True:
            event, values = endsejwindow.read()
            if event in (None, 'Quitter'):
                break

            if(values['finsejourdate'] != ''):
                datesortie = values['finsejourdate']
            else:
                datesortie = datetime.date.today().strftime("%d/%m/%Y")
            datesortie = datesortie + ' ' + str(values['hours']) + ':' + str(values['minutes']) + ':' + str(
                values[
                    'seconds'])
            requestjson = json.dumps(
                {'requestType': 'endsej', 'ipp': values['ID'], 'nom': values['namebox'], 'ddn': values['birthdate'],
                 'prenom': values['firstnamebox'], 'dfs': datesortie
                 })  # Génère la requête JSON à partir des entrées
            print(requestjson)
            try:
                response = requests.post(url, data=requestjson, headers={'Content-Type': 'application/json'})
                print(response)
            except requests.exceptions.RequestException as e:
                # Popup d'erreur
                w, h = sg.Window.get_screen_size()
                errorlayout = [
                    [sg.Text('Erreur lors de l\'exécution de la requête !')],
                    [sg.Multiline(e, size=(200, 18), disabled=True)]
                ]

                errorwindow = sg.Window('CPT', errorlayout, background_color='red', size=(int(w / 3), int(h / 3)))
                errorevent = errorwindow.read()
                if (errorevent in (None, 'Ok')):
                    errorwindow.close()
        endsejwindow.close()

#TODO Ajouter partie fin de séjour et requête de fin de séjour pour ws_update
