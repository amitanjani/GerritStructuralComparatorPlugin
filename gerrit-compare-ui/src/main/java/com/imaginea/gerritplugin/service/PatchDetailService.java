package com.imaginea.gerritplugin.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gerrit.common.errors.NoSuchEntityException;
import com.google.gerrit.extensions.annotations.Export;
import com.google.gerrit.httpd.rpc.BaseServiceImplementation.Failure;
import com.google.gerrit.reviewdb.client.Account;
import com.google.gerrit.reviewdb.client.Change;
import com.google.gerrit.reviewdb.client.Patch;
import com.google.gerrit.reviewdb.client.PatchLineComment;
import com.google.gerrit.reviewdb.client.PatchSet;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.ChangeUtil;
import com.google.gerrit.server.CurrentUser;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.project.ChangeControl;
import com.google.gerrit.server.project.NoSuchChangeException;
import com.google.gwtorm.server.OrmException;
import com.google.gwtorm.server.SchemaFactory;
import com.google.inject.Inject;
import com.imaginea.comparator.domain.DraftMessage;
import com.imaginea.gerritplugin.utils.DraftUtil;
import com.imaginea.gerritplugin.utils.PatchDetail;

/* 
 * PatchDetailService is servlet defined to save reviewer comment in DB
 * */

@Export("/patchDetailService")
public class PatchDetailService extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = Logger.getLogger(PatchDetailService.class);
	
	private final ChangeControl.Factory changeControlFactory;
	private ReviewDb db = null;
	private final SchemaFactory<ReviewDb> dbFactory;
	private ChangeControl control;

	@Inject
	public PatchDetailService(SchemaFactory<ReviewDb> dbFactory,
			final ChangeControl.Factory changeControlFactory){
		this.dbFactory = dbFactory;
		this.changeControlFactory = changeControlFactory;
	}
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String reviewerComment = req.getParameter("message");
		String baseUrl = req.getParameter("baseUrl");
		String patchUrl = req.getParameter("patchUrl");
		String flag =  req.getParameter("flag");
		int line = Integer.valueOf(req.getParameter("lineNumber"));
		int tmpSide = Integer.valueOf(req.getParameter("side"));
		
		Patch.Key parentKey = null;
		PatchDetail detail = null;
		PatchLineComment comment = null;
		
		final short side;
		int SideUI = 0;
		
		List<DraftMessage> draftMessage = null;
		DraftMessage message = null;
		
		log.debug("reviewerComment "+reviewerComment);
		log.debug("lineNumber "+line);
		log.debug("patchUrl "+patchUrl);
		log.debug("Side "+tmpSide);
		
		if( tmpSide == 1 ){
			SideUI = 0;
			validateChangeId(baseUrl);
			detail = new PatchDetail(baseUrl);
			if (!detail.isBaseSet()){
				side = (short) 1;
			}else{
				side = (short) 0;
			}
		}else if( tmpSide == 3 ){
			SideUI = 1;
			validateChangeId(patchUrl);
			detail = new PatchDetail(patchUrl);
			if (detail.isBaseSet()){
				side = (short) 0;
			}else{
				side = (short) 1;
			}
		}else{
			side = (short) 1;
			log.debug("Invalid side");
		}
		log.debug("Side::"+side);
		 try {
			draftMessage = loadDraftMessage( baseUrl, patchUrl );
		} catch (OrmException e1) {
			log.debug("OrmException ",e1);
		}

		final CurrentUser user = control.getCurrentUser();
		final Account.Id me = user instanceof IdentifiedUser ? ((IdentifiedUser) user).getAccountId(): null;
		log.debug("account.getId() " + me);
		
		for(Object obj:draftMessage){
			message = (DraftMessage)obj;
			if( null != message && message.getLine() == line && message.getSide() == SideUI && message.getStatus().toString().equals("DRAFT")){
				 if( tmpSide == 1 ){
					 parentKey = getPatchKey(baseUrl);
				 }else if( tmpSide == 3 ){
					 parentKey = getPatchKey(patchUrl);
				 }
				comment = new PatchLineComment(new PatchLineComment.Key(parentKey, message.getUuid()),line, me, null);
				 if( flag.equalsIgnoreCase("discard")){
					 try {
						deleteDraft(parentKey, message.getUuid());
					} catch (Failure e) {
						log.debug("Failure ",e);
					} catch (OrmException e) {
						log.debug("OrmException ",e);
					}
				 }
			}
		}
		
		 if( flag.equalsIgnoreCase("save")){
			 if( null == comment ){
				 log.debug("comment null");
				 if( tmpSide == 1 ){
					 parentKey = getPatchKey(baseUrl);
				 }else if( tmpSide == 3 ){
					 parentKey = getPatchKey(patchUrl);
				 }
				 comment = new PatchLineComment(new PatchLineComment.Key(parentKey, null),line, me, null);
			 }
			 comment.setSide(side);
			 comment.setMessage( reviewerComment );
			 try {
				saveDraft(comment);
			} catch (NoSuchChangeException e) {
				log.debug("NoSuchChangeException ",e);
			} catch (OrmException e) {
				log.debug("OrmException ",e);
			}
		 }
	}
	
	
	private Patch.Key getPatchKey(String patchUrl){
		Change.Id change;
		PatchSet.Id idSide;
		
		PatchDetail patchDetail = new PatchDetail(patchUrl);
		int patchId = patchDetail.getPatchSetId();
		int change_id = patchDetail.getChangeId();
		String fileName = patchDetail.getFileName();
		
		change = new Change.Id(change_id);
		idSide = new PatchSet.Id(change, patchId);
		
		try {
			control = changeControlFactory.validateFor(change);
		} catch (NoSuchChangeException e1) {
			log.debug("NoSuchChangeException ", e1);
		} catch (OrmException e1) {
			log.debug("OrmException ", e1);
		}
		
		Patch.Key parentKey = new Patch.Key(idSide, fileName);
		
		return parentKey;
		
	}
	
	
	
	// Arguement require: patchLineComment Object
	private PatchLineComment saveDraft( PatchLineComment comment ) throws NoSuchChangeException, OrmException {
		    if (comment.getStatus() != PatchLineComment.Status.DRAFT) {
		      throw new IllegalStateException("Comment published");
		    }

		    final Patch.Key patchKey = comment.getKey().getParentKey();
		    final PatchSet.Id patchSetId = patchKey.getParentKey();
		    final Change.Id changeId = patchKey.getParentKey().getParentKey();

		    try {
				db = dbFactory.open();
			} catch (OrmException e) {
				log.error("OrmException::",e);
			}
		    
		    db.changes().beginTransaction(changeId);
		    try {
		      changeControlFactory.validateFor(changeId);
		      if (db.patchSets().get(patchSetId) == null) {
		        throw new NoSuchChangeException(changeId);
		      }

		      CurrentUser user = control.getCurrentUser();
			  Account.Id me = user instanceof IdentifiedUser ? ((IdentifiedUser) user).getAccountId(): null;
		      
			  if (comment.getKey().get() == null) {
		        if (comment.getLine() < 1) {
		          throw new IllegalStateException("Comment line must be >= 1, not "
		              + comment.getLine());
		        }

		        if (comment.getParentUuid() != null) {
		          final PatchLineComment parent =
		              db.patchComments().get(
		                  new PatchLineComment.Key(patchKey, comment.getParentUuid()));
		          if (parent == null || parent.getSide() != comment.getSide()) {
		            throw new IllegalStateException("Parent comment must be on same side");
		          }
		        }
			        
		        final PatchLineComment nc =
		            new PatchLineComment(new PatchLineComment.Key(patchKey, ChangeUtil
		                .messageUUID(db)), comment.getLine(), me, comment.getParentUuid());
		        log.debug("patchKey "+patchKey);
		        log.debug("ChangeUtil.messageUUID(db) "+ChangeUtil.messageUUID(db));
		        log.debug("me "+me);
		        log.debug("comment.getParentUuid() "+comment.getParentUuid());
		        nc.setSide(comment.getSide());
		        nc.setMessage(comment.getMessage());
		        log.debug("Collections.singleton(nc)"+Collections.singleton(nc));
		        db.patchComments().insert(Collections.singleton(nc));
		        db.commit();
		        return nc;

		      } else {
		        if (!me.equals(comment.getAuthor())) {
		          throw new NoSuchChangeException(changeId);
		        }
		        comment.updated();
		        db.patchComments().update(Collections.singleton(comment));
		        db.commit();
		        db.close();
		        return comment;
		      }
		    } finally {
		      db.rollback();
		      db.close();
		    }
		  }
	 
	 
	private void deleteDraft(Patch.Key patch_key, String UUID) throws OrmException, Failure{
		 log.debug("UUID from deleteDraft() method "+UUID);
		 
		 PatchLineComment.Key commentKey = new PatchLineComment.Key(patch_key, UUID);
		 Change.Id changeId = commentKey.getParentKey().getParentKey().getParentKey();
		 log.debug("commentKey:: "+commentKey);
	     log.debug("Change.Id:: "+changeId);
	        try {
				db = dbFactory.open();
			} catch (OrmException e) {
				log.error("OrmException::",e);
			}
	        
	        db.changes().beginTransaction(changeId);
	        
	        try {
	          log.debug("Inside try block PatchLineComment ");
	          PatchLineComment comment = db.patchComments().get( commentKey );
	          log.debug("PatchLineComment:: "+comment);
	          if (comment == null) {
	            throw new Failure(new NoSuchEntityException());
	          }
	          
	          final CurrentUser user = control.getCurrentUser();
	          final Account.Id me = user instanceof IdentifiedUser ? ((IdentifiedUser) user).getAccountId(): null;
	      	  log.debug(" comment.getAuthor()::"+comment.getAuthor() );
	          if ( null != me && !me.equals(comment.getAuthor())) {
	            throw new Failure(new NoSuchEntityException());
	          }
	          if (comment.getStatus() != PatchLineComment.Status.DRAFT) {
	            throw new Failure(new IllegalStateException("Comment published"));
	          }
	          db.patchComments().delete(Collections.singleton(comment));
	          db.commit();
	          db.close();
	        } finally {
	          db.rollback();
	          db.close();
	        }
	 }
	 
	private void validateChangeId( String patchUrl ){
		PatchDetail patchDetail = new PatchDetail(patchUrl);
		Change.Id changeId = new Change.Id( patchDetail.getChangeId() );
		try {
			control = changeControlFactory.validateFor( changeId );
		} catch (NoSuchChangeException e1) {
			log.debug("NoSuchChangeException ", e1);
		} catch (OrmException e1) {
			log.debug("OrmException ", e1);
		}
	}
	

	private List<DraftMessage> loadDraftMessage( String baseUrl, String patchUrl ) throws OrmException {
		DraftUtil draftUtil = new DraftUtil();
		List<DraftMessage> loadDraftMessage = draftUtil.loadDraftMessage(dbFactory, changeControlFactory, baseUrl, patchUrl);
		return loadDraftMessage;
	  }
}
