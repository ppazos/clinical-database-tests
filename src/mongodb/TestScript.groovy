@Grab(group='com.gmongo', module='gmongo', version='1.3')
@Grab(group='com.thoughtworks.xstream', module='xstream', version='1.4.7')

import com.gmongo.GMongoClient
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.thoughtworks.xstream.XStream

//def oid = new org.bson.types.ObjectId()
//println oid // 5452fe259d41f01413f3068e

/*
1. Run:
> mongo
> use openehr
> db.createUser("user","pass")
> 

2. The database is created the first time a document is added to a collection.
*/

credentials = MongoCredential.createMongoCRCredential('user', 'openehr', 'pass' as char[])
client = new GMongoClient(new ServerAddress(), [credentials])

// Base de datos openEHR
def db = client.getDB("openehr")

//println db.getClass() // com.mongodb.DBApiLayer

// ===================================================
// Agrega un EHR a la coleccion ehrs
db.ehrs << [
             ehrId: java.util.UUID.randomUUID() as String,
             dateCreated: new Date(),
             systemId: 'CABOLABS_OPENEHR_EHR',
             compositions: [],
             contribution: [],
             subject: [ /* Campo de EHR_STATUS puesto directamente dentro del EHR para simplificar */
               uid: java.util.UUID.randomUUID() as String,
               firstName: 'Pablo',
               lastName: 'Pazos',
               dob: new Date(81, 9, 24, 9, 59),
               idCode: 'ABC1234',
               idType: 'DNI'
             ]
           ]


// Todos los EHRs
def res = db.ehrs.find() // http://docs.mongodb.org/manual/reference/method/db.collection.find/

res.each{ println it.getClass().toString() +" - "+ it.toString() } // com.mongodb.BasicDBObject


// Un EHR
def ehr = db.ehrs.findOne() // http://docs.mongodb.org/manual/reference/method/db.collection.findOne/


// Test imprime el EHR en XML
XStream xstream = new XStream()
//println "ehr: "+ xstream.toXML(ehr)


def ehr2 = db.ehrs.findOne(dateCreated: ehr.dateCreated)

//println "ehr2: "+ xstream.toXML(ehr2)


// Agrego una composition al EHR
ehr2.compositions << [
                       category: 'event',
                       composer: [
                         firstName: 'Carlos',
                         lastName: 'Gomez',
                         idCode: 'asdfasdf',
                         idType: 'RRHHID'
                       ],
                       context: [
                         startTime: new Date(),
                         location: 'Depto. de Emergencias'
                       ]
                     ]
                     
ehr2.compositions.save(ehr2.compositions)
                     
ehr = db.ehrs.findOne()
println "ehr con composition: "+ xstream.toXML(ehr.compositions)

// ===================================================
// Elimina todos los EHRs
db.ehrs.remove([:])

res = db.ehrs.find()

res.each{ println it.getClass().toString() +" - "+ it.toString() } // com.mongodb.BasicDBObject

