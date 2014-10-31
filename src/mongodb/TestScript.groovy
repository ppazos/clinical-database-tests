@Grab(group='com.gmongo', module='gmongo', version='1.3')

import com.gmongo.GMongoClient
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress

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
             subject: [
               uid: java.util.UUID.randomUUID() as String,
               firstName: 'Pablo',
               lastName: 'Pazos',
               dob: new Date(81, 9, 24, 9, 59),
               idCode: 'ABC1234',
               idType: 'DNI'
             ]
           ]

def res = db.ehrs.find()

res.each{ println it.getClass().toString() +" - "+ it.toString() } // com.mongodb.BasicDBObject

// ===================================================
// Elimina todos los EHRs
db.ehrs.remove([:])

res = db.ehrs.find()

res.each{ println it.getClass().toString() +" - "+ it.toString() } // com.mongodb.BasicDBObject



