package db.training.easy.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharsetFilter implements Filter {

	FilterConfig config;

	String encoding = "UTF-8";

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * Sets the character encoding on the request
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.Servle tRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
		request.setCharacterEncoding(encoding);
		chain.doFilter(request, response);
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConf ig)
	 */
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		this.encoding = config.getInitParameter("requestEncoding");
	}
}