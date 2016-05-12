package it.infn.mw.iam.oidc;

import org.springframework.security.authentication.AuthenticationServiceException;

public class OidcClientError extends AuthenticationServiceException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private final String error;
  private final String errorDescription;
  private final String errorUri;

  public OidcClientError(String message, String error, String errorDescription,
    String errorUri) {
    super(message);
    this.error = error;
    this.errorDescription = errorDescription;
    this.errorUri = errorUri;

  }

  public String getError() {

    return error;
  }

  public String getErrorDescription() {

    return errorDescription;
  }

  public String getErrorUri() {

    return errorUri;
  }

}