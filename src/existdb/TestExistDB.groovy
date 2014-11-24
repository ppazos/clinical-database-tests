//@Grab(group='net.exist', module='exist-xqj', version='1.0.1')

import javax.xml.xquery.*;
import javax.xml.namespace.QName;
import net.xqj.exist.ExistXQDataSource;

XQDataSource xqs = new ExistXQDataSource();
xqs.setProperty("serverName", "localhost");
xqs.setProperty("port", "8080");

XQConnection conn = xqs.getConnection('admin', 'toor');

XQExpression expr = conn.createExpression(); // Create a reusable XQuery Expression object

// execute an XQuery expression
XQResultSequence result = expr.executeQuery('''
 declare default element namespace "http://schemas.openehr.org/v1";
 for $n in fn:collection('/db/apps/compositions')//data 
  return fn:data($n/name/value)'''
)

println result.getClass() // super obscure class name ...
//println result.getSequenceAsString()
//println result.count()

// Process the result sequence iteratively
while (result.next()) {
  // Print the current item in the sequence
  println result.getItemAsString(null)
}
 
// Free all resources created by the connection
conn.close();
