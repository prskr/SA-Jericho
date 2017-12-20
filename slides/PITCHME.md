@title[Einführung]

# Container with machine guns

Marko Grgic, Peter Kurfer, Thomas Mildner, Sebastian Weißenbacher

---?image=assets/images/ContainerWithMachineGuns_Web.png&size=auto 70%

---

### Agenda

* Problembeschreibung
* Docker
* Gatling
* Kubernetes
* Ausblick

---

### Problembeschreibung

&rarr; Skalierung einer Anwendung <br/>
&rarr; Welche Last hält die Anwendung aus? <br/>
&rarr; Wie stabil ist die Softwarearchitektur? <br/>
&rarr; Skalierung in der Cloud <br/>


---

### Docker

&rarr; Containerbasierende Virtualisierung <br/>
&rarr; Betriebssystemunabhängig  <br/>
&rarr; geringere Hardware im Vergleich zu VM`s <br/>


+++

### Container Architektur

<img src="assets/images/ContainerArchitecture.png" alt="Container Architektur" style='height: 50%; width: 50%;'/>



+++
### Docker Architektur

![Docker Architecture](assets/images/DockerEngine.png)

+++

### Docker Registry

&rarr; Jedes Image verfügt über "Tag" <br/>
&rarr; Privates vs. öffentliches Registries <br/>
&rarr; DockerHub, Docker Cloud

+++

### Docker Compose

&rarr; Mehrere Container gleichzeitig benötigt <br/>
&rarr; Verwaltung mit Dockerfiles - Starten mit einem Befehl <br/>

+++

### Beispiel: Docker Compose
```docker
	version: '3'
	services:
	  web:
	    build: .
	    ports:
	     - "5000:5000"
	  redis:
	    image: "redis:alpine"
```

+++

### Netzwerk

&rarr; Swarm Modus <br/>
&rarr; Oberlay- Netzwerk 

+++

### Docker Mounts

![Docker Mount](assets/images/DockerMounts.png)

---
## Performance Tests

&rarr; ISO 9126 regelt Software Qualität <br/>
&rarr; Punkt Effizienz mit Zeitverhalten und Ressourcenverbrauch <br/>
&rarr; Finden von Bottlenecks <br/>

+++

### Einsatzgebiete von Performance Tests

&rarr; Vorhersage von Bottlenecks <br/>
&rarr; Skalierung der Anwendung  <br/>
&rarr; Feedback für Benutzer verbessern <br/>
&rarr; Verbesserung / Optimierung der Software Architektur <br/>

+++

### Gatling

&rarr; Performance Test Tool <br/>
&rarr; 20. Dezember 2011 veröffentlicht <br/>
&rarr; in Scala implementiert <br/>

+++ 

### Gatling - OpenHub

&ge; 50k Zeilen Code <br/>
&ge; 100 Contributors <br/>
&rarr; Apache License 2.0 veröffentlicht <br/>
&rarr; Eigene DSL für Benutzung

+++

### Gatling - Continous Integration

&rarr; Integrierbar in CI Umgebung (bsp. Jenkins) <br/>
&rarr; schnelles Feedback für Entwickler <br/>
&rarr; Änderung in Verhalten der Software - Anpassung der Software Architektur

+++

### Gatling Test Scenario

```scala
class RandomJokeSimulation extends Simulation {
    val httpConf = http
        .baseURL("http://192.168.111.20:58080")
        .acceptHeader("application/json")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")
    val scn = scenario("RandomJokeSimulation").repeat(100) {
        exec(http("getRandomJoke")
        .get("/api/v1/joke/random"))
    }    
    setUp(
        scn.inject(atOnceUsers(100))
    ).protocols(httpConf)
}
```
---

### Kubernetes

&rarr; Managment Tool für Cluster Umgebungen <br/>
&rarr; 07. Juni 2014 veröffentlicht <br/>
&rarr; Konkurrenz zu Apache Mesos, Hadoop YARN <br/>


---?image=assets/images/Kubernetes.png&size=auto 70%

+++ 

### Kubernetes Architektur

&rarr; Kublet - zentrale Komponente einer Node <br/>
&rarr; Kube Proxy - Netzwerk Proxy und Load Balancer <br/>
&rarr; cAdvisor - zeichnet Ressourcen eines Containers auf <br/>

+++

### Skalierung

&rarr; Automatische Horizontale Skalierung <br/>
&rarr; Horizontal Pod Autoscaler = Konfiguration über Auslastung <br/>
&rarr; Schwellenwerte für Skalierung beachten 

+++
### Persistenz

&rarr; Generelles Problem von Container-Anwendungen <br/>
&rarr; Lösung über Volumes - stürzt Container ab sind Daten weg <br/>
&rarr; Persistent Volumes - bereitgestellte Ressource im Cluster
+++

### Service Discovery - Load Balancing

&rarr; Problem ist das Finden der flüchtigen Pods <br/>
&rarr; Lösung mit zentralen Services - konfigurierbar <br/>
&rarr; Routen Netzwerkverkehr zu passenden Nodes
---

### Fazit & Ausblick

&rarr; Perfomance Tests = wichtiges Werkzeug <br/>
&rarr; Zukünftig noch mehr Cloud, Containerisierung <br/>
&rarr; Cluster Managment Systeme wie Kubernetes nötig
