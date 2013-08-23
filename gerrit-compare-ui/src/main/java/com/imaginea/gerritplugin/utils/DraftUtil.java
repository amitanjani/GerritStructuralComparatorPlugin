package com.imaginea.gerritplugin.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gerrit.reviewdb.client.Account;
import com.google.gerrit.reviewdb.client.Change;
import com.google.gerrit.reviewdb.client.PatchLineComment;
import com.google.gerrit.reviewdb.client.PatchSet;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.CurrentUser;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.project.ChangeControl;
import com.google.gerrit.server.project.NoSuchChangeException;
import com.google.gwtorm.server.OrmException;
import com.google.gwtorm.server.ResultSet;
import com.google.gwtorm.server.SchemaFactory;
import com.imaginea.comparator.domain.DraftMessage;

public class DraftUtil {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(DraftUtil.class);
	private ReviewDb db ;
	private ChangeControl control;
	
	public List<DraftMessage> loadDraftMessage( SchemaFactory<ReviewDb> dbFactory, ChangeControl.Factory changeControlFactory,String baseUrl, String patchUrl ) {
		List<DraftMessage> reviewerComments = null;
		List<DraftMessage> reviewerCommentsDiffPatch = null;
		
		reviewerComments = loadReviewerComments(dbFactory, changeControlFactory, patchUrl);
		reviewerCommentsDiffPatch = loadReviewerComments(dbFactory, changeControlFactory, baseUrl);
		
		if( null !=reviewerComments && null != reviewerCommentsDiffPatch){
			return mergeReviewerComments(reviewerComments, reviewerCommentsDiffPatch);
		} else if ( null == reviewerComments && null != reviewerCommentsDiffPatch ){
			reviewerComments = new ArrayList<DraftMessage>();
			for(Object obj:reviewerCommentsDiffPatch){
				DraftMessage message = null;
				message = (DraftMessage)obj;
				if(message.getSide() == 1){
					message.setSide(0);
				} else{
					message.setSide(1);
				}
				reviewerComments.add(message);
			}
			return reviewerComments;
		}
		
		return reviewerComments;
	  }
	
	
	private List<DraftMessage> getDraftMessages( Change.Id changeId, PatchSet.Id patchSetId, String fileName ) throws OrmException{
		ResultSet<PatchSet> source = db.patchSets().byChange(changeId);
	    List<DraftMessage> reviewerComments = new ArrayList<DraftMessage>();
	    final CurrentUser user = control.getCurrentUser();
	    final Account.Id me = user instanceof IdentifiedUser ? ((IdentifiedUser) user).getAccountId():null;
	    for (PatchSet ps : source) {
	      final PatchSet.Id psId = ps.getId();
	      if( psId.equals(patchSetId) && control.isPatchVisible(ps, db) && me != null){
	        	log.debug("patchesWithDraftComments "+psId);
	        	ResultSet<PatchLineComment> patchComment = db.patchComments().byPatchSet(psId);
	        	Iterator itr = patchComment.iterator();
	        	PatchLineComment comment = null;
	        	while( itr.hasNext() ){
	        			comment = (PatchLineComment)itr.next();
		        		log.debug("Message "+comment.getMessage());
		        		log.debug("Side "+comment.getSide());
		        		log.debug("Line Number "+comment.getLine());
		        		log.debug("Author "+comment.getAuthor());
		        		log.debug("Written On::"+comment.getWrittenOn());
		        		log.debug("Status::"+comment.getStatus().toString());
		        		log.debug("File Name:: "+comment.getKey().getParentKey().getFileName());
		        		if( me.equals(comment.getAuthor()) && null != fileName && fileName.equals(comment.getKey().getParentKey().getFileName())){
		        			DraftMessage message = new DraftMessage();
		        			message.setLine(comment.getLine());
		        			message.setMessage(comment.getMessage());
		        			message.setSide(comment.getSide());
		        			message.setStatus(comment.getStatus().toString());
		        			Account account = db.accounts().get(me);
		        			message.setAuthor(account.getFullName());
		        			message.setWrittenOn(comment.getWrittenOn());
		        			message.setUuid(comment.getKey().get());
		        			reviewerComments.add(message);
		        		}
	        	}
	      }
	    }
		return reviewerComments;
	}

	private List<DraftMessage> mergeReviewerComments(List<DraftMessage> reviewerCommentList1, List<DraftMessage> reviewerCommentList2){
		List<DraftMessage> reviewerComments = new ArrayList<DraftMessage>();
		DraftMessage message = null;
		for(Object obj:reviewerCommentList1){
				message = (DraftMessage)obj;
				if(message.getSide() == 1){
					reviewerComments.add(message);
				}				
			}
		
		for(Object obj:reviewerCommentList2){
			message = (DraftMessage)obj;
			if(message.getSide() == 1){
				message.setSide(0);
				reviewerComments.add(message);
			}				
		}
		return reviewerComments;
		}

	private List<DraftMessage> loadReviewerComments( SchemaFactory<ReviewDb> dbFactory, ChangeControl.Factory changeControlFactory, String patchUrl ){
		int id = 0;
		int change_id = 0;
		String fileName = null;
		
		List<DraftMessage> reviewerComments = null;
		
		PatchDetail patchDetail = new PatchDetail(patchUrl);
		id = patchDetail.getPatchSetId();
		change_id = patchDetail.getChangeId();
		fileName = patchDetail.getFileName();
		
		if(!patchDetail.isBaseSet()){
			Change.Id changeId = new Change.Id(change_id);
			PatchSet.Id patchSetId = new PatchSet.Id(changeId, id);

			log.debug("loadPatchSets() Method Arguement " + changeId);
			try {
				db = dbFactory.open();
			} catch (OrmException e) {
				log.error("OrmException::", e);
			}

			try {
				control = changeControlFactory.validateFor(changeId);
			} catch (NoSuchChangeException e) {
				log.debug("NoSuchChangeException ", e);
			} catch (OrmException e) {
				log.debug("OrmException ", e);
			}

			try {
				reviewerComments = getDraftMessages(changeId, patchSetId, fileName);
			} catch (OrmException e) {
				log.debug("OrmException ", e);
			}
			
			db.close();
		}
		
		return reviewerComments;
	}
}
