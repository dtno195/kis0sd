package com.sd.config.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sd.common.exception.BusinessMessage;
import com.sd.common.exception.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Dustin
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Autowired
  private ObjectMapper objectMapper;


  /**
   * Handle authentication exception.
   *
   * @param request  the request
   * @param response the response
   * @param e        the authentication exception
   * @throws IOException      the io exception
   * @throws ServletException the servlet exception
   */
  @Override
  public void commence(HttpServletRequest request,
                       HttpServletResponse response,
                       AuthenticationException e) throws IOException, ServletException {
    log.error("JwtAuthenticationEntryPoint: {}", e.getMessage());
    response.resetBuffer();
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    final String message = e.getMessage().startsWith("message.") ? e.getMessage() : BusinessMessage.JWT_UNAUTHORIZED_REQUEST;
    ResponseEntity<String> responseBody = ResponseEntity.status(ResponseCode.CODE_UNAUTHORIZED)
            .body(message);
    response.getWriter().write(this.objectMapper.writeValueAsString(responseBody));
    response.flushBuffer();
  }
}
