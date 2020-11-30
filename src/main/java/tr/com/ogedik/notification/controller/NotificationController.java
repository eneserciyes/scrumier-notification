package tr.com.ogedik.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tr.com.ogedik.commons.constants.Services;
import tr.com.ogedik.commons.rest.request.model.EmailRequest;
import tr.com.ogedik.commons.rest.response.AbstractResponse;
import tr.com.ogedik.notification.services.AuthenticatedEmailService;

/*
 * @author enes.erciyes
 */
@Controller
public class NotificationController {
  @Autowired private AuthenticatedEmailService authenticatedEmailService;

  @PostMapping(Services.Path.MAIL)
  public AbstractResponse sendMail(@RequestBody EmailRequest request) {
    return AbstractResponse.build(
        authenticatedEmailService.sendEmail(
            request.getTo(), request.getSubject(), request.getBody()));
  }
}
