/**
 * 
 */
package de.xwic.appkit.core.scheduler;

import java.io.Serializable;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;

import de.xwic.appkit.core.cluster.ClusterEvent;
import de.xwic.appkit.core.cluster.ClusterEventListener;
import de.xwic.appkit.core.cluster.CommunicationException;
import de.xwic.appkit.core.cluster.EventTimeOutException;
import de.xwic.appkit.core.cluster.ICluster;
import de.xwic.appkit.core.cluster.IClusterService;
import de.xwic.appkit.core.cluster.IClusterServiceHandler;
import de.xwic.appkit.core.cluster.IRemoteService;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.daos.IActivityDAO;
import de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO;
import de.xwic.appkit.core.model.entities.IActivity;
import de.xwic.appkit.core.model.entities.IServerConfigProperty;
import de.xwic.appkit.core.model.util.ServerConfig;

/**
 * Scheduler implementation used inside the cluster support. Looks also by using Server Config
 * Property that no other node currently executes the scheduler.
 * 
 * @author dotto
 */
public class QuartzClusterScheduler extends QuartzScheduler implements IScheduler, IClusterService,
		ISchedulerClusterService {

	private static Log log = LogFactory.getLog(QuartzClusterScheduler.class);
	private static final int RETRY = 60; // tries for 60 sec. to start the scheduler
	private static final long THRESHOLD = 1000 * 60; // 1min.
	private static final String TIMESTMAP_KEY = "scheduler.upTimestamp";
	private IServerConfigProperty timeStampProp = null;
	private boolean activeInstance = false;

	protected String myName = null;
	protected ICluster cluster;

	protected boolean master = false;
	protected IClusterServiceHandler csHandler;

	/**
	 * @param tz
	 * @throws SchedulerException
	 */
	public QuartzClusterScheduler(TimeZone tz) throws SchedulerException {
		super(tz);
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.scheduler.QuartzScheduler#start()
	 */
	@Override
	public void start() {
		if (isMaster()) {
			startInstance();

			try {
				cluster.sendEvent(new ClusterEvent(getClass().getName(), "start", null), true);
			} catch (EventTimeOutException e) {
				log.warn("Error distributing start event", e);
			}

		} else {

			// obtain next number from master
			IRemoteService rsMaster = csHandler.getMasterService();
			if (rsMaster != null) {
				try {
					rsMaster.invokeMethod("start", null);
				} catch (CommunicationException e) {
					throw new IllegalStateException("Scheduler Activation from remote master failed.", e);
				}
			}
		}
	}

	/**
	 * Internal used class which really starts Quartz instance.
	 */
	private synchronized void startInstance() {

		if (!activeInstance) {
			try {
				boolean checked = false;
				int checkCount = 1;
				while (!checked) {

					Long lastTimestampLong = getTimestameConfigProp();
					if (lastTimestampLong != null) {
						long currentTime = System.currentTimeMillis();
						if ((lastTimestampLong + THRESHOLD) > currentTime) {
							if (checkCount >= RETRY) {
								throw new SchedulerException(
										"Scheduler seems to be active on another cluster instance");
							}
							checkCount++;
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								log.error(e.getMessage(), e);
							}
						} else {
							checked = true;
						}
					} else {
						checked = true;
					}
				}

				scheduler.start();
				activeInstance = true;
				addCurrentTimeStamp();
				init();
				Thread t1 = new Thread(new ConfigUpdateSchedulerThread());
				t1.start();
				log.debug("Scheduler started");
			} catch (SchedulerException e) {
				log.error("Cannot start the scheduler", e);
			}
		} else {
			log.debug("Scheduler already running");
		}
	}

	/**
	 * @return
	 */
	private Long getTimestameConfigProp() {
		IServerConfigPropertyDAO dao = (IServerConfigPropertyDAO) DAOSystem
				.getDAO(IServerConfigPropertyDAO.class);

		if (timeStampProp == null) {
			timeStampProp = dao.getConfigProperty(TIMESTMAP_KEY);
		}

		if (timeStampProp == null) {
			return null;
		}

		HibernateUtil.currentSession().refresh(timeStampProp);
		return Long.parseLong(timeStampProp.getValue());
	}

	private void addCurrentTimeStamp() {
		String timeString = String.valueOf(System.currentTimeMillis());
		ServerConfig.set(TIMESTMAP_KEY, timeString);
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.scheduler.QuartzScheduler#stop()
	 */
	@Override
	public void stop() {

		if (isMaster()) {
			stopInstance();
			// don't send anything. It just means the scheduler needs to be shutdown and another
			// node might get master which will take it.
			/*
			 * try { cluster.sendEvent(new ClusterEvent(getClass().getName(), "stopScheduler",
			 * null), true); } catch (EventTimeOutException e) {
			 * log.warn("Error distributing stopScheduler event", e); }
			 */

		}
	}

	private synchronized void stopInstance() {
		if (activeInstance) {
			super.stop();
			activeInstance = false;
			log.debug("Scheduler stopped");
		} else {
			log.debug("Scheduler currently not running...");
		}
	}

	/**
	 * Remotely invoked on master to add the job
	 * 
	 * @param id
	 * @throws ClassNotFoundException
	 * @throws SchedulerException
	 */
	public void addCronJob(Integer id) throws SchedulerException, ClassNotFoundException {
		try {
			log.debug("Invoke addCronJob function for activity #" + id);

			super.addCronJob(getActivity(id));
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private IActivity getActivity(Integer id) {
		IActivityDAO activityDao = DAOSystem.getDAO(IActivityDAO.class);
		IActivity activity = activityDao.getEntity(id);
		return activity;
	}

	/**
	 * Remotely invoked on master to change the job
	 * 
	 * @param id
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	public void changeCronJob(Integer id) throws SchedulerException, ClassNotFoundException {
		try {
			log.debug("Invoke changeCronJob function for activity #" + id);
			super.changeCronJob(getActivity(id));
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.xwic.appkit.core.scheduler.QuartzScheduler#changeCronJob(de.xwic.appkit.core.model.entities
	 * .IActivity)
	 */
	@Override
	public void changeCronJob(IActivity activity) throws SchedulerException, ClassNotFoundException {
		Integer entityId = new Integer(activity.getId());
		if (isMaster()) {
			super.changeCronJob(activity);
			try {
				cluster.sendEvent(new ClusterEvent(getClass().getName(), "changeCronJob", entityId), true);
			} catch (EventTimeOutException e) {
				log.warn("Error distributing addCronJob event", e);
			}

		} else {

			// obtain next number from master
			IRemoteService rsMaster = csHandler.getMasterService();
			if (rsMaster != null) {
				try {
					rsMaster.invokeMethod("changeCronJob", new Serializable[] { entityId });
				} catch (CommunicationException e) {
					throw new IllegalStateException("Scheduler change job from remote master failed.", e);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.xwic.appkit.core.scheduler.QuartzScheduler#addCronJob(de.xwic.appkit.core.model.entities
	 * .IActivity)
	 */
	@Override
	public void addCronJob(IActivity activity) throws SchedulerException, ClassNotFoundException {
		Integer entityId = new Integer(activity.getId());
		if (isMaster()) {
			super.addCronJob(activity);
			try {
				cluster.sendEvent(new ClusterEvent(getClass().getName(), "addCronJob", entityId), true);
			} catch (EventTimeOutException e) {
				log.warn("Error distributing addCronJob event", e);
			}

		} else {
			// obtain next number from master
			IRemoteService rsMaster = csHandler.getMasterService();
			if (rsMaster != null) {

				try {
					rsMaster.invokeMethod("addCronJob", new Serializable[] { entityId });
				} catch (CommunicationException e) {
					throw new IllegalStateException("Scheduler add job from remote master failed.", e);
				}
			}
		}
	}

	/**
	 * @param id
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	public void runJob(Integer id) throws SchedulerException, ClassNotFoundException {
		try {
			log.debug("Invoke runJob function for activity #" + id);
			super.runJob(getActivity(id));
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.xwic.appkit.core.scheduler.QuartzScheduler#runJob(de.xwic.appkit.core.model.entities.IActivity
	 * )
	 */
	@Override
	public void runJob(IActivity activity) {
		Integer entityId = new Integer(activity.getId());
		if (isMaster()) {
			super.runJob(activity);
			try {
				cluster.sendEvent(new ClusterEvent(getClass().getName(), "runJob", entityId), true);
			} catch (EventTimeOutException e) {
				log.warn("Error distributing addCronJob event", e);
			}
		} else {
			IRemoteService rsMaster = csHandler.getMasterService();
			if (rsMaster != null) {
				try {
					rsMaster.invokeMethod("runJob", new Serializable[] { entityId });
				} catch (CommunicationException e) {
					throw new IllegalStateException("Scheduler run job from remote master failed.", e);
				}
			}
		}
	}

	/**
	 * @param id
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	public Boolean interruptJob(Integer id) throws SchedulerException, ClassNotFoundException {
		boolean result;
		try {
			log.debug("Invoke runJob function for activity #" + id);
			result = super.interruptJob(getActivity(id));
		} finally {
			HibernateUtil.closeSession();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.xwic.appkit.core.scheduler.QuartzScheduler#interruptJob(de.xwic.appkit.core.model.entities
	 * .IActivity)
	 */
	@Override
	public boolean interruptJob(IActivity activity) {
		Integer entityId = new Integer(activity.getId());
		if (isMaster()) {
			boolean result = super.interruptJob(activity);
			try {
				cluster.sendEvent(new ClusterEvent(getClass().getName(), "interruptJob", entityId), true);
			} catch (EventTimeOutException e) {
				log.warn("Error distributing addCronJob event", e);
			}
			return result;
		} else {
			IRemoteService rsMaster = csHandler.getMasterService();
			if (rsMaster != null) {
				try {
					Boolean result = (Boolean) rsMaster.invokeMethod("interruptJob",
							new Serializable[] { entityId });
					return result;
				} catch (CommunicationException e) {
					throw new IllegalStateException("Scheduler run job from remote master failed.", e);
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IClusterService#onRegistration(java.lang.String,
	 * de.xwic.appkit.cluster.ICluster, de.xwic.appkit.cluster.impl.ClusterServiceHandler)
	 */
	@Override
	public void onRegistration(String name, ICluster cluster, IClusterServiceHandler csHandler) {
		this.myName = name;
		this.cluster = cluster;
		this.csHandler = csHandler;

		cluster.addEventListener(new ClusterEventListener() {

			@Override
			public void receivedEvent(ClusterEvent event) {

			}
		}, getClass().getName());

	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IClusterService#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return master;
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IClusterService#surrenderMasterRole()
	 */
	@Override
	public Serializable surrenderMasterRole() {
		log.debug("surrenderMasterRole for Scheduler...");
		master = false;
		stopInstance();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IClusterService#obtainMasterRole(java.io.Serializable)
	 */
	@Override
	public void obtainMasterRole(Serializable remoteMasterData) {
		master = true;
		log.debug("ObtainMasterRole for Scheduler...");
		startInstance();
	}

	/**
	 * Thread that set periodically new timestamp as server config property
	 * 
	 * @author dotto
	 */
	private class ConfigUpdateSchedulerThread implements Runnable {

		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while (activeInstance) {
				// just fetch it to be in sync.
				addCurrentTimeStamp();
				try {
					Thread.sleep(THRESHOLD);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

}
