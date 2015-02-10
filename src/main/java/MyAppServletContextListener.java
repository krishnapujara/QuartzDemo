import org.quartz.*;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyAppServletContextListener implements ServletContextListener{

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("ServletContextListener destroyed");
    }

    //Run this before web application is started
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("ServletContextListener started 1");
        ServletContext ctx = servletContextEvent.getServletContext();
        StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY);

        JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("dummyJobName", "group1").build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dummyTriggerName", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *"))
                .build();

        //schedule it
        Scheduler scheduler = null;
        try{
            scheduler = factory.getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        }catch(SchedulerException schedulerException){
            schedulerException.printStackTrace();
        }
    }
}