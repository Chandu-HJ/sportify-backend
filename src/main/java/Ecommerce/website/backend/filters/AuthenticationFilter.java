package Ecommerce.website.backend.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import Ecommerce.website.backend.Entities.Role;
import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.UsersRepo;
import Ecommerce.website.backend.service.Auth;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class AuthenticationFilter extends OncePerRequestFilter {



	private final Auth authService;
	private final UsersRepo userRepo;
	
	
	@Autowired
	public AuthenticationFilter(Auth authService, UsersRepo userRepo) {
		this.authService = authService;
		this.userRepo = userRepo;
	}
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		//Allow unauthenticated access to specific endPoints
		if(httpRequest.getRequestURI().equals("/api/users/register") || httpRequest.getRequestURI().equals("/api/auth/login")) {
			chain.doFilter(request, response);
			return;
		}
		
		if(httpRequest.getMethod().equalsIgnoreCase("OPTIONS" )) {
			httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			return;
		}
		
		String token = getAuthTokenFromCookies(httpRequest);
		if(token == null || !authService.validateToken(token)) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.getWriter().write("Unauthorized: Invalid or missing token");
			return;
		}
		
		String username = authService.getUsernameFromToken(token);
		
		Optional<User> userOptional = userRepo.findByUsername(username);
		
		if(userOptional.isEmpty()) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.getWriter().write("Unauthorized: user not found");
			return;
		}
		
		if(httpRequest.getRequestURI().startsWith("/api/customer") && userOptional.get().getRole() != Role.CUSTOMER) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.getWriter().write("Unauthorized: Role MissMathched Customers Only");
			return;
		}
			
		if(httpRequest.getRequestURI().startsWith("/api/admin") && userOptional.get().getRole() != Role.ADMIN) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.getWriter().write("Unauthorized: Role MissMathched Admins Only");
			return;
		}
			
		httpRequest.setAttribute("authenticatedUser", userOptional.get());
		chain.doFilter(request, response);
		
	}
	
	
	
	
	private String getAuthTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
//			return Arrays.stream(cookies)
//					.filter(cookie -> "authToken".equals(cookie.getName()))
//					.map(cookie-> cookie.getValue())
//					.findFirst()
//					.orElse(null);
			
			for(Cookie cookie: cookies) {
				if("authToken".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
			
		}
		
		return null;
		
		
	}
	

}


//@WebFilter(urlPatterns = "/api/*")
//public class AuthenticationFilter implements Filter {
//
//
//
//	private final Auth authService;
//	private final UsersRepo userRepo;
//	
//	
//	@Autowired
//	public AuthenticationFilter(Auth authService, UsersRepo userRepo) {
//		this.authService = authService;
//		this.userRepo = userRepo;
//	}
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		HttpServletRequest httpRequest = (HttpServletRequest) request;
//		HttpServletResponse httpResponse = (HttpServletResponse) response;
//		
//		//Allow unauthenticated access to specific endPoints
//		if(httpRequest.getRequestURI().equals("/api/users/register") || httpRequest.getRequestURI().equals("/api/auth/login")) {
//			chain.doFilter(request, response);
//			return;
//		}
//		
//		if(httpRequest.getMethod().equalsIgnoreCase("OPTIONS" )) {
//			httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
//			httpResponse.setStatus(HttpServletResponse.SC_OK);
//			return;
//		}
//		
//		String token = getAuthTokenFromCookies(httpRequest);
//		if(token == null || !authService.validateToken(token)) {
//			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			httpResponse.getWriter().write("Unauthorized: Invalid or missing token");
//			return;
//		}
//		
//		String username = authService.getUsernameFromToken(token);
//		
//		Optional<User> userOptional = userRepo.findByUsername(username);
//		
//		if(userOptional.isEmpty()) {
//			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			httpResponse.getWriter().write("Unauthorized: user not found");
//			return;
//		}
//		
//		if(httpRequest.getRequestURI().startsWith("/api/customer") && userOptional.get().getRole() != Role.CUSTOMER) {
//			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			httpResponse.getWriter().write("Unauthorized: Role MissMathched Customers Only");
//			return;
//		}
//			
//		if(httpRequest.getRequestURI().startsWith("/api/admin") && userOptional.get().getRole() != Role.ADMIN) {
//			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			httpResponse.getWriter().write("Unauthorized: Role MissMathched Admins Only");
//			return;
//		}
//			
//		httpRequest.setAttribute("authenticatedUser", userOptional.get());
//		chain.doFilter(request, response);
//		
//	}
//	
//	
//	private String getAuthTokenFromCookies(HttpServletRequest request) {
//		Cookie[] cookies = request.getCookies();
//		if(cookies != null) {
////			return Arrays.stream(cookies)
////					.filter(cookie -> "authToken".equals(cookie.getName()))
////					.map(cookie-> cookie.getValue())
////					.findFirst()
////					.orElse(null);
//			
//			for(Cookie cookie: cookies) {
//				if("authToken".equals(cookie.getName())) {
//					return cookie.getValue();
//				}
//			}
//			
//		}
//		
//		return null;
//		
//		
//	}
//
//}



