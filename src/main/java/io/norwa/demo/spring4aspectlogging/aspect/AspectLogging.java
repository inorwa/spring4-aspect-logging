package io.norwa.demo.spring4aspectlogging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class AspectLogging {

	@Autowired(required = false)
	private HttpServletRequest request;

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
			"|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
			"|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
			"|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
			"|| @annotation(org.springframework.web.bind.annotation.PatchMapping)" +
			"|| @annotation(org.springframework.web.bind.annotation.PutMapping)")
	public void controllerMappings() {
	}


	@Around("controllerMappings()")
	public Object logInput(ProceedingJoinPoint joinPoint) throws Throwable {

		final String params = request.getParameterMap().entrySet()
				.stream()
				.map(e -> e.getKey() + "=" + Arrays.stream(e.getValue()).collect(Collectors.joining(",")))
				.collect(Collectors.joining(";"));

		StopWatch sw = new StopWatch();
		sw.start();
		Object obj = joinPoint.proceed();
		sw.stop();

		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		logger.info(joinPoint.getSignature().getName() + " path " + request.getServletPath() +
				" from " + request.getRemoteAddr() +
				" by user " + request.getRemoteUser() +
				" with params " + params +
				" takes " + sw.getLastTaskTimeMillis() + " ms");

		return obj;
	}
}
