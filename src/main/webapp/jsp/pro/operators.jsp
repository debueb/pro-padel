<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/pro/include/head.jsp"/>
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-10 col-lg-offset-1 col-xl-8 col-xl-offset-2">
            <h2 class="text-center">Features</h2>
            <div class="flex-container">
                <div class="panel flex-item panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title text-center">Turnierverwaltung</h3>
                    </div>
                    <div class="panel-body">
                        Verwalte deine Turniere, egal ob Jeder-gegen-Jeden, Rundenturnier oder KO-System mit Vorrunde. Veröffentliche deine Turniere optional auf pro-padel.de
                    </div>
                </div>
                <div class="panel flex-item panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title text-center">Ligaverwaltung</h3>
                    </div>
                    <div class="panel-body">
                        Verwalte deine Liga, damit du weniger mit Excel rumfummeln must. Teilnehmer können sich gegenseitig finden und Spiele verabreden, das Ergebnis eintragen und die immer aktuelle Tabelle einsehen.
                    </div>
                </div>
                <div class="panel flex-item panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title text-center">Ranking</h3>
                    </div>
                    <div class="panel-body">
                        Nutze die Vorteile des Elo Ranking, dass sich auf Grundlage aller veranstaltenten Spiele immer selbst aktualisiert.
                    </div>
                </div>
                <div class="panel flex-item panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title text-center">Spielbörse</h3>
                    </div>
                    <div class="panel-body">
                        Spielervermittlung
                    </div>
                </div>
                <div class="panel flex-item panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title text-center">Spieler und Team Daten</h3>
                    </div>
                    <div class="panel-body">
                        Greife auf eine umfangreiche Liste von Padel Spielern und Teams zurück. Erstelle neue Spieler und Teams und teile so mit anderen Betreibern.
                    </div>
                </div>
                <div class="panel flex-item panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title text-center">Adaptive Design</h3>
                    </div>
                    <div class="panel-body">
                        Das Portal passt sich automatisch an alle Endgeräte an und Du kannst es farblich nach deinen Wünschen konfigurieren.
                    </div>
                </div>
                <div class="panel flex-item panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title text-center">Buchungssystem</h3>
                    </div>
                    <div class="panel-body">
                        Nutze unser flexibles und mobil optimiertes Buchungssystem. Erlaube deinen Spieler die Buchung via PayPal, Lastschrift, Kreditkarte oder Gutschein. Erstelle Reservierungen für bereits geblockte Spielzeiten.
                    </div>
                </div>
                <div class="panel flex-item panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title text-center">Content Management System</h3>
                    </div>
                    <div class="panel-body">
                        Erstelle eigene Inhalte mit unserem einfachen WYSIWYG Edtitor.
                    </div>
                </div>
            </div>
            <hr/>
            <div class="row">
                <h2 class="text-center">Referenzen</h2>
                <div class="flex-container">
                    <c:forEach var="Customer" items="${Customers}">
                        <div class="panel flex-item panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title text-center">${Customer}</h3>
                            </div>
                            <div class="panel-body no-padding">
                                <c:forEach var="domainName" items="${Customer.domainNames}" end="0">
                                    <iframe src="http://${domainName}" width="100%" height="568px" frameborder="0"></iframe>
                                    </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                    <hr/>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <h2 class="text-center">Preise</h2>
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title text-center">Preistabelle</h3>
                        </div>
                        <div class="panel-body">
                            <table class="table table-striped">
                                <thead>
                                <th></th>
                                <th class="text-center">Basic</th>
                                <th class="text-center">Pro</th>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Turnierverwaltung</td>
                                        <td class="text-center">&#10003;</td>
                                        <td class="text-center">&#10003;</td>
                                    </tr>
                                    <tr>
                                        <td>Ligaverwaltung</td>
                                        <td class="text-center">&#10003;</td>
                                        <td class="text-center">&#10003;</td>
                                    </tr>
                                    <tr>
                                        <td>Ranking</td>
                                        <td class="text-center">&#10003;</td>
                                        <td class="text-center">&#10003;</td>
                                    </tr>
                                    <tr>
                                        <td>Spieler und Team Daten</td>
                                        <td class="text-center">&#10003;</td>
                                        <td class="text-center">&#10003;</td>
                                    </tr>
                                    <tr>
                                        <td>Spielbörse</td>
                                        <td class="text-center">&#x2717;</td>
                                        <td class="text-center">&#10003;</td>
                                    </tr>
                                    <tr>
                                        <td>Buchungssystem</td>
                                        <td class="text-center">&#x2717;</td>
                                        <td class="text-center">&#10003;</td>
                                    </tr>
                                    <tr>
                                        <td>Content Management System</td>
                                        <td class="text-center">&#x2717;</td>
                                        <td class="text-center">&#10003;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td class="text-center">0,00 EUR mtl.</td>
                                        <td class="text-center">30,00 EUR mtl.</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <hr/>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <h2 class="text-center">Anmelden</h2>
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title text-center">Jetzt registrieren</h3>
                        </div>
                        <div class="panel-body">
                            Registriere dich jetzt als Betreiber und starte in wenigen Minuten.
                        </div>
                        <div class="panel-footer">
                            <a class="btn btn-default btn-block" href="/pro/operators/newaccount">Jetzt anmelden</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/pro/include/footer.jsp"/>