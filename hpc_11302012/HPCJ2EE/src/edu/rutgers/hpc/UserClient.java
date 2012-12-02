package edu.rutgers.hpc;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.ektorp.*;
import org.ektorp.impl.*;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;
import org.ektorp.http.*;

public class UserClient {

	
	public static void main(String[] args) throws Exception
	{
		
		



	//	HttpClient httpClient = new StdHttpClient.Builder().url("http://127.0.0.1:5984/").build();
	
	//	HttpClient httpClient = new StdHttpClient.Builder().url("https://hpc.iriscouch.com:6984/").username("vpathak").password("vpathak123").enableSSL(true).relaxedSSLSettings(true).build();
		
		HttpClient httpClient = new StdHttpClient.Builder().url("https://127.0.0.1:6984/").username("admin").password("hpc1234").enableSSL(true).relaxedSSLSettings(true).build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		
   // HttpClient httpClient2 = new StdHttpClient.Builder().url("http://127.0.0.1:5984/").build();
	
		// HttpClient httpClient2 = new StdHttpClient.Builder().url("https://hpc.iriscouch.com:6984/").username("vpathak").password("vpathak123").enableSSL(true).relaxedSSLSettings(true).build();
		HttpClient httpClient2 = new StdHttpClient.Builder().url("https://127.0.0.1:6984/").username("admin").password("hpc1234").enableSSL(true).relaxedSSLSettings(true).build();
		
		CouchDbInstance dbInstance2 = new StdCouchDbInstance(httpClient2);
		
       
		CouchDbConnector db = null;
		CouchDbConnector iodb = null;
				// if the second parameter is true, the database will be created if it doesn't exists
		try{
		  db = dbInstance.createConnector("brokerdb", true);
		  iodb = dbInstance2.createConnector("iodb", true);
		}catch(DbAccessException connectionException)
		{
			System.out.println("CouchDB is down");
			connectionException.printStackTrace();
		}
		User user = new User();
		user.setUserID("parveen");
		user.setPassword("abcd1234");
		user.setAddress("7, Main street");
		user.setCity("Woodbridge");
		user.setPostalCode("070955");
		user.setState("NJ");
		user.setEmail("parveen@rutgers.edu");
		user.setPhoneNo("732-123-2222");
		user.setType("user");
		ArrayList authors = new ArrayList();
		authors.add("parveen");
		user.setAuthors(authors);
	    UserRepository userRepository = new UserRepository(db);
	    userRepository.add(user);
	    
	    ArrayList returnedUsers = (ArrayList) userRepository.findByUserID("parveen");
	    
	    System.out.println("Returned User _id " + ((User) returnedUsers.get(0)).getId());

	 //   User storedUser = userRepository.findByUserID(userID)
	    
	    Worker worker = new Worker();
	    worker.setUserID("joe");
	    worker.setPassword("joe1234");
	    worker.setAddress("30, river rd");
	    worker.setCity("piscatway");
	    worker.setPostalCode("07309");
	    worker.setState("NJ");
	    worker.setEmail("joe@rutgers.edu");
	    worker.setPhoneNo("732-911-2222");
	    worker.setType("worker");
	    
		ArrayList authors1 = new ArrayList();
		authors1.add("joe");
		worker.setAuthors(authors1);
	    List<String> skills = new ArrayList<String>();
	    
	    
	    skills.add("Driving NJ");
	    skills.add("Driving PA");
	    skills.add("Driving OH");
	 
	    
	   
	    skills.add("Teaching Algebra");
	    skills.add("Teaching Calculus");
	    skills.add("Teaching Discrete Mathematics");
	  
	    
	    
	   
	    worker.setSkills(skills);
	    
	    WorkerRepository workerRepository = new WorkerRepository(db);
	    workerRepository.add(worker);
	    
        ArrayList returnedUsers1 = (ArrayList) workerRepository.findByUserID("joe");
	    
	    System.out.println("Returned worker _id " + ((Worker) returnedUsers1.get(0)).getId());
	    System.out.println("Returned worker name " + ((Worker) returnedUsers1.get(0)).getUserID());
	    System.out.println("Returned worker _rev " + ((Worker) returnedUsers1.get(0)).getRevision());
	    
	    
	    WorkRequest workRequest = new WorkRequest();
	    
	    UUID requestID = UUID.randomUUID();
	    
	    workRequest.setRequestID(requestID.toString());
	    workRequest.setRequestorID(user.getUserID());
	    workRequest.setAuthors(authors);
	    
	    List<String> skillsNeeded = new ArrayList<String>();
	   
	    
	    skillsNeeded.add("Driving NJ");
	    skillsNeeded.add("Driving CA");
	    
	    workRequest.setSkillsNeeded(skillsNeeded);
	    
	    
	   
	    WorkRequestRepository workRequestRepository = new WorkRequestRepository(db);
	    
	    workRequestRepository.add(workRequest);
	    
	    long dbSequenceNumber = db.getDbInfo().getUpdateSeq();
	    
	    WorkOutputRepository workOutputRespository1 = new WorkOutputRepository(iodb);
		 WorkOutput output1 = new WorkOutput();
		    output1.setWorkAssignmentID("dummy");
		    HashMap<String,String> outputVals1 = new HashMap<String, String>();
		    outputVals1.put("Drive Step 1", "Take I-78 West");
		    outputVals1.put("Drive Step 2", "Take I-476 South" );
		    outputVals1.put("Drive Step 3", "Take I-95 South");
		    output1.setResults(outputVals1);
		    output1.setAuthors(authors1);
		    
		    workOutputRespository1.add(output1);
		    
		    WorkInputRepository workInputRepositoryx = new WorkInputRepository(iodb);
		    WorkInput workinputx = new WorkInput();
		    workinputx.setWorkAssignmentID("dummy");
		    HashMap<String,String> inputx = new HashMap<String, String>();
		    inputx.put("Start Location", "Piscatway, NJ");
		    inputx.put("End Location", "Cincinnati, OH");
		    workinputx.setInput(inputx);
		    workinputx.setAuthors(authors);
		    workInputRepositoryx.add(workinputx);
	    
	    
	   
		    Map<String, Object> securityDoc = new HashMap<String, Object>();
		    securityDoc.put("_id", "_design/Security");
		    securityDoc.put("views", "{}");
		    securityDoc.put("lists", "{}");
		    securityDoc.put("shows", "{}");
		    securityDoc.put("language", "javascript");
		    securityDoc.put("filters", "{}");
		    securityDoc.put("updates", "{}");
		    
		    String validateFunction = "function contains(a, obj) { \n    for (var i = 0; i < a.length; i++) { \n        if (a[i] === obj) { \n            return true; \n        } \n    } \n    return false; \n} \n\nfunction(newDoc, oldDoc, userCtx, secObj)\n {      \n    if (newDoc.authors === undefined)\n    {          throw({forbidden: 'Document must have an author.'}); \n      }\n     if(!contains(newDoc.authors, userCtx.name))\n    {          throw({forbidden: 'Only Document Owner can update this Document.'}); \n      }\n\n     \n}";
	
		    securityDoc.put("validate_doc_update", validateFunction);
		    Map<String, Object> securityDoc2 = new HashMap<String, Object>();
		   
		    securityDoc2.put("_id", "_design/Security");
		    securityDoc2.put("views", "{}");
		    securityDoc2.put("lists", "{}");
		    securityDoc2.put("shows", "{}");
		    securityDoc2.put("language", "javascript");
		    securityDoc2.put("filters", "{}");
		    securityDoc2.put("updates", "{}");
		    
		    String validateFunction2 = "function contains(a, obj) { \n    for (var i = 0; i < a.length; i++) { \n        if (a[i] === obj) { \n            return true; \n        } \n    } \n    return false; \n} \n\nfunction(newDoc, oldDoc, userCtx, secObj)\n {      \n    if (newDoc.authors === undefined)\n    {          throw({forbidden: 'Document must have an author.'}); \n      }\n     if(!contains(newDoc.authors, userCtx.name))\n    {          throw({forbidden: 'Only Document Owner can update this Document.'}); \n      }\n\n     \n}";
	
		    securityDoc2.put("validate_doc_update", validateFunction2);
		    
		    db.create(securityDoc);
		
		    iodb.create(securityDoc2);
		    
		    WorkAssignment workAssignment = new WorkAssignment();
		    workAssignment.setRequestorID("dummy");
		    workAssignment.setWorkerID("dummy");
		    workAssignment.setWorkRequestID("dummy");
		    workAssignment.setAssignmentStatus("assigned");
		    ArrayList authors2 = new ArrayList();
			authors2.add("admin");
			
			workAssignment.setAuthors(authors2);
		    
		    java.util.Date date = new java.util.Date();
		    java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
		    
		    workAssignment.setAssignmentStatusTimeStamp(ts.toString());
		    WorkAssignmentRepository workassignmentRespository1 = new WorkAssignmentRepository(db);
		    
		    workassignmentRespository1.add(workAssignment);
		    

	    List retworkRequests =    workRequestRepository.findByRequestID(requestID.toString());
	    
	    System.out.println("Work Request ID = "+ ((WorkRequest)retworkRequests.get(0)).getRequestID());
	      return;//I'm done here
//	    while (true)
//		{
//		
//
//				ChangesCommand cmd = new ChangesCommand.Builder()
//				                         .since(dbSequenceNumber).includeDocs(true).includeDocs(true).filter("Worker/assignments_by_request_id").param("requestID", requestID.toString())
//				                         .build();
//
//				List<DocumentChange> changes = db.changes(cmd);
//				for(DocumentChange change : changes) {
//					 String docId = change.getId();
//					 System.out.println("docid = "+ docId);
//					    System.out.println("doc =" + change.getDoc());
//					    String workAssignmentID = change.getDocAsNode().get("workAssignmentID").getTextValue();
//					    System.out.println("work Assignment iD=" + workAssignmentID);
//					    
//					    
//					   
//					    
//			
//					    
//					    WorkAssignmentRepository workassignmentRespository = new WorkAssignmentRepository(db);
//					    
//					
//					    
//					 
//					    List<WorkAssignment> assignments = (List<WorkAssignment>) workassignmentRespository.findByWorkRequestID(requestID.toString());
//					    
//					    WorkAssignment retassignment = (WorkAssignment) assignments.get(0);
//					    
//					    if(retassignment.getWorkInputID() == null)
//					    {
//					    
//					    System.out.println("Work is assigned to " + retassignment.getWorkerID());
//					    System.out.println("WorkAssignment ID =  " + retassignment.getWorkAssignmentID());
//					    System.out.println("WorkRequest  ID =  " + retassignment.getWorkRequestID());
//					    
//					    WorkInputRepository workInputRepository = new WorkInputRepository(iodb);
//					    WorkInput workinput = new WorkInput();
//					    workinput.setWorkAssignmentID(retassignment.getWorkAssignmentID());
//					    HashMap<String,String> input1 = new HashMap<String, String>();
//					    input1.put("Start Location", "Piscatway, NJ");
//					    input1.put("End Location", "Cincinnati, OH");
//					    workinput.setInput(input1);
//					    workInputRepository.add(workinput);
//					    retassignment.setWorkInputID(workinput.getWorkInputID());
//					    workassignmentRespository.update(retassignment);
//					    }
//					    if(retassignment.getWorkOutputID() != null)
//					    {
//						    WorkOutputRepository workOutputRespository = new WorkOutputRepository(iodb);
//						    
//						
//						    
//						 
//						    List<WorkOutput> outputs = (List<WorkOutput>) workOutputRespository.findByWorkAssignmentID(workAssignmentID);
//						    
//						    WorkOutput output = (WorkOutput) outputs.get(0);
//						    
//						   
//						    System.out.println("WorkOutput ID =  " + output.getWorkOutputID());
//						    
//						    HashMap<String,String> results = new HashMap<String,String>();
//						    results = output.getResults();
//						    
//						    for (Map.Entry<String, String> entry : results.entrySet()) { 
//						        String key = entry.getKey(); 
//						        String value = entry.getValue(); 
//						        System.out.println("Key = " + key);
//						    	System.out.println("Value = " + value);
//						    }
//						    System.exit(0);
//					    
//					   // String requestID = change.getDocAsNode().get("workRequestID").getTextValue();
//					    
//					   // 
//					   // String workAssignmentID = change.getDocAsNode().get("workAssignmentID").getTextValue();
//					  //  System.out.println("work Request ID=" + requestID);
//				    
//				}
//		}
//		}
	      
	      
	      //ABOVE CODE CAN BE COMMENTED OUT
	      
//	    ChangesCommand chgcmd = new ChangesCommand.Builder().continuous(true).heartbeat(10).includeDocs(true).filter("Worker/assignments_by_request_id").param("requestID", requestID.toString()).build();
//		ChangesFeed feed = db.changesFeed(chgcmd);
//
//	    String workAssignmentID = null;
//		while (feed.isAlive()) {
//		    DocumentChange change = feed.next();
//		    String docId = change.getId();
//		    System.out.println("docid = "+ docId);
//		    System.out.println("doc =" + change.getDoc());
//		    
//		     workAssignmentID = change.getDocAsNode().get("workAssignmentID").getTextValue();
//		    System.out.println("work Assignment iD=" + workAssignmentID);
//		    
//		    
//		   
//		    
//
//		    
//		    WorkAssignmentRepository workassignmentRespository = new WorkAssignmentRepository(db);
//		    
//		
//		    
//		 
//		    List<WorkAssignment> assignments = (List<WorkAssignment>) workassignmentRespository.findByWorkRequestID(requestID.toString());
//		    
//		    WorkAssignment retassignment = (WorkAssignment) assignments.get(0);
//		    
//		    if(retassignment.getWorkInputID() == null)
//		    {
//		    
//		    System.out.println("Work is assigned to " + retassignment.getWorkerID());
//		    System.out.println("WorkAssignment ID =  " + retassignment.getWorkAssignmentID());
//		    System.out.println("WorkRequest  ID =  " + retassignment.getWorkRequestID());
//		    
//		    WorkInputRepository workInputRepository = new WorkInputRepository(iodb);
//		    WorkInput workinput = new WorkInput();
//		    workinput.setWorkAssignmentID(retassignment.getWorkAssignmentID());
//		    HashMap<String,String> input1 = new HashMap<String, String>();
//		    input1.put("Start Location", "Piscatway, NJ");
//		    input1.put("End Location", "Cincinnati, OH");
//		    workinput.setInput(input1);
//		    workInputRepository.add(workinput);
//		    retassignment.setWorkInputID(workinput.getWorkInputID());
//		    workassignmentRespository.update(retassignment);
//		    }
//		    if(retassignment.getWorkOutputID() != null)
//		    {
//			    WorkOutputRepository workOutputRespository = new WorkOutputRepository(iodb);
//			    
//			
//			    
//			 
//			    List<WorkOutput> outputs = (List<WorkOutput>) workOutputRespository.findByWorkAssignmentID(workAssignmentID);
//			    
//			    WorkOutput output = (WorkOutput) outputs.get(0);
//			    
//			   
//			    System.out.println("WorkOutput ID =  " + output.getWorkOutputID());
//			    
//			    HashMap<String,String> results = new HashMap<String,String>();
//			    results = output.getResults();
//			    
//			    for (Map.Entry<String, String> entry : results.entrySet()) { 
//			        String key = entry.getKey(); 
//			        String value = entry.getValue(); 
//			        System.out.println("Key = " + key);
//			    	System.out.println("Value = " + value);
//			    }
//			  
//			   // feed.cancel();
//			   // break;
//
//				System.exit(0);
//		    	
//		    }
//		    
//		    
//		 
//		    
//		   
//		 
//		    
//		}
//		
		
		
//		if(workAssignmentID != null)
//		{
//	//	 ChangesCommand chgcmdworkoutput = new ChangesCommand.Builder().includeDocs(true).filter("WorkInput/output_by_assignment_id").param("workAssignmentID", workAssignmentID).build();
//			 ChangesCommand chgcmdworkoutput = new ChangesCommand.Builder().includeDocs(true).build();
//				
//			ChangesFeed workoutputfeed = iodb.changesFeed(chgcmdworkoutput);
//			
//			while ( workoutputfeed.isAlive()) {
//			    DocumentChange outputchange =  workoutputfeed.next();
//			    String outputdocId = outputchange.getId();
//			    System.out.println("docid = "+ outputdocId);
//			    System.out.println("doc =" + outputchange.getDoc());
//			    
//			    String outputworkAssignmentID = outputchange.getDocAsNode().get("workAssignmentID").getTextValue();
//			    System.out.println("output work Assignment iD=" + outputworkAssignmentID);
//			    
//			    
//			   
//			    
//
//			    
//			    WorkOutputRepository workOutputRespository = new WorkOutputRepository(iodb);
//			    
//			
//			    
//			 
//			    List<WorkOutput> outputs = (List<WorkOutput>) workOutputRespository.findByWorkAssignmentID(outputworkAssignmentID);
//			    
//			    WorkOutput output = (WorkOutput) outputs.get(0);
//			    
//			   
//			    System.out.println("WorkAssignment ID =  " + output.getWorkAssignmentID());
//			    
//			    HashMap<String,String> results = new HashMap<String,String>();
//			    results = output.getResults();
//			    
//			    for (Map.Entry<String, String> entry : results.entrySet()) { 
//			        String key = entry.getKey(); 
//			        String value = entry.getValue(); 
//			        System.out.println("Key = " + key);
//			    	System.out.println("Value = " + value);
//			    }
//			   // workoutputfeed.cancel();
//			    break;
//			    
//			} //End of while
//		}//End of If

	 //   workerRepository.remove((Worker) returnedUsers1.get(0));
	  //  userRepository.remove((User) returnedUsers.get(0));
	    
	 //   WorkAssignment workAssignment = new WorkAssignment();
	 //   workAssignment.setRequestorID("parveen");
//	    workAssignment.setWorkerID("joe");
//	    workAssignment.setWorkRequestID(requestID.toString());
//	    
//	    WorkAssignmentRepository workassignmentRespository = new WorkAssignmentRepository(db);
//	    
//	    workassignmentRespository.add(workAssignment);
	    
//	    List<WorkAssignment> assignments = (List<WorkAssignment>) workassignmentRespository.findByWorkRequestID(requestID.toString());
	    
//	    WorkAssignment retassignment = (WorkAssignment) assignments.get(0);
	    
//	    System.out.println("Work is assigned to " + retassignment.getWorkerID());
	    
	    
	    
	    
	    
	  
	    
	}
}
