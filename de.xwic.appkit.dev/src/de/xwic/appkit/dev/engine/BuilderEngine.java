/**
 * 
 */
package de.xwic.appkit.dev.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import de.xwic.appkit.dev.engine.model.DataModel;
import de.xwic.appkit.dev.engine.model.EntityModel;
import de.xwic.appkit.dev.engine.model.EntityProperty;

/**
 * @author lippisch
 *
 */
public class BuilderEngine {

	private final Log log = LogFactory.getLog(getClass()) ;
	
	private AppContext context;
	private VelocityEngine ve;

	private Template tplEntity;
	private Template tplEntityInterface;
	private Template tplEntityHistory;
	private Template tplEntityFromBase;
	private Template tplDAOImpl;
	private Template tplHeader;
	private Template tplDAOInterface;
	private Template tplHbmXml;
	private Template tplHbmHisXml;
	private Template tplEntityXml;
	private Template tplListsetupXml;
	private Template tplEditorXml;

	private Template tplModelConfig;

	private Template tplProductSetup;

	/**
	 * Construct new BuilderEngine on given context.
	 * @param context
	 */
	public BuilderEngine(AppContext context) {
		this.context = context;
	}
	
	/**
	 * 
	 */
	public void start() {
		
		ve = new VelocityEngine();
		Properties prop = new Properties();
		prop.setProperty("resource.loader", "file, class");
		prop.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		prop.setProperty("file.resource.loader.path", ".");
		prop.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		ve.init(prop);

		tplHeader = ve.getTemplate("tpl/java_header.vtl");
		tplEntity = ve.getTemplate("tpl/entity_impl.vtl");
		tplEntityHistory = ve.getTemplate("tpl/entity_historyimpl.vtl");
		tplEntityFromBase = ve.getTemplate("tpl/entity_implfrombase.vtl");
		tplHbmXml = ve.getTemplate("tpl/hbmxml.vtl");
		tplHbmHisXml = ve.getTemplate("tpl/hbmhistoryxml.vtl");
		tplEntityInterface = ve.getTemplate("tpl/entity_interface.vtl");
		tplDAOImpl = ve.getTemplate("tpl/dao_impl.vtl");
		tplDAOInterface = ve.getTemplate("tpl/dao_interface.vtl");

		tplEntityXml = ve.getTemplate("tpl/entity_xml.vtl");
		tplListsetupXml = ve.getTemplate("tpl/listsetup_xml.vtl");
		tplEditorXml = ve.getTemplate("tpl/editor_xml.vtl");
		tplModelConfig = ve.getTemplate("tpl/model_config.vtl");
		tplProductSetup = ve.getTemplate("tpl/product_setup.vtl");

	}

	/**
	 * 
	 */
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return "1";
	}

	/**
	 * @param model
	 * @throws IOException 
	 */
	public void generateFiles(DataModel model, boolean daoFiles, boolean listSetup, boolean editors) throws IOException {
		
		log.info("Domain: " + model.getDomainId());
		
		File srcBase = new File(context.getRepositoryRoot(), 
				model.getProjectName() + "/" + 
				context.getSourceFolderName() + "/" + 
				model.getPackageName().replace('.', '/'));

		log.info("Source File Base: " + srcBase.getCanonicalPath());
		
		if (!srcBase.exists()) {
			srcBase.mkdirs();
		}
		
		
		for (EntityModel entity : model.getEntities()) {
			log.info("Processing Entity " + entity.getName());

			// generate the entity class
			
			File entityImplFolder = new File(srcBase, "entities/impl");
			if (!entityImplFolder.exists()) {
				entityImplFolder.mkdirs();
			}
			
			VelocityContext veCtx = new VelocityContext();
			veCtx.put("package", model.getPackageName());
			veCtx.put("model", model);
			veCtx.put("entity", entity);

			if (daoFiles) {
				if(entity.isHistoryEntity()) {
					File entityHisImplFolder = new File(srcBase, "entities/impl/history");
					if (!entityHisImplFolder.exists()) {
						entityHisImplFolder.mkdirs();
					}
					File entityFromBaseJavaFile = new File(entityImplFolder, entity.getName() + ".java");
					generateFile(entityFromBaseJavaFile, veCtx, tplEntityFromBase);
					
					File entityHistoryJavaFile = new File(entityHisImplFolder, entity.getName() + "History.java");
					generateFile(entityHistoryJavaFile, veCtx, tplEntityHistory);
				}
				
				File entityJavaFile = new File(entityImplFolder, entity.getName() + (entity.isHistoryEntity() ? "Base.java" : ".java"));
				generateFile(entityJavaFile, veCtx, tplEntity);
				
				File hbmXmlFile = new File(entityImplFolder, entity.getName() + ".hbm.xml");
				generateFile(hbmXmlFile, veCtx, tplHbmXml, false);
				
				if(entity.isHistoryEntity()) {
					File hbmHisXmlFile = new File(entityImplFolder, entity.getName() + "_HIS.hbm.xml");
					generateFile(hbmHisXmlFile, veCtx, tplHbmHisXml, false);
				}
				
				// generate the interface file
				File entityFolder = new File(srcBase, "entities");
				// it must already exist as it is simply the parent of the implementation folder
				File entityInterfaceJavaFile = new File(entityFolder, "I" + entity.getName() + ".java");
				generateFile(entityInterfaceJavaFile, veCtx, tplEntityInterface);
	
				// generate the DAO implementation file
				File daoImplFolder = new File(srcBase, "dao/impl");
				if (!daoImplFolder.exists()) {
					daoImplFolder.mkdirs();
				}
				File daoImplJavaFile = new File(daoImplFolder, entity.getName() + "DAO.java");
				generateFile(daoImplJavaFile, veCtx, tplDAOImpl);
	
				// and the DAO interface
				File daoFolder = new File(srcBase, "dao");
				File daoJavaFile = new File(daoFolder, "I" + entity.getName() + "DAO.java");
				generateFile(daoJavaFile, veCtx, tplDAOInterface);
				
			}
			
			// add the entity.xml in the product configuration
			if (!context.getProductConfigFolder().exists()) {
				context.getProductConfigFolder().mkdirs();
			}
			File domainFolder = new File(context.getProductConfigFolder(), model.getDomainId());
			domainFolder.mkdirs();

			// entities xml file
			File cfgEntities = new File(domainFolder, "entities");
			cfgEntities.mkdirs();
			File entityXmlFile = new File(cfgEntities, entity.getName() + ".xml");
			generateFile(entityXmlFile, veCtx, tplEntityXml,false);

			if (listSetup) {
				// listsetup
				File cfgListsetup = new File(domainFolder, "listsetup");
				cfgListsetup.mkdirs();
				File entityListetupFile = new File(cfgListsetup, entity.getName() + "-default.list.xml");
				generateFile(entityListetupFile, veCtx, tplListsetupXml,false);
			}

			if (editors) {
				// editor
				File cfgEditors = new File(domainFolder, "editors");
				cfgEditors.mkdirs();
				File editorFile = new File(cfgEditors, entity.getName() + "-default.editor.xml");
				generateFile(editorFile, veCtx, tplEditorXml,false);
			}
			
		}
		
		if (daoFiles) {
			VelocityContext veCtx = new VelocityContext();
			veCtx.put("package", model.getPackageName());
			veCtx.put("model", model);

			String javaDomainName = model.getDomainId().substring(0, 1).toUpperCase() + model.getDomainId().substring(1);
			File modelConfigFile = new File(srcBase, javaDomainName + "ModelConfig.java");
			
			veCtx.put("domain", javaDomainName);
			generateFile(modelConfigFile, veCtx, tplModelConfig);
		}

		// product setup part
		VelocityContext veCtx = new VelocityContext();
		veCtx.put("package", model.getPackageName());
		veCtx.put("model", model);

		File prodSetupPartFile = new File(context.getProductConfigFolder(), "product.setup-part.xml");
		
		veCtx.put("domain", model.getDomainId());
		generateFile(prodSetupPartFile, veCtx, tplProductSetup, false);

		
	}

	private void generateFile(File javaFile, VelocityContext veCtx, Template template) {
		generateFile(javaFile, veCtx, template, true);
	}

	/**
	 * @param javaFile
	 * @param veCtx
	 * @param tplEntity2
	 */
	private void generateFile(File javaFile, VelocityContext veCtx, Template template, boolean includeHeader) {
		
		try {
			FileWriter fw = new FileWriter(javaFile, false);
			if (includeHeader) {
				tplHeader.merge(veCtx, fw);
			}
			template.merge(veCtx, fw);
			fw.flush();
			fw.close();
			context.countFileCreated();
		} catch (IOException ioe) {
			log.error("Error writing template", ioe);
		}

	}

	/**
	 * Bundles are properties files with the labels. By default we try to 'keep' the old values as they
	 * may have been adjusted.
	 * @param model
	 * @throws IOException 
	 * @throws ConfigurationException 
	 */
	public void updateBundles(DataModel model, boolean override) throws IOException, ConfigurationException {

		if (!context.getProductConfigFolder().exists()) {
			context.getProductConfigFolder().mkdirs();
		}
		File bundleFolder = new File(context.getProductConfigFolder(), model.getDomainId() + "/bundles");
		bundleFolder.mkdirs();

		File entityBundle = new File(bundleFolder, model.getDomainId() + ".entities.resources_en.properties");
		
		Properties oldProperties = new Properties();
		if (entityBundle.exists() && !override) {
			FileReader fr = new FileReader(entityBundle);
			oldProperties.load(fr);
			fr.close();
		}
		
		Set<String> keysUsed = new HashSet<String>();
		
		FileWriter writer = new FileWriter(entityBundle);
		PrintWriter pw = new PrintWriter(writer);
		try {
			
			pw.println("# Entity Bundle for " + model.getDomainId() + " entities.");
			pw.println("# Created by AppKitDev ");
			pw.println("");
			
			for (EntityModel entity : model.getEntities()) {
				pw.println("# " + entity.getName());
				String fullClass = model.getPackageName() + ".entities.I" + entity.getName();
				writeBundleLine(pw, oldProperties, keysUsed, fullClass, entity.getName());
				for(EntityProperty prop : entity.getProperties()) {
					String propTitle = prop.getName().substring(0, 1).toUpperCase() + prop.getName().substring(1);
					writeBundleLine(pw, oldProperties, keysUsed, fullClass + "." + prop.getName(), propTitle);
				}
				writeBundleLine(pw, oldProperties, keysUsed, fullClass + ".createdAt", "Created At");
				writeBundleLine(pw, oldProperties, keysUsed, fullClass + ".createdFrom", "Created From");
				writeBundleLine(pw, oldProperties, keysUsed, fullClass + ".lastModifiedAt", "Last Modified At");
				writeBundleLine(pw, oldProperties, keysUsed, fullClass + ".lastModifiedFrom", "Last Modified From");
				pw.println("");
			}
			
			// write those entries that have not been written yet
			for (Object key : oldProperties.keySet()) {
				String sKey = (String)key;
				if (!keysUsed.contains(sKey)) {
					pw.println(sKey + "=" + oldProperties.getProperty(sKey, ""));
				}
			}
			
		} finally {
			pw.flush();
			pw.close();
			writer.close();
		}
		
	}

	/**
	 * @param pw 
	 * @param oldProperties
	 * @param keysUsed
	 * @param string
	 * @param name
	 */
	private void writeBundleLine(PrintWriter pw, Properties oldProperties, Set<String> keysUsed, String key, String val) {
		
		pw.println(key + "=" + oldProperties.getProperty(key, val));
		keysUsed.add(key);
				
	}
	
}
