package io.macgyver.core.scheduler;

import groovy.lang.Closure;
import groovy.lang.DelegatingMetaClass;
import groovy.lang.ExpandoMetaClass;
import io.macgyver.core.Kernel;
import io.macgyver.core.script.ScriptExecutor;

import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

public class SchedulerUtil  {
	



	static Logger logger = LoggerFactory.getLogger(SchedulerUtil.class);
	
	static Map<String,Closure> closureMap = Maps.newConcurrentMap();
	
	public static class JobImpl implements Job {

		@Override
		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			
			
			String closureUUID = context.getJobDetail().getJobDataMap().getString("_code");
			if (closureUUID!=null) {
				Closure closure = closureMap.get(closureUUID);
				closure.call();
			}
			
		}
		
	}
	public void scheduleCronScript(String crontab, String description, Object script) throws SchedulerException, ParseException  {
		
		logger.info("Scheduling "+crontab+"   "+script.getClass());
		String closureUUID = UUID.randomUUID().toString();
		  	CronTriggerImpl cti = new CronTriggerImpl();
			cti.setCronExpression(crontab);
			cti.setName(UUID.randomUUID().toString());
			
			if (script instanceof Closure) {
				
				
				closureMap.put(closureUUID,(Closure) script);
			
			}
			
			JobDetail jd = JobBuilder.newJob(JobImpl.class).usingJobData("_code",closureUUID).withDescription(description).build();
			
			logger.info("scheduleJob {} {}",jd,cti);
			Kernel.getInstance().getApplicationContext().getBean(Scheduler.class).scheduleJob(jd, cti);
	}
	
	public static void schedule(Map<String,Object> x) {
		
	}
	public static void scheduleCron(String crontab, String description, Object script) throws SchedulerException, ParseException{
		SchedulerUtil util = Kernel.getInstance().getApplicationContext().getBean(SchedulerUtil.class);
		logger.info("scheduler util: {}",util);
		util.scheduleCronScript(crontab, description, script);
		
		
	}
	
	

}
