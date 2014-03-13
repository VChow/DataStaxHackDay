package com.kudu.lib;


import java.util.ArrayList;
import java.util.List;







import com.datastax.driver.core.*;

public final class Keyspaces {



public Keyspaces(){

}

public static void SetUpKeySpaces(Cluster c){
try{
//Add some keyspaces here
String createkeyspace="create keyspace if not exists shoutBox WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
String CreateUserTable = "CREATE TABLE if not exists users ("+
"user varchar,"+
" password varchar,"+
" email text,"+
"secondaryEmail text," +
"authLevel int," +
" PRIMARY KEY (user, password)"+
");";
String CreateShoutsTable = "CREATE TABLE if not exists shouts ("+
"user varchar,"+
" interaction_time timeuuid,"+
" shout varchar,"+
" PRIMARY KEY (user,interaction_time)"+
") WITH CLUSTERING ORDER BY (interaction_time DESC);";
String CreateFollowersTable = "CREATE TABLE if not exists followers ("+
"user varchar," +
" following set<text>," +
"PRIMARY KEY (user));";
String CreateAdmin = "INSERT INTO users (user, password, email, authLevel) VALUES (\'admin\', \'password\', \'admin\', 2);";
Session session = c.connect();
try{
PreparedStatement statement = session.prepare(createkeyspace);
BoundStatement boundStatement = new BoundStatement(statement);
ResultSet rs = session.execute(boundStatement);

}
catch(Exception et){
System.out.println("Can't create shoutbox "+et);
}

//now add some column families
session.close();
session = c.connect("shoutBox");
System.out.println(""+CreateUserTable);

try{
SimpleStatement cqlQuery = new SimpleStatement(CreateUserTable);
session.execute(cqlQuery);
cqlQuery = new SimpleStatement(CreateShoutsTable);
session.execute(cqlQuery);
cqlQuery = new SimpleStatement(CreateFollowersTable);
session.execute(cqlQuery);
cqlQuery = new SimpleStatement(CreateAdmin);
session.execute(cqlQuery);
}
catch(Exception et){
System.out.println("Can't create tables "+et);
}
session.close();

}catch(Exception et){
System.out.println("Other keyspace or coulm definition error" +et);
}

}
}