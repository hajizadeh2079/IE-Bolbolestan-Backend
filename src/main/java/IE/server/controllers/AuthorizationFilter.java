package IE.server.controllers;

import IE.server.services.Signature;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(2)
@ WebFilter(asyncSupported = true, urlPatterns = { "/*" })
public class AuthorizationFilter implements Filter {

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        if(req.getServletPath().contains("/students") && !req.getServletPath().contains("/password/reset"))
            chain.doFilter(request, response);

        else if(req.getServletPath().contains("/students/password/reset")) {
            String jws = req.getHeader("Token");
            try {
                String id =  Jwts.parser().setSigningKey(Signature.getSignature("bolbolestan")).parseClaimsJws(jws).getBody().getSubject();
                id = id.replace("forgetPassword", "");
                request.setAttribute("id", id);
                chain.doFilter(request, response);
            }
            catch (JwtException e) {
                resp.setStatus(403);
            }
            catch (IllegalArgumentException e) {
                resp.setStatus(401);
            }
        }

        else {
            String jws = req.getHeader("Token");
            try {
                String id =  Jwts.parser().setSigningKey(Signature.getSignature("bolbolestan")).parseClaimsJws(jws).getBody().getSubject();
                if(id.contains("forgetPassword"))
                    resp.setStatus(403);
                else {
                    request.setAttribute("id", id);
                    chain.doFilter(request, response);
                }
            }
            catch (JwtException e) {
                resp.setStatus(403);
            }
            catch (IllegalArgumentException e) {
                resp.setStatus(401);
            }
        }
    }

    @Override
    public void destroy() {
    }
}