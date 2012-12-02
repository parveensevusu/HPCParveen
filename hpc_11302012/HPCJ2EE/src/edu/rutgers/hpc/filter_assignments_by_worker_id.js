function (doc,req)

{
		if(doc.type == "WorkAssignment" )
			{
			  if(doc.workerID)
				  {
				     if (doc.workerID == req.query.workerID)
				    	 {
				    	 return true;
				    	 }
				  }
			}
		return false;
		
}