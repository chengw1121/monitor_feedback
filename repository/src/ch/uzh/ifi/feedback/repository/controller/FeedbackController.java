package ch.uzh.ifi.feedback.repository.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;
import ch.uzh.ifi.feedback.repository.validation.FeedbackValidator;

@RequestScoped
@Controller(Feedback.class)
public class FeedbackController extends RestController<Feedback>{

	@Inject
	public FeedbackController(FeedbackService dbService, FeedbackValidator validator, HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
	}
	
	@Path("/feedbacks")
	@GET
	@Authenticate(UserAuthenticationService.class)
	public List<Feedback> GetAll() throws Exception {
		return super.GetAll();
	}
	
	@Path("/applications/{appId}/feedbacks")
	@GET
	@Authenticate(UserAuthenticationService.class)
	public List<Feedback> GetAllForApplication(@PathParam("appId")Integer applicationId) throws Exception {
		return super.GetAllFor("application_id", applicationId);
	}
	
	@Path("/feedbacks/{id}")
	@GET
	@Authenticate(UserAuthenticationService.class)
	public Feedback GetByFeedbackId(@PathParam("id")Integer id) throws Exception {
		return super.GetById(id);
	}
	
	@Path("/feedbacks")
	@POST
	public Feedback InsertFeedback(Feedback feedback) throws Exception {
		return super.Insert(feedback);
	}
	
	@Path("/feedbacks/{id}")
	@DELETE
	@Authenticate(UserAuthenticationService.class)
	public void DeleteFeedback(@PathParam("id")Integer id) throws Exception 
	{
		super.Delete(id);
	}
}
